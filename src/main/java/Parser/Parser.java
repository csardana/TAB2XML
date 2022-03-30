package Parser;

import Components.wMeasure;
import Components.wNote;
import Components.wPart;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;


import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Parser {
    String fileLocation;
    Document mainDoc;
    //ArrayList<Part> partsList;
    wPart mainWPart;
    public Parser(String XMLString)
    {
        SAXBuilder saxBuilder = new SAXBuilder();
        try
        {
            mainDoc= saxBuilder.build(new StringReader(XMLString));
            //mainDoc = saxBuilder.build(new File(XMLString));
        }
        catch (JDOMException | IOException e)
        {
            e.printStackTrace();
        }
    }

    public void createParts() throws IOException {
        Element rootTag = mainDoc.getRootElement();
        System.out.println("The root tag is: " + rootTag.getName());

        Element partTag = rootTag.getChild("part");
//        System.out.println("The part tag that contains the part we want is: " + partTag.getName() +
//                ", and it has ID: " + partTag.getAttributeValue("id"));

        List<Element> measureTagsList = partTag.getChildren("measure");

        wPart wPartOne = new wPart(partTag.getAttributeValue("id"));
        ArrayList<wMeasure> wMeasureList = new ArrayList<>();
        for (int i = 0; i < measureTagsList.size(); i++)
        {
            //Code below handles the following attributes of Measure:
            //Divisions - Key - Time - Clef -
            Element measureTag = measureTagsList.get(i);
            wMeasure newWMeasure = new wMeasure();

            Element attributesTag = measureTag.getChild("attributes");

            Element divisionsTag = attributesTag.getChild("divisions");
            int divisions = Integer.parseInt(divisionsTag.getValue());
            newWMeasure.setDivisions(divisions);

            Element keyTag = attributesTag.getChild("key");
            Element fifthsTag = keyTag.getChild("fifths");
            int keyFifths = Integer.parseInt( fifthsTag.getValue() );
            newWMeasure.setKey(keyFifths);

            try
            {
                Element timeTag = attributesTag.getChild("time");
                Element beatsTag = timeTag.getChild("beats");
                Element beatTypeTag = timeTag.getChild("beat-type");
                int[] time = new int[2];
                time[0] = Integer.parseInt( beatsTag.getValue() );
                time[1] = Integer.parseInt( beatTypeTag.getValue() );
                newWMeasure.setTime(time);
            }
            catch(Exception e){}

            try
            {
                Element clefTag = attributesTag.getChild("clef");
                Element signTag = clefTag.getChild("sign");
                Element lineTag = clefTag.getChild("line");
                int cleftLine = Integer.parseInt( lineTag.getValue() );
                newWMeasure.setCleft(signTag.getValue(), cleftLine);
            }
            catch(Exception e){}

            // V V V
            //Don't forget to add <staff-details> handling
            // ^ ^ ^

            ///////////////////////////////
            //Handling notes of a measure//
            ///////////////////////////////
            List<Element> noteTagsList = measureTag.getChildren("note");
            ArrayList<wNote> notesList = new ArrayList<>();
            for( int z=0; z <= noteTagsList.size()-1; z++ )
            {
                //Code below handles the following attributes of Note:
                //chord - pitch(step, octave) - duration - voice

                Element noteTag = noteTagsList.get(z);
                wNote newWNote = new wNote();

                try
                {
                    Element chordTag = noteTag.getChild("chord");
                    if (chordTag != null)
                    {
                        newWNote.setChord(true);
                    }
                }
                catch (Exception e){}

                Element pitchTag = noteTag.getChild("pitch");
                Element stepTag = pitchTag.getChild("step");
                Element octaveTag = pitchTag.getChild("octave");
                int pitchOctave = Integer.parseInt( octaveTag.getValue() );
                newWNote.setPitch(stepTag.getValue(), pitchOctave);

                try
                {
                    Element durationTag = noteTag.getChild("duration");
                    int duration = Integer.parseInt( durationTag.getValue() );
                    newWNote.setDuration(duration);
                }
                catch (Exception e){}

                try
                {
                    Element voiceTag = noteTag.getChild("voice");
                    int noteVoice = Integer.parseInt( voiceTag.getValue() );
                    newWNote.setVoice(noteVoice);
                }
                catch (Exception e){}

                try
                {
                    Element typeTag = noteTag.getChild("type");
                    newWNote.setType(typeTag.getValue());
                }

                catch (Exception e){}

                try
                {
                    Element notationsTag = noteTag.getChild("notations");
                    Element technicalTag = notationsTag.getChild("technical");
                    Element stringTag = technicalTag.getChild("string");
                    Element fretTag = technicalTag.getChild("fret");
                    String noteString = stringTag.getValue();
                    String fret = fretTag.getValue();

                    newWNote.setNoteString(Integer.parseInt(noteString));
                    newWNote.setFret(Integer.parseInt(fret));

                }
                catch (Exception e){}

                notesList.add(newWNote);
            }

            newWMeasure.addNotes(notesList);
            wMeasureList.add(newWMeasure);
        }

        wPartOne.setMeasures(wMeasureList);
        this.mainWPart = wPartOne;
        //File output = new File("/Users/walido/Documents/University/2nd Year/2nd Year - Winter 2022/EECS 2311/Project Components/Parser Tests/output.txt");
        //File output = new File("output.txt");
//        Writer output = new FileWriter("output.txt", false);
//        try {
//            output.write(wPartOne.toString());
//            output.flush();
//            output.close();
//        }
//        catch(IOException e){
//            System.out.println(e);
//        }

    }

    public wPart getPart()
    {
        return this.mainWPart;
    }
}