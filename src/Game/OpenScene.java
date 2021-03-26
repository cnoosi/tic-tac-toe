package Game;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.File;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class OpenScene {

    public void start(Stage window, String fxmlFile, String headerName) throws Exception {

        Parent root = FXMLLoader.load(getClass().getResource(fxmlFile));
        window.setTitle(headerName);
        window.setResizable(false);
        window.setScene(new Scene(root));
        window.show();
        String musicPath = "/Users/kasra/Documents/GitHub/TicTacToe/tic-tac-toe/src/resources/images/soundTrack.wav";
        MusicPlayer music = new MusicPlayer(musicPath);
        music.playMusic();
        music.stopMusic();
    }

    public void start(Stage window, Parent root, String headerName) throws Exception {
        window.setTitle(headerName);
        window.setResizable(false);
        window.setScene(new Scene(root));
        window.show();
        String musicPath = "/Users/kasra/Documents/GitHub/TicTacToe/tic-tac-toe/src/resources/images/soundTrack.wav";
        MusicPlayer music = new MusicPlayer(musicPath);
        music.playMusic();
        music.stopMusic();
    }
}
