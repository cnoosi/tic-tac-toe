package MenuUI;

import Game.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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
        openScene.start(stage, "/BoardUI/BoardUI.fxml", "Tic-Tac-Toe - Game");
    }

    @FXML
    public void handleTwoPlayerMode(ActionEvent event) throws Exception
    {
        Stage stage = (Stage) twoPlayerBtn.getScene().getWindow();
        openScene.start(stage, "/BoardUI/BoardUI.fxml", "Tic-Tac-Toe - Game");
    }
}
