package models.preview.content;

import java.util.ArrayList;
import java.util.List;

import GUI.PreviewFX;
import converter.Instrument;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import models.measure.note.Note;
import utility.Settings;

public class DrawMeasure {
	private Instrument instrument;
	// The list of positions on the staff is reserved for drum notes only.
	private String[][] staffPositions = {{"C","4"}, {"D","4"}, {"E","4"}, {"F","4"}, {"G","4"}, {"A","4"}, {"B","4"},
			 { "C","5"}, {"D","5"}, {"E","5"}, {"F","5"}, {"G","5"}, {"A","5"}, {"B","5"}, {"C","6"}};
	private double V_BASE_POS = 160; // The vertical position of C4, which will be used as an offset for other note positions.
	private double[] linePositions;
	private AnchorPane measurePane;
	private final int MEASURE_PANE_HEIGHT = 200;
	private List<AnchorPane> notePanes;
	private List<Note> notes;
	private double sizeTracker;
	
	public DrawMeasure(List<Note> notes, double[] linePositions) {
		// Initialization:
		this.instrument = Settings.getInstance().getInstrument();
		this.linePositions = linePositions;
		this.measurePane = new AnchorPane();
		this.notePanes = new ArrayList<AnchorPane>();
		this.notes = notes;
		this.sizeTracker = 0.0;
		System.out.println("Yes");
	}
	
	public void draw() {
		// Measure setup:
		this.measurePane.setPrefHeight(this.MEASURE_PANE_HEIGHT);
		
		// Note creation:
		for(int i = 0; i < this.notes.size(); i++) {
			Note note = this.notes.get(i);
			System.out.println(i);
			AnchorPane notePane = new AnchorPane();
			if (this.instrument == Instrument.GUITAR) {
				// If the instrument for this music score is a guitar:
				
				// notePane setup:
				notePane.setPrefHeight(20);
				notePane.setStyle("-fx-background-color: #ffffff;");
				
				// Label creation (for text):
				Label fretLabel = new Label();
				String labelText = note.getNotations().getTechnical().getFret() + "";
				fretLabel.setText(labelText);
				fretLabel.setFont(Font.font(15));
				fretLabel.setPrefWidth(15); // Has to change when there's a pull-off or hammer-on...
				fretLabel.setAlignment(Pos.CENTER);
				notePane.getChildren().add(fretLabel);
				
				// Handling of decorations (pull-off and hammer-on):
				// (Not implemented yet)
				
				// Chord handling: <-- New method?
				if(note.getChord() == null) { 
					// The note isn't (or no longer) attached to a chord. Otherwise, note is attached to a chord.
					this.sizeTracker += 15; // Increment by 15px for every note plotted that isn't in a chord.
				}
			} else if (this.instrument == Instrument.DRUMS) {
				// If the instrument for this music score are the drums (in development):
				
				// Chord handling: <-- New method?
				if(note.getChord() == null) { 
					// The note isn't (or no longer) attached to a chord. Otherwise, note is attached to a chord.
					this.sizeTracker += 30; // Increment by 30px for every note plotted that isn't in a chord.
				}
			} else { // BASS
				// Not implemented.
			}
			this.notePanes.add(notePane);
			this.measurePane.getChildren().add(notePane);
			AnchorPane.setTopAnchor(notePane, this.getNotePosition(note));
			AnchorPane.setLeftAnchor(notePane, this.sizeTracker);
		}
	}
	
	private double getNotePosition(Note note) {
		double notePosition = 0;
		if (this.instrument == Instrument.GUITAR) {
			// If the instrument for this music score is a guitar:
			
			notePosition = this.linePositions[note.getNotations().getTechnical().getString() - 1] - 10;
		} else if (this.instrument == Instrument.DRUMS) {
			// If the instrument for this music score are the drums (in development):
			
			// Retrieval of note step and octave:
			String noteStep = note.getUnpitched().getDisplayStep();
			String noteOctave = note.getUnpitched().getDisplayOctave() + "";
			
			boolean continueLooping = true; // Used to break out of loop if match found.
			for(int i = 0; continueLooping && i < staffPositions.length; i++) {
				if(staffPositions[i][0].equals(noteStep) && staffPositions[i][1].equals(noteOctave)) {
					notePosition = this.V_BASE_POS - (i * 10);
					continueLooping = false;
				}
			}
		} else { // BASS
			// Not implemented.
		}
		return notePosition;
	}
	
	public AnchorPane getDrawnMeasure() {
		return this.measurePane;
	}
	
	public double getDrawnMeasureSize() {
		return this.sizeTracker;
	}
}
