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
    private int                 playerCount = 1;
    private int                 boardSize = 3;
    private Game                game   = new Game(boardSize, playerCount);
    private boolean             game_has_winner = false;
    private final Image         YToken = new Image("/resources/images/Rect.png");
    private final Image         XToken = new Image("/resources/images/Oh.png");


     public void SetMultiplayer () {
        playerCount = 2;
        boardSize = 3;
        game   = new Game(boardSize, playerCount);
        game_has_winner = false;
    }

    public void SetSingleplayer(ComputerAlgorithm algo) {
         playerCount = 1;
         boardSize = 3;
         game = new Game(boardSize, playerCount, algo);
         game_has_winner = false;
    }

    @FXML private ArrayList<Button>    buttonList;
    @FXML private ArrayList<ImageView> imageList;
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

    public int getIndexFromRowCol(int row, int col)
    {
        return row * 3 + col;
    }

    public Position getPositionFromIndex(int i)
    {
        int row = i / game.getBoardSize();
        int col = i % game.getBoardSize();
        return new Position(row, col);
    }

    public void updateTokens()
    {
        notificationLabel.setText("");
        for (int i = 0; i < game.getBoardSize(); i++) {
            for (int j = 0; j < game.getBoardSize(); j++) {
                ImageView image = imageList.get(getIndexFromRowCol(i, j));
                int tokenAtPosition = game.getPosition(i, j);
                if (tokenAtPosition == 0)
                    image.setImage(null);
                else if (tokenAtPosition == 1)
                    image.setImage(XToken);
                else if (tokenAtPosition == 2)
                    image.setImage(YToken);
            }
        }
    }

    public void checkWin()
    {
        int winner = game.checkWin();
        System.out.println(winner);
        if (winner != 0)
        {
            if (winner == -1)
            {
                notificationLabel.setTextFill(new Color(1, 1, 1, 1));
                notificationLabel.setText("Tie!");
            }
            else if (winner == 1)
            {
                notificationLabel.setTextFill(new Color(0.2, 1, 1, 1));
                notificationLabel.setText("Player " + winner + " wins!");
            }
            else if (winner == 2)
            {
                notificationLabel.setTextFill(new Color(1, 0, 1, 1));
                notificationLabel.setText("Player " + winner + " wins!");
            }
            setDisable(true);
            game_has_winner = true;
        }
        game_has_winner = false;
    }

    public void setDisable(boolean mode)
    {
        for(Button btn : buttonList)
            btn.setDisable(mode);
    }
}
