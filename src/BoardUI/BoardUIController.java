package BoardUI;

import MenuUI.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.paint.Color;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import Game.*;
import javafx.stage.Stage;

import java.util.*;

public class BoardUIController
{
    private int                 playerCount = 1;
    private int                 boardSize = 3;
    private Game                game   = new Game(boardSize, playerCount);
    private boolean             game_has_winner = false;
    private Image               YToken = new Image("/resources/images/TokenX.png");
    private Image               XToken = new Image("/resources/images/TokenO.png");
    private ComputerAlgorithm   ai = new Minimax();
    private OpenScene           openScene = new OpenScene();

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
                    {
                        notificationLabel.setTextFill(new Color(1, 0, 0, 1));
                        notificationLabel.setText("This position is not available. Please select another!");
                    }
                    checkWin();
                }
            }
        }
    }

    @FXML
    public void handleMenuClick(ActionEvent even) throws Exception
    {
        Stage stage = (Stage) resetBtn.getScene().getWindow();
        FXMLLoader root = new FXMLLoader();
        root.setLocation(getClass().getResource("/MenuUI/MenuUI.fxml"));
        Parent frame = root.load();
        MenuUIController controller = (MenuUIController) root.getController();
        openScene.start(stage, frame, "Tic-Tac-Toe - Menu");
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
                notificationLabel.setTextFill(new Color(1, 1, 0, 1));
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

    public void setLocalPlayerCount(int playerCount)
    {
        this.playerCount = playerCount;
    }

    public void resetGame()
    {
        game = new Game(boardSize, playerCount);
        updateTokens();
        setDisable(false);
        game_has_winner = false;
        notificationLabel.setText("");
    }
}
