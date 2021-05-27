package UserInterface;

import BoardUI.BoardUIController;
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

    Stage mainStage;
    ArrayList<UIObserver> observers;
    ArrayList<ClientObserver> clientObservers;

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
    ArrayList<Observer> loginObservers = new ArrayList<>();
    ArrayList<Observer> menuObservers  = new ArrayList<>();
    ArrayList<Observer> boardObservers = new ArrayList<>();

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
//        if(message.getMessageType().equals("Login")) {
//            System.out.println(message.toString());
//
//            //Test
//            message.setMessageType("Sheesh");
//            notifyObservers(message);
//        }

        System.out.println(message.toString());
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
    }

    @Override
    public void notifyObservers(ObserverMessage message)
    {
        String messageType = message.getMessageType();
        switch (messageType)
        {
            case "LoginMessage":

        }
        loginObservers.get(0).update(message);
    }

//    public UIProcess(ClientProcess client, Stage primaryStage)
//    {
//        this.client = client;
//        this.mainStage = primaryStage;
//        this.observers = new ArrayList<>();
//        this.clientObservers = new ArrayList<>();
//        try
//        {
//            FXMLLoader boardLoader = new FXMLLoader(getClass().getResource("/BoardUI/BoardUI.fxml"));
//            this.boardRoot = boardLoader.load();
//            this.boardController = boardLoader.getController();
//            boardController.addObserver(this);
//            this.addObserver(boardController);
//            //this.addClientObserver(client);
//
//            FXMLLoader menuLoader = new FXMLLoader(getClass().getResource("/MenuUI/MenuUI.fxml"));
//            this.menuRoot = menuLoader.load();
//            this.menuController = menuLoader.getController();
//        }
//        catch (Exception ex) {
//            ex.printStackTrace();
//        }
//    }


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
            Scene newScene = new Scene(this.loginRoot);
            primaryStage.setScene(newScene);
            primaryStage.show();
        }
        else if (page.equals("Menu"))
        {
            Scene newScene = new Scene(this.menuRoot);
            primaryStage.setScene(newScene);
            primaryStage.show();
        }
        else if (page.equals("Board"))
        {
            Scene newScene = new Scene(this.boardRoot);
            primaryStage.setScene(newScene);
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
        //notifyObservers(new Position(row, col), newToken);
    }

    public void updateBoardUI(int currentToken, int winner)
    {
        //notifyObservers(new Position(20, 20), currentToken);
    }


//    @Override
//    public void addObserver(Object o)
//    {
//        observers.add((UIObserver) o);
//    }
//
//    @Override
//    public void removeObserver(Object o)
//    {
//        observers.remove(o);
//    }
//
//    @Override
//    public void notifyObservers(Position pos, int token)
//    {
//        observers.forEach(observer -> observer.update(pos, token));
//    }
//
//    //
//    @Override
//    public void update(Position pos, int token)
//    {
//        System.out.println(pos.toString());
//        notifyClientObservers(pos);
//    }
//
//    //
//    @Override
//    public void addClientObserver(Object o)
//    {
//        clientObservers.add((ClientObserver) o);
//    }
//
//    @Override
//    public void removeClientObserver(Object o)
//    {
//        clientObservers.remove(o);
//    }
//
//    @Override
//    public void notifyClientObservers(Position pos)
//    {
//        clientObservers.forEach(clientObserver -> clientObserver.clientUpdate(pos));
//    }
}
