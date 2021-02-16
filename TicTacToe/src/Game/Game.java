package Game;

public class Game
{
    // creating a board where
    // 0 - empty
    // 1 - X
    // 2 - O
    private int[][] board;
    private int currentToken;


    // creates a new board initialized to 0
    public Game()
    {
        board = new int[3][3];
        currentToken = 1;
    }

    /*
    if the selected spot is empty, current letter is set to the spot
    else, throws an exception which will be shown in UI error text area
     */
    public void setPos(int x, int y, int currentToken)
    {
        if(board[x][y] == 0)
        {
            board[x][y] = currentToken;
        }
    }

    // if current token == X, return O, else return X
    public void switchToken()
    {
        if(currentToken == 1)
            currentToken = 2;
        else
            currentToken = 1;
    }

    public int getToken()
    {
        return currentToken;
    }

}
