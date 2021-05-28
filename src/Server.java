import Game.*;
import Networking.ClientProcess;
import Networking.ServerProcess;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Server extends Application
{
    @Override
    public void start(Stage primaryStage) throws Exception
    {
        ServerProcess server = new ServerProcess(primaryStage);
        Thread serverThread = new Thread(server);
        serverThread.start();
    }
    public static void main(String [] args)
    {
        launch(args);
    }
}
