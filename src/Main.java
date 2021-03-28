import Game.*;
import Networking.ServerProcess;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Main extends Application
{
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
            try
            {
                Socket socket = new Socket("localhost", 8000);
                DataInputStream inputStream = new DataInputStream(socket.getInputStream());
                DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());

                boolean continueMessaging = true;
                Scanner input = new Scanner(System.in);
                while (continueMessaging)
                {
                    System.out.print("Enter a message: ");
                    String newMessage = input.nextLine();
                    outputStream.writeUTF(newMessage);
                }
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
            launch(args);
        }
    }
}

