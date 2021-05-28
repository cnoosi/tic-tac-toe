package UserControlsUI;

import Game.DbManager;
import Game.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

public class UserPrefUIController implements Initializable {

    @FXML TextField userName;
    @FXML TextField firstName;
    @FXML TextField lastName;
    @FXML PasswordField pass;
    @FXML Button    submitNameBtn;

    public void handleSubmitButton(ActionEvent event) throws Exception{
        String newFirst = firstName.getText();
        String newLast  = lastName.getText();
        String password =  pass.getText();
        boolean userValid = DbManager.getInstance().userFound(DbManager.getInstance().getCurrentUser().getUserName(),password);
        if(userValid)
        {
            DbManager.getInstance().changeName(newFirst,newLast,DbManager.getInstance().getCurrentUser().getUserName());
        }
        else{
            System.out.println("incorrect password");
        }

    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        User current = new User();
        current = DbManager.getInstance().getCurrentUser();
        firstName.setText(current.getFirstName());
        lastName.setText(current.getLastName());

    }
}
