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
        Scene scene = new Scene(root);
        window.setTitle(headerName);
        window.setResizable(false);
        window.setScene(scene);
        window.show();
//        String musicPath = "/Users/kasra/Documents/GitHub/TicTacToe/tic-tac-toe/src/resources/images/soundTrack.wav";
//        MusicPlayer music = new MusicPlayer(musicPath);
//        music.playMusic();
//        music.stopMusic();
    }

    public void start(Stage window, Parent root, String headerName) throws Exception {
        Scene scene = new Scene(root);
        window.setTitle(headerName);
        window.setResizable(false);
        window.setScene(scene);
        window.show();
//        String musicPath = "/Users/kasra/Documents/GitHub/TicTacToe/tic-tac-toe/src/resources/images/soundTrack.wav";
//        MusicPlayer music = new MusicPlayer(musicPath);
//        music.playMusic();
//        music.stopMusic();
    }
}
