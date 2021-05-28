package UserInterface;

import AdminDashUI.AdminDashUIController;
import MenuUI.MenuUIController;
import Networking.*;
import Observers.Observer;
import Observers.ObserverMessage;
import Observers.Subject;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Map;

public class ServerUIProcess implements Subject, Observer
{
    private ServerProcess serverProcess;
    private Stage primaryStage;
    private Parent adminDashRoot;
    private AdminDashUIController adminController;
    private FXMLLoader adminLoader;
    private Scene scene;
    private Observer uiObserver;

    ArrayList<String> ActiveGamesInfo = new ArrayList<>();
    ArrayList<String> CompletedGamesInfo = new ArrayList<>();
    ArrayList<String> RegisteredPlayersInfo = new ArrayList<>();
    ArrayList<String> ActiveClientsInfo = new ArrayList<>();


    public ServerUIProcess(ServerProcess serverProcess, Stage primaryStage)
    {
        this.serverProcess = serverProcess;
        this.primaryStage  = primaryStage;
        load();

        // default
        scene = new Scene(adminDashRoot);
        addObserver(adminController);
        adminController.addObserver(this);

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

    @Override
    public void update(ObserverMessage message)
    {
        String type = message.getMessageType();

        if(type.equals("ActiveGames"))
        {
            ArrayList<String> ActiveGames = new ArrayList<>();
            Map<String, GameProcess> map = serverProcess.getGamesService().getLiveGames();
            for(Map.Entry<String, GameProcess> item : map.entrySet())
            {
                ActiveGames.add("Game ID:\n" + item.getValue().getId());
//                ActiveGames.add("Spectator Count:\n" + (item.getValue().getSpectatorCount()));
//                ActiveGames.add("Players: \n" + (item.getValue().getPlayers().toString()));
//                ActiveGames.add("Start Time:\n" + (item.getValue().getStartTime()));
//                ActiveGames.add("End Time:\n" + (item.getValue().getEndTime()));
//                ActiveGames.add("Game: \n" + item.getValue().getGame().toString());
            }
            notifyObservers(new ObserverMessage("ActiveGamesList", ActiveGames));
        }

        else if(type.equals("CompletedGames"))
        {
            ArrayList<GameHistory> completedGameList = serverProcess.getDatabase().getAllGames();
            ArrayList<String> completedGameListStr = new ArrayList<>();

            for(int i = 0; i < completedGameList.size(); i++)
            {
                completedGameListStr.add(completedGameList.get(i).getGameId());
            }

            notifyObservers(new ObserverMessage("CompletedGameList", completedGameListStr));
        }

        else if(type.equals("RegisteredPlayers"))
        {
            ArrayList<User> users = serverProcess.getDatabase().getAllPlayers();
            ArrayList<String> usersStr = new ArrayList<>();

            for(int i = 0; i < users.size(); i++)
            {
                usersStr.add(users.get(i).getUserName());
            }

            notifyObservers(new ObserverMessage("RegisteredPlayersList", usersStr));
        }

        else if(type.equals("ActiveConnections"))
        {
            ArrayList<ClientConnection> connections = serverProcess.getConnections();
            ArrayList<String> connectionsStr = new ArrayList<>();

            for(int i = 0; i < connections.size(); i++)
            {
                connectionsStr.add(String.valueOf(connections.get(i).getId()));
            }

            notifyObservers(new ObserverMessage("ActiveConnectionsList", connectionsStr));
        }
    }

    @Override
    public void addObserver(Object o)
    {
        uiObserver = (Observer) o;
    }

    @Override
    public void removeObserver(Object o)
    {
        uiObserver = null;
    }

    @Override
    public void notifyObservers(ObserverMessage message)
    {
        uiObserver.update(message);
    }
}
