package MenuUI;

import Game.ComputerAlgorithm;
import Game.Game;
import Game.Minimax;
import Game.Position;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class MenuUIController
{

    @FXML private Button resetBtn;

    @FXML private Label notificationLabel;

    @FXML
    public void handleButtonClick(ActionEvent event)
    {
        for(int button = 0; button < buttonList.size(); button++)
        {
            if(event.getSource() == buttonList.get(button))
            {
                if (!game_has_winner)
                {
                    Position pos = getPositionFromIndex(button);
                    int token_moved = game.requestPosition(pos.getRow(), pos.getCol());
                    if (token_moved != 0)
                        updateTokens();
                    else
                        notificationLabel.setText("This position is not available. Please select another!");
                    checkWin();
                }
            }
        }
    }

    @FXML
    public void handleResetClick(ActionEvent even)
    {
        game = new Game(boardSize, playerCount);
        updateTokens();
        setDisable(false);
        game_has_winner = false;
        notificationLabel.setText("");
    }

}
