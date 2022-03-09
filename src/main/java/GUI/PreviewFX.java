package GUI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import models.content.ContentManager;
import models.measure.Measure;
import models.measure.note.Note;

public class PreviewFX {
	// Defining default pane height and width:
	int PANE_WIDTH = 750;
	int PANE_HEIGHT = 200;
	GridPane gridPane;
	int numOfStaffs;
	double posTracker;
	HashMap<String, String> posTrackerList = new HashMap(); 
	
	// Defining staff line positions with respect to the size of one pane:
	int[] linePositions = {50, 70, 90, 110, 130, 150};
	
	ContentManager musicContent;
	
	public PreviewFX(ContentManager musicContent) {
		this.musicContent = musicContent;
		Map<Integer, Map<Integer, Measure>> contentMap = musicContent.getContentMap(); // Also in constructMeasureGrid()
		//int numOfRows = Math.round(contentMap.get(1).size() / 2);
		
		gridPane = new GridPane();
		gridPane.setPadding(new Insets(0,0,0,14));
		
		this.numOfStaffs = 0; // Refers to the number of staffs created for measures.
		
		this.posTracker = 0; // Used to track px position for measure, timeSig, and 'TAB' clef.
		
		// First time staff construction for music score:
		AnchorPane parentAnchor = this.constructStaff();
		VBox clef = this.constructClef();
		VBox timeSig = this.constructTimeSig(1,1); // Can be added later?
		parentAnchor.getChildren().addAll(clef, timeSig);
		
		// NOTE: indices of contentMap start at 1, not 0.
		for(int partIndex = 1; partIndex < contentMap.size() + 1; partIndex++) { // For some part:
			
			for(int measureIndex = 1; measureIndex < contentMap.get(partIndex).size() + 1; measureIndex++) { // For some measure:
				
				// Checking if the following measure can fit onto the staff. If true, make new staff. If false, ignore.
				if(!this.staffHasSpace(partIndex, measureIndex)) { 
					// NOTE: Order is important. posTracker has to be reset before the construction of the new staff.
					this.gridPane.add(parentAnchor, 0, this.numOfStaffs); // Add previous staff to root GridPane.
					this.numOfStaffs ++; // Increment the # of staff that exist.
					this.posTracker = 0; // Reset posTracker for the new staff.
					parentAnchor = this.constructStaff(); // Construct a new staff.
					clef = this.constructClef(); // Construct a new clef.
					parentAnchor.getChildren().add(clef); // Add clef to new staff.
				} else {
					// Check if the following measure is not the last measure in the music score.
					// If true, add bar line. If false, ignore.
					// NOTE: If the previous staff didn't have enough space to fit a measure in it, this 'else'
					// makes sure a bar line isn't added after a new staff is created.
					if(measureIndex > 1 && measureIndex < contentMap.get(partIndex).size() + 1) {
						Line barLine = this.constructBarLine(this.posTracker);
						parentAnchor.getChildren().add(barLine);
					}
				}
				
				// Check if timeSig changed or not. If true, add new timeSig. If false, ignore.
				if(this.isDifferentTimeSig(partIndex, measureIndex)) {
					timeSig = this.constructTimeSig(partIndex, measureIndex);
					parentAnchor.getChildren().add(timeSig);
				}
				
				posTrackerList.put(Integer.toString(measureIndex), posTracker + ":" + numOfStaffs);
				GridPane measureGrid = this.constructMeasureGrid(partIndex, measureIndex);
				parentAnchor.getChildren().add(measureGrid); // Add measureGrid to parent Anchor.
				
			}
		}
		this.gridPane.add(parentAnchor, 0, this.numOfStaffs);
	}
	
	public GridPane getGridPane() {
		return this.gridPane;
	}
	
	public HashMap<String, String> getPosTrackerList() {
		return posTrackerList;
	}
	
