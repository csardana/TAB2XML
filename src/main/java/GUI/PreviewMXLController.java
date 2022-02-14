package GUI;

import javafx.application.Application;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

public class PreviewMXLController {
	public class Gui extends Application{
		public Gui() {
			Application.launch();
		}

		@Override
		public void start(Stage stage) throws Exception {
			// TODO Auto-generated method stub
			 Line line = new Line(); 
	         
		      //Setting the properties to a line 
		      line.setStartX(100.0); 
		      line.setStartY(150.0); 
		      line.setEndX(500.0); 
		      line.setEndY(150.0); 
		         
		      //Creating a Group 
		      Group root = new Group(line); 
		         
		      //Creating a Scene 
		      Scene scene = new Scene(root, 600, 300); 
		         
		      //Setting title to the scene 
		      stage.setTitle("Sample application"); 
		         
		      //Adding the scene to the stage 
		      stage.setScene(scene); 
		         
		      //Displaying the contents of a scene 
		      stage.show(); 
		}
	}
	
	    
	   
}
