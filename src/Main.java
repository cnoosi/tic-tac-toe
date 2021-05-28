import Game.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Main extends Application
{
    @Override
    public void start(Stage primaryStage) throws Exception
    {
        Font.loadFont(getClass().getResourceAsStream("/resources/images/Streamster.ttf"), 12);
        OpenScene sceneOpener = new OpenScene();
        sceneOpener.start(primaryStage, "/MenuUI/MenuUI.fxml", "Tic-Tac-Toe - Menu");
    }

    public static void main(String[] args) {
        launch(args);
    }
}

