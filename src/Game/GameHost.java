package Game;

import java.util.ArrayList;

// Class to be used by server / host of the connection
public class GameHost {
    private ArrayList<Player> players = new ArrayList<Player>();
    private GameState currentGameState = new GameState();
    private int currentToken = -1;
    //private Board board; //Rename Game class to Board when necessary. Doing so will break current game

    public GameHost() {}

    public int subscribe(Player p)
    {
        //Get next available token to return and add to player list, update clients
        int newToken = 0;


        updatePlayers();
        return newToken;
    }

    public void unsubscribe(Player p)
    {

    }

    public boolean requestMove(int token, int row, int col)
    {
        checkWin();
        return true;
    }
    public void checkWin()
    {
        currentGameState.setCurrentWinner(-1);
    }
    public void resetGame()
    {
        currentGameState = new GameState();
        //board = new Board();
        updateBoardData();
        updateGameState();
    }
    public void setNextToken()
    {
        currentToken = currentToken + 1 > players.size() ? currentToken + 1 : 0;
        currentGameState.setCurrentToken(currentToken);
        updateGameState();
    }


    // Subject (host) attributes
    public void updateBoardData()
    {
        for (Player p : players)
        {
            //p.boardDataReceived(board.getBoardData());
        }
    }

    public void updateGameState()
    {
        for (Player p : players)
        {
            p.gameStateReceived(currentGameState);
        }
    }
    public void updatePlayers()
    {
        for (Player p : players)
        {
            p.playersReceived(players);
        }
    }
}
