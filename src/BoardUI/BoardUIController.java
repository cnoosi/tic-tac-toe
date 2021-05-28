package BoardUI;

import MenuUI.*;
import Linkers.*;
import Messages.MoveMessage;
import Observers.Observer;
import Observers.ObserverMessage;
import Observers.Subject;
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

public class BoardUIController implements Initializable, Observer, Subject
{
    private int                 playerCount = 1;
    private int                 boardSize = 3;
    private Game                game   = new Game(boardSize, playerCount);
    private boolean             game_has_winner = false;
    private Image               YToken = new Image("/resources/images/TokenX.png");
    private Image               XToken = new Image("/resources/images/TokenO.png");
    private ComputerAlgorithm   ai = new Minimax();
    private OpenScene           openScene = new OpenScene();
    private int token = 1;
    private boolean singlePlayer = false;

    private ArrayList<BoardObserver> observers = new ArrayList<>();

    @FXML private ArrayList<Button>    buttonList;
    @FXML private ArrayList<ImageView> imageList;
    @FXML private Button resetBtn;
    @FXML private Label notificationLabel;
    @FXML private MediaView mv;

    private MediaPlayer mp;
    private Media me;

    private Observer UIProcess;

    @FXML
    public void handleButtonClick(ActionEvent event)
    {
        for(int button = 0; button < buttonList.size(); button++)
        {
            if(event.getSource() == buttonList.get(button))
            {
                System.out.println(singlePlayer);
                Position pos = getPositionFromIndex(button);
                ArrayList<String> move = new ArrayList<String>();
                move.add(String.valueOf(pos.getRow()));
                move.add(String.valueOf(pos.getCol()));
                move.add(String.valueOf(token));

                if(singlePlayer)
                {
                    ArrayList<String> move = new ArrayList<String>();
                    move.add(String.valueOf(pos.getRow()));
                    move.add(String.valueOf(pos.getCol()));
                    move.add(String.valueOf(1));
                    boolean moveMade = game.requestPosition(pos.getRow(), pos.getCol(), 1);
                    if (moveMade)
                    {
                        update(new ObserverMessage("UIMove", move));

                        int winnerToken1 = game.checkWin();
                        move.add(String.valueOf(winnerToken1));
                        update(new ObserverMessage("UIMove", move));

                        if (winnerToken1 == 0)
                        {
                            Position aiPos = ai.getMove(game);
                            ArrayList<String> aiMove = new ArrayList<String>();
                            aiMove.add(String.valueOf(aiPos.getRow()));
                            aiMove.add(String.valueOf(aiPos.getCol()));
                            aiMove.add(String.valueOf(2));
                            boolean aiMoveMade = game.requestPosition(aiPos.getRow(), aiPos.getCol(), 2);
                            if (aiMoveMade)
                            {
                                update(new ObserverMessage("UIMove", aiMove));

                                int winnerToken2 = game.checkWin();
                                aiMove.add(String.valueOf(winnerToken2));
                                update(new ObserverMessage("UIMove", aiMove));
                            }
                        }
                    }
                }
                else
                    notifyObservers(new ObserverMessage("Move", move));
            }
        }
    }

    @FXML
    public void handleMenuClick(ActionEvent even)
    {
        notifyObservers(new ObserverMessage("Home"));
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

    public void setDisable(boolean mode)
    {
        for(Button btn : buttonList)
            btn.setDisable(mode);
    }

    public void setSinglePlayer(boolean mode)
    {
        singlePlayer = mode;
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
    public void update(ObserverMessage message)
    {
        String type = message.getMessageType();
        if(type.equals("UIMove"))
        {
            Platform.runLater(new Runnable(){
                @Override
                public void run() {
                    int row = Integer.parseInt(message.getMessage().get(0));
                    int col = Integer.parseInt(message.getMessage().get(1));
                    int currentToken = Integer.parseInt(message.getMessage().get(2));
                    int currentWinner = 0;
                    if (message.getMessage().size() == 4)
                        currentWinner = Integer.parseInt(message.getMessage().get(3));
                    if(currentWinner != 0)
                    {
                        notificationLabel.setTextFill(Color.WHITE);
                        if (currentWinner != -1)
                            notificationLabel.setText("Winner is: Player " + currentWinner);
                        else
                            notificationLabel.setText("It's a tie!");
                        setDisable(true);
                    }
                    setImage(currentToken, row, col);
                }
            });
        }

        else if(type.equals("ClearBoard"))
        {
            imageList.forEach(imageView -> imageView.setImage(null));
        }

        else if(type.equals("SinglePlayerMode"))
        {
            setSinglePlayer(true);
            game = new Game(3, 1);
        }
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

//    @Override
//    public void addObserver(Object o)
//    {
//        observers.add((BoardObserver) o);
//    }
//
//    @Override
//    public void removeObserver(Object o)
//    {
//        observers.remove(o);
//    }
//
//    @Override
//    public void notifyObservers(Position pos, int token)
//    {
//        observers.forEach(observer -> observer.update(pos, token));
//    }
//
//    @Override
//    public void update(Position pos, int token)
//    {
//        this.token = token;
//        Platform.runLater(new Runnable(){
//            @Override
//            public void run() {
//                if(pos.getRow() == 20) {
//                    notificationLabel.setTextFill(Color.WHITE);
//                    notificationLabel.setText("Winner is: Player " + token);
//                    setDisable(true);
//                }
//                else
//                    setImage(token, pos.getRow(), pos.getCol());
//            }
//        });
//    }
}
