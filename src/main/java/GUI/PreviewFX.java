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
import models.measure.Measure;
import models.measure.note.Note;
import models.preview.content.ContentManager;

public abstract class PreviewFX {
	// Defining default pane height and width:
	double PANE_WIDTH = 750;
	double PANE_HEIGHT = 200;
	GridPane gridPane;
	int numOfStaffs;
	int numOfStaffLines;
	double staffHeight;
	protected double posTracker;
	protected ContentManager musicContent;
	HashMap<String, String> posTrackerList = new HashMap(); 
	
	// Defining staff line positions with respect to the size of one pane:
	double[] linePositions;
	
	public PreviewFX(ContentManager musicContent, int numOfStaffLines) {
		this.musicContent = musicContent;
		Map<Integer, Map<Integer, Measure>> contentMap = musicContent.getContentMap(); // Also in constructMeasureGrid()
		
		gridPane = new GridPane();
		gridPane.setPadding(new Insets(0,0,0,10));
		
		this.numOfStaffLines = numOfStaffLines;
		this.numOfStaffs = 0; // Refers to the number of staffs created for measures.
		this.posTracker = 0; // Used to track px position for measure, timeSig, and 'TAB' clef.
		
		// First time staff construction for music score:
		this.findLinePositions();
		AnchorPane parentAnchor = this.constructStaff();
		VBox clef = this.constructClef();
		VBox timeSig = this.constructTimeSig(1,1); // Can be added later?
		parentAnchor.getChildren().addAll(clef, timeSig);
		
		// NOTE: indices of contentMap start at 1, not 0.
		for(int partIndex = 1; partIndex < contentMap.size() + 1; partIndex++) { // For some part:
			
			for(int measureIndex = 1; measureIndex < contentMap.get(partIndex).size() + 1; measureIndex++) { // For some measure:
				
				// Checking if the following measure can fit onto the staff. If true, check if a bar line is needed.
				// If false, create a new staff.
				if(this.staffHasSpace(partIndex, measureIndex)) { 
					// Check if the following measure is NOT the first or last measure in the musical score.
					// If true, add a bar line to separate measures. If false, ignore.
					//System.out.println("Has space.");
					if(measureIndex > 1 && measureIndex < contentMap.get(partIndex).size() + 1) {
						Line barLine = this.constructBarLine(this.posTracker);
						parentAnchor.getChildren().add(barLine);
					}
					
				} else {
					// NOTE: Order is important. posTracker has to be reset before the construction of the new staff.
					this.gridPane.add(parentAnchor, 0, this.numOfStaffs); // Add previous staff to root GridPane.
					this.numOfStaffs ++; // Increment the # of staff that exist.
					this.posTracker = 0; // Reset posTracker for the new staff.
					parentAnchor = this.constructStaff(); // Construct a new staff.
					clef = this.constructClef(); // Construct a new clef.
					parentAnchor.getChildren().add(clef); // Add clef to new staff.
				}
				
				// Check if timeSig changed or not. If true, add new timeSig. If false, ignore.
				if(this.isDifferentTimeSig(partIndex, measureIndex)) {
					timeSig = this.constructTimeSig(partIndex, measureIndex);
					parentAnchor.getChildren().add(timeSig);
				}
				
				posTrackerList.put(Integer.toString(measureIndex), posTracker + ":" + numOfStaffs);
				AnchorPane measureAnchor = this.constructMeasure(partIndex, measureIndex);
				parentAnchor.getChildren().add(measureAnchor); // Add measureGrid to parent Anchor.
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
	
	public void findLinePositions() {
		// Algorithm to find line positions based on the number of staff lines.
		this.linePositions = new double[this.numOfStaffLines];
		this.staffHeight = ((this.numOfStaffLines - 1) * 20);
		double heightDiff = PANE_HEIGHT - this.staffHeight;
		for(int i = 0; i < this.numOfStaffLines; i++) {
			this.linePositions[i] = (heightDiff / 2) + (i * 20);
		}
	}
	
	public AnchorPane constructStaff() {
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
		Line beginningBarLine = this.constructBarLine(0);
		Line endingBarLine = this.constructBarLine(PANE_WIDTH);
			
		parentAnchor.getChildren().addAll(beginningBarLine, endingBarLine);
	
		return parentAnchor;
	}
	
	public Line constructBarLine(double posTracker) {		
		Line barLine = new Line();
		barLine.setLayoutY(PANE_HEIGHT / 2);
		barLine.setStartY(-(this.staffHeight / 2));
		barLine.setEndY(this.staffHeight / 2);
		barLine.setStroke(Color.BLACK);
		barLine.setStrokeWidth(2);
		
		System.out.println("Barline: " + posTracker);
		AnchorPane.setLeftAnchor(barLine, posTracker);
		
		this.posTracker += 15;
		
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
		
		//this.posTracker += 30;
		
		return timeSigBox;
	}
	
	public abstract AnchorPane constructMeasure(int partIndex, int measureIndex); // Abstract might not be needed!!!
	
	//public abstract AnchorPane constructNote(Note note);
	
	public boolean staffHasSpace(int partIndex, int measureIndex) {
		// Method to check when a new staff needs to be created based on measure sizes.
		double sizeTracker = 0.0;
		List<Note> noteList = this.musicContent.getContentMap().get(partIndex).get(measureIndex).getNotesBeforeBackup();
		
		for(int i = 0; i < noteList.size(); i++) { // For some note 'i':
			Note note = noteList.get(i);
			// Check if note is in a chord: If true, ignore. If false, increment sizeTracker.
			if(note.getChord() == null) {
				sizeTracker += 30.0;
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
