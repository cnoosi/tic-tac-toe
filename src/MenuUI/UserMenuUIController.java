package MenuUI;

import Game.Database;
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

    public void handleLoginBtn(ActionEvent event) throws Exception{
        Scanner scanner = new Scanner(System.in);
        Database db = new Database();
        String user = userName.getText();
        String pass = password.getText();

        boolean userValid = db.find(user,pass);
        if(userValid)
        {
            userValidation.setText("user has been found!");
        }
        else
        {
            userValidation.setText("User not found");
        }

        System.out.println("username: " + user + "\n" + "pass: " + pass + "\n");

    }

    public void handleCreateAcctBtn(ActionEvent event) throws Exception {
        Scanner scanner = new Scanner(System.in);
        Database db = new Database();

        String user   = userName2.getText();
        String firstN = firstName.getText();
        String lastN  = lastName.getText();
        String pass   = password2.getText();



        boolean usrExists = db.userExists(user);
        if (usrExists) {
            userValidation2.setText("that username is already taken");
        } else {
            db.insert(user,firstN,lastN,pass);
            userValidation2.setText("account successfully created!!");
        }

        System.out.println("username: " + user + "\n" + "pass: " + pass + "\n");
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
