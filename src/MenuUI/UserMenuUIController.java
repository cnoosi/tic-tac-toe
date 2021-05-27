package MenuUI;

import Game.Database;
import Game.DbManager;
import Game.OpenScene;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.scene.control.*;

import javax.swing.*;
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
//      Database db      = new Database();
        String   user    = userName.getText();
        String   pass    = password.getText();
        boolean  userValid;

        //CHECKS TO SEE IF THE USER EXISTS
        userValid = DbManager.getInstance().userFound(user,pass);
//      userValid = db.find(user,pass);

        if(userValid)
        {
            userValidation.setText("user has been found!");
//            DbManager.getInstance().setCurrentUser();
        }
        else
        {
            userValidation.setText("User not found");
        }
    }

    //********************************************
    //CREATE ACCOUNT FUNCTION
    //********************************************
    public void handleCreateAcctBtn(ActionEvent event) throws Exception {

        String user   = userName2.getText();
        String firstN = firstName.getText();
        String lastN  = lastName.getText();
        String pass   = password2.getText();

        boolean available = DbManager.getInstance().userAvailable(user);
        if (!available)
        {
            userValidation2.setText("that username is already taken");
        }
        else
        {
            DbManager.getInstance().addUser(user,firstN,lastN,pass);
            userValidation2.setText("account successfully created!!");
        }

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