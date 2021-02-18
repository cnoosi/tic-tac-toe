package BoardUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Button;
import Game.*;

public class BoardUIController
{
    private Game game = new Game();
    private Image XToken = new Image("/resources/images/Rect.png");
    private Image YToken = new Image("/resources/images/Oh.png");

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

    @FXML
    public void handleButtonClick(ActionEvent event)
    {
        if (event.getSource() == btn_00)
        {
            setToken(box_00);
        }

        else if (event.getSource() == btn_01)
        {
            setToken(box_01);
        }

        else if (event.getSource() == btn_02)
        {
            setToken(box_02);
        }

        else if (event.getSource() == btn_10)
        {
            setToken(box_10);
        }

        else if (event.getSource() == btn_11)
        {
            setToken(box_11);
        }

        else if (event.getSource() == btn_12)
        {
            setToken(box_12);
        }

        else if (event.getSource() == btn_20)
        {
            setToken(box_20);
        }

        else if (event.getSource() == btn_21)
        {
            setToken(box_21);
        }

        else if (event.getSource() == btn_22)
        {
            setToken(box_22);
        }
    }

    public void setToken(ImageView image)
    {
        game.switchToken();
        if(game.getToken() == 1)
            image.setImage(XToken);
        else
            image.setImage(YToken);
    }
}
