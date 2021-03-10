package Game;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;

import java.lang.Math;

public class RandomMandom implements ComputerAlgorithm
{

    private Position position = new Position();

    public Position getMove(Game copy)
    {
        try
        {
            int i = 0;
            int j = 0;

            Game game = (Game)copy.clone();

            if(game.checkWin() == 0)
            {
                while (game.getPosition(i,j) != 0) {
                    i = (int)(Math.random()* 3);
                    j = (int)(Math.random()* 3);
                    if(game.getPosition(i,j) == 0)
                        break;
                    //game.requestPosition(i,j);
                    //System.out.println("token : " + game.requestPosition(i,j));
                    System.out.println(i + "," + j);
                }
                position.setRow(i);
                position.setCol(j);

            }

        }
        catch (Exception e) {}
        return position;
    }
}
