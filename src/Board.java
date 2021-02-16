public class Board
{
    private int[][] boardData;
    private int boardSize;

    Board(int boardSize)
    {
        this.boardSize = boardSize;
        this.boardData = new int[boardSize][boardSize];
    }
    Board(int[][] boardData, int boardSize)
    {
        this.boardData = boardData;
        this.boardSize = boardSize;
    }

    public boolean SetBoardPlacement(int i, int j, int player)
    {
        if (boardData[i][j] == 0)
        {
            boardData[i][j] = player;
            //ui change stuff
            return true;
        }
        return false;
    }

    public void ClearBoard()
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

    public int CheckWin()
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
