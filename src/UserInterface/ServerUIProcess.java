package UserInterface;

import AdminDashUI.AdminDashUIController;
import MenuUI.MenuUIController;
import Networking.ServerProcess;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;

public class ServerUIProcess
{
    ServerProcess serverProcess;
    Stage primaryStage;
    Parent adminDashRoot;
    AdminDashUIController adminController;
    FXMLLoader adminLoader;

    public ServerUIProcess(ServerProcess serverProcess, Stage primaryStage)
    {
        this.serverProcess = serverProcess;
        this.primaryStage  = primaryStage;
        load();
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

    }
}
