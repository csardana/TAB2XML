package GUI;


import java.util.HashMap;
import java.util.Map;
import converter.Instrument;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import models.measure.Measure;
import models.preview.content.ContentManager;
import models.preview.content.DrawMeasure;
import utility.Settings;

public class PreviewFX {
	// Defining default pane height and width:
	double PANE_WIDTH = 750;
	double PANE_HEIGHT = 300;
	GridPane gridPane;
	int numOfStaffs;
	int numOfStaffLines;
	int numOfBarLines;
	double staffHeight;
	protected double posTracker;
	protected ContentManager musicContent;
	Instrument instrument;
	HashMap<String, String> posTrackerList = new HashMap(); 
	
	// Defining staff line positions with respect to the size of one pane:
	double[] linePositions;
	
	public PreviewFX(ContentManager musicContent, int numOfStaffLines) {
		this.musicContent = musicContent;
		this.instrument = Settings.getInstance().getInstrument();
		Map<Integer, Map<Integer, Measure>> contentMap = musicContent.getContentMap(); // Also in constructMeasureGrid()
		
		gridPane = new GridPane();
		gridPane.setPadding(new Insets(0,0,0,10));
		
		this.numOfStaffLines = numOfStaffLines;
		this.numOfStaffs = 0; // Refers to the number of staffs created for measures.
		this.posTracker = 0; // Used to track px position for measure, timeSig, and 'TAB' clef.
		this.numOfBarLines = 1;
		
		// First time staff construction for music score:
		this.findLinePositions();
		AnchorPane parentAnchor = this.constructStaff();
		AnchorPane barLabel = this.constructBarLabel(this.posTracker);
		this.numOfBarLines ++;
		AnchorPane clef = this.constructClef();
		VBox timeSig = this.constructTimeSig(1,1); // Can be added later?
		parentAnchor.getChildren().addAll(barLabel, clef, timeSig);
		
		// NOTE: indices of contentMap start at 1, not 0.
		for(int partIndex = 1; partIndex < contentMap.size() + 1; partIndex++) { // For some part:
			
			for(int measureIndex = 1; measureIndex < contentMap.get(partIndex).size() + 1; measureIndex++) { // For some measure:
				
				boolean addBar = true; // Check for if bar line needed.
				
				DrawMeasure drawMeasure = new DrawMeasure(contentMap.get(partIndex).get(measureIndex).getNotesBeforeBackup(), this.linePositions);
				drawMeasure.draw();
				AnchorPane measureAnchor = drawMeasure.getDrawnMeasure();
				
				// Checking if the following measure can fit onto the staff. If true, check if a bar line is needed.
				// If false, create a new staff.
				if(!this.staffHasSpace(partIndex, measureIndex, drawMeasure)) { 
					// NOTE: Order is important. posTracker has to be reset before the construction of the new staff.
					this.gridPane.add(parentAnchor, 0, this.numOfStaffs); // Add previous staff to root GridPane.
					this.numOfStaffs ++; // Increment the # of staff that exist.
					this.posTracker = 0; // Reset posTracker for the new staff.
					parentAnchor = this.constructStaff(); // Construct a new staff.
					if((this.numOfBarLines % 2) == 1) { // Odd # of bar lines.
						barLabel = this.constructBarLabel(this.posTracker);
						parentAnchor.getChildren().add(barLabel);
					}
					clef = this.constructClef(); // Construct a new clef.
					parentAnchor.getChildren().add(clef); // Add clef to new staff.
					this.numOfBarLines ++;
					addBar = false;
				} 
				
				// Check if bar line is needed.
				if(addBar && (measureIndex > 1 && measureIndex < contentMap.get(partIndex).size() + 1)) {
					this.posTracker += 30;
					Line barLine = this.constructBarLine(this.posTracker);
					if((this.numOfBarLines % 2) == 1) { // Odd # of bar lines.
						barLabel = this.constructBarLabel(this.posTracker);
						parentAnchor.getChildren().add(barLabel);
					}
					parentAnchor.getChildren().add(barLine);
					this.numOfBarLines ++;
				}
				
				// Check if timeSig changed or not.
				if(this.isDifferentTimeSig(partIndex, measureIndex)) {
					timeSig = this.constructTimeSig(partIndex, measureIndex);
					parentAnchor.getChildren().add(timeSig);
				}
				
				posTrackerList.put(Integer.toString(measureIndex), posTracker + ":" + numOfStaffs);
				
				parentAnchor.getChildren().add(measureAnchor); // Add measureGrid to parent Anchor.
				AnchorPane.setLeftAnchor(measureAnchor, this.posTracker);
				this.posTracker += drawMeasure.getDrawnMeasureSize();
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
		parentAnchor.setPrefHeight(this.PANE_HEIGHT);
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
		
		AnchorPane.setLeftAnchor(barLine, posTracker);
		
		return barLine;
	}
	
	public AnchorPane constructBarLabel(double posTracker) {
		AnchorPane labelPane = new AnchorPane();
		Label barLabel = new Label();
		barLabel.setText(this.numOfBarLines + "");
		barLabel.setFont(Font.font(20));
		labelPane.getChildren().add(barLabel);
		
		AnchorPane.setTopAnchor(labelPane, this.linePositions[0] - 30.0);
		AnchorPane.setLeftAnchor(labelPane, posTracker - 5.0);
		
		return labelPane;
	}
	
	public AnchorPane constructClef() {
		AnchorPane clefPane = new AnchorPane();
		if(this.instrument == Instrument.GUITAR) {
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
			clefPane.getChildren().add(clefBox);
			AnchorPane.setTopAnchor(clefPane, this.linePositions[0] - 11); // Move 10px back vertically to be in place.
			AnchorPane.setLeftAnchor(clefPane, 5.0);
			
			this.posTracker += 20;
			
		} else if (this.instrument == Instrument.DRUMS) {
			Line line1 = new Line();
			line1.setStrokeWidth(15);
			line1.setLayoutY(20);
			line1.setStartY(-20);
			line1.setStartY(20);
			Line line2 = new Line();
			line2.setStrokeWidth(15);
			line2.setLayoutX(20);
			line2.setLayoutY(20);
			line2.setStartY(-20);
			line2.setStartY(20);
			clefPane.getChildren().addAll(line1, line2);
			AnchorPane.setTopAnchor(clefPane, this.linePositions[0] + 10); // Move 10px forward vertically to be in place.
			AnchorPane.setLeftAnchor(clefPane, 15.0);
			
			this.posTracker += 30;
			
		}
		return clefPane;
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
		
		if(this.instrument == Instrument.GUITAR) {
			AnchorPane.setTopAnchor(timeSigBox, this.linePositions[0] + 9);
		} else if (this.instrument == Instrument.DRUMS) {
			AnchorPane.setTopAnchor(timeSigBox, this.linePositions[0]);
		}
		this.posTracker += 20; 
		AnchorPane.setLeftAnchor(timeSigBox, this.posTracker);
		this.posTracker += 30;
		
		return timeSigBox;
	}
	
	public boolean staffHasSpace(int partIndex, int measureIndex, DrawMeasure drawnMeasure) {
		// Method to check when a new staff needs to be created based on measure sizes.
		// Check includes: if a bar line can fit, if a -different- time sig. can fit, and if the measure of notes can fit.
		
		double sizeTracker = 30; // Starts at 30 because of bar line.
		
		// Check if timeSig needs to be changed.
		if(this.isDifferentTimeSig(partIndex, measureIndex)) {
			sizeTracker += 50;
		}
		
		sizeTracker += drawnMeasure.getDrawnMeasureSize(); // Size of measure (notes).
		
		return ((PANE_WIDTH - this.posTracker) - sizeTracker) > 0;
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
		
		return output;
	}
	
}
