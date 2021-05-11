package BoardUI;

import MenuUI.*;
import Linkers.*;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import Game.*;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.net.URL;
import java.util.*;

public class BoardUIController implements Initializable, BoardSubject, UIObserver
{
    private int                 playerCount = 1;
    private int                 boardSize = 3;
    private Game                game   = new Game(boardSize, playerCount);
    private boolean             game_has_winner = false;
    private Image               YToken = new Image("/resources/images/TokenX.png");
    private Image               XToken = new Image("/resources/images/TokenO.png");
    private ComputerAlgorithm   ai = new Minimax();
    private OpenScene           openScene = new OpenScene();
    private int token;

    private ArrayList<BoardObserver> observers = new ArrayList<>();

    @FXML private ArrayList<Button>    buttonList;
    @FXML private ArrayList<ImageView> imageList;
    @FXML private Button resetBtn;
    @FXML private Label notificationLabel;
    @FXML private MediaView mv;

    private MediaPlayer mp;
    private Media me;

    @FXML
    public void handleButtonClick(ActionEvent event)
    {
        for(int button = 0; button < buttonList.size(); button++)
        {
            if(event.getSource() == buttonList.get(button))
            {
                Position pos = getPositionFromIndex(button);
                notifyObservers(pos, token);
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

    public void setImage(int token, int row, int col)
    {
        if (token == 1)
            imageList.get(getIndexFromRowCol(row, col)).setImage(XToken);
        else
            imageList.get(getIndexFromRowCol(row, col)).setImage(YToken);
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

    public void setResult(int token, int winner)
    {
        notificationLabel.setText("Winner is: " + token);
        setDisable(true);
    }

    public Label getNotificationLabel()
    {
        return notificationLabel;
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String path = new File("src/resources/images/background1.mp4").getAbsolutePath();

        me = new Media(new File(path).toURI().toString());
        mp = new MediaPlayer(me);
        mv.setMediaPlayer(mp);
        mp.setAutoPlay(true);
//        mp.setCycleCount(MediaPlayer.INDEFINITE);
        mp.setOnEndOfMedia(new Runnable() {
            @Override
            public void run() {
                mp.seek(Duration.ZERO);
                mp.play();
            }
        });
    }

    @Override
    public void addObserver(Object o)
    {
        observers.add((BoardObserver) o);
    }

    @Override
    public void removeObserver(Object o)
    {
        observers.remove(o);
    }

    @Override
    public void notifyObservers(Position pos, int token)
    {
        observers.forEach(observer -> observer.update(pos, token));
    }

    @Override
    public void update(Position pos, int token)
    {
        this.token = token;
        Platform.runLater(new Runnable(){
            @Override
            public void run() {
                if(pos.getRow() == 20) {
                    notificationLabel.setTextFill(Color.WHITE);
                    notificationLabel.setText("Winner is: Player " + token);
                    setDisable(true);
                }
                else
                    setImage(token, pos.getRow(), pos.getCol());
            }
        });
    }
}
