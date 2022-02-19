package utility;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import converter.Score;
import custom_exceptions.TXMLException;

public class MusicXMLCreator {
    private Score score;

    public MusicXMLCreator(Score s) {
        score = s;
    }

    public String generateMusicXML() {
        if(score.at.text.isBlank()){
            return "";
        }
        XmlMapper mapper = new XmlMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        String xmlString = "";
        try {
            xmlString = mapper.writeValueAsString(score.getModel());
            xmlString = xmlString.replace("noteBefore", "note");
            xmlString = xmlString.replace("noteAfter", "note");
            xmlString = xmlString.replaceAll("\\R[ \\t]*<midiinstruments>[\\s\\S]*</midiinstruments>[ \\t]*\\R", "\n");
            xmlString = xmlString.replaceAll("\\R[ \\t]*<timeModification>\\s*<actual-notes>\\d+</actual-notes>\\s*<normal-notes>\\d+</normal-notes>\\s*</timeModification>[ \\t]*\\R", "\n");
           
        }catch (JsonProcessingException e) {
        	// Erased 'throws TXMLException' in the Catch block.
            e.printStackTrace();
            return "";
        }

        xmlString = """
                <?xml version="1.0" encoding="UTF-8" standalone="no"?>
                <!DOCTYPE score-partwise PUBLIC "-//Recordare//DTD MusicXML 3.1 Partwise//EN" "http://www.musicxml.org/dtds/partwise.dtd">
                """
                + xmlString;
        return xmlString;
    }
    
//    public static Instrument getInstrumentEnum(String instrument) {
//        if (instrument.equalsIgnoreCase("guitar"))
//            return Instrument.GUITAR;
//        else if (instrument.equalsIgnoreCase("drum"))
//            return Instrument.DRUMS;
//        else if (instrument.equalsIgnoreCase("bass"))
//            return Instrument.BASS;
//        else
//            return Instrument.AUTO;
//    }

//    public static void setTitle(String title) {
//        if (SCORE==null || title.isBlank()) return;
//        SCORE.title = title;
//    }
//    
//    public static void setArtist(String artist) {
//        if (SCORE==null || artist.isBlank()) return;
//        SCORE.artist = artist;
//    }

}