	public AnchorPane constructStaff() {
		// TO-DO:
		// - Look into replacing layout methods with Anchor's.
		
		AnchorPane parentAnchor = new AnchorPane(); // Parent AnchorPane for each staff.
		parentAnchor.setPrefHeight(200);
		parentAnchor.setStyle("-fx-background-color: #ffffff;");
		
		// Constructing horizontal lines:
		for(int i = 0; i < linePositions.length; i++) { // For some line 'i':
			Line staffLine = new Line();
			staffLine.setLayoutX(PANE_WIDTH / 2);
			// Start to end = 750px (the width of the staff).
			staffLine.setStartX(0 - (PANE_WIDTH / 2));
			staffLine.setEndX((PANE_WIDTH / 2));
			staffLine.setLayoutY(linePositions[i]);
			staffLine.setStroke(Color.BLACK);
			
			parentAnchor.getChildren().add(staffLine);
		}
		
		//Constructing beginning and ending (2) vertical lines:
		for(int i = 0; i < 2; i++) {
			Line barLine;
			if(i > 0) { // Ending vertical line x pos.:
				barLine = this.constructBarLine(PANE_WIDTH);
			} else { // Beginning vertical line x pos.:
				barLine = this.constructBarLine(0);
			}
			
			parentAnchor.getChildren().add(barLine);
		}
	
		return parentAnchor;
	}
	
	public Line constructBarLine(double posTracker) {
		// TO-DO:
		// - Look into replacing layout methods with Anchor's.
		
		Line barLine = new Line();
		barLine.setLayoutY(PANE_HEIGHT / 2);
		// Start to end = 100px (the height of the staff).
		barLine.setStartY(50 - (PANE_HEIGHT / 2));
		barLine.setEndY((PANE_HEIGHT / 2) - 50);
		barLine.setStroke(Color.BLACK);
		barLine.setStrokeWidth(2);
		
		AnchorPane.setLeftAnchor(barLine, posTracker);
		
		this.posTracker += 20;
		
		return barLine;
	}
	
	public VBox constructClef() {
		VBox clefBox = new VBox();
		Text t = new Text();
		t.setText("T");
		t.setFont(Font.font(30));
		Text a = new Text();
		a.setText("A");
		a.setFont(Font.font(30));
		Text b = new Text();
		b.setText("B");
		b.setFont(Font.font(30));
		clefBox.getChildren().addAll(t, a, b);
		
		AnchorPane.setTopAnchor(clefBox, 39.0); //~40px
		AnchorPane.setBottomAnchor(clefBox, 39.0); //~40px
		AnchorPane.setLeftAnchor(clefBox, 5.0);

		return clefBox;
	}
	
	public VBox constructTimeSig(int partIndex, int measureIndex) {
		VBox timeSigBox = new VBox();
		int[] timeSig = this.musicContent.getMeasureTimeSig(partIndex, measureIndex);
		Text tsnum = new Text();
		tsnum.setText(timeSig[0] + "");
		tsnum.setFont(Font.font(30));
		Text tsden = new Text();
		tsden.setText(timeSig[1] + "");
		tsden.setFont(Font.font(30));

		timeSigBox.getChildren().addAll(tsnum, tsden);
		
		AnchorPane.setTopAnchor(timeSigBox, 59.0); //~60px
		AnchorPane.setBottomAnchor(timeSigBox, 59.0); //~60px
		AnchorPane.setLeftAnchor(timeSigBox, this.posTracker);
		
		this.posTracker += 30;
		
		return timeSigBox;
	}
	
