package models.preview.content;

import java.util.List;
import java.util.Map;

import GUI.PreviewFX;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import models.measure.Measure;
import models.measure.note.Note;

public class PreviewGuitarMS extends PreviewFX {
	// Assuming every fretPane is ~20px, these positions will center every label on a staff line.
	
	public PreviewGuitarMS(ContentManager musicContent, int numOfStaffLines) {
		super(musicContent, numOfStaffLines);
	}
	
	public AnchorPane constructMeasure(int partIndex, int measureIndex) {
		double[] fretPositions = {40, 60, 80, 100, 120, 140};
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
			AnchorPane fretPane = this.constructNote(note);
			measureAnchor.getChildren().add(fretPane);
			AnchorPane.setTopAnchor(fretPane, fretPositions[note.getNotations().getTechnical().getString() - 1]);
			if(i != 0 && note.getChord() == null) { 
				// The note isn't (or no longer) attached to a chord. Otherwise, note is attached to a chord.
				posTracker += 30; // Increment by 30px for every note plotted that isn't in a chord.
			} 
			AnchorPane.setLeftAnchor(fretPane, posTracker);
		}
		this.posTracker += posTracker + 30; // Add size of measure and additional spacing for the barLine (in px).
		return measureAnchor;
	}
	
	public AnchorPane constructNote(Note note) {
		AnchorPane fretPane = new AnchorPane();
		fretPane.setPrefHeight(20);
		fretPane.setStyle("-fx-background-color: #ffffff;");
		Label fretLabel = new Label();
		fretLabel.setText(note.getNotations().getTechnical().getFret() + "");
		fretLabel.setFont(Font.font(15));
		fretLabel.setPrefWidth(15);
		fretLabel.setAlignment(Pos.CENTER);
		fretPane.getChildren().add(fretLabel);
		
		return fretPane;
	}
}
