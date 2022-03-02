package GUI;

import java.io.Closeable;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.xml.parsers.ParserConfigurationException;

import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.model.StyleSpans;
import org.jfugue.integration.MusicXmlParser;
import org.jfugue.player.ManagedPlayer;
import org.jfugue.player.Player;
import org.staccato.StaccatoParserListener;

import converter.Converter;
import converter.Instrument;
import converter.measure.TabMeasure;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.IndexRange;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import models.content.ContentManager;
import models.content.GuitarContent;
import nu.xom.ParsingException;
import utility.Range;
import utility.Settings;

public class MainViewController extends Application {
	
	private Preferences prefs;
	public static ExecutorService executor = Executors.newSingleThreadExecutor();
	public File saveFile;
	private static boolean isEditingSavedFile;
	
	public Window convertWindow;
	public Window settingsWindow;

	public Highlighter highlighter;
	public Converter converter;

	@FXML  Label mainViewState;
	@FXML  TextField instrumentMode;
	
	@FXML public CodeArea mainText;

	@FXML  TextField gotoMeasureField;
	@FXML  BorderPane borderPane;
	@FXML  Button saveTabButton;
	@FXML  Button saveMXLButton;
	@FXML  Button showMXLButton;
	@FXML  Button previewButton;
	@FXML  Button goToline;
	@FXML  ComboBox<String> cmbScoreType;


	public MainViewController() {
		Settings s = Settings.getInstance();
		prefs = Preferences.userRoot();
		s.inputFolder = prefs.get("inputFolder", System.getProperty("user.home"));
		s.outputFolder = prefs.get("outputFolder", System.getProperty("user.home"));
		s.tsNum = Integer.parseInt(prefs.get("tsNum", "4"));
		s.tsDen = Integer.parseInt(prefs.get("tsDen", "4"));
		s.errorSensitivity = Integer.parseInt(prefs.get("errorSensitivity", "4"));
	}

	@FXML 
	public void initialize() {
		mainText.setParagraphGraphicFactory(LineNumberFactory.get(mainText));
		converter = new Converter(this);
		highlighter = new Highlighter(this, converter);
    	listenforTextAreaChanges();
	}

	@FXML
	private void handleCurrentSongSettings() {
		Parent root;
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("GUI/currentSongSettingsWindow.fxml"));
			root = loader.load();
			CurrentSongSettingsWindowController controller = loader.getController();
			controller.setMainViewController(this);
			settingsWindow = this.openNewWindow(root, "Current Song Settings");
		} catch (IOException e) {
			Logger logger = Logger.getLogger(getClass().getName());
			logger.log(Level.SEVERE, "Failed to create new Window.", e);
		}
	}
	
	@FXML
	private void handleSystemDefaultSettings() {
		Parent root;
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("GUI/systemDefaultSettingsWindow.fxml"));
			root = loader.load();
			SystemDefaultSettingsWindowController controller = loader.getController();
			controller.setMainViewController(this);
			settingsWindow = this.openNewWindow(root, "System Default Settings");
		} catch (IOException e) {
			Logger logger = Logger.getLogger(getClass().getName());
			logger.log(Level.SEVERE, "Failed to create new Window.", e);
		}
	}

	@FXML
	private void handleNew() {
		boolean userOkToGoAhead = promptSave();
		if (!userOkToGoAhead) return;
		this.mainText.clear();
		instrumentMode.setText("None");
		isEditingSavedFile = false;
	}

	@FXML
	private void handleOpen() {
		boolean userOkToGoAhead = promptSave();
		if (!userOkToGoAhead) return;

		String startFolder = prefs.get("inputFolder", System.getProperty("user.home"));
		File openDirectory;
		if (saveFile!=null && saveFile.canRead()) {
			openDirectory = new File(saveFile.getParent());
		}else
			openDirectory = new File(startFolder);

		if(!openDirectory.canRead()) {
			openDirectory = new File("c:/");
		}

		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
		fileChooser.setInitialDirectory(openDirectory);
		File openedFile = fileChooser.showOpenDialog(MainApp.STAGE);
		if (openedFile==null) return;
		if (openedFile.exists()) {
			try {
				String newText = Files.readString(Path.of(openedFile.getAbsolutePath())).replace("\r\n", "\n");
				mainText.replaceText(new IndexRange(0, mainText.getText().length()), newText);
			}catch (Exception e) {
				e.printStackTrace();
			}
		}

		saveFile = openedFile;
		isEditingSavedFile = true;

	}
