package Game;

public class Game
{
    private int[][] boardData;
    private int boardSize;
    private int token;

    public Game(int boardSize)
    {
        this.boardSize = boardSize;
        this.boardData = new int[boardSize][boardSize];
        this.token = 1;
    }
    public Game(int[][] boardData, int boardSize)
    {
        this.boardData = boardData;
        this.boardSize = boardSize;
        this.token = 1;
    }

    public void switchToken()
    {
        if (token == 1)
            token = 2;
        else
            token = 1;
    }

    public int getToken()
    {
        return token;
    }

    public boolean setPosition(int i, int j)
    {
        if (boardData[i][j] == 0)
        {
            boardData[i][j] = token;
            //ui change stuff (probably shouldn't do it within button)
            return true;
        }
        return false;
    }

    public void clearBoard()
    {
        for (int i = 0; i < boardSize; i++)
        {
            for (int j = 0; j < boardSize; j++)
            {
                boardData[i][j] = 0;
                //ui change stuff
            }
        }
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
            boolean isSamePlayer = true;
            for (int i = 1; i < boardSize; i++)
            {
                if (consecutivePlayer[i] != consecutivePlayer[i - 1])
                {
                    isSamePlayer = false;
                    break;
                }
            }
            if (isSamePlayer)
                return consecutivePlayer[0];
        }
        //Vertical Check
        for (int col = 0; col < boardSize; col++)
        {
            for (int row = 0; row < boardSize; row++)
            {
                consecutivePlayer[row] = boardData[row][col];
            }
            boolean isSamePlayer = true;
            for (int i = 1; i < boardSize; i++)
            {
                if (consecutivePlayer[i] != consecutivePlayer[i - 1])
                {
                    isSamePlayer = false;
                    break;
                }
            }
            if (isSamePlayer)
                return consecutivePlayer[0];
        }
        //Cross Check
        for (int i = 0; i < boardSize; i++)
        {
            consecutivePlayer[i] = boardData[i][i];
        }
        boolean isSamePlayer = true;
        for (int i = 1; i < boardSize; i++)
        {
            if (consecutivePlayer[i] != consecutivePlayer[i - 1])
            {
                isSamePlayer = false;
                break;
            }
        }
        if (isSamePlayer)
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
}
