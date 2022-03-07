package GUI;

import java.io.File;

import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;

import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import models.content.ContentManager;
import utility.Settings;

public class PreviewController {
	ContentManager musicContent;
	GridPane gridPane;
	File file;

	@FXML BorderPane borderPane;
	@FXML ScrollPane scrollPane;
	@FXML Button saveMusicSheetButton;
	
	// Column span preferred width: 750px
	// Row span height: 200px
	
	public PreviewController() {
	}
	
	@FXML
	public void initialize() {
		System.out.println("Preview Launched");
	}
	
	public void setContentManager(ContentManager musicContent) {
		this.musicContent = musicContent;
		this.buildPane();
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

}
