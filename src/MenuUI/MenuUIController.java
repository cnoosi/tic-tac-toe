package MenuUI;

import Game.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class MenuUIController
{
    OpenScene openScene = new OpenScene();
    @FXML private Button singlePlayerBtn;
    @FXML private Button twoPlayerBtn;

    @FXML
    public void handleSinglePlayerMode(ActionEvent event) throws Exception
    {
        Stage stage = (Stage) singlePlayerBtn.getScene().getWindow();
        openScene.start(stage, "/BoardUI/BoardUI.fxml", "Tic-Tac-Toe - Single Player Game");
        //Somehow get boardui controller so we can change local player # to 1
    }

    @FXML
    public void handleTwoPlayerMode(ActionEvent event) throws Exception
    {
        Stage stage = (Stage) twoPlayerBtn.getScene().getWindow();
        openScene.start(stage, "/BoardUI/BoardUI.fxml", "Tic-Tac-Toe - Two Player Game");
        Parent root = FXMLLoader.load(getClass().getResource("/BoardUI/BoardUI.fxml"));
        //Somehow get boardui controller so we can change local player # to 2
    }
}
