package converter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import converter.measure.TabMeasure;
import converter.note.StartTieDecorator;
import converter.note.StopTieDecorator;
import converter.note.TabNote;
import converter.note.TieNote;
import custom_exceptions.TXMLException;
import models.Creator;
import models.Identification;
import models.Part;
import models.ScorePartwise;
import models.part_list.MIDIInstrument;
import models.part_list.PartList;
import models.part_list.ScoreInstrument;
import models.part_list.ScorePart;
import utility.AnchoredText;
import utility.DrumPieceInfo;
import utility.DrumUtils;
import utility.GuitarUtils;
import utility.Range;
import utility.Settings;
import utility.ValidationError;

public class Score extends ScoreComponent {

	private Map<Integer, String> scoreTextFragments;
	private List<TabSection> tabSectionList;
    public List<TabMeasure> tabMeasureList;

    public Score(String textInput) {
    	
    	TabMeasure.MEASURE_INDEX = 0;
    	DrumUtils.createDrumSet();
		DrumUtils.createDrumNickNames();
		at = new AnchoredText(textInput, 0, 0);
    	detectInstrument();
        scoreTextFragments = getScoreTextFragments(at.text);
        tabSectionList = createTabSectionList(scoreTextFragments);
        tabMeasureList = createMeasureList();
        applyTimeSignatureUntilNextChange();
        setDivisions();
        createTiedNotes();
    }

	/**
	 * Updates the value of instrument in Settings if it was set to auto-detect
	 */
	public void detectInstrument() {
		if (Settings.getInstance().getInstrumentSetting() == InstrumentSetting.AUTO) {
			double guitarScore = 0;
			double drumScore = 0;
			Matcher lineMatcher = Pattern.compile("(?<=^|\\n)[^\\n]+(?=$|\\n)").matcher(at.text);
			int lineCount = 0;
			while (lineMatcher.find()) { // go through each line
				String x = lineMatcher.group();
				String pattern = TabSection.tabRowLinePattern();
				Matcher tabRowLineMatcher = Pattern.compile(pattern).matcher(x);
				if (tabRowLineMatcher.find()) {
					lineCount ++;
					if (lineCount > 12) break; // Should know the instrument by then
					String line = tabRowLineMatcher.group();
					if (line.charAt(0) == '\n') {
						line = line.substring(1);
					}
					AnchoredText nameData = new TabRow().nameOf(line, 0);
					String name = nameData.text;
					String tab = line.substring(nameData.positionInScore + name.length());
					guitarScore += GuitarUtils.isGuitarLineLikelihood(name, tab);
					drumScore += DrumUtils.isDrumLineLikelihood(name, tab);
				}
			}
			if (guitarScore > drumScore) {
				Settings.getInstance().setDetectedInstrument(Instrument.GUITAR);
			}
			else {
				Settings.getInstance().setDetectedInstrument(Instrument.DRUMS);
			}
		}
	}
    /**
     * Breaks input text (at wherever it finds blank lines) up into smaller pieces to make further analysis of each
     * piece of text with regex more efficient
     * @param input the string which is to be broken up into its fragments
     * @return an ordered map mapping the position of each broken up piece of text(Integer[startIndex, endIndex]) to the
     * actual piece of text (String)
     */
    public LinkedHashMap<Integer, String> getScoreTextFragments(String input) {
    	//TODO Should use anchored text also
        LinkedHashMap<Integer, String> inputFragments = new LinkedHashMap<>();

        // Finding the point where there is a break between two pieces of text.
        // (i.e. a newline, then a blank line(a line containing nothing or just whitespace) then another newline
        // is considered to be where there is a break between two pieces of text)
        Pattern textBreakPattern = Pattern.compile("((\\R|^)[ ]*(?=\\R)){2,}|^|$");
        Matcher textBreakMatcher = textBreakPattern.matcher(input);

        Integer previousTextBreakEnd = null;
        while(textBreakMatcher.find()) {
            if (previousTextBreakEnd==null) {
                previousTextBreakEnd = textBreakMatcher.end();
                continue;
            }

            int paragraphStart = previousTextBreakEnd;
            int paragraphEnd = textBreakMatcher.start();
            String fragment = at.text.substring(previousTextBreakEnd,paragraphEnd);
            if (!fragment.strip().isEmpty()) {
                inputFragments.put(paragraphStart, fragment);
            }
            previousTextBreakEnd = textBreakMatcher.end();
        }
        return inputFragments;
    }
    
