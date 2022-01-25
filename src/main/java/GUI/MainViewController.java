package GUI;

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

import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.model.StyleSpans;

import converter.Converter;
import converter.measure.TabMeasure;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.IndexRange;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
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
			
//			chirag
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
		// converter.getMusicXML() returns the MusicXML output as a String
		
		Label secondLabel = new Label("Label");

		StackPane secondaryLayout = new StackPane();
		secondaryLayout.getChildren().add(secondLabel);

		Scene secondScene = new Scene(secondaryLayout, 230, 100);

		// New window (Stage)
		Stage newWindow = new Stage();
		newWindow.setTitle("Sheet Music");
		newWindow.setScene(secondScene);

		// Set position of second window, related to primary window.
		newWindow.setX( 200);
		newWindow.setY( 100);

		newWindow.show();
		
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