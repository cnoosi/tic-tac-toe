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
        Thread serverThread = new Thread(new ServerProcess(primaryStage));
        serverThread.start();
        primaryStage.show();
    }
    public static void main(String [] args)
    {
        launch(args);
    }
}
