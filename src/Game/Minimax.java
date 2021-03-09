package Game;

public class Minimax implements ComputerAlgorithm
{
    static private boolean minimize = true;
    static private boolean maximize = false;

    private Position position = new Position();

    public Position getMove(Game copy)
    {
        try
        {
            Game game = (Game)copy.clone();

            // making a copy of the board
            int moveScore  = -999;

            for(int r = 0; r < game.getBoardSize(); r++)
            {
                for(int c = 0; c < game.getBoardSize(); c++)
                {
                    if(game.getBoardData()[r][c] == 0)
                    {
                        game.getBoardData()[r][c] = game.getToken();

                        int currentScore = minimax(game, 0, false);

                        game.getBoardData()[r][c] = 0;

                        if(currentScore > moveScore)
                        {
                            position.setRow(r);
                            position.setCol(c);
                            moveScore = currentScore;
                        }
                    }
                }
            }
        }
        catch (Exception e) {}
        return position;
    }

    public int minimax(Game game, int depth, boolean isMax)
    {
        int checkBoard = game.checkWin();

        if(checkBoard == 1)
        {
            return (-1)*checkBoard;
        }

        if(checkBoard == 2)
        {
            return checkBoard;
        }

        if(checkBoard == -1)
        {
            return 0;
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
                        game.setPosition(i, j, 2);
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
                        game.setPosition(i, j, 1);
                        score = Math.min(score, minimax(game, depth + 1, !isMax));
                        game.unsetPosition(i, j);
                    }
                }
            }
            return score;
        }
    }
}
