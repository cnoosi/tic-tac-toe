package MenuUI;

import Game.Database;
import Game.DbManager;
import Game.OpenScene;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.scene.control.*;

import javax.swing.*;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;

public class UserMenuUIController implements Initializable {

    @FXML private Button homeBtn;
    @FXML private Button loginBtn;
    @FXML private TextField userName;
    @FXML private PasswordField password;
    @FXML private TextField userName2;
    @FXML private TextField firstName;
    @FXML private TextField     lastName;
    @FXML private PasswordField password2;
    @FXML private PasswordField password3;

    private OpenScene openScene = new OpenScene();

    @FXML private MediaView     mv;
    private MediaPlayer mp;
    private Media me;

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
            if(DbManager.getInstance().isUserDeleted(user))
            {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setHeaderText("DELETED USER");
                errorAlert.setContentText("The following account can not be accessed because it has been deleted");
                errorAlert.showAndWait();
            }
            else
            {

                Alert errorAlert = new Alert(Alert.AlertType.CONFIRMATION);
                errorAlert.setHeaderText("USER FOUND");
                errorAlert.setContentText("you are logged in! Welcome back " + user);
                errorAlert.showAndWait();
                DbManager.getInstance().setCurrentUser(user);
            }

        }
        else
        {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("USER NOT FOUND");
            errorAlert.setContentText("The following username and password do not match any existing users, try re-entering username and password");
            errorAlert.showAndWait();
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
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("USERNAME NOT AVAILABLE");
            errorAlert.setContentText("The following username is already taken by another player, try a different one");
            errorAlert.showAndWait();
        }
        else
        {
            if(pass.equals(password3.getText()))
            {
                Alert errorAlert = new Alert(Alert.AlertType.CONFIRMATION);
                errorAlert.setHeaderText("ACCOUNT SUCCESSFULLY CREATED");
                errorAlert.setContentText("Account is created! Welcome " + user);
                errorAlert.showAndWait();
                DbManager.getInstance().addUser(user,firstN,lastN,pass);
            }
            else
            {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setHeaderText("PASSWORDS DO NOT MATCH");
                errorAlert.setContentText("The password and password confirmation fields do not match, make sure they are the same");
                errorAlert.showAndWait();
            }

        }

    }
    
    public void handleLogoutButton(ActionEvent event) throws Exception{
            DbManager.getInstance().setCurrentUserIndex(0);
    }
    public void handleHomeBtn(ActionEvent event) throws Exception {
        Stage stage = (Stage) homeBtn.getScene().getWindow();
        FXMLLoader root = new FXMLLoader();
        root.setLocation(getClass().getResource("/MenuUI/MenuUI.fxml"));
        Parent frame = root.load();
        MenuUIController controller = (MenuUIController) root.getController();
        openScene.start(stage, frame, "Tic-Tac-Toe - Menu");
        mp.stop();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        String path = new File("src/resources/images/background.mp4").getAbsolutePath();
        me = new Media(new File(path).toURI().toString());
        mp = new MediaPlayer(me);
        mv.setMediaPlayer(mp);
        mp.setAutoPlay(true);
        mp.setCycleCount(MediaPlayer.INDEFINITE);
    }
}
