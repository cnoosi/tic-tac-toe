package BoardUI;

import javafx.scene.paint.Color;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import Game.*;

public class BoardUIController
{
    @FXML private ImageView box_00;
    @FXML private ImageView box_01;
    @FXML private ImageView box_02;
    @FXML private ImageView box_10;
    @FXML private ImageView box_11;
    @FXML private ImageView box_12;
    @FXML private ImageView box_20;
    @FXML private ImageView box_21;
    @FXML private ImageView box_22;

    @FXML private Button btn_00;
    @FXML private Button btn_01;
    @FXML private Button btn_02;
    @FXML private Button btn_10;
    @FXML private Button btn_11;
    @FXML private Button btn_12;
    @FXML private Button btn_20;
    @FXML private Button btn_21;
    @FXML private Button btn_22;

    @FXML private Label notificationLabel;

    private Game game = new Game(3);
    //private Notification = new Notification(notificationLabel);
    private Image XToken = new Image("/resources/images/x.png");
    private Image YToken = new Image("/resources/images/o.png");

    @FXML
    public void handleButtonClick(ActionEvent event)
    {
        if (event.getSource() == btn_00)
        {
            setToken(box_00, 0, 0);
        }

        else if (event.getSource() == btn_01)
        {
            setToken(box_01, 0, 1);
        }

        else if (event.getSource() == btn_02)
        {
            setToken(box_02, 0, 2);
        }

        else if (event.getSource() == btn_10)
        {
            setToken(box_10, 1,0);
        }

        else if (event.getSource() == btn_11)
        {
            setToken(box_11, 1, 1);
        }

        else if (event.getSource() == btn_12)
        {
            setToken(box_12, 1,2);
        }

        else if (event.getSource() == btn_20)
        {
            setToken(box_20, 2, 0);
        }

        else if (event.getSource() == btn_21)
        {
            setToken(box_21, 2, 1);
        }

        else if (event.getSource() == btn_22)
        {
            setToken(box_22, 2, 2);
        }
    }

    public void setToken(ImageView image, int i, int j)
    {
        notificationLabel.setText("");
        boolean move = game.setPosition(i, j);
        if (move)
        {
            if (game.getToken() == 1)
                image.setImage(XToken);
            else
                image.setImage(YToken);
            game.switchToken();
            int winner = game.checkWin();
            if (winner != 0) {
                if (winner == -1) {
                    notificationLabel.setTextFill(new Color(1, 1, 1, 1));
                    notificationLabel.setText("Tie!");
                    return;
                } else if (winner == 1)
                    notificationLabel.setTextFill(new Color(0, 0, 1, 1));
                else if (winner == 2)
                    notificationLabel.setTextFill(new Color(1, 0, 1, 1));
                notificationLabel.setText("Player " + winner + " wins!");
            }
        }
        else
        {
            notificationLabel.setTextFill(new Color(1, 0, 0, 1));
            notificationLabel.setText("This position is already taken! Please select another");
        }
    }
}
