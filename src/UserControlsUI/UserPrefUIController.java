package UserControlsUI;

import Networking.*;
import MenuUI.MenuUIController;
import Observers.Observer;
import Observers.ObserverMessage;
import Observers.Subject;
import UserInterface.UIProcess;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class UserPrefUIController implements Initializable, Observer, Subject
{
    @FXML TabPane tabPane;
    //private OpenScene openScene = new OpenScene();

    //Name Change UI elements
    @FXML TextField     firstName;
    @FXML TextField     lastName;
    @FXML PasswordField pass;

    //UserName Change UI elements
    @FXML TextField     currentUsername;
    @FXML TextField     newUsername;
    @FXML PasswordField pass1;

    //Password Change UI elements
    @FXML PasswordField currentPassword;
    @FXML PasswordField newPassword;
    @FXML PasswordField passwordConfirm;

    //User Delete UI elemts
    @FXML TextField     userNameDelete;
    @FXML PasswordField passwordDelete;

    private Observer UIProcess;

    public void handleNameChange(ActionEvent event) throws Exception{
        String newFirst     = firstName.getText();
        String newLast      = lastName.getText();
        String password     = pass.getText();
        boolean userValid   = DbManager.getInstance().userFound(DbManager.getInstance().getCurrentUser().getUserName(),password);

        if(userValid)
        {
            DbManager.getInstance().changeInfo("FirstName",newFirst,DbManager.getInstance().getCurrentUser().getUserName());
            DbManager.getInstance().changeInfo("LastName",newLast,DbManager.getInstance().getCurrentUser().getUserName());
            Alert errorAlert = new Alert(Alert.AlertType.CONFIRMATION);
            errorAlert.setHeaderText("USER INFO CHANGED");
            errorAlert.setContentText("You have successfully changed your user information, please restart the game in order for changes to fully take place");
            errorAlert.showAndWait();
        }
        else{
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("INCORRECT PASSWORD");
            errorAlert.setContentText("The username and password do not match, retry entering your password");
            errorAlert.showAndWait();
        }
    }

    public void handleUsernameChange(ActionEvent event) throws Exception{
        String currentUsern = currentUsername.getText();
        String newUsern     = newUsername.getText();
        String password     = pass1.getText();
        boolean userValid   = DbManager.getInstance().userFound(DbManager.getInstance().getCurrentUser().getUserName(),password);

        if(userValid)
        {
            if(DbManager.getInstance().userAvailable(newUsern))
            {
                DbManager.getInstance().changeInfo("Username",newUsern,currentUsern);
                Alert errorAlert = new Alert(Alert.AlertType.CONFIRMATION);
                errorAlert.setHeaderText("USERNAME  CHANGED");
                errorAlert.setContentText("You have successfully changed your username, please restart the game in order for changes to fully take place");
                errorAlert.showAndWait();
            }
            else
            {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setHeaderText("USERNAME NOT AVAILABLE");
                errorAlert.setContentText("The username you chose is already taken by another player");
                errorAlert.showAndWait();
            }
        }
        else
        {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("INCORRECT USERNAME OR PASSWORD");
            errorAlert.setContentText("The username and password do not match, retry entering your username and password");
            errorAlert.showAndWait();
        }
    }

    public void handlePasswordChange(ActionEvent event) throws Exception{
        String currentPass  = currentPassword.getText();
        String newPass      = newPassword.getText();
        boolean userValid   = DbManager.getInstance().userFound(DbManager.getInstance().getCurrentUser().getUserName(),currentPass);

        if(userValid)
        {
            if(newPass.equals(passwordConfirm.getText()))
            {
                DbManager.getInstance().changeInfo("Password",newPass,DbManager.getInstance().getCurrentUser().getUserName());
                Alert errorAlert = new Alert(Alert.AlertType.CONFIRMATION);
                errorAlert.setHeaderText("PASSWORD  CHANGED");
                errorAlert.setContentText("You have successfully changed your password, please restart the game in order for changes to fully take place");
                errorAlert.showAndWait();
            }
            else
            {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setHeaderText("PASSWORDS DO NOT MATCH");
                errorAlert.setContentText("The passwords do not match, make sure the new password and the new password confirmation fields match");
                errorAlert.showAndWait();
            }
        }
        else
        {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("INCORRECT USERNAME OR PASSWORD");
            errorAlert.setContentText("The username and password do not match, retry entering your password");
            errorAlert.showAndWait();
        }

    }

    public void handleDeleteUser(ActionEvent event)
    {
        String username = userNameDelete.getText();
        String password = passwordDelete.getText();
        if(username.equals(DbManager.getInstance().getCurrentUser().getUserName()))
        {
            boolean userValid   = DbManager.getInstance().userFound(DbManager.getInstance().getCurrentUser().getUserName(),password);
            if(userValid)
            {
                DbManager.getInstance().deleteUser(DbManager.getInstance().getCurrentUser().getUserName());
                Alert errorAlert = new Alert(Alert.AlertType.CONFIRMATION);
                errorAlert.setHeaderText("USER DELETED");
                errorAlert.setContentText("You have successfully deleted your account, please restart the game in order for changes to fully take place");
                errorAlert.showAndWait();
            }
            else
            {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setHeaderText("INCORRECT PASSWORD");
                errorAlert.setContentText("The username and password do not match, retry entering your password");
                errorAlert.showAndWait();
            }
        }
        else
        {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("INCORRECT USERNAME");
            errorAlert.setContentText("Username doesn't match the current account retry entering your username");
            errorAlert.showAndWait();
        }
    }

    public void handleHomeButton(ActionEvent event) throws Exception{
        notifyObservers(new ObserverMessage("Home"));
//        Stage stage = (Stage) tabPane.getScene().getWindow();
//        FXMLLoader root = new FXMLLoader();
//        root.setLocation(getClass().getResource("/MenuUI/MenuUI.fxml"));
//        Parent frame = root.load();
//        MenuUIController controller = (MenuUIController) root.getController();
        //openScene.start(stage, frame, "Tic-Tac-Toe - Menu");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        User current = new User();
        current = DbManager.getInstance().getCurrentUser();
        firstName.setText(current.getFirstName());
        lastName.setText(current.getLastName());

    }

    @Override
    public void update(ObserverMessage message)
    {

    }

    @Override
    public void addObserver(Object o)
    {
        UIProcess = (Observer) o;
    }

    @Override
    public void removeObserver(Object o)
    {
        UIProcess = null;
    }

    @Override
    public void notifyObservers(ObserverMessage message)
    {
        UIProcess.update(message);
    }
}
