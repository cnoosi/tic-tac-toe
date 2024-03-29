package UserInterface;

import BoardUI.BoardUIController;
import Game.*;
import Game.Position;
import GameHistoryUI.GameHistoryUIController;
import MenuUI.MenuUIController;
import MenuUI.UserMenuUIController;
import Networking.ClientProcess;
import UserControlsUI.UserPrefUIController;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import Observers.*;

import javax.crypto.AEADBadTagException;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

//BoardObserver, ClientSubject, UISubject,

public class UIProcess implements Subject, Observer
{
    ClientProcess client;
    Game localGame;
    Scene scene;

    //FXMl Loaders
    FXMLLoader menuLoader;
    FXMLLoader boardLoader;
    FXMLLoader loginLoader;
    FXMLLoader historyLoader;
    FXMLLoader preferenceLoader;

    //FXML Holder
    Parent menuRoot;
    Parent boardRoot;
    Parent loginRoot;
    Parent historyRoot;
    Parent preferenceRoot;

    //Controllers
    MenuUIController menuController;
    BoardUIController boardController;
    UserMenuUIController loginController;
    GameHistoryUIController historyController;
    UserPrefUIController preferenceController;

    //Primary Stage
    Stage primaryStage;

    //Observers
    ArrayList<Observer> loginObservers      = new ArrayList<>();
    ArrayList<Observer> menuObservers       = new ArrayList<>();
    ArrayList<Observer> boardObservers      = new ArrayList<>();
    ArrayList<Observer> clientObservers     = new ArrayList<>();
    ArrayList<Observer> historyObservers    = new ArrayList<>();
    ArrayList<Observer> preferenceObservers = new ArrayList<>();

    // Updated Constructor
    public UIProcess(ClientProcess client, Stage primaryStage)
    {
        this.client       = client;
        this.primaryStage = primaryStage;
        load();
    }

    // Add new user interfaces as needed
    private void load()
    {
        try
        {
            // initialize FXMLLoaders
            loginLoader   = new FXMLLoader(getClass().getResource("/MenuUI/UserMenuUI.fxml"));
            menuLoader    = new FXMLLoader(getClass().getResource("/MenuUI/MenuUI.fxml"));
            boardLoader   = new FXMLLoader(getClass().getResource("/BoardUI/BoardUI.fxml"));
            historyLoader = new FXMLLoader(getClass().getResource("/GameHistoryUI/GameHistoryUI.fxml"));
            preferenceLoader = new FXMLLoader(getClass().getResource("/UserControlsUI/UserPrefUI.fxml"));

            // set fxml roots
            loginRoot   = loginLoader.load();
            menuRoot    = menuLoader.load();
            boardRoot   = boardLoader.load();
            historyRoot = historyLoader.load();
            preferenceRoot = preferenceLoader.load();

            // get controllers for every fxml
            loginController   = loginLoader.getController();
            menuController    = menuLoader.getController();
            boardController   = boardLoader.getController();
            historyController = historyLoader.getController();
            preferenceController = preferenceLoader.getController();

            // add this class as observer to other classes
            loginController.addObserver(this);
            menuController.addObserver(this);
            boardController.addObserver(this);
            historyController.addObserver(this);
            preferenceController.addObserver(this);

            // add observers
            this.addObserver(loginController);
            this.addObserver(menuController);
            this.addObserver(boardController);
            this.addObserver(historyController);
            this.addObserver(preferenceController);
            this.addObserver(client);


            // default scene
            scene = new Scene(menuRoot);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    // Handle update from subject
    @Override
    public void update(ObserverMessage message)
    {
        String type = message.getMessageType();

        // Generic
        if(type.equals("Home"))
        {
            openPage("Menu");
            notifyObservers(new ObserverMessage("MusicPlayer"));
            localGame = null;
        }

        // Main Menu Actions
        else if(type.equals("UserMenu"))
        {
            openPage("Login");
        }

        else if(type.equals("SinglePlayer"))
        {
            clearBoard();
            openPage("Board");
            localGame = new Game();
            notifyObservers(new ObserverMessage("SinglePlayerMode"));
        }

        else if(type.equals("SinglePlayerMove"))
        {
            System.out.println("here");
            int row = Integer.parseInt(message.getMessage().get(0));
            int col = Integer.parseInt(message.getMessage().get(1));
            int token = Integer.parseInt(message.getMessage().get(2));
            System.out.println(row + " " + col + " " + token);

            if(localGame.checkWin() == 0)
            {
                if(localGame.requestPosition(row, col, token))
                    localGame.setPosition(row, col, token);
                updateBoardUI(row, col, token);
                System.out.println(localGame);
            }
        }

        else if(type.equals("UserPreference"))
        {
            openPage("UserPreference");
        }

        else if(type.equals("MultiPlayer"))
        {
            clearBoard();
            openPage("Board");
            notifyObservers(message);
        }

        else if(type.equals("GameHistory"))
        {
            openPage("History");
            message.setMessageType("GameHistoryUpdate");
            notifyObservers(message);
        }

        else if(type.equals("Spectate"))
        {
            clearBoard();
        }

        else if(type.equals("ReplayGame"))
        {
            clearBoard();
            notifyObservers(message);
            openPage("Board");
        }

        // User Menu Actions
        else if(type.equals("Login") || type.equals("Logout") || type.equals("CreateAccount") || type.equals("Move") ||
                type.equals("ChangeName") || type.equals("ChangeUsername") || type.equals("ChangePassword") || type.equals("DeleteUser"))
        {
            notifyObservers(message);
        }
    }

    // Handle observers - UIProcess being subject
    @Override
    public void addObserver(Object o)
    {
        if(o instanceof UserMenuUIController)
            loginObservers.add((Observer) o);

        else if(o instanceof MenuUIController)
            menuObservers.add((Observer) o);

        else if(o instanceof  BoardUIController)
            boardObservers.add((Observer) o);

        else if(o instanceof GameHistoryUIController)
            historyObservers.add((Observer) o);

        else if(o instanceof ClientProcess)
            clientObservers.add((Observer) o);

        else if(o instanceof UserPrefUIController)
            preferenceObservers.add((Observer) o);
    }

    @Override
    public void removeObserver(Object o)
    {
        if(o instanceof UserMenuUIController)
            loginObservers.remove(o);

        else if(o instanceof MenuUIController)
            menuObservers.remove(o);

        else if(o instanceof  BoardUIController)
            boardObservers.remove(o);

        else if(o instanceof GameHistoryUIController)
            historyObservers.remove(o);

        else if(o instanceof ClientProcess)
            clientObservers.remove(o);

        else if(o instanceof UserPrefUIController)
            preferenceObservers.remove(o);
    }

    @Override
    public void notifyObservers(ObserverMessage message)
    {
        String type = message.getMessageType();

        if(type.equals("MusicPlayer") || type.equals("UserStatusChanged"))
        {
            menuObservers.forEach(observer -> observer.update(message));
        }

        else if(type.equals("Login") || type.equals("Logout") || type.equals("CreateAccount")
                || type.equals("Move") || type.equals("MultiPlayer") || type.equals("GameHistoryUpdate") ||
                type.equals("ReplayGame") || type.equals("ChangeName") || type.equals("ChangeUsername") ||
                type.equals("ChangePassword") || type.equals("DeleteUser"))
        {
            clientObservers.forEach(observer -> observer.update(message));
        }

        else if(type.equals("UIMove") || type.equals("ClearBoard") || type.equals("SinglePlayerMode") ||
                type.equals("UIBoardChange"))
        {
            boardObservers.forEach(observer -> observer.update(message));
        }

        else if(type.equals("liveGameList") || type.equals("historyGameList"))
        {
            historyObservers.forEach(observer -> observer.update(message));
        }
    }

    /**********************************
     ***** Controlling UI methods *****
     **********************************/
    public void clearBoard()
    {
        notifyObservers(new ObserverMessage("ClearBoard"));
    }

    private String getFXML(String scene)
    {
        String fxml = "";
        if(scene.equals("Login"))
            fxml = "/MenuUI/UserMenuUI.fxml";

        else if(scene.equals("Menu"))
            fxml = "/MenuUI/MenuUI.fxml";

        else if(scene.equals("Board"))
            fxml = "/BoardUI/BoardUI.fxml";

        // Continue as adding pages

        return fxml;
    }


    public void openPage(String page)
    {
        if(page.equals("Login"))
        {
            scene.setRoot(loginRoot);
            primaryStage.setScene(scene);
            primaryStage.show();
        }
        else if (page.equals("Menu"))
        {
            scene.setRoot(menuRoot);
            primaryStage.setScene(scene);
            primaryStage.show();
        }
        else if (page.equals("Board"))
        {
            scene.setRoot(boardRoot);
            primaryStage.setScene(scene);
            primaryStage.show();
        }

        else if(page.equals("History"))
        {
            scene.setRoot(historyRoot);
            primaryStage.setScene(scene);
            primaryStage.show();
        }

        else if(page.equals("UserPreference"))
        {
            scene.setRoot(preferenceRoot);
            primaryStage.setScene(scene);
            primaryStage.show();
        }
    }

    public void setActiveUsername(boolean isLoggedIn, String username)
    {
        ArrayList<String> messageData = new ArrayList<>();
        messageData.add(username);
        messageData.add(String.valueOf(isLoggedIn));
        notifyObservers(new ObserverMessage("UserStatusChanged", messageData));
        // do something with their username
    }

    public void newChat(String playerName, String playerChat)
    {
        System.out.println("add new chat: " + playerName + ": " + playerChat);
    }

    //ui.gameHistoryRecieved(liveGameList, historyGameList);

    public void gameHistoryRecieved(ArrayList<String> liveGameList, ArrayList<String> historyGameList)
    {
        notifyObservers(new ObserverMessage("liveGameList", liveGameList));
        notifyObservers(new ObserverMessage("historyGameList", historyGameList));
    }

    public void joinQueue()
    {

    }

    public void leaveQueue()
    {

    }

    public void changeUIBoardToken(int row, int col, int newToken, int winnerToken)
    {
        ArrayList<String> move = new ArrayList<>();

        move.add(String.valueOf(row));
        move.add(String.valueOf(col));
        move.add(String.valueOf(newToken));
        move.add(String.valueOf(winnerToken));

        notifyObservers(new ObserverMessage("UIMove", move));
    }

    public void updateBoardUI(int currentToken, int winner, int spectators)
    {
        ArrayList<String> data = new ArrayList<>();
        data.add(String.valueOf(currentToken));
        data.add(String.valueOf(winner));
        data.add(String.valueOf(spectators));
        notifyObservers(new ObserverMessage("UIBoardChange", data));
    }

    public void createAlert(Alert.AlertType alertType, String header, String content)
    {
        Platform.runLater(new Runnable(){
            @Override
            public void run() {
                Alert errorAlert = new Alert(alertType);
                errorAlert.setHeaderText(header);
                errorAlert.setContentText(content);
                errorAlert.showAndWait();
            }
        });
    }
}
