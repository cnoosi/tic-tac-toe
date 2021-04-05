package MenuUI;

import BoardUI.*;
import Game.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Slider;
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

import javafx.scene.image.ImageView;
import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.ResourceBundle;


public class MenuUIController implements Initializable
{
    private OpenScene openScene = new OpenScene();
    @FXML private Button singlePlayerBtn;
    @FXML private Button multiPlayerBtn;
    @FXML private MediaView mv;
    @FXML private ImageView sngl;
    @FXML private ImageView mlti;

    private MediaPlayer mp;
    private Media me;



    @FXML
    public void handleSinglePlayerMode(ActionEvent event) throws Exception
    {
        Stage stage = (Stage) singlePlayerBtn.getScene().getWindow();
        FXMLLoader root = new FXMLLoader();
        root.setLocation(getClass().getResource("/BoardUI/BoardUI.fxml"));
        Parent frame = root.load();
        BoardUIController controller = (BoardUIController) root.getController();
        controller.setLocalPlayerCount(1);
        controller.resetGame();
        openScene.start(stage, frame, "Tic-Tac-Toe - Single Player Game");
        mp.stop();
    }

    @FXML
    public void handleTwoPlayerMode(ActionEvent event) throws Exception
    {
        Stage stage = (Stage) multiPlayerBtn.getScene().getWindow();
        FXMLLoader root = new FXMLLoader();
        root.setLocation(getClass().getResource("/BoardUI/BoardUI.fxml"));
        Parent frame = root.load();
        BoardUIController controller = (BoardUIController) root.getController();
        controller.setLocalPlayerCount(2);
        controller.resetGame();
        openScene.start(stage, frame, "Tic-Tac-Toe - Two Player Game");
        mp.stop();
    }



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

}
