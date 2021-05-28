package MenuUI;

import Game.OpenScene;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.scene.control.*;
import Observers.*;
import java.util.ArrayList;
import java.util.Scanner;

public class UserMenuUIController implements Observer, Subject
{
    @FXML private Button homeBtn;
    @FXML private Button loginBtn;
    @FXML private TextField userName;
    @FXML private PasswordField password;
    @FXML private TextField userName2;
    @FXML private TextField firstName;
    @FXML private TextField lastName;
    @FXML private PasswordField password2;
    @FXML private Label userValidation;
    @FXML private Label userValidation2;
    private OpenScene openScene = new OpenScene();

    private Observer UIProcess;


    //********************************************
    //LOGIN FUNCTION
    //********************************************
    public void handleLoginBtn(ActionEvent event) throws Exception{
        ArrayList<String> userInfo = new ArrayList<>();
        userInfo.add(userName.getText());
        userInfo.add(password.getText());

        notifyObservers(new ObserverMessage("Login", userInfo));
    }

    //********************************************
    //CREATE ACCOUNT FUNCTION
    //********************************************
    public void handleCreateAcctBtn(ActionEvent event) throws Exception {

        ArrayList<String> userInfo = new ArrayList<>()
        {
            {
                add(userName2.getText());
                add(firstName.getText());
                add(lastName.getText());
                add(password2.getText());
            }
        };

        notifyObservers(new ObserverMessage("CreateAccount", userInfo));
    }

    public void handleLogoutButton(ActionEvent event) throws Exception {
        notifyObservers(new ObserverMessage("Logout"));
    }

    public void handleHomeBtn(ActionEvent event)
    {
        notifyObservers(new ObserverMessage("Home"));
    }

    // Observer & Subject Handling
    @Override
    public void update(ObserverMessage message)
    {
        userValidation.setText(message.getMessageType());
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
        if(UIProcess != null)
            UIProcess.update(message);
    }
}
