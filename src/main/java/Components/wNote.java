package Components;

public class wNote {
    boolean chord = false;

    String pitchStep;
    int pitchOctave;

    int duration;
    int voice;
    String type;

    int noteString;
    int fret;

    public wNote()
    {
    }

    public void setChord(boolean chord)
    {
        this.chord = chord;
    }

    public boolean getChord()
    {
        return this.chord;
    }

    public void setPitch(String pitchStep, int pitchOctave)
    {
        this.pitchStep = pitchStep;
        this.pitchOctave = pitchOctave;
    }

    public String getPitchStep()
    {
        return this.pitchStep;
    }
    public int getPitchOctave()
    {
        return this.pitchOctave;
    }

    public void setDuration(int duration)
    {
        this.duration = duration;
    }

    public int getDuration()
    {
        return this.duration;
    }

    public void setVoice(int voice)
    {
        this.voice = voice;
    }

    public int getVoice()
    {
        return this.voice;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getType()
    {
        return this.type;
    }

    public void setNoteString(int noteString)
    {
        this.noteString = noteString;
    }

    public int getNoteString()
    {
        return this.noteString;
    }

    public void setFret(int fret)
    {
        this.fret = fret;
    }

    public int getFret()
    {
        return this.fret;
    }

    public String toString()
    {
        String content = "== \n";

        content += "Pitch Step: " + this.pitchStep + "\n";
        content += "Pitch Octave: " + this.pitchOctave + "\n";
        content += "Chord: " + this.chord + "\n";
        content += "Duration: " + this.duration + "\n";
        content += "Voice: " + this.voice + "\n";
        content += "Type: " + this.type + "\n";
        content += "note String: " + this.noteString + "\n";
        content += "Fret: " + this.fret + "\n";
        content += "== \n";

        return content;
    }
}