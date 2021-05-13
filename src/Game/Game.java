package Game;

import java.util.ArrayList;

public class Game implements Cloneable
{
    private int[][]             boardData;
    private int                 boardSize;
    private int                 token = 1;
    private int                 moveIndex = 0;
    private long                lastMove = System.currentTimeMillis();
    private int                 winnerToken = 0;
    private ArrayList<Position> moves = new ArrayList<Position>();
    private int                 localPlayers;
    private ComputerAlgorithm   ai;

    public Game()
    {
        this.boardSize = 3;
        this.boardData = new int[boardSize][boardSize];
        this.localPlayers = 2;
        ai = new Minimax();
    }

    public Game(int localPlayers)
    {
        this.boardSize = 3;
        this.boardData = new int[boardSize][boardSize];
        this.localPlayers = localPlayers;
        ai = new Minimax();
    }

    public Game(int boardSize, int localPlayers)
    {
        this.boardSize = boardSize;
        this.boardData = new int[boardSize][boardSize];
        this.localPlayers = localPlayers;
        ai = new Minimax();
    }

    public Game(int[][] boardData, int boardSize, int localPlayers)
    {
        this.boardData = boardData;
        this.boardSize = boardSize;
        this.localPlayers = localPlayers;
        ai = new Minimax();
    }

    public int[][] getBoardData()
    {
        return boardData;
    }

    public int getBoardSize()
    {
        return boardSize;
    }

    public void switchToken()
    {
        lastMove = System.currentTimeMillis();
        token = (token == 1? 2:1);
    }

    public int getToken()
    {
        return token;
    }

    public int getWinner() {return winnerToken;}

    public long getLastMove() {return lastMove;}

    public int getPosition(int i, int j)
    {
        return boardData[i][j];
    }

    public boolean requestPosition(int i, int j, int playerToken)
    {
        if (this.token == playerToken && getPosition(i, j) == 0)
        {
            setPosition(i, j, playerToken);
            this.moves.add(new Position(i, j));
            switchToken();
            // Make a move for the AI if only single player
            if (this.token == 2 && this.localPlayers == 1)
            {
                Position pos = ai.getMove(this);
                requestPosition(pos.getRow(), pos.getCol(), playerToken);
            }
            return true;
        }
        return false;
    }

    public void setPosition(int i, int j, int token)
    {
        lastMove = System.currentTimeMillis();
        boardData[i][j] = token;
    }

    public void unsetPosition(int i, int j)
    {
        boardData[i][j] = 0;
    }

    public void reset()
    {
        for (int i = 0; i < boardSize; i++)
        {
            for (int j = 0; j < boardSize; j++)
            {
                boardData[i][j] = 0;
            }
        }
    }

    protected boolean checkConsecutivePlayer(int [] array)
    {
        for (int i = 1; i < array.length; i++)
        {
            if (array[i] == 0 || array[i] != array[i - 1])
            {
                return false;
            }
        }
        return true;
    }


    public int checkWin()
    {
        int[] consecutivePlayer = new int[boardSize];

        //Horizontal Check
        for (int row = 0; row < boardSize; row++)
        {
            for (int col = 0; col < boardSize; col++)
            {
                consecutivePlayer[col] = boardData[row][col];
            }
            if(checkConsecutivePlayer(consecutivePlayer))
            {
                this.winnerToken = consecutivePlayer[0];
                return this.winnerToken;
            }
        }

        //Vertical Check
        for (int col = 0; col < boardSize; col++)
        {
            for (int row = 0; row < boardSize; row++)
            {
                consecutivePlayer[row] = boardData[row][col];
            }
            if(checkConsecutivePlayer(consecutivePlayer))
            {
                this.winnerToken = consecutivePlayer[0];
                return this.winnerToken;
            }
        }

        //Cross Check (Left -> right)
        for (int i = 0; i < boardSize; i++)
        {
            consecutivePlayer[i] = boardData[i][i];
        }
        if(checkConsecutivePlayer(consecutivePlayer))
        {
            this.winnerToken = consecutivePlayer[0];
            return this.winnerToken;
        }

        //Cross Check (Right -> left)
        for (int i = boardSize - 1; i >= 0; i--)
        {
            int j = boardSize - 1 - i;
            consecutivePlayer[i] = boardData[i][j];
        }
        if(checkConsecutivePlayer(consecutivePlayer))
        {
            this.winnerToken = consecutivePlayer[0];
            return this.winnerToken;
        }

        //Full board check
        boolean isBoardFull = true;
        for (int row = 0; row < boardSize; row++)
        {
            for (int col = 0; col < boardSize; col++)
            {
                if (boardData[row][col] == 0)
                {
                    isBoardFull = false;
                    break;
                }
            }
        }
        if (isBoardFull)
        {
            this.winnerToken = -1;
            return this.winnerToken; //Tie!
        }

        this.winnerToken = 0;
        return this.winnerToken; //No winner
    }

    public int getWinnerToken() {return this.winnerToken;}
    public ArrayList<Position> getMoves() {return this.moves;}

    @Override
    public String toString()
    {
        String output = "";

        for(int r = 0; r < boardSize; r++)
        {
            for(int c = 0; c < boardSize; c++)
            {
                output += boardData[r][c] + "  ";
            }
            output += "\n";
        }

        return output;
    }

    @Override
    public Object clone() throws CloneNotSupportedException{
        return super.clone();
    }
}
