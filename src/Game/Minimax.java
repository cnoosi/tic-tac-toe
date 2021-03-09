package Game;

public class Minimax implements ComputerAlgorithm
{
    static private boolean minimize = true;
    static private boolean maximize = false;

    private int row = -1;
    private int col = -1;

    public int getRow()
    {
        return row;
    }

    public int getCol()
    {
        return col;
    }

    public void setMove(Game copy)
    {
        try
        {
            Game game = (Game)copy.clone();


        System.out.println("Here");
        // making a copy of the board
        int moveScore  = -999;

        for(int r = 0; r < game.getBoardData().length; r++)
        {
            for(int c = 0; c < game.getBoardData().length; c++)
            {
                if(game.getBoardData()[r][c] == 0)
                {
                    game.getBoardData()[r][c] = game.getToken();

                    int currentScore = minimax(game, 0, false);

                    game.getBoardData()[r][c] = 0;

                    if(currentScore > moveScore)
                    {
                        row = r;
                        col = c;
                        moveScore = currentScore;
                    }
                }
            }
        }
        //game.setPosition(row, col);
        }
        catch (Exception e) {}
    }

    public int minimax(Game game, int depth, boolean isMax)
    {
        int checkBoard = game.checkWin();

        if(checkBoard == 1 || checkBoard == 2 || checkBoard == -1)
        {
            return checkBoard;
        }

        // if maximizing for ai
        if(isMax)
        {
            int score = -999;
            for(int i = 0; i < game.getBoardSize(); i++)
            {
                for(int j = 0; j < game.getBoardSize(); j++)
                {
                    if(game.getBoardData()[i][j] == 0)
                    {
                        game.setPosition(i, j);
                        score = Math.max(score, minimax(game, depth + 1, !isMax));
                        game.unsetPosition(i, j);
                    }
                }
            }
            return score;
        }

        // if minimizing for player
        else
        {
            int score = 999;
            for(int i = 0; i < game.getBoardSize(); i++)
            {
                for(int j = 0; j < game.getBoardSize(); j++)
                {
                    if(game.getBoardData()[i][j] == 0)
                    {
                        game.switchToken();
                        game.setPosition(i, j);
                        score = Math.min(score, minimax(game, depth + 1, isMax));
                        game.unsetPosition(i, j);
                    }
                }
            }
            return score;
        }
    }
}
