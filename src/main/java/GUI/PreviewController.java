package GUI;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import javafx.util.Duration;
import models.preview.content.ContentManager;
import models.preview.content.PreviewDrumMS;
import models.preview.content.PreviewGuitarMS;

import javax.imageio.ImageIO;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.xml.parsers.ParserConfigurationException;

import org.jfugue.integration.MusicXmlParser;
import org.jfugue.player.ManagedPlayer;
import org.jfugue.player.Player;
import org.staccato.StaccatoParserListener;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfDocument;
import com.itextpdf.text.pdf.PdfWriter;

import converter.Converter;
import converter.Instrument;
import javafx.animation.FadeTransition;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
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
	private int tempoSpeed = 150;

	@FXML BorderPane borderPane;
	@FXML ScrollPane scrollPane;
	@FXML Button goToMeasureButton;
	@FXML Button saveMusicSheetButton;
	@FXML Button playMusicSheetButton;
	@FXML Button pauseMusicSheetButton;
	@FXML TextField gotoMeasureField;
	@FXML Button tempoButton;
	@FXML TextField changeTempoField;



	
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
			this.previewFX = new PreviewFX(musicContent, 6);
			
		} else if (Settings.getInstance().getInstrument() == Instrument.DRUMS) {
			this.instrumentCheck = "DRUMS";
			this.previewFX = new PreviewFX(musicContent, 5);

		} else { // BASS
			this.instrumentCheck = "Acoustic_Bass";

		}
		
		this.buildPane(); // Construct gridPane.
	}

	private void buildPane() {
		GridPane gridPane = this.previewFX.getGridPane();
		this.gridPane = gridPane;
		scrollPane.setContent(gridPane);
	}

	private BufferedImage generate_png_from_container(WritableImage snapshot) {
        BufferedImage tempImg = SwingFXUtils.fromFXImage(snapshot, null);
        BufferedImage img = null;
        byte[] imageInByte;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(tempImg, "png", baos);
            baos.flush();
            imageInByte = baos.toByteArray();
            baos.close();
            InputStream in = new ByteArrayInputStream(imageInByte);
            img = ImageIO.read(in);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
       //the final image sent to the PDJpeg
       return img;
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
		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("PDF", "*.pdf"));
		File newFile = fileChooser.showSaveDialog(stage);
		if (newFile != null) {
			String fileName = newFile.getName();
			String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
			if(fileExtension.equals("png")) {
				try {
					WritableImage image = gridPane.snapshot(new SnapshotParameters(), null);
					ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", newFile);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				WritableImage image = gridPane.snapshot(new SnapshotParameters(), null);
				BufferedImage bufferedImage = generate_png_from_container(image);
				Document document = new Document();
		        PdfWriter writer;
				try {
					writer = PdfWriter.getInstance(document,
					                       new FileOutputStream(newFile.getAbsolutePath()));
			        document.open();
			        PdfContentByte pdfCB = new PdfContentByte(writer);
			        Image image2 = Image.getInstance(pdfCB, bufferedImage, 1);
			        image2.scaleToFit(500, 500);
			        document.add(image2);
			        document.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (DocumentException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@FXML
	private void playMusicSheetButtonHandle()
			throws ValidityException, ParsingException, IOException, ParserConfigurationException {
		// TO-DO:
		// - Reorganize if time permits.

		StaccatoParserListener listener = new StaccatoParserListener();
		MusicXmlParser parser = new MusicXmlParser();
		parser.addParserListener(listener);
		Converter conv = new Converter(this.mvc);
		conv.update();
		parser.parse(conv.getMusicXML());
		Player player = new Player();
		org.jfugue.pattern.Pattern musicXMLPattern = listener.getPattern().setTempo(this.tempoSpeed)
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
		// this.playMusicSheetButton.setDisable(false);
		// this.pauseMusicSheetButton.setDisable(true);
	}

	private void onExit() {
		if (this.managedPlayer.isPlaying() || this.managedPlayer.isPaused()) {
			this.managedPlayer.finish();
		}
	}
	@FXML
	private void tempoButtonHandle() {
		try {
			this.tempoSpeed = Integer.parseInt(changeTempoField.getText());
		  } catch (NumberFormatException e) {
			  Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setContentText("Enter a valid integer");
				alert.setHeaderText(null);
				alert.show();
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
			posY = posY * 220;
		}
		
		Text text = new Text();
		text.setText(gotoMeasureField.getText());
		text.setTranslateX(posX);
		text.setTranslateY(posY);
		text.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 30));
		text.setFill(Color.GRAY);
		FadeTransition ft = new FadeTransition();
		ft.setDuration(Duration.millis(4000));
		ft.setFromValue(10);
		ft.setToValue(0);
		ft.setCycleCount(1);
		ft.setAutoReverse(true);
		ft.setNode(text);
		ft.play();
		gridPane.getChildren().add(text);
		double textBound = posY + 70.00;
		double thisBound = gridPane.getBoundsInLocal().getMaxY();
		double scrollY = textBound/thisBound;
		if(scrollY < 0.1) {
			scrollY = 0.0;
		} else if(scrollY > 0.8) {
			scrollY = 1.0;
		}
		scrollPane.setVvalue(scrollY);
	}

}
