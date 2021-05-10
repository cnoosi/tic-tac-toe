package UserInterface;

import BoardUI.BoardUIController;
import MenuUI.MenuUIController;
import Networking.ClientProcess;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class UIProcess {
    ClientProcess client;
    Parent menuRoot;
    Parent boardRoot;
    MenuUIController menuController;
    BoardUIController boardController;
    Stage mainStage;

    public UIProcess(ClientProcess client, Stage primaryStage)
    {
        this.client = client;
        this.mainStage = primaryStage;
        try
        {
            FXMLLoader boardLoader = new FXMLLoader(getClass().getResource("/BoardUI/BoardUI.fxml"));
            this.boardRoot = boardLoader.load();
            this.boardController = boardLoader.getController();

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
        boardController.setImage(newToken, row, col);
    }

    public void updateBoardUI(int currentToken, int winner)
    {

    }
}
