package Game;

public class Game implements Cloneable
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
        if (token == 1)
            token = 2;
        else
            token = 1;
    }

    public int getToken()
    {
        return token;
    }

    public int getPosition(int i, int j)
    {
        return boardData[i][j];
    }

    public boolean setPosition(int i, int j)
    {
        if (getPosition(i, j) == 0)
        {
            boardData[i][j] = token;
            return true;
        }
        return false;
    }

    public void unsetPosition(int i, int j)
    {
        boardData[i][j] = 0;
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
        boolean isSamePlayer = true;

        //Horizontal Check
        for (int row = 0; row < boardSize; row++)
        {
            for (int col = 0; col < boardSize; col++)
            {
                consecutivePlayer[col] = boardData[row][col];
            }
            isSamePlayer = true;
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
            isSamePlayer = true;
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

        //Cross Check (Left -> right)
        for (int i = 0; i < boardSize; i++)
        {
            consecutivePlayer[i] = boardData[i][i];
        }
        isSamePlayer = true;
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

        //Cross Check (Right -> left)
        for (int i = boardSize - 1; i >= 0; i--)
        {
            int j = boardSize - 1 - i;
            consecutivePlayer[i] = boardData[i][j];
        }
        isSamePlayer = true;
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

    @Override
    public Object clone() throws CloneNotSupportedException{
        return super.clone();
    }
}
