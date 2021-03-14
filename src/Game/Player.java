package Game;

import java.util.ArrayList;

public class Player {
    private String name;
    private int winCount;
    private GameState currentGameState;
    private ArrayList<Player> players;
    private int[][] boardData;
    private int token;

    public Player()
    {

    }

    public boolean sendMoveRequest(int i, int j)
    {
        return true;
    }
}
