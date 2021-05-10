package UserInterface;

import BoardUI.BoardUIController;
import Game.Position;
import MenuUI.MenuUIController;
import Networking.ClientProcess;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import Linkers.*;

import java.util.ArrayList;

public class UIProcess implements BoardObserver, ClientSubject, UISubject
{
    ClientProcess client;
    Parent menuRoot;
    Parent boardRoot;
    MenuUIController menuController;
    BoardUIController boardController;
    Stage mainStage;
    ArrayList<UIObserver> observers;
    ArrayList<ClientObserver> clientObservers;

    public UIProcess(ClientProcess client, Stage primaryStage)
    {
        this.client = client;
        this.mainStage = primaryStage;
        this.observers = new ArrayList<>();
        this.clientObservers = new ArrayList<>();
        try
        {
            FXMLLoader boardLoader = new FXMLLoader(getClass().getResource("/BoardUI/BoardUI.fxml"));
            this.boardRoot = boardLoader.load();
            this.boardController = boardLoader.getController();
            boardController.addObserver(this);
            this.addObserver(boardController);
            this.addClientObserver(client);

            FXMLLoader menuLoader = new FXMLLoader(getClass().getResource("/MenuUI/MenuUI.fxml"));
            this.menuRoot = menuLoader.load();
            this.menuController = menuLoader.getController();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void openPage(String page)
    {
        if (page.equals("Menu"))
        {
            Scene newScene = new Scene(this.menuRoot);
            mainStage.setScene(newScene);
            mainStage.show();
        }
        else if (page.equals("Board"))
        {
            Scene newScene = new Scene(this.boardRoot);
            mainStage.setScene(newScene);
            mainStage.show();
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
        notifyObservers(new Position(row, col), newToken);
    }

    public void updateBoardUI(int currentToken, int winner)
    {
        notifyObservers(new Position(20, 20), currentToken);
    }

    @Override
    public void addObserver(Object o)
    {
        observers.add((UIObserver) o);
    }

    @Override
    public void removeObserver(Object o)
    {
        observers.remove(o);
    }

    @Override
    public void notifyObservers(Position pos, int token)
    {
        observers.forEach(observer -> observer.update(pos, token));
    }

    //
    @Override
    public void update(Position pos, int token)
    {
        System.out.println(pos.toString());
        notifyClientObservers(pos);
    }

    //
    @Override
    public void addClientObserver(Object o)
    {
        clientObservers.add((ClientObserver) o);
    }

    @Override
    public void removeClientObserver(Object o)
    {
        clientObservers.remove(o);
    }

    @Override
    public void notifyClientObservers(Position pos)
    {
        clientObservers.forEach(clientObserver -> clientObserver.clientUpdate(pos));
    }
}