    /**
     * Creates a List of TabSection objects from the extracted fragments of a String.
     * These TabSection objects are not guaranteed to be valid. You can find out if all the TabSection
     * objects in this score are actually valid by calling the Score().validate() method.
     * @param stringFragments A Map which maps an Integer to a String, where the String is the broken up fragments of a
     *                        piece of text, and the Integer is the starting index at which the fragment starts in the
     *                        original text from which the fragments were derived.
     * @return a list of TabSection objects.
     */
    private List<TabSection> createTabSectionList(Map<Integer, String> stringFragments) {
        List<TabSection> tabSectionList = new ArrayList<>();
        boolean isFirstTabSection = true;
        for (Map.Entry<Integer, String> fragment : stringFragments.entrySet()) {
        	String tabSectionRegexPattern = TabSection.getRegexPattern();
			Matcher matcher = Pattern.compile(tabSectionRegexPattern, Pattern.MULTILINE).matcher(fragment.getValue());
			while (matcher.find()) {
				AnchoredText at = new AnchoredText(matcher.group(), fragment.getKey() + matcher.start(), 0);
				tabSectionList.add(new TabSection(at, isFirstTabSection));
				isFirstTabSection = false;
			}
        }
        return tabSectionList;
    }

    //TODO Update to use the attribute tabMeasureList
    private void applyTimeSignatureUntilNextChange() {
	    int currBeatCount = Settings.getInstance().tsNum;
	    int currBeatType = Settings.getInstance().tsDen;
	    for (TabSection tabSection : tabSectionList) {
	        TabRow tabRow = tabSection.getTabRow();
	            for (TabMeasure measure : tabRow.getMeasureList()) {
	                if (measure.changesTimeSignature) {
	                    currBeatCount = measure.getBeatCount();
	                    currBeatType = measure.getBeatType();
	                }
	                measure.setTimeSignature(currBeatCount, currBeatType);
	            }
	    }
	}

	private void setDivisions() {
	    for (TabMeasure tabMeasure : this.tabMeasureList) {
	        tabMeasure.setDivisions();
	    }
	}

	private void createTiedNotes() {
		// Create tied notes within a measure
		boolean noSplit = false;
		while (!noSplit) {
			noSplit = true;
			for (TabMeasure m : tabMeasureList) {
				if (m.createTiedNotes())
					noSplit = false;
			}
		}
		// Create tied notes between measures
		String message = "success";
		for (int i=0; i < tabMeasureList.size() - 1; i++) {
			TabMeasure secondMeasure = tabMeasureList.get(i+1);
			if (secondMeasure.voiceSortedNoteList.size() > 0) {
				List<TabNote> firstVoice = secondMeasure.voiceSortedNoteList.get(0);

				if (firstVoice.get(0) instanceof TieNote) {
					// Assumption: TieNote objects will only be at the beginning of the measure
					long tieCount = firstVoice.stream().filter(n -> n instanceof TieNote).count();
					TabMeasure firstMeasure = tabMeasureList.get(i);
					if (!firstMeasure.getVoiceSortedChordList().isEmpty()) {
						int numberOfChords = firstMeasure.getVoiceSortedChordList().get(0).size();
						if (numberOfChords > 0) {
							List<TabNote> lastChord = firstMeasure.getVoiceSortedChordList().get(0).get(numberOfChords - 1);
							for (TabNote n: lastChord) {
								n.addDecorator(new StartTieDecorator(), message);
							}
							List<TabNote> newNotes = new ArrayList<>();
							for (int j = 0; j < tieCount; j++) {
								TabNote tn = firstVoice.remove(0); // remove this fake note
								for (TabNote n: lastChord) {
									TabNote newNote = n.copy();
									newNote.setDivisions(tn.divisions);
									newNote.setDuration(tn.duration);
									newNote.distance = tn.distance;
									newNote.addDecorator(new StopTieDecorator(), message);
									if (j < tieCount - 1) newNote.addDecorator(new StartTieDecorator(), message);
									newNotes.add(newNote);
								}
							} 
							firstVoice.addAll(0, newNotes);							
						}
					}
				}
			}
		}
	}

	public List<TabSection> getTabSectionList() {
        return this.tabSectionList;
    }
    
    public List<TabMeasure> getMeasureList() {
        return this.tabMeasureList;
    }
    
