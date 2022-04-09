package models.preview.content;

import java.util.ArrayList;
import java.util.List;

import GUI.PreviewFX;
import converter.Instrument;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import models.measure.note.Note;
import models.measure.note.Notehead;
import utility.Settings;

public class DrawMeasure {
	private Instrument instrument;
	// The list of positions on the staff is reserved for drum notes only.
	private String[][] staffPositions = {{"C","4"}, {"D","4"}, {"E","4"}, {"F","4"}, {"G","4"}, {"A","4"}, {"B","4"},
			 { "C","5"}, {"D","5"}, {"E","5"}, {"F","5"}, {"G","5"}, {"A","5"}, {"B","5"}, {"C","6"}};
	private double vBasePos; // The vertical position of C4, which will be used as an offset for other note positions.
	private double[] linePositions;
	private AnchorPane measurePane;
	private final int MEASURE_PANE_HEIGHT = 200;
	private List<AnchorPane> notePanes;
	private List<Note> notes;
	private List<ArrayList<Note>> chords;
	private int numOfChords;
	private double sizeTracker;
	
	public DrawMeasure(List<Note> notes, double[] linePositions) {
		// Initialization:
		this.instrument = Settings.getInstance().getInstrument();
		this.linePositions = linePositions;
		this.vBasePos = this.linePositions[this.linePositions.length - 1] + 20;
		this.measurePane = new AnchorPane();
		this.notePanes = new ArrayList<AnchorPane>();
		this.notes = notes;
		this.chords = new ArrayList<ArrayList<Note>>();
		this.numOfChords = 0;
		this.sizeTracker = 0.0;
	}
	
