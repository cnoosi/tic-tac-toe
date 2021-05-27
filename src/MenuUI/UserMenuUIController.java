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
//        Scanner  scanner = new Scanner(System.in);
////      Database db      = new Database();
//        String   user    = userName.getText();
//        String   pass    = password.getText();
//        ArrayList<String> information = new ArrayList<>();
//        information.add(user);
//        information.add(pass);

        ArrayList<String> userInfo = new ArrayList<>() {{add(userName.getText()); add(password.getText());}};

//        boolean  userValid;
//
//        //CHECKS TO SEE IF THE USER EXISTS
//        userValid = DbManager.getInstance().userFound(user,pass);
////      userValid = db.find(user,pass);
//
//        if(userValid)
//        {
//            userValidation.setText("user has been found!");
//            DbManager.getInstance().setCurrentUser(user);
//        }
//        else
//        {
//            userValidation.setText("User not found");
//        }

        notifyObservers(new ObserverMessage("Login", userInfo));
    }

    //********************************************
    //CREATE ACCOUNT FUNCTION
    //********************************************
    public void handleCreateAcctBtn(ActionEvent event) throws Exception {

//        String user   = userName2.getText();
//        String firstN = firstName.getText();
//        String lastN  = lastName.getText();
//        String pass   = password2.getText();

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

//        boolean available = DbManager.getInstance().userAvailable(user);
//        if (!available)
//        {
//            userValidation2.setText("that username is already taken");
//        }
//        else
//        {
//            DbManager.getInstance().addUser(user,firstN,lastN,pass);
//            userValidation2.setText("account successfully created!!");
//        }

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
