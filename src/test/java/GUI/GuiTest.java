package GUI;

import java.io.IOException;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxAssert;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.matcher.base.NodeMatchers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


@Disabled
@ExtendWith(ApplicationExtension.class)
public class GuiTest  extends ApplicationTest {
    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("GUI/mainView.fxml"));
        Scene scene = new Scene(root);


        scene.getStylesheets().add(getClass().getClassLoader().getResource("GUI/styles.css").toExternalForm());

        stage.setTitle("TAB 2 XML");
        stage.setMinWidth(700);
        stage.setMinHeight(500);
        stage.setScene(scene);
        stage.show();
    }

    @Test
    public void testEmptyInput(FxRobot robot) {
        robot.clickOn("#convertButton");
        FxAssert.verifyThat("#convertButton", NodeMatchers.isDisabled());
    }

    @Test
    public void invalidInput(FxRobot robot) {
        robot.clickOn("#TEXT_AREA");
        robot.write("this text is not a valid measure", 0);
        FxAssert.verifyThat("#convertButton", NodeMatchers.isDisabled());
    }

}

