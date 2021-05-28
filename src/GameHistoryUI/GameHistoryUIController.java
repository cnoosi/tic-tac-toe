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
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.File;
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

    @FXML private MediaView mv;
    private MediaPlayer mp;
    private Media me;


    boolean selectedHistoryGame = false;

    private ArrayList<String> gameHistoryId;
    private ArrayList<String> liveGamesId;

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
        ArrayList<String> gameMessage = new ArrayList<>();
        if (selectedHistoryGame)
        {
            int selectedIndex = gameHistoryList.getSelectionModel().getSelectedIndex();
            gameMessage.add(gameHistoryId.get(selectedIndex));
        }
        else
        {
            int selectedIndex = liveGamesList.getSelectionModel().getSelectedIndex();
            gameMessage.add(liveGamesId.get(selectedIndex));
        }
        //System.out.println(currentGame);
        notifyObservers(new ObserverMessage("ReplayGame", gameMessage));
    }

    public void handleIdSelection() throws Exception{
        //**********************************
        //
        //    Load up info to the gameinfo
        //
        //**********************************
        selectedHistoryGame = true;
        gameInfo.setText(gameHistoryList.getSelectionModel().getSelectedItem());
    }

    public void handleLiveIdSelection()
    {
        selectedHistoryGame = false;
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
        liveGamesList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        gameHistoryList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        //**********************************
        //
        //    Set the background video
        //
        //**********************************
        String path = new File("src/resources/images/background.mp4").getAbsolutePath();
        me = new Media(new File(path).toURI().toString());
        mp = new MediaPlayer(me);
        mv.setMediaPlayer(mp);
        mp.setAutoPlay(true);
        mp.setCycleCount(MediaPlayer.INDEFINITE);
    }

    @Override
    public void update(ObserverMessage message)
    {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                String type = message.getMessageType();

                if (type.equals("liveGameList")) {
                    liveGamesList.getItems().clear();
                    liveGamesId = message.getMessage();
                    for (int i = 0; i < liveGamesId.size(); i++) {
                        System.out.println("ADDING " + i);
                        liveGamesList.getItems().add("Live Game " + i);
                    }
                } else if (type.equals("historyGameList")) {
                    gameHistoryList.getItems().clear();
                    gameHistoryId = message.getMessage();
                    for (int i = 0; i < gameHistoryId.size(); i++) {
                        gameHistoryList.getItems().add("Previous Game " + i);
                    }
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