    public TabMeasure getMeasure(int measureCount) {
        try {
			return tabMeasureList.get(measureCount - 1); // -1 due to 0 indexing
		} catch (IndexOutOfBoundsException e) {
			return null;
		}
    }

    private List<TabMeasure> createMeasureList() {
        List<TabMeasure> measureList = new ArrayList<>();
        for (TabSection tabSection : this.tabSectionList) {
            TabRow tabRow = tabSection.getTabRow();
            measureList.addAll(tabRow.getMeasureList());
            
        }
        return measureList;
    }

    private PartList getDrumPartList() {
        List<ScorePart> scoreParts = new ArrayList<>();
        ScorePart scorePart = new ScorePart("P1", "Drumset");
        List<ScoreInstrument> scoreInstruments = new ArrayList<>();
        List<MIDIInstrument> midiInstruments = new ArrayList<>();
        

        for (DrumPieceInfo drumPieceInfo : DrumUtils.drumSet.values()) {
        	String partID = drumPieceInfo.getMidiID();
            scoreInstruments.add(new ScoreInstrument(partID, drumPieceInfo.getFullName()));
            // Assumption: partID is of the form P1-IXX, where XX are digits
            int pitch = Integer.parseInt(partID.substring(4, 6));
            midiInstruments.add(new MIDIInstrument(partID, pitch));
        }
        scorePart.setScoreInstruments(scoreInstruments);
        scorePart.setMIDIInstruments(midiInstruments);
        scoreParts.add(scorePart);

        return new PartList(scoreParts);
    }

    private PartList getGuitarPartList() {
        List<ScorePart> scoreParts = new ArrayList<>();
        scoreParts.add(new ScorePart("P1", "Guitar"));
        return new PartList(scoreParts);
    }

    private PartList getBassPartList() {
        List<ScorePart> scoreParts = new ArrayList<>();
        scoreParts.add(new ScorePart("P1", "Bass"));
        return new PartList(scoreParts);
    }
    
	public ScorePartwise getModel() {
		// Erased 'throws TXMLException' in the definition.
	
	    List<models.measure.Measure> measures = new ArrayList<>();
	    for (TabMeasure tabMeasure : this.tabMeasureList) {
	        measures.add(tabMeasure.getModel());
	    }
	    Part part = new Part("P1", measures);
	    List<Part> parts = new ArrayList<>();
	    parts.add(part);
	
	    PartList partList;
	    if (Settings.getInstance().getInstrument() == Instrument.DRUMS)
	        partList = this.getDrumPartList();
	    else if (Settings.getInstance().getInstrument() == Instrument.GUITAR)
	        partList = this.getGuitarPartList();
	    else
	        partList = this.getBassPartList();
	
	    ScorePartwise scorePartwise = new ScorePartwise("3.1", partList, parts);
	    scorePartwise.setMovementTitle(Settings.getInstance().title);
	    scorePartwise.setIdentification(new Identification(new Creator("composer", Settings.getInstance().artist)));
	    return scorePartwise;
	}

	@Override
	public List<Range> getRanges() {
		return new ArrayList<>(Collections.singletonList(new Range(0, at.text.length())));
	}

	/** 
	 * Anything outside recognized tab sections is marked as ignored.
	 * Validates all TabSection objects it aggregates.
	 */
	public List<ValidationError> validate() {
	    
	    int prevEndIdx = 0;
	    ArrayList<Range> positions = new ArrayList<>();
	    for (TabSection tabSection : this.tabSectionList) {
	    	String uninterpretedFragment = at.text.substring(prevEndIdx, tabSection.at.positionInScore);
	    	if (!uninterpretedFragment.isBlank()) {
	    		positions.add(new Range(prevEndIdx, prevEndIdx+uninterpretedFragment.length()));
	    	}
	    	prevEndIdx = tabSection.endIndex;
	    }
	
	    String restOfDocument = at.text.substring(prevEndIdx);
	    if (!restOfDocument.isBlank()) {
	        positions.add(new Range(prevEndIdx, prevEndIdx+restOfDocument.length()));
	    }
	
	    if (!positions.isEmpty()) {
	        addError("This text will be ignored.", 4, positions);        
	    }
	
	    // Validate your aggregates (regardless of if you're valid, as there is no significant
	    // validation performed upon yourself that precludes your aggregates from being valid)
	    for (TabSection colctn : this.tabSectionList) {
	        errors.addAll(colctn.validate());
	    }
	
	    return errors;
	}

}
