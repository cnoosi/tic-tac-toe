package UserInterface;

import AdminDashUI.AdminDashUIController;
import MenuUI.MenuUIController;
import Networking.DbManager;
import Networking.ServerProcess;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ServerUIProcess
{
    private ServerProcess serverProcess;
    private Stage primaryStage;
    private Parent adminDashRoot;
    private AdminDashUIController adminController;
    private FXMLLoader adminLoader;
    private Scene scene;


    public ServerUIProcess(ServerProcess serverProcess, Stage primaryStage)
    {
        this.serverProcess = serverProcess;
        this.primaryStage  = primaryStage;
        load();

        // default
        scene = new Scene(adminDashRoot);
        serverProcess.
    }

    public void load()
    {
        try
        {
            adminLoader = new FXMLLoader(getClass().getResource("/AdminDashUI/AdminDashUI.fxml"));
            adminDashRoot = adminLoader.load();
            adminController = adminLoader.getController();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void openPage(String page)
    {
        if(page.equals("Menu"))
        {
            scene.setRoot(adminDashRoot);
            primaryStage.setScene(scene);
            primaryStage.show();
        }

    }
}
