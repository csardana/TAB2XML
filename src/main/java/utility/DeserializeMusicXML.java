package utility;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import models.ScorePartwise;

public class DeserializeMusicXML {
	//private Score score;

	public DeserializeMusicXML(String musicXML) throws JsonMappingException, JsonProcessingException{
		XmlMapper xmlMapper = new XmlMapper();
		ScorePartwise value = xmlMapper.readValue(musicXML, ScorePartwise.class);
		/*
		 * Note 1: MusicXMLCreator was passed Score.getModel (RT- ScorePartwise) in the serialization
		 * of Score. Thus, the musicXML can be deserialized into a ScorePartwise object to access 
		 * music content.
		 * 
		 * Note 2: The Jackson library doesn't know how to create objects, so default constructors
		 * were implemented in almost all of the attribute types (and their attribute types) of class
		 * ScorePartwise to bypass errors associated with the creation of class instances.
		 * Exceptions: Work, String, ...
		 * 
		 * Note 3: Check Discord Notes for latest error from making default constructors repeatedly.
		 */
	}

}
