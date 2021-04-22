import Game.*;
import Networking.ClientProcess;
import Networking.ServerProcess;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sqlite.connect.net.sqlitetutorial;;

public class Main extends Application
{
    connect();
    @Override
    public void start(Stage primaryStage) throws Exception
    {
        OpenScene sceneOpener = new OpenScene();
        sceneOpener.start(primaryStage, "/MenuUI/MenuUI.fxml", "Tic-Tac-Toe - Menu");
    }

    public static void main(String[] args) {
        System.out.println(args[0]);
        if (args[0].equals("server"))
        {
            Thread serverThread = new Thread(new ServerProcess());
            serverThread.start();
        }
        else if (args[0].equals("client"))
        {
            //Eventually turn this into a ClientProcess!
            Thread clientThread = new Thread(new ClientProcess());
            clientThread.start();

            launch(args);
        }
    }
}

