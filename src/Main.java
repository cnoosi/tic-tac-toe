import Game.*;
import Networking.ServerProcess;
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
        sceneOpener.start(primaryStage, "/MenuUI/MenuUI.fxml", "Tic-Tac-Toe - Menu");
    }

    public static void main(String[] args) {
        if (args[0] == "server")
        {
            Thread serverThread = new Thread(new ServerProcess());
            serverThread.start();
        }
        else if (args[0] == "client")
        {

        }
        launch(args);
    }
}

