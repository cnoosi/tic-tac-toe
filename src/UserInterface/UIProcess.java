package UserInterface;

import BoardUI.BoardUIController;
import Game.*;
import Game.Position;
import MenuUI.MenuUIController;
import MenuUI.UserMenuUIController;
import Networking.ClientProcess;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import Linkers.*;
import Observers.*;

import javax.crypto.AEADBadTagException;
import java.io.IOException;
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

    //FXML Holder
    Parent menuRoot;
    Parent boardRoot;
    Parent loginRoot;

    //Controllers
    MenuUIController menuController;
    BoardUIController boardController;
    UserMenuUIController loginController;

    //Primary Stage
    Stage primaryStage;

    //Observers
    ArrayList<Observer> loginObservers  = new ArrayList<>();
    ArrayList<Observer> menuObservers   = new ArrayList<>();
    ArrayList<Observer> boardObservers  = new ArrayList<>();
    ArrayList<Observer> clientObservers = new ArrayList<>();

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
            loginLoader = new FXMLLoader(getClass().getResource("/MenuUI/UserMenuUI.fxml"));
            menuLoader  = new FXMLLoader(getClass().getResource("/MenuUI/MenuUI.fxml"));
            boardLoader = new FXMLLoader(getClass().getResource("/BoardUI/BoardUI.fxml"));

            // set fxml roots
            loginRoot   = loginLoader.load();
            menuRoot    = menuLoader.load();
            boardRoot   = boardLoader.load();

            // get controllers for every fxml
            loginController = loginLoader.getController();
            menuController  = menuLoader.getController();
            boardController = boardLoader.getController();

            // add this class as observer to other classes
            loginController.addObserver(this);
            menuController.addObserver(this);
            boardController.addObserver(this);

            // add observers
            this.addObserver(loginController);
            this.addObserver(menuController);
            this.addObserver(boardController);
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
        }

        // Main Menu Actions
        else if(type.equals("UserMenu"))
        {
            openPage("Login");
        }

        else if(type.equals("SinglePlayer"))
        {
            openPage("Board");
            localGame = new Game();
        }

        else if(type.equals("MultiPlayer"))
        {
            openPage("Board");
        }

        else if(type.equals("GameHistory")) // modify later
        {
            openPage("GameHistory");
        }

        else if(type.equals("Spectate"))
        {

        }

        else if(type.equals("LocalMove"))
        {

        }

        // User Menu Actions
        else if(type.equals("Login") || type.equals("Logout") || type.equals("CreateAccount") || type.equals("Move"))
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

        else if(o instanceof ClientProcess)
            clientObservers.add((Observer) o);
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

        else if(o instanceof ClientProcess)
            clientObservers.remove(o);
    }

    @Override
    public void notifyObservers(ObserverMessage message)
    {
        String type = message.getMessageType();

        if(type.equals("MusicPlayer"))
        {
            menuObservers.forEach(observer -> observer.update(message));
        }

        else if(type.equals("Login") || type.equals("Logout") || type.equals("CreateAccount") || type.equals("Move"))
        {
            clientObservers.forEach(observer -> observer.update(message));
        }

        else if(type.equals("UIMove"))
        {
            boardObservers.forEach(observer -> observer.update(message));
        }
    }

    /**********************************
     ***** Controlling UI methods *****
     **********************************/
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
    }

    public void newChat(String playerName, String playerChat)
    {
        System.out.println("add new chat: " + playerName + ": " + playerChat);
    }

    public void startGame()
    {
        //openPage("Board");
    }

    public void joinQueue()
    {
        //openPage("Board");
    }

    public void leaveQueue()
    {
        //openPage("Menu");
    }

    public void changeUIBoardToken(int row, int col, int newToken)
    {
        System.out.println("function called");
        ArrayList<String> move = new ArrayList<>();

        move.add(String.valueOf(row));
        move.add(String.valueOf(col));
        move.add(String.valueOf(newToken));

        notifyObservers(new ObserverMessage("UIMove", move));
    }

    public void updateBoardUI(int currentToken, int winner, int spectators)
    {
        //notifyObservers(new Position(20, 20), currentToken);
    }
}
