package Game;

import java.util.ArrayList;

public class Player {
    private GameHost host;
    private String name = "John Doe";
    private int winCount = 0;
    private GameState currentGameState = new GameState();
    private ArrayList<Player> players = new ArrayList<Player>();
    private int[][] boardData;
    private int token = -1;

    public Player(GameHost host)
    {
        this.host = host;
    }
    public Player(GameHost host, String name)
    {
        this.host = host;
        this.name = name;
    }

    public boolean sendMoveRequest(int row, int col)
    {
        boolean moveCompleted = host.requestMove(this.token, row, col);
        return true;
    }



    // Observer Attributes
    public void boardDataReceived(int[][] boardData)
    {
        this.boardData = boardData;
        //Update board ui
    }

    public void gameStateReceived(GameState currentGameState)
    {
        this.currentGameState = currentGameState;
        //Update state ui
    }

    public void playersReceived(ArrayList<Player> players)
    {
        this.players = players;
        //Update player ui
    }
}