//hi 
	@FXML
	private boolean handleSaveAs() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Save As");
		fileChooser.setInitialDirectory(new File(Settings.getInstance().outputFolder));
		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
		if (saveFile!=null) {
			fileChooser.setInitialFileName(saveFile.getName());
			fileChooser.setInitialDirectory(new File(saveFile.getParent()));
		}

		File newSaveFile = fileChooser.showSaveDialog(MainApp.STAGE);
		if (newSaveFile==null) return false;
		try {
			FileWriter myWriter = new FileWriter(newSaveFile.getPath());
			myWriter.write(mainText.getText());
			myWriter.close();

			saveFile = newSaveFile;
			isEditingSavedFile = true;
		} catch (IOException e) {
			return false;
		}
		return true;
	}

	@FXML
	private boolean handleSave() {
		if (!isEditingSavedFile || saveFile==null || !saveFile.exists())
			return this.handleSaveAs();
		try {
			FileWriter myWriter = new FileWriter(saveFile.getPath());
			myWriter.write(mainText.getText());
			myWriter.close();
		} catch (IOException e) {
			return false;
		}
		return true;
	}

	private boolean promptSave() {

		//we don't care about overwriting a blank file. If file is blank, we are ok to go. it doesn't matter if it is saved or not
		if (mainText.getText().isBlank())  return true;

		try {
			if (saveFile!=null && Files.readString(Path.of(saveFile.getAbsolutePath())).replace("\r\n", "\n").equals(mainText.getText()))
				return true;    //if file didn't change, we are ok to go. no need to save anything, no chance of overwriting.
		}catch (Exception e){
			e.printStackTrace();
		}

		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("Unsaved file");
		alert.setHeaderText("This document is unsaved and will be overwritten. Do you want to save it first?");
		alert.setContentText("Choose your option.");

		ButtonType buttonTypeSave = new ButtonType("Save");
		ButtonType buttonTypeOverwrite = new ButtonType("Overwrite");
		ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

		alert.getButtonTypes().setAll(buttonTypeSave, buttonTypeOverwrite, buttonTypeCancel);

		Optional<ButtonType> result = alert.showAndWait();

		boolean userOkToGoAhead = false;
		if (result.get() == buttonTypeSave){
			boolean saved;
			if (isEditingSavedFile) {
				saved = handleSave();
			}else {
				saved = handleSaveAs();
			}
			if (saved)
				userOkToGoAhead = true;
		} else if (result.get() == buttonTypeOverwrite) {
			// ... user chose "Override". we are good to go ahead
			userOkToGoAhead = true;
		}
		//if user chose "cancel", userOkToGoAhead is still false. we are ok.
		return userOkToGoAhead;
	}

	private Window openNewWindow(Parent root, String windowName) {
		Stage stage = new Stage();
		stage.setTitle(windowName);
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.initOwner(MainApp.STAGE);
		stage.setResizable(false);
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
		return scene.getWindow();
	}

	@FXML
	private void saveTabButtonHandle() {
		Parent root;
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("GUI/convertWindow.fxml"));
			root = loader.load();
			SaveMXLController controller = loader.getController();
			controller.setMainViewController(this);
			convertWindow = this.openNewWindow(root, "ConversionOptions");
		} catch (IOException e) {
			Logger logger = Logger.getLogger(getClass().getName());
			logger.log(Level.SEVERE, "Failed to create new Window.", e);
		}
	}
	
	@FXML
	void saveMXLButtonHandle() {
		Parent root;
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("GUI/saveMXLWindow.fxml"));
			root = loader.load();
			SaveMXLController controller = loader.getController();
			controller.setMainViewController(this);
			convertWindow = this.openNewWindow(root, "Save MusicXML");
		} catch (IOException e) {
			Logger logger = Logger.getLogger(getClass().getName());
			logger.log(Level.SEVERE, "Failed to create new Window.", e);
		}
	}
	
	@FXML
	private void showMXLButtonHandle() {
		Parent root;
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("GUI/showMXL.fxml"));
			root = loader.load();
			ShowMXLController controller = loader.getController();
			controller.setMainViewController(this);
			controller.update();
			convertWindow = this.openNewWindow(root, "MusicXML output");
		} catch (IOException e) {
			Logger logger = Logger.getLogger(getClass().getName());
			logger.log(Level.SEVERE, "Failed to create new Window.", e);
		}
	}

	@FXML
	private void previewButtonHandle() throws IOException {
		System.out.println("Preview Button Clicked!");
		
		ContentManager musicContent = null;
		if(Settings.getInstance().getInstrument() == Instrument.GUITAR) {
			musicContent = new GuitarContent(converter.getScore().getModel());
			((GuitarContent) musicContent).getCrucialNoteData(1);
		} else if(Settings.getInstance().getInstrument() == Instrument.DRUMS) {
			// Implement later.
		} else { // BASS
			// Implement later.
		}

		musicContent.getMeasureNotePitch(1);
		
		try {
	        
			
			// See if you can load an FXML several times to implement several staffs.
		
			Stage stage = new Stage();
			//Group root = new Group(); // A basic root node that is a "grouping" of nodes
			
			AnchorPane root = new AnchorPane();
			root.setPrefSize(600, 400);
			
			//FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("GUI/Preview.fxml"));
			//Parent root = loader.load();
			int[] linePositions = {40, 60, 80, 100, 120, 140};
			
			ScrollPane scroll = new ScrollPane();
			scroll.setPrefSize(550, 350);
			scroll.setPadding(new Insets(0,0,0,0));
			AnchorPane.setLeftAnchor(scroll, 25.0);
			AnchorPane.setTopAnchor(scroll, 25.0);
			
			Pane pane = new Pane();
			
			// Staff lines:
			Line bar1 = new Line();
			bar1.setStroke(Color.BLACK);
			bar1.setLayoutX(25);
			bar1.setStartY(-50);
			bar1.setEndY(50);
			bar1.setLayoutY(90);
			bar1.setStrokeWidth(3);
			
	        Line line1 = new Line();
			line1.setStroke(Color.BLACK);
			line1.setLayoutX(275);
			line1.setStartX(-250);
			line1.setEndX(250);
			line1.setLayoutY(40);
			
			Line line2 = new Line();
			line2.setStroke(Color.BLACK);
			line2.setLayoutX(275);
			line2.setStartX(-250);
			line2.setEndX(250);
			line2.setLayoutY(60);
			
			Line line3 = new Line();
			line3.setStroke(Color.BLACK);
			line3.setLayoutX(275);
			line3.setStartX(-250);
			line3.setEndX(250);
			line3.setLayoutY(80);
			
			Line line4 = new Line();
			line4.setStroke(Color.BLACK);
			line4.setLayoutX(275);
			line4.setStartX(-250);
			line4.setEndX(250);
			line4.setLayoutY(100);
			
			Line line5 = new Line();
			line5.setStroke(Color.BLACK);
			line5.setLayoutX(275);
			line5.setStartX(-250);
			line5.setEndX(250);
			line5.setLayoutY(120);
			
			Line line6 = new Line();
			line6.setStroke(Color.BLACK);
			line6.setLayoutX(275);
			line6.setStartX(-250);
			line6.setEndX(250);
			line6.setLayoutY(140);
			
			// TAB clef:
			Text t = new Text();
			t.setText("T");
			t.setFont(Font.font("Arial", 30));
			t.setLayoutX(30);
			t.setLayoutY(70);
			Text a = new Text();
			a.setText("A");
			a.setFont(Font.font("Arial", 30));
			a.setLayoutX(30);
			a.setLayoutY(100);
			Text b = new Text();
			b.setText("B");
			b.setFont(Font.font("Arial", 30));
			b.setLayoutX(30);
			b.setLayoutY(130);
			
			// Time Signature of Measure:
			int[] timeSig = musicContent.getMeasureTimeSig(1);
			Text tsnum = new Text();
			tsnum.setText(timeSig[0] + "");
			tsnum.setFont(Font.font("Arial", 30));
			tsnum.setLayoutX(60);
			tsnum.setLayoutY(80);
			Text tsden = new Text();
			tsden.setText(timeSig[1] + "");
			tsden.setFont(Font.font("Arial", 30));
			tsden.setLayoutX(60);
			tsden.setLayoutY(120);
			
			StaccatoParserListener listener = new StaccatoParserListener();
			MusicXmlParser parser = new MusicXmlParser();
	        parser.addParserListener(listener);
	        Converter conv = new Converter(this);
	        conv.update();
	        parser.parse(conv.getMusicXML());
	        Player player = new Player();
	        org.jfugue.pattern.Pattern musicXMLPattern = listener.getPattern().setTempo(300).setInstrument("guitar");
	        Sequence mySequence = player.getSequence(musicXMLPattern);
	        ManagedPlayer OurPlayer = player.getManagedPlayer();

			Button button = new Button("Play Music");
			button.setOnAction(value ->  {
				
			
					
			        try {
						OurPlayer.start(mySequence);
					} catch (InvalidMidiDataException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (MidiUnavailableException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}


		        });
			
			Button button1 = new Button("Pause Music");
			button1.setOnAction(value ->  {
				
				System.out.print("paused");

		        OurPlayer.pause();


		        });
			
//			Button button2 = new Button("Reset Music");
//			button.setOnAction(value ->  {
//				
//			
//					
//			        OurPlayer.reset();
//
//
//		        });

		    HBox hbox = new HBox();

		    HBox.setHgrow(button, Priority.ALWAYS);
		    HBox.setHgrow(button1, Priority.ALWAYS);
//		    HBox.setHgrow(button2, Priority.ALWAYS);
		    hbox.getChildren().add(button);
		    hbox.getChildren().add(button1);
//		    hbox.getChildren().add(button2);
		    
			
			pane.setPrefSize(550, 180);
			pane.setStyle("-fx-background-color: #ffffff;" + "-fx-border-color: #000000");
			pane.getChildren().add(bar1);
			pane.getChildren().add(line1);
			pane.getChildren().add(line2);
			pane.getChildren().add(line3);
			pane.getChildren().add(line4);
			pane.getChildren().add(line5);
			pane.getChildren().add(line6);
			pane.getChildren().add(t);
			pane.getChildren().add(a);
			pane.getChildren().add(b);
			pane.getChildren().add(tsnum);
			pane.getChildren().add(tsden);
//			pane.getChildren().add(button);
			pane.getChildren().add(hbox);
			
			
			double counter = tsden.getLayoutX() + 40; // After timeSig x position + 40 units
			for(int i = 0; i < ((GuitarContent) musicContent).getCrucialNoteData(1).length; i++) {
				// A lot of controls are needed to determine chords, bar lines, etc.
				// Stackpane is needed here to achieve a background color
				Text digit = new Text();
				digit.setText(((GuitarContent) musicContent).getCrucialNoteData(1)[i][1] + "");
				digit.setLayoutY(linePositions[((GuitarContent) musicContent).getCrucialNoteData(1)[i][0] - 1] + 5);
				digit.setLayoutX(counter);
				digit.setFont(Font.font("Arial",15));
				digit.setStyle("-fx-highlight-fill: #000000");
				pane.getChildren().add(digit);
				counter += 30;
			}
			
			scroll.setContent(pane);
			root.getChildren().add(scroll);
			
			Scene scene = new Scene(root);
			scene.setFill(Color.WHITE);
			
			stage.setTitle("Preview Music");
			stage.setWidth(615);
			stage.setHeight(400);
			stage.setResizable(false);
			
			stage.setScene(scene);
			stage.show();
			
			stage.setOnCloseRequest(event -> {
				OurPlayer.finish();
			    // Save file
			});
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		// converter.getMusicXML() returns the MusicXML output as a String
		
	
	}

	public void refresh() {
        mainText.replaceText(new IndexRange(0, mainText.getText().length()), mainText.getText()+" ");
    }

	@FXML
	private void handleGotoMeasure() {
		int measureNumber = Integer.parseInt( gotoMeasureField.getText() );
		if (!goToMeasure(measureNumber)) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setContentText("Measure " + measureNumber + " could not be found.");
			alert.setHeaderText(null);
			alert.show();
		}
	}
	
    private boolean goToMeasure(int measureCount) {
        TabMeasure measure = converter.getScore().getMeasure(measureCount);
        if (measure == null) return false;
        List<Range> linePositions = measure.getRanges();
        int pos = linePositions.get(0).getStart();
    	mainText.moveTo(pos);
        mainText.requestFollowCaret();
        mainText.requestFocus();
        return true;
    }

    public void listenforTextAreaChanges() {
        //Subscription cleanupWhenDone = 
    	mainText.multiPlainChanges()
                .successionEnds(Duration.ofMillis(350))
                .supplyTask(this::update)
                .awaitLatest(mainText.multiPlainChanges())
                .filterMap(t -> {
                    if(t.isSuccess()) {
                        return Optional.of(t.get());
                    } else {
                        t.getFailure().printStackTrace();
                        return Optional.empty();
                    }
                })
                .subscribe(highlighter::applyHighlighting);
    }
    
    public Task<StyleSpans<Collection<String>>> update() {
    	String text = mainText.getText();

        Task<StyleSpans<Collection<String>>> task = new Task<>() {
            @Override
            protected StyleSpans<Collection<String>> call() {
            	converter.update();
            	
                if (converter.getScore().getTabSectionList().isEmpty()){
                	saveMXLButton.setDisable(true);
                	previewButton.setDisable(true);
                	showMXLButton.setDisable(true);
                }
                else
                {
                	saveMXLButton.setDisable(false);
                	previewButton.setDisable(false);
                	showMXLButton.setDisable(false);
                }
                return highlighter.computeHighlighting(text);
            }
        };
        executor.execute(task);
        task.isDone();
        return task;
    }
    
	@Override
	public void start(Stage primaryStage) throws Exception {

	}
}