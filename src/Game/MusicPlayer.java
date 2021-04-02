package Game;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.io.File;

public class MusicPlayer {
    Clip clip;

    MusicPlayer (String filePath){
        try {
            File musicPath = new File(filePath);
            if (musicPath.exists()) {
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicPath);
                clip = AudioSystem.getClip();
                clip.open(audioInput);
                //JOptionPane.showMessageDialog(null, "Press OK to stop playing");
            } else {
                System.out.println("Can't find music file");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    void playMusic() {
        clip.start();
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    void stopMusic() {
        clip.stop();
    }
}
