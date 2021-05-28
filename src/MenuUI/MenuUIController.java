package MenuUI;

import AdminDashUI.AdminDashUIController;
import BoardUI.*;
import Game.*;
import Observers.Observer;
import Observers.ObserverMessage;
import Observers.Subject;
import GameHistoryUI.GameHistoryUIController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import java.io.IOError;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import javafx.scene.image.ImageView;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class MenuUIController implements Initializable, Observer, Subject
{
    private       OpenScene openScene = new OpenScene();
    @FXML private Button    singlePlayerBtn;
    @FXML private Button    multiPlayerBtn;
    @FXML private Button    userMenuBtn;
    @FXML private Button    gameHistoryBtn;
    @FXML private Button    userPrefBtn;
    @FXML private MediaView mv;
    @FXML private ImageView sngl;
    @FXML private ImageView mlti;
    @FXML private Label     usernameLabel;

    private MediaPlayer mp;
    private Media me;
    private Observer UIProcess;


    @FXML
    public void handleSinglePlayerMode(ActionEvent event)
    {
        notifyObservers(new ObserverMessage("SinglePlayer"));
        mp.stop();
    }

    @FXML
    public void handleTwoPlayerMode(ActionEvent event)
    {
        notifyObservers(new ObserverMessage("MultiPlayer"));
        mp.stop();
    }

    public void handleUserMenuButton(ActionEvent event)
    {
        notifyObservers(new ObserverMessage("UserMenu"));
        mp.stop();
    }

    public void handleGameHistoryButton(ActionEvent event)
    {
        notifyObservers(new ObserverMessage("GameHistory"));
        mp.stop();
    }

    public void setInteractablesDisabled(boolean disabled)
    {
        singlePlayerBtn.setDisable(disabled);
        multiPlayerBtn.setDisable(disabled);
        gameHistoryBtn.setDisable(disabled);
        userPrefBtn.setDisable(disabled);
    }

    @FXML
    public void handleUserPrefBtn(ActionEvent event)
    {
        notifyObservers(new ObserverMessage("UserPreference"));
    }

    /***************************************************/
    public void handleAdminDashButton(ActionEvent event)
    {
        // Should not be here
    }
    /****************************************************/

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String path = new File("src/resources/images/comp 1.mp4").getAbsolutePath();

        me = new Media(new File(path).toURI().toString());
        mp = new MediaPlayer(me);
        mv.setMediaPlayer(mp);
        mp.setAutoPlay(true);
        mp.setCycleCount(MediaPlayer.INDEFINITE);


        //********************************************
        //ADDING GRAPHICS TO THE SINGLE PLAYER BUTTON
        //********************************************
        String path1 = new File("src/resources/images/singlePlayerButton.gif").getAbsolutePath();
        Image img1 = new Image(new File(path1).toURI().toString());
        ImageView view1 = new ImageView(img1);
        view1.setFitHeight(180);
        view1.setPreserveRatio(true);
        //Setting a graphic to the button
        singlePlayerBtn.setGraphic(view1);
        singlePlayerBtn.setStyle("-fx-background-color: transparent;");

        //Possibly add a mouse hover function to change the image state
        singlePlayerBtn.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

            }
        });

        //********************************************
        //ADDING GRAPHICS TO THE  MULTIPLAYER BUTTON
        //********************************************
        String path2 = new File("src/resources/images/multiPlayerButton.gif").getAbsolutePath();
        Image img2 = new Image(new File(path2).toURI().toString());
        ImageView view2 = new ImageView(img2);
        view2.setFitHeight(180);
        view2.setPreserveRatio(true);
        //Setting a graphic to the button
        multiPlayerBtn.setGraphic(view2);
        multiPlayerBtn.setStyle("-fx-background-color: transparent;");

        //Possibly add a mouse hover function to change the image state
        multiPlayerBtn.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

            }
        });

    }


    // Observer & Subject Handling
    @Override
    public void update(ObserverMessage message)
    {
        String type = message.getMessageType();

        if(type.equals("MusicPlayer"))
        {
            mp.play();
        }
        else if (type.equals("UserStatusChanged"))
        {
            String username = message.getMessage().get(0);
            boolean isLoggedIn = Boolean.parseBoolean(message.getMessage().get(1));
            if (isLoggedIn)
            {
                setInteractablesDisabled(false);
                usernameLabel.setText(username);
            }
            else
            {
                setInteractablesDisabled(true);
                usernameLabel.setText("Logged Out");
            }
        }
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
