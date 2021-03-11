import Game.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application
{
    @Override
    public void start(Stage primaryStage) throws Exception
    {
        OpenScene sceneOpener = new OpenScene();
        //Parent menuRoot = FXMLLoader.load(getClass().getResource("/MenuUI/MenuUI.fxml"));
        //Parent gameRoot = FXMLLoader.load(getClass().getResource("/BoardUI/BoardUI.fxml"));
        sceneOpener.start(primaryStage, "/MenuUI/MenuUI.fxml", "Tic-Tac-Toe - Menu");
        //primaryStage.setTitle("Tic Tac Toe - Menu");
        //primaryStage.setResizable(false);
        //primaryStage.setScene(new Scene(menuRoot));
        //primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

