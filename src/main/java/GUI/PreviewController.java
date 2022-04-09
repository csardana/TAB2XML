package GUI;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javafx.util.Duration;  

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
import javafx.animation.FadeTransition;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
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
	PreviewFX previewFX;
	
	@FXML BorderPane borderPane;
	@FXML ScrollPane scrollPane;
	@FXML Button goToMeasureButton;
	@FXML Button saveMusicSheetButton;
	@FXML Button playMusicSheetButton;
	@FXML Button pauseMusicSheetButton;
	@FXML TextField gotoMeasureField;

	
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
		if (Settings.getInstance().getInstrument() == Instrument.GUITAR) {
			this.instrumentCheck = "GUITAR";
			this.buildPane();

		} else if (Settings.getInstance().getInstrument() == Instrument.DRUMS) {
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
		previewFX = new PreviewFX(this.musicContent);
		GridPane gridPane = previewFX.getGridPane();
		this.gridPane = gridPane;
		scrollPane.setContent(gridPane);
	}

	@FXML
	private void saveMusicSheetButtonHandle() {
		// TO-DO:
		// - Add an extension filter that allows the user to make a *.pdf.

		Window stage = saveMusicSheetButton.getScene().getWindow();
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Save");
		fileChooser.setInitialDirectory(new File(Settings.getInstance().outputFolder));
		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image", "*.png"));
		File newFile = fileChooser.showSaveDialog(stage);
		if (newFile != null) {
			try {
				WritableImage image = gridPane.snapshot(new SnapshotParameters(), null);
				ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", newFile);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@FXML
	private void playMusicSheetButtonHandle()
			throws ValidityException, ParsingException, IOException, ParserConfigurationException {

//get the listener 
		StaccatoParserListener listener = new StaccatoParserListener();
		MusicXmlParser parser = new MusicXmlParser();
		parser.addParserListener(listener);
		Converter conv = new Converter(this.mvc);
		conv.update();
		parser.parse(conv.getMusicXML());
		//The player which plays the music 
		Player player = new Player();
		//can set the tempo here 
		org.jfugue.pattern.Pattern musicXMLPattern = listener.getPattern().setTempo(300)
				.setInstrument(this.instrumentCheck);
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
		// this.playMusicSheetButton.setDisable(true);
		// this.pauseMusicSheetButton.setDisable(false);
		this.mvc.convertWindow.setOnCloseRequest(event -> this.onExit()); // Close property enabled.
	}

	@FXML
	private void pauseMusicSheetButtonHandle() {
		this.managedPlayer.pause();
		this.managedPlayer.getTickLength();

	}

	private void onExit() {
		if (this.managedPlayer.isPlaying() || this.managedPlayer.isPaused()) {
			this.managedPlayer.finish();
		}
	}

	@FXML
	private void goToMeasureButtonHandle() {
		HashMap<String, String> posTrackerMap = previewFX.getPosTrackerList();
		String positionXY = posTrackerMap.get(gotoMeasureField.getText());
		String[] posXYArr = positionXY.split(":");
		Double posX = Double.parseDouble(posXYArr[0]);
		Integer posY = Integer.parseInt(posXYArr[1]);
		System.out.println("Goto Measure Clicked:" + gotoMeasureField.getText());
		if(posY == 0) {
			posY = 20;
		} else {
			posY = posY * 200;
		}
		
		Text text = new Text();
		text.setText(gotoMeasureField.getText());
		text.setTranslateX(posX);
		text.setTranslateY(posY);
		text.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 30));
		text.setFill(Color.GRAY);
		FadeTransition ft = new FadeTransition();
		ft.setDuration(Duration.millis(3000));
		ft.setFromValue(10);
		ft.setToValue(0);
		ft.setCycleCount(1);
		ft.setAutoReverse(true);
		ft.setNode(text);
		ft.play();
		gridPane.getChildren().add(text);
	}

}
