package BoardUI;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import Game.*;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.*;

public class SplashPageController {
    @FXML private Button multiplayer;

    public void multiplayerButtonClicked(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/BoardUI/BoardUI.fxml"));
        Parent root = loader.load();
        BoardUIController UIController = loader.getController();
        UIController.SetMultiplayer();

        Stage primaryStage = new Stage();
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public void hardButtonClicked(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/BoardUI/BoardUI.fxml"));
        Parent root = loader.load();
    }

}
