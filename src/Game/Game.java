package Game;

public class Game implements Cloneable
{
    private int[][]             boardData;
    private int                 boardSize;
    private int                 token;
    private int                 localPlayers;
    private ComputerAlgorithm   ai;

    public Game()
    {
        this.boardSize = 3;
        this.boardData = new int[boardSize][boardSize];
        this.token = 1;
        this.localPlayers = 1;
        ai = new Minimax();
    }

    public Game(int boardSize, int localPlayers)
    {
        this.boardSize = boardSize;
        this.boardData = new int[boardSize][boardSize];
        this.token = 1;
        this.localPlayers = localPlayers;
        ai = new Minimax();
    }

    public Game(int[][] boardData, int boardSize, int localPlayers)
    {
        this.boardData = boardData;
        this.boardSize = boardSize;
        this.token = 1;
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
        token = (token == 1? 2:1);
    }

    public int getToken()
    {
        return token;
    }

    public int getPosition(int i, int j)
    {
        return boardData[i][j];
    }

    public int requestPosition(int i, int j)
    {
        if (getPosition(i, j) == 0)
        {
            int playerToken = this.token;
            setPosition(i, j, playerToken);
            switchToken();
            // Make a move for the AI if only single player
            if (this.token == 2 && this.localPlayers == 1)
            {
                Position pos = ai.getMove(this);
                requestPosition(pos.getRow(), pos.getCol());
            }
            return playerToken;
        }
        return 0;
    }

    public void setPosition(int i, int j, int token)
    {
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
                return consecutivePlayer[0];
        }

        //Vertical Check
        for (int col = 0; col < boardSize; col++)
        {
            for (int row = 0; row < boardSize; row++)
            {
                consecutivePlayer[row] = boardData[row][col];
            }
            if(checkConsecutivePlayer(consecutivePlayer))
                return consecutivePlayer[0];
        }

        //Cross Check (Left -> right)
        for (int i = 0; i < boardSize; i++)
        {
            consecutivePlayer[i] = boardData[i][i];
        }
        if(checkConsecutivePlayer(consecutivePlayer))
            return consecutivePlayer[0];

        //Cross Check (Right -> left)
        for (int i = boardSize - 1; i >= 0; i--)
        {
            int j = boardSize - 1 - i;
            consecutivePlayer[i] = boardData[i][j];
        }
        if(checkConsecutivePlayer(consecutivePlayer))
            return consecutivePlayer[0];

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
            return -1; //Tie!

        return 0; //No winner
    }

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
