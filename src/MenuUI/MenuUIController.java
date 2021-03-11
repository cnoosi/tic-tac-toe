package MenuUI;

import BoardUI.*;
import Game.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.net.URL;

public class MenuUIController
{
    OpenScene openScene = new OpenScene();
    @FXML private Button singlePlayerBtn;
    @FXML private Button twoPlayerBtn;

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
    }

    @FXML
    public void handleTwoPlayerMode(ActionEvent event) throws Exception
    {
        Stage stage = (Stage) twoPlayerBtn.getScene().getWindow();
        FXMLLoader root = new FXMLLoader();
        root.setLocation(getClass().getResource("/BoardUI/BoardUI.fxml"));
        Parent frame = root.load();
        BoardUIController controller = (BoardUIController) root.getController();
        controller.setLocalPlayerCount(2);
        controller.resetGame();
        openScene.start(stage, frame, "Tic-Tac-Toe - Two Player Game");
    }
}
