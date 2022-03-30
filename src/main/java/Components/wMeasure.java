package Components;

import java.util.ArrayList;

public class wMeasure {
    int divisions;
    int Key;
    //This key corresponds to the fifth tag in musicXMl so <fifth>key</fifth>

    int[] time = new int[2];

    String cleftSign; //G or C
    int cleftLine; // Line #

    ArrayList<wNote> notesList = new ArrayList<>();

    public wMeasure()
    {
    }

    public int getKey() {
        return Key;
    }

    public void setKey(int key) {
        Key = key;
    }

    public int getDivisions() {
        return divisions;
    }

    public void setDivisions(int divisions) {
        this.divisions = divisions;
    }

    public String getCleftSign() {
        return this.cleftSign;
    }

    public int getCleftLine() {
        return cleftLine;
    }

    public void setCleft(String cleftSign, int cleftLine) {
        this.cleftSign = cleftSign;
        this.cleftLine = cleftLine;
    }

    public int[] getTime() {
        return time;
    }

    public void setTime(int[] time) {
        this.time = time;
    }

    public void addNotes(ArrayList<wNote> notesList)
    {
        this.notesList = notesList;
    }

    public ArrayList<wNote> getNotes()
    {
        return this.notesList;
    }

    public String toString()
    {
        String content = "";
        content += "Divisions: " + this.divisions + "\n";
        content += "Key: " + this.Key + "\n";
        content += "Beats | Beat Type: " + time[0] + ", " + time[1] + "\n";
        content += "Cleft: " + cleftSign + " on line " + cleftLine + "\n \n";
        for (int i = 0 ; i < this.notesList.size(); i++)
        {
            wNote currentWNote = this.notesList.get(i);
            content += "Note " + (i+1) + ": \n";
            content += currentWNote.toString();
            content += "\n";
        }

        return content;
    }

}