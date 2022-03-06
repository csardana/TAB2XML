package GUI;

import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import models.content.ContentManager;

public class PreviewController {
	ContentManager musicContent;
	@FXML ScrollPane scrollPane;
	
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
	}
	
	public void buildPane() {
		GridPane gridPane = (new PreviewFX(this.musicContent)).getGridPane();
		scrollPane.setContent(gridPane);
	}

}
