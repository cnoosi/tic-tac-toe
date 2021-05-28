package AdminDashUI;

import Game.OpenScene;
import MenuUI.MenuUIController;
import Observers.Observer;
import Observers.ObserverMessage;
import Observers.Subject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AdminDashUIController implements Initializable, Subject, Observer
{

    @FXML private ListView<String>  informationList;
    @FXML private TextArea          information;
    @FXML private Button            activeGamesBtn;
    @FXML private Button            completedGamesBtn;
    @FXML private Button            registeredPlayersBtn;
    @FXML private Button            activeConnectionsBtn;
    @FXML private Text              informationText;
    @FXML private ListView<String>  usersList;
    @FXML private Button            updateBtn;
    @FXML private TextField         username;
    @FXML private TextField         password;
    @FXML private Text              currentUsername;

    private Observer serverUIProcess;

    @FXML
    public void handleActiveGamesBtn(ActionEvent event)
    {
        informationText.setText(activeGamesBtn.getText());
        notifyObservers(new ObserverMessage("ActiveGames"));
    }

    @FXML
    public void handleCompletedGamesBtn(ActionEvent event)
    {
        informationText.setText(completedGamesBtn.getText());
        notifyObservers(new ObserverMessage("CompletedGames"));
    }

    @FXML
    public void handleRegisteredPlayersBtn(ActionEvent event)
    {
        informationText.setText(registeredPlayersBtn.getText());
        notifyObservers(new ObserverMessage("RegisteredPlayers"));
    }

    @FXML
    public void handleActiveConnectionsBtn(ActionEvent event)
    {
        informationText.setText(activeConnectionsBtn.getText());
        notifyObservers(new ObserverMessage("ActiveConnections"));
    }

    @FXML
    public void handeUpdateButton(ActionEvent event)
    {
        ArrayList<String> information = new ArrayList<>();
        information.add(username.getText());
        information.add(currentUsername.getText());
        information.add(password.getText());

        notifyObservers(new ObserverMessage("UserChange", information));
    }

    @FXML
    public void fillUsersList()
    {
        notifyObservers(new ObserverMessage("UpdateList"));
    }

    public void handleIdSelection() throws Exception{
        //**********************************
        //
        //    Load up info to the gameinfo
        //
        //**********************************;
        information.setText(informationList.getSelectionModel().getSelectedItem());

    }

    public void handleUserSelection()
    {
        username.setText(usersList.getSelectionModel().getSelectedItem());
        currentUsername.setText(username.getText());
    }

    public void handleHomeBtn(ActionEvent event) throws Exception {
//        Stage stage = (Stage) homeBtn.getScene().getWindow();
//        FXMLLoader root = new FXMLLoader();
//        root.setLocation(getClass().getResource("/MenuUI/MenuUI.fxml"));
//        Parent frame = root.load();
//        MenuUIController controller = (MenuUIController) root.getController();
//        openScene.start(stage, frame, "Tic-Tac-Toe - Menu");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        informationList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        usersList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }

    @Override
    public void update(ObserverMessage message)
    {
        String type = message.getMessageType();
        if(type.equals("ActiveGamesList"))
        {
            informationList.getItems().clear();
            for(int i = 0; i < message.getMessage().size(); i++)
            {
                informationList.getItems().add(message.getMessage().get(i));
            }
        }

        else if(type.equals("CompletedGameList"))
        {
            informationList.getItems().clear();
            for(int i = 1; i < message.getMessage().size(); i++)
            {
                informationList.getItems().add(message.getMessage().get(i));
            }
        }

        else if(type.equals("RegisteredPlayersList"))
        {
            informationList.getItems().clear();
            for(int i = 1; i < message.getMessage().size(); i++)
            {
                informationList.getItems().add(message.getMessage().get(i));
            }
        }

        else if(type.equals("ActiveConnectionsList"))
        {
            informationList.getItems().clear();
            for(int i = 0; i < message.getMessage().size(); i++)
            {
                informationList.getItems().add(message.getMessage().get(i));
            }
        }

        else if(type.equals("UpdateListStr"))
        {
            usersList.getItems().clear();
            for(int i = 1; i < message.getMessage().size(); i++)
            {
                usersList.getItems().add(message.getMessage().get(i));
            }
        }

    }

    @Override
    public void addObserver(Object o)
    {
        serverUIProcess = (Observer) o;
    }

    @Override
    public void removeObserver(Object o)
    {
        serverUIProcess = null;
    }

    @Override
    public void notifyObservers(ObserverMessage message)
    {
        serverUIProcess.update(message);
    }
}