	public GridPane constructMeasureGrid(int partIndex, int measureIndex) {
		// TO-DO:
		// - Add possible 7th row to the top of the staff for measure numbering.
		
		Map<Integer, Map<Integer, Measure>> contentMap = this.musicContent.getContentMap();
		// Grid pane constructor for some measure 'measureIndex' in part 'partIndex' :
		GridPane measureGrid = new GridPane();
		// measureGrid.setGridLinesVisible(true); // Grids for testing.
		measureGrid.setPrefHeight(120);
		AnchorPane.setBottomAnchor(measureGrid, 40.0);
		AnchorPane.setTopAnchor(measureGrid, 40.0);
		AnchorPane.setLeftAnchor(measureGrid, this.posTracker); // Tracks next pos. of measure.
		
		// Explicitly defining each row to be 20px in height to match staff lines:
		for (int i = 0; i < 6; i++) {
	         RowConstraints row = new RowConstraints(20);
	         measureGrid.getRowConstraints().add(row);
		}
		
		// Retrieving a Java list of notes for some measure 'measureIndex' in part 'partIndex':
		List<Note> noteList = contentMap.get(partIndex).get(measureIndex).getNotesBeforeBackup();
		int chordCounter = 1; // Used to subtract from position 'z' when a proceeding note is a chord.
		
		for(int i = 0; i < noteList.size(); i++) { // For some Note 'z':
			Note note = noteList.get(i);
			// Development of a label with a background:
			AnchorPane fretPane = new AnchorPane();
			fretPane.setPrefHeight(20);
			fretPane.setStyle("-fx-background-color: #ffffff;");
			Label fretLabel = new Label();
			fretLabel.setText(note.getNotations().getTechnical().getFret() + "");
			fretLabel.setFont(Font.font(15));
			fretLabel.setPrefWidth(15);
			fretLabel.setAlignment(Pos.CENTER);
			fretPane.getChildren().add(fretLabel);
			
			// The string # from a note's notation element starts at index 1, hence the subtraction.
			if(note.getChord() == null) { // If 'null', the note isn't (or no longer) attached to a chord.
				measureGrid.add(fretPane, i, note.getNotations().getTechnical().getString() - 1);
				System.out.println("Step: " + note.getNotations().getTechnical().getString());
				//System.out.println(i);
				
				if(chordCounter > 1) { // Reset counter for next chord.
					chordCounter = 1;
				}
				this.posTracker += 15; // Increment by 20px for every note plotted that isn't in a chord.
			} else { // Note is attached to a chord.
				measureGrid.add(fretPane, (i - chordCounter), note.getNotations().getTechnical().getString() - 1);
				System.out.println("Chord: " + note.getNotations().getTechnical().getString());
				chordCounter++;
			}
		}
		this.posTracker += 20; // Additional spacing for barLine and next measure.
		
		return measureGrid;
	}
	
	public boolean staffHasSpace(int partIndex, int measureIndex) {
		// Method to check when a new staff needs to be created based on measure sizes.
		double sizeTracker = 0.0;
		List<Note> noteList = this.musicContent.getContentMap().get(partIndex).get(measureIndex).getNotesBeforeBackup();
		
		for(int i = 0; i < noteList.size(); i++) { // For some note 'i':
			Note note = noteList.get(i);
			// Check if note is in a chord: If true, ignore. If false, increment sizeTracker.
			if(note.getChord() == null) {
				sizeTracker += 15.0;
			}
		}
		// Check if timeSig needs to be changed. If true, increment sizeTracker. If false, ignore.
		if(this.isDifferentTimeSig(partIndex, measureIndex)) {
			sizeTracker += 30;
		}
		//System.out.println(sizeTracker); // For testing purposes.
		return (PANE_WIDTH - (this.posTracker + sizeTracker)) > 0;
	}
	
	public boolean isDifferentTimeSig(int partIndex, int measureIndex) {
		// Compare currentTimeSig with the one before it. If they're different, then output true.
		boolean output = false;
		if(measureIndex > 1) {
			int[] currentTimeSig = this.musicContent.getMeasureTimeSig(partIndex, measureIndex);
			int[] previousTimeSig = this.musicContent.getMeasureTimeSig(partIndex, (measureIndex - 1));
			if(currentTimeSig[0] != previousTimeSig[0] || currentTimeSig[1] != previousTimeSig[1]) {
				output = true;
			}
		}
		//System.out.println("Measure's timeSig different than previous? " + output); // For testing purposes.
		return output;
	}
	
}
