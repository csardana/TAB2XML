package XMLPlayer;

import Components.wMeasure;
import Components.wNote;
import Components.wPart;
import org.jfugue.player.Player;

import java.util.ArrayList;
import java.util.Hashtable;

public class mxmlPlayer {
    wPart mainWPart;
    ArrayList<String> readableMusic = new ArrayList<>();
    //Array of strings representing tones that can be played by Jfugue
    //Every index corresponds to a string of a measure
    public mxmlPlayer(wPart wPart)
    {
        this.mainWPart = wPart;
    }

    private Character getLetteredDuration(String type)
    {
        Hashtable<String, Character> conversion = new Hashtable<String, Character>();
        conversion.put("whole", 'w');
        conversion.put("half", 'h');
        conversion.put("quarter", 'q');
        conversion.put("eighth", 'i');
        conversion.put("16th", 's');
        conversion.put("32nd", 't');

        return conversion.get(type);
    }

    public void createReadableMusic()
    {
        ArrayList<wMeasure> measuresList = mainWPart.getMeasures();
        for (int mi=0; mi < measuresList.size(); mi++)
        {
            String readableMeasure = "";
            ArrayList<wNote> notesList = measuresList.get(mi).getNotes();
            for (int ni = 0; ni < notesList.size(); ni++) {
                wNote currentWNote = notesList.get(ni);
                if(currentWNote.getChord() == false)
                {
                    readableMeasure += " " + currentWNote.getPitchStep() + currentWNote.getPitchOctave() +
                            this.getLetteredDuration(currentWNote.getType());
                }
                else
                {
                    readableMeasure += "+" + currentWNote.getPitchStep() + currentWNote.getPitchOctave() +
                            this.getLetteredDuration(currentWNote.getType());
                }
            }
            this.readableMusic.add(readableMeasure);
        }
    }

    public void playMusic()
    {
        Player player = new Player();
        String musicScript = "";
        for (int i = 0; i < this.readableMusic.size(); i++)
        {
            musicScript += this.readableMusic.get(i) + " | ";
        }

        //musicScript = "I[GUITAR] " + musicScript;
        System.out.println(musicScript);
        player.play(musicScript);
    }



}