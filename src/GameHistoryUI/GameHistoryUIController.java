package GameHistoryUI;

import Game.OpenScene;
import MenuUI.MenuUIController;
import Observers.Observer;
import Observers.ObserverMessage;
import Observers.Subject;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class GameHistoryUIController implements Initializable, Observer, Subject
{
    @FXML private ListView<String>  gameHistoryList;
    @FXML private ListView<String>  liveGamesList;
    @FXML private TextArea          gameInfo;
    @FXML private TextField         gameId;
    @FXML private Button            searchBtn;
    @FXML private Button            replayBtn;
    @FXML private Button            homeBtn;
    private OpenScene openScene = new OpenScene();

    private Observer UIProcess;

    public void handleSearchBtn(ActionEvent event) throws Exception{
        //**********************************
        //
        //    Search for specific game
        //
        //**********************************

        notifyObservers(new ObserverMessage(""));
    }

    public void handleReplayBtn(ActionEvent event) throws Exception{
        //**********************************
        //
        //    open new board ui window to
        //    spectate game
        //
        //**********************************
        ArrayList<String> currentGame = new ArrayList<>();
        currentGame.add(gameHistoryList.getItems().get(gameHistoryList.getSelectionModel().getSelectedIndex()));
        System.out.println(currentGame);
        notifyObservers(new ObserverMessage("ReplayGame", currentGame));
    }

    public void handleIdSelection() throws Exception{
        //**********************************
        //
        //    Load up info to the gameinfo
        //
        //**********************************
        gameInfo.setText(gameHistoryList.getSelectionModel().getSelectedItem());
    }

    public void handleLiveIdSelection()
    {
        gameInfo.setText(liveGamesList.getSelectionModel().getSelectedItem());
    }

    @FXML
    public void handleHomeBtn(ActionEvent event) throws Exception {
        notifyObservers(new ObserverMessage("Home"));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //**********************************
        //
        //    INITIALIZE THE LIST HERE
        //
        //**********************************
        gameHistoryList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        liveGamesList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }

    @Override
    public void update(ObserverMessage message)
    {
        Platform.runLater(new Runnable(){
            @Override
            public void run() {
                gameHistoryList.getItems().clear();
                liveGamesList.getItems().clear();
                System.out.println("history update");
                String type = message.getMessageType();

                if (type.equals("liveGameList"))
                {
                    for(String item : message.getMessage())
                        liveGamesList.getItems().add(item);
                }
                else if(type.equals("historyGameList"))
                {
                    for(String item : message.getMessage())
                        gameHistoryList.getItems().add(item);
                }
            }
        });
    }

    @Override
    public void addObserver(Object o)
    {
        UIProcess = (Observer) o;
    }

    @Override
    public void removeObserver(Object o)
    {
        UIProcess = null;
    }

    @Override
    public void notifyObservers(ObserverMessage message)
    {
        if(UIProcess != null)
            UIProcess.update(message);
    }
}
