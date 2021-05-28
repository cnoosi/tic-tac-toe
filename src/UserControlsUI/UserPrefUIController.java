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
import java.util.ArrayList;
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

    public void handleNameChange(ActionEvent event) throws Exception
    {
        String password     = pass.getText();
        String newFirst     = firstName.getText();
        String newLast      = lastName.getText();

        ArrayList<String> newData = new ArrayList<>();
        newData.add(password);
        newData.add(newFirst);
        newData.add(newLast);
        notifyObservers(new ObserverMessage("ChangeName", newData));
    }

    public void handleUsernameChange(ActionEvent event) throws Exception{
        String newUsern     = newUsername.getText();
        String password     = pass1.getText();

        ArrayList<String> newData = new ArrayList<>();
        newData.add(password);
        newData.add(newUsern);
        notifyObservers(new ObserverMessage("ChangeUsername", newData));
    }

    public void handlePasswordChange(ActionEvent event) throws Exception{
        String currentPass  = currentPassword.getText();
        String newPass      = newPassword.getText();

        if(newPass.equals(passwordConfirm.getText()))
        {
            ArrayList<String> newData = new ArrayList<>();
            newData.add(currentPass);
            newData.add(newPass);
            notifyObservers(new ObserverMessage("ChangePassword", newData));
        }
        else
        {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("PASSWORDS DO NOT MATCH");
            errorAlert.setContentText("The passwords do not match, make sure the new password and the new password confirmation fields match");
            errorAlert.showAndWait();
        }
    }

    public void handleDeleteUser(ActionEvent event)
    {
        String username = userNameDelete.getText();
        String password = passwordDelete.getText();

        ArrayList<String> newData = new ArrayList<>();
        newData.add(username);
        newData.add(password);
        notifyObservers(new ObserverMessage("DeleteUser", newData));
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
    public void initialize(URL url, ResourceBundle resourceBundle)
    {

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
