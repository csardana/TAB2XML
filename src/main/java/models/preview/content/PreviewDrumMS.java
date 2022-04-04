package models.preview.content;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import GUI.PreviewFX;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import models.measure.Measure;
import models.measure.note.Note;
import models.measure.note.Notehead;

public class PreviewDrumMS extends PreviewFX {
	
	public PreviewDrumMS(ContentManager contentManager, int numOfStaffLines) {
		super(contentManager, numOfStaffLines);
	}
	
	public AnchorPane constructMeasure(int partIndex, int measureIndex) {
		String[][] staffPositions = {{"C","4"}, {"D","4"}, {"E","4"}, {"F","4"}, {"G","4"}, {"A","4"}, {"B","4"},
						 { "C","5"}, {"D","5"}, {"E","5"}, {"F","5"}, {"G","5"}, {"A","5"}, {"B","5"}, {"C","6"}};
		double vPosOfC4 = 160; // The vertical position of C4, which will be used as an offset for other note positions.
		Map<Integer, Map<Integer, Measure>> contentMap = this.musicContent.getContentMap();
		// AnchorPane constructor for some measure 'measureIndex' in part 'partIndex' :
		AnchorPane measureAnchor = new AnchorPane();
		measureAnchor.setPrefHeight(200);
		AnchorPane.setLeftAnchor(measureAnchor, this.posTracker); // Tracks next pos. of measure.
		
		// Retrieving a Java list of notes for some measure 'measureIndex' in part 'partIndex':
		List<Note> noteList = contentMap.get(partIndex).get(measureIndex).getNotesBeforeBackup();
		int listSize = noteList.size();
		double posTracker = 0; // Track the position of notes within the measure.
		
		for(int i = 0; i < listSize; i++) { // For some Note 'i':
			Note note = noteList.get(i);
			double notePosition = this.getPositionOnStaff(note, staffPositions, vPosOfC4);
			AnchorPane notePane = this.constructNote(note, notePosition);
			measureAnchor.getChildren().add(notePane);
			AnchorPane.setTopAnchor(notePane, notePosition);
			if(note.getChord() == null) { 
				// The note isn't (or no longer) attached to a chord. Otherwise, note is attached to a chord.
				posTracker += 30; // Increment by 3px for every note plotted that isn't in a chord.
			}
			AnchorPane.setLeftAnchor(notePane, posTracker);
		}
		this.posTracker += posTracker + 30; // Add size of measure and additional spacing for the barLine (in px).
		return measureAnchor;
	}
	
	public AnchorPane constructNote(Note note, double notePosition) {
		// Ledger lines apply to: C4, A5, and C6.
		AnchorPane notePane = new AnchorPane();
		notePane.setPrefHeight(20);
		
		String noteType = note.getType();
		Notehead noteHead = note.getNotehead();
		
		System.out.println(notePosition);
		if(notePosition == 160 || notePosition == 40 || notePosition == 20) {
			Line line = new Line();
			line.setStartX(0);
			line.setStartY(0);
			line.setEndX(20);
			line.setEndY(0);
			
			notePane.getChildren().add(line);
		}
		
		if(noteHead != null) { // Fix.
			Line line1 = new Line();
			Line line2 = new Line();
			line1.setStartX(0);
			line1.setStartY(10);
			line1.setEndX(20);
			line1.setEndY(-10);
			
			line2.setStartX(0);
			line2.setStartY(-10);
			line2.setEndX(20);
			line2.setEndY(10);
			
			notePane.getChildren().addAll(line1,line2);
		} else {
			Ellipse ellipse = new Ellipse();
			ellipse.setRadiusX(6);
			ellipse.setRadiusY(10);
			ellipse.setRotate(27.5);
			
			notePane.getChildren().add(ellipse);
		}
		return notePane;
	}
	
	public double getPositionOnStaff(Note note, String[][] staffPositions, double vPosOfC4) {
		double vPos = 0;
		boolean continueLooping = true;
		String noteStep = note.getUnpitched().getDisplayStep();
		String noteOctave = note.getUnpitched().getDisplayOctave() + "";
		for(int i = 0; continueLooping && i < staffPositions.length; i++) {
			if(staffPositions[i][0].equals(noteStep) && staffPositions[i][1].equals(noteOctave)) {
				vPos = vPosOfC4 - (i * 10);
				continueLooping = false;
			}
		}
		return vPos;
	}

}
