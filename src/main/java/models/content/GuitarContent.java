package models.content;

import java.util.List;
import java.util.Map;

import models.ScorePartwise;
import models.measure.attributes.Attributes;
import models.measure.note.Note;
import utility.Settings;

public class GuitarContent extends ContentManager {
	private String[][] staffTunings;
	
	public GuitarContent(ScorePartwise sp) {
		super(sp);
		
		// With respect to the examples provided for the system,
		// the staff tunings are only mentioned in the first measure.
		// Note: Line 1 starts at the top of the staff.
		
		// staffTunings[line#][pos.0 = step, pos.1 = octave]
		// Might have to be changed later if custom tuning exists.
		String[][] staffTunings = Settings.getInstance().getGuitarTuning();
	}
	
	public int[][] getCrucialNoteData(int measureKey) {
		List<Note> guitarNotes = partAndMeasureMap.get(1).get(measureKey).getNotesBeforeBackup();
		int[][] notationOutput = new int[guitarNotes.size()][2];
		for(int i = 0; i < guitarNotes.size(); i++) {
			// For some note:
			notationOutput[i][0] = guitarNotes.get(i).getNotations().getTechnical().getString();
			notationOutput[i][1] = guitarNotes.get(i).getNotations().getTechnical().getFret();
			try {
				System.out.println( guitarNotes.get(i).getChord());
			} catch (Exception e) {
				
			}
			
			//System.out.println(String.format("Line: %d, Fret: %d", notationOutput[i][0], notationOutput[i][1]));
		}
		return notationOutput;
	}
	
	public String[][] getStaffTunings() {
		return this.staffTunings;
	}
	
	public void setStaffTunings(String[][] staffTunings) {
		this.staffTunings = staffTunings;
	}
}