	public void draw() {
		// Measure setup:
		this.measurePane.setPrefHeight(this.MEASURE_PANE_HEIGHT);
		
		// Chord tracking:
		ArrayList<Note> notesInChord = new ArrayList<Note>();
		
		// Note creation:
		for(int i = 0; i < this.notes.size(); i++) {
			Note note = this.notes.get(i);
			AnchorPane notePane = null;
			double notePosition = this.getNotePosition(note);
			
			if (this.instrument == Instrument.GUITAR) {
				// If the instrument for this music score is a guitar:
				notePane = this.constructGuitarNote(note);
				
				// Handling of decorations (pull-off and hammer-on)?
				// (Not implemented yet)
				
				// Chord handling: <-- New method?
				if(note.getChord() == null) { 
					// The note isn't (or no longer) attached to a chord. Otherwise, note is attached to a chord.
					this.sizeTracker += 15; // Increment by 15px for every note plotted that isn't in a chord.
				}
				
			} else if (this.instrument == Instrument.DRUMS) {
				// If the instrument for this music score are the drums (in development):
				notePane = this.constructDrumNoteHead(note, notePosition);
				// Add stem creation method here.
				
				
				// Chord handling: <-- New method?
				if(note.getChord() == null) { 
					// The note isn't (or no longer) attached to a chord. Otherwise, note is attached to a chord.
					if(!notesInChord.isEmpty()) {
						this.chords.add(notesInChord);
						this.drawStem(notesInChord);
						notesInChord = new ArrayList<Note>();
					}
					this.sizeTracker += 30; // Increment by 30px for every note plotted that isn't in a chord.
					this.drawStem(note, notePosition);
				} else { // The note is attached to a chord.
					if(notesInChord.isEmpty()) { // The previous note will be in the same chord.
						this.numOfChords ++;
						notesInChord.add(this.notes.get(i-1));
					} 
					notesInChord.add(note);
					
					if(i == this.notes.size() - 1) { // When last note in a measure is in a chord...
						this.chords.add(notesInChord);
						this.drawStem(notesInChord);
						notesInChord = new ArrayList<Note>();
					}
				}
				
			} else { // BASS
				// Not implemented.
			}
			this.notePanes.add(notePane);
			this.measurePane.getChildren().add(notePane);
			AnchorPane.setTopAnchor(notePane, notePosition);
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
					notePosition = this.vBasePos - (i * 10);
					continueLooping = false;
				}
			}
		} else { // BASS
			// Not implemented.
		}
		return notePosition;
	}
	
	private AnchorPane constructGuitarNote(Note note) {
		// notePane setup:
		AnchorPane notePane = new AnchorPane();
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
		
		return notePane;
	}
	
	private AnchorPane constructDrumNoteHead(Note note, double notePosition) {
		// notePane setup:
		AnchorPane notePane = new AnchorPane();
		notePane.setPrefHeight(20);
		
		// Type of note head:
		String noteType = note.getType();
		Notehead noteHead = note.getNotehead();
		
		// Note creation:
		if(noteHead != null) { // "X" note.
			Line line1 = new Line();
			Line line2 = new Line();
			line1.setStartX(-6);
			line1.setStartY(10);
			line1.setEndX(6);
			line1.setEndY(-10);
			
			line2.setStartX(-6);
			line2.setStartY(-10);
			line2.setEndX(6);
			line2.setEndY(10);
			
			notePane.getChildren().addAll(line1,line2);
		} else if (noteType.equals("half")) { // Half note.
			Ellipse ellipse = new Ellipse();
			ellipse.setRadiusX(6);
			ellipse.setRadiusY(10);
			ellipse.setRotate(27.5);
			ellipse.setFill(Color.WHITE);
			ellipse.setStroke(Color.BLACK);
			
			notePane.getChildren().add(ellipse);
		} else { // Quarter, even though it's "eighth". Fix later.
			Ellipse ellipse = new Ellipse();
			ellipse.setRadiusX(6);
			ellipse.setRadiusY(10);
			ellipse.setRotate(27.5);
			
			notePane.getChildren().add(ellipse);
		}
		
		// Ledger lines apply to C4, A5, and C6:
		if(notePosition == (this.linePositions[4]+20) ||
				notePosition == (this.linePositions[0]-20) ||
				notePosition == (this.linePositions[0]+40)) {
			Line line = new Line();
			line.setStartX(-6);
			line.setStartY(0);
			line.setEndX(6);
			line.setEndY(0);
			
			notePane.getChildren().add(line);
		}
		
		return notePane;
	}
	
	private void drawStem(Note note, double notePosition) {
		// (Overloaded method) Used on notes that aren't in chords.
		Line stem = new Line();
		stem.setStartY(notePosition - 40);
		if (note.getNotehead() != null) { // "X"
			stem.setEndY(notePosition - 10);
		} else { // Not "X"
			stem.setEndY(notePosition);
		}
		this.measurePane.getChildren().add(stem);
		AnchorPane.setLeftAnchor(stem, this.sizeTracker + 6);
	}
	
	private void drawStem(ArrayList<Note> notesInChord) {
		// (Overloaded method) Used on notes that are in chords.
		double largestYPos = 0;
		double smallestYPos = 0;
		//double previousNotePos = 0;
		for(int i = 0; i < notesInChord.size(); i++) {
			double noteYPos = this.getNotePosition(notesInChord.get(i));
			if(i == 0) {
				largestYPos = noteYPos;
				smallestYPos = noteYPos;
			} else {
				if(noteYPos > largestYPos) {
					largestYPos = noteYPos;
				} else if(noteYPos < smallestYPos) {
					smallestYPos = noteYPos;
				} 
			}
		}
		Line stem = new Line();
		stem.setStartY(largestYPos);
		stem.setEndY(smallestYPos - 40);
		this.measurePane.getChildren().add(stem);
		//AnchorPane.setTopAnchor(stem, );
		AnchorPane.setLeftAnchor(stem, this.sizeTracker + 6);
	}
	
	public AnchorPane getDrawnMeasure() {
		return this.measurePane;
	}
	
	public double getDrawnMeasureSize() {
		return this.sizeTracker;
	}
}
