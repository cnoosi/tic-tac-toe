import Game.*;
import Networking.ClientProcess;
import Networking.ServerProcess;
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

        //This ONLY starts on the client, so we can start the ui here
        ClientProcess newClient = new ClientProcess(primaryStage);
        Thread clientThread = new Thread(newClient);
        clientThread.start();

        //OpenScene sceneOpener = new OpenScene();
        //sceneOpener.start(primaryStage, "/MenuUI/MenuUI.fxml", "Tic-Tac-Toe - Menu");
    }

    public static void main(String[] args) {
        System.out.println(args[0]);
        if (args[0].equals("server"))
        {
            Thread serverThread = new Thread(new ServerProcess());
            serverThread.start();
            //launch(args);
        }
        else if (args[0].equals("client"))
        {
            launch(args);
        }
    }
}

