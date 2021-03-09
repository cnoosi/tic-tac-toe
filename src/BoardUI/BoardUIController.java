package BoardUI;

import javafx.scene.paint.Color;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import Game.*;
import java.util.*;

public class BoardUIController
{
    private Game  game   = new Game(3);
    private Image YToken = new Image("/resources/images/Rect.png");
    private Image XToken = new Image("/resources/images/Oh.png");

    private ComputerAlgorithm ai = new Minimax();

    @FXML private ArrayList<Button>    buttonList;
    @FXML private ArrayList<ImageView> imageList;
    @FXML private Button resetBtn;

    private Game game = new Game(3);
    //private Notification = new Notification(notificationLabel);
    private Image XToken = new Image("/resources/images/x.png");
    private Image YToken = new Image("/resources/images/o.png");

    @FXML
    public void handleButtonClick(ActionEvent event)
    {
        for(int button = 0; button < buttonList.size(); button++)
        {
            if(event.getSource() == buttonList.get(button))
            {
                setToken(imageList.get(button), button/3, button%3);
            }
        }

        Position pos = ai.getMove(game);
        setToken(imageList.get(pos.getRow()*3 + pos.getCol()), pos.getRow(), pos.getCol());
    }

    @FXML
    public void handleResetClick(ActionEvent even)
    {
        game = new Game(3);
        for(ImageView img : imageList)
            img.setImage(null);
        setDisable(false);
        notificationLabel.setText("");
    }

    public void setToken(ImageView image, int i, int j)
    {
        notificationLabel.setText("");
        boolean move = game.setPosition(i, j);
        if (move) {
            if (game.getToken() == 1)
                image.setImage(XToken);
            else
                image.setImage(YToken);
            game.switchToken();
            int winner = game.checkWin();
            System.out.println(winner);
            if (winner != 0) {
                if (winner == -1) {
                    notificationLabel.setTextFill(new Color(0, 0, 0, 1));
                    notificationLabel.setText("Tie!");
                    return;
                } else if (winner == 1)
                    notificationLabel.setTextFill(new Color(0.2, 1, 1, 1));
                else if (winner == 2)
                    notificationLabel.setTextFill(new Color(0, 0, 0, 1));
                notificationLabel.setText("Player " + winner + " wins!");
                setDisable(true);
            }
        } else {
            notificationLabel.setTextFill(new Color(1, 0, 0, 1));
            notificationLabel.setText("This position is already taken! Please select another");
        }
    }

    public void setDisable(boolean mode)
    {
        for(Button btn : buttonList)
            btn.setDisable(mode);
    }
}
