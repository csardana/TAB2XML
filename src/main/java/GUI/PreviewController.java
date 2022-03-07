package GUI;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.xml.parsers.ParserConfigurationException;

import org.jfugue.integration.MusicXmlParser;
import org.jfugue.player.ManagedPlayer;
import org.jfugue.player.Player;
import org.staccato.StaccatoParserListener;

import converter.Converter;
import converter.Instrument;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import models.content.ContentManager;
import nu.xom.ParsingException;
import nu.xom.ValidityException;
import utility.Settings;

public class PreviewController {
	ContentManager musicContent;
	GridPane gridPane;
	File file;
	String instrumentCheck;
	MainViewController mvc;
	ManagedPlayer managedPlayer;

	@FXML BorderPane borderPane;
	@FXML ScrollPane scrollPane;
	@FXML Button goToMeasureButton;
	@FXML Button saveMusicSheetButton;
	@FXML Button playMusicSheetButton;
	@FXML Button pauseMusicSheetButton;
	
	// Column span preferred width: 750px
	// Row span height: 200px
	
	public PreviewController() {
	}
	
	@FXML
	public void initialize() {
		System.out.println("Preview Launched");
	}
	
	public void setContentManager(MainViewController mvc, ContentManager musicContent) {
		this.musicContent = musicContent;
		this.mvc = mvc;
		if(Settings.getInstance().getInstrument() == Instrument.GUITAR) {
			this.instrumentCheck = "GUITAR";
			this.buildPane();
			
		} else if(Settings.getInstance().getInstrument() == Instrument.DRUMS) {
			// Implement later...
			Label label = new Label();
			label.setPrefWidth(750);
			label.setText("Feature is still in development.");
			label.setFont(Font.font(50));
			label.setAlignment(Pos.CENTER);
			scrollPane.setContent(label);
			
			this.playMusicSheetButton.setDisable(true);
	        this.pauseMusicSheetButton.setDisable(true);
	        this.goToMeasureButton.setDisable(true);
	        this.saveMusicSheetButton.setDisable(true);
			// this.instrumentCheck = "BASS_DRUM";
			
		} else { // BASS
			// Implement later.
			// this.instrumentCheck = "Acoustic_Bass";
			
		}
	}
	
	private void buildPane() {
		GridPane gridPane = (new PreviewFX(this.musicContent)).getGridPane();
		this.gridPane = gridPane;
		scrollPane.setContent(gridPane);
	}
	
	@FXML
	private void saveMusicSheetButtonHandle() {
		//TO-DO:
		// - Add an extension filter that allows the user to make a *.pdf.
		
		Window stage = saveMusicSheetButton.getScene().getWindow();
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Save");
		fileChooser.setInitialDirectory(new File(Settings.getInstance().outputFolder));
		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image", "*.png"));
		File newFile = fileChooser.showSaveDialog(stage);
		//System.out.println(fileChooser.getSelectedExtensionFilter());
		if(newFile != null) {
			try {
				WritableImage image = gridPane.snapshot(new SnapshotParameters(), null);
				ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", newFile);
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	@FXML
	private void playMusicSheetButtonHandle() throws ValidityException, ParsingException, IOException, ParserConfigurationException {
		// TO-DO:
		// - Reorganize if time permits.

		StaccatoParserListener listener = new StaccatoParserListener();
		MusicXmlParser parser = new MusicXmlParser();
        parser.addParserListener(listener);
        Converter conv = new Converter(this.mvc);
        conv.update();
        parser.parse(conv.getMusicXML());
        Player player = new Player();
        org.jfugue.pattern.Pattern musicXMLPattern = listener.getPattern().setTempo(300).setInstrument(this.instrumentCheck);
        Sequence mySequence = player.getSequence(musicXMLPattern);
        ManagedPlayer OurPlayer = player.getManagedPlayer();
        this.managedPlayer = OurPlayer;
        try {
			this.managedPlayer.start(mySequence);
		} catch (InvalidMidiDataException e) {
			e.printStackTrace();
		} catch (MidiUnavailableException e) {
			e.printStackTrace();
		}
        
        // Disables / enables buttons to be clicked.
        //this.playMusicSheetButton.setDisable(true);
        //this.pauseMusicSheetButton.setDisable(false);
        this.mvc.convertWindow.setOnCloseRequest(event -> this.onExit()); // Close property enabled.
	}
	
	@FXML
	private void pauseMusicSheetButtonHandle() {
		this.managedPlayer.pause();
		this.managedPlayer.getTickLength();
		//this.playMusicSheetButton.setDisable(false);
        //this.pauseMusicSheetButton.setDisable(true);
	}
	
	private void onExit() {
		if(this.managedPlayer.isPlaying() || this.managedPlayer.isPaused()) {
			this.managedPlayer.finish();
		}
	}
	
	@FXML
	private void goToMeasureButtonHandle() {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Unfinished Feature");
		alert.setHeaderText("Using the go to measure feature is still in development!");
		alert.setContentText("Try accessing this feature in a later version of this application.");
		alert.show();
	}

}
