package MenuUI;

import Game.OpenScene;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.scene.control.*;

import java.util.Scanner;

public class UserMenuUIController {

    @FXML private Button homeBtn;
    @FXML private Button loginBtn;
    @FXML private TextField userName;
    @FXML private TextField password;
    @FXML private TextField userName2;
    @FXML private TextField firstName;
    @FXML private TextField lastName;
    @FXML private TextField password2;
    @FXML private Label userValidation;
    @FXML private Label userValidation2;
    private OpenScene openScene = new OpenScene();


    //********************************************
    //LOGIN FUNCTION
    //********************************************
    public void handleLoginBtn(ActionEvent event) throws Exception{
        Scanner  scanner = new Scanner(System.in);
        String   user    = userName.getText();
        String   pass    = password.getText();

        //CHECKS TO SEE IF THE USER EXISTS
        //client.writeMessage(new AccountMessage(AccountAction.Login, user, null, null, pass));
    }

    //********************************************
    //CREATE ACCOUNT FUNCTION
    //********************************************
    public void handleCreateAcctBtn(ActionEvent event) throws Exception {

        String user   = userName2.getText();
        String firstN = firstName.getText();
        String lastN  = lastName.getText();
        String pass   = password2.getText();

        //client.writeMessage(new AccountMessage(AccountAction.Register, user, firstN, lastN, pass));

        //userValidation2.setText("that username is already taken");
    }
    
    public void handleLogoutButton(ActionEvent event) throws Exception {
        //client.writeMessage(new AccountMessage(AccountAction.Logout));
    }

    public void handleHomeBtn(ActionEvent event) throws Exception {
        Stage stage = (Stage) homeBtn.getScene().getWindow();
        FXMLLoader root = new FXMLLoader();
        root.setLocation(getClass().getResource("/MenuUI/MenuUI.fxml"));
        Parent frame = root.load();
        MenuUIController controller = (MenuUIController) root.getController();
        openScene.start(stage, frame, "Tic-Tac-Toe - Menu");
    }

}
