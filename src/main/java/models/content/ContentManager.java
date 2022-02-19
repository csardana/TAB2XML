package models.content;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import converter.Instrument;
import models.ScorePartwise;
import models.Part;
import models.measure.Measure;
import models.measure.attributes.Attributes;
import models.measure.note.Note;
import models.measure.note.Pitch;
import models.measure.note.notations.Notations;
import utility.Settings;

public class ContentManager {
	ScorePartwise sp;
	/*
	 * Starting from left (outer most Map) to right (inner most Map), the following declaration 
	 * can be interpreted as:
	 * 1. For every part, there is a map of its measures...
	 * 2. For every measure mapping, there is a corresponding measure object.
	 * 
	 * Note: the declaration was left ambiguous past retrieving a measure object for flexibility
	 * in the implementation of future methods.
	 */
	Map<Integer, Map<Integer, Measure>> partAndMeasureMap = new HashMap<>();
	
	// Note: this constructor requires a ScorePartwise object from the MainViewController.
	public ContentManager(ScorePartwise sp) {
		this.sp = sp;
		for(int i = 0; i < this.sp.getParts().size(); i++) {
			// For some Part 'i' (i.e. P1), make a Measure map:
			Map<Integer, Measure> measureMap = new HashMap<>();
			for(int j = 0; j < this.sp.getParts().get(i).getMeasures().size(); j++) {
				// In every Measure map, add Measure objects 'j' in an ordered sequence:
				measureMap.put(this.sp.getParts().get(i).getMeasures().get(j).getNumber(),
						this.sp.getParts().get(i).getMeasures().get(j));
			}
			// Attach the corresponding Measure map to Part 'i':
			partAndMeasureMap.put(i, measureMap);
		}
	}
	
	// Get the time signature for a specified measure:
	public int[] getMeasureTimeSig(int measureKey) {
		Attributes measureAttributes = partAndMeasureMap.get(1).get(measureKey).getAttributes();
		int[] timeSig = new int[2];
		try {
			timeSig[0] = measureAttributes.getTime().getBeats();
			timeSig[1] = measureAttributes.getTime().getBeatType();
		} catch(NullPointerException e) {
			// Default Time Signature
			timeSig[0] = Settings.getInstance().tsNum;
			timeSig[1] = Settings.getInstance().tsDen;
		}
		return timeSig;
	}
	
	// Get a multidimensional String[note#][pos.0 = step, pos.1 = octave] array for a specified measure:
	public String[][] getMeasureNotePitch(int measureKey) {
		List<Note> notes = partAndMeasureMap.get(1).get(measureKey).getNotesBeforeBackup();
		String[][] pitchOutput = new String[notes.size()][2];
		for(int i = 0; i < notes.size(); i++) {
			pitchOutput[i][0] = notes.get(i).getPitch().getStep();
			pitchOutput[i][1] = notes.get(i).getPitch().getOctave() + "";
			System.out.println(String.format("Step: %s, Octave: %s", pitchOutput[i][0], pitchOutput[i][1]));
		}
		return pitchOutput;
	}
	
	// For some Measure in a Measure map:
	public void readDevMapping() {
		String output = ""; // String to build visual map for development purposes.
		
		for(int partKey : partAndMeasureMap.keySet()) {
			// For some Measure map in a Part:
			output += "\nPart: " + partKey;
			for(int measureKey : partAndMeasureMap.get(partKey).keySet()) {
				// For some Measure in a Measure map:
				output+= "\n\tMeasure: " + measureKey + "\n\t\tDivisions: " + 
						partAndMeasureMap.get(partKey).get(measureKey).getAttributes().getDivisions();
				
				try { // 'Try' and retrieve any explicitly mentioned time signature. Re-use for specific implementations.
					output+= "\n\t\tTime Sig.: " + 
							partAndMeasureMap.get(partKey).get(measureKey).getAttributes().getTime().getBeats() +
							partAndMeasureMap.get(partKey).get(measureKey).getAttributes().getTime().getBeatType();
				} catch (NullPointerException e) { // If no time signature is mentioned...
					output += "\n\t\tDefault Time Sig.: " + Settings.getInstance().tsNum +" / " + Settings.getInstance().tsDen;
				}
				
				for(int i = 0; i < partAndMeasureMap.get(partKey).get(measureKey).getNotesBeforeBackup().size(); i++) {
					// For some Note objects 'i'
					output +="\n\t\t\tNote " + (i+1) + ": " + 
							partAndMeasureMap.get(partKey).get(measureKey).getNotesBeforeBackup().get(i).getType() + " (Beat Step & Octave: " +
							partAndMeasureMap.get(partKey).get(measureKey).getNotesBeforeBackup().get(i).getPitch().getStep() +
							partAndMeasureMap.get(partKey).get(measureKey).getNotesBeforeBackup().get(i).getPitch().getOctave() + ") (Beat Value: " + 
							((double) partAndMeasureMap.get(partKey).get(measureKey).getNotesBeforeBackup().get(i).getDuration()
							/partAndMeasureMap.get(partKey).get(measureKey).getAttributes().getDivisions() +")");
				}
			}
		}
		System.out.println(output);
	}
	
}
