package converter;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import GUI.MainViewController;
import utility.MusicXMLCreator;
import utility.ValidationError;
//Hi hello world
public class Converter {

//	private Score score;  // this is score
	private MusicXMLCreator mxlc;
	private MainViewController mvc;
	
	public Converter(MainViewController mvc) {
		this.mvc = mvc;
	}
	
	public void update() {
		score = new Score(mvc.mainText.getText());
		mxlc = new MusicXMLCreator(score);
	}
	
	public String getMusicXML() {
		return mxlc.generateMusicXML();
	}
	
	public Score getScore() {
		return score;
	}
	
	public List<ValidationError> validate() {
		return score.validate();
	}
    public void saveMusicXMLFile(File file) {
        try {
            PrintWriter writer;
            writer = new PrintWriter(file);
            writer.println(mxlc.generateMusicXML());
            writer.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
}
