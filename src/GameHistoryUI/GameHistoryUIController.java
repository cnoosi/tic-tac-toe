package GameHistoryUI;

import Game.OpenScene;
import MenuUI.MenuUIController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class GameHistoryUIController implements Initializable {
    @FXML private ListView<String>  idList;
    @FXML private TextArea          gameInfo;
    @FXML private TextField         gameId;
    @FXML private Button            searchBtn;
    @FXML private Button            replayBtn;
    @FXML private Button            homeBtn;
    private OpenScene openScene = new OpenScene();

    public void handleSearchBtn(ActionEvent event) throws Exception{
        //**********************************
        //
        //    Search for specific game
        //
        //**********************************
    }

    public void handleReplayBtn(ActionEvent event) throws Exception{
        //**********************************
        //
        //    open new board ui window to
        //    spectate game
        //
        //**********************************
    }

    public void handleIdSelection() throws Exception{
        //**********************************
        //
        //    Load up info to the gameinfo
        //
        //**********************************
        gameInfo.setText(idList.getSelectionModel().getSelectedItem());

    }

    public void handleHomeBtn(ActionEvent event) throws Exception {
        Stage stage = (Stage) homeBtn.getScene().getWindow();
        FXMLLoader root = new FXMLLoader();
        root.setLocation(getClass().getResource("/MenuUI/MenuUI.fxml"));
        Parent frame = root.load();
        MenuUIController controller = (MenuUIController) root.getController();
        openScene.start(stage, frame, "Tic-Tac-Toe - Menu");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //**********************************
        //
        //    INITIALIZE THE LIST HERE
        //
        //**********************************
        idList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        idList.getItems().add("peepee");
        idList.getItems().add("poopoo");
    }
}
