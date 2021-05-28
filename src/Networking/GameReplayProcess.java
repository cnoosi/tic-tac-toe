package Networking;

import Game.Position;
import Networking.ClientProcess;
import Networking.Pair;

import java.util.ArrayList;

public class GameReplayProcess implements Runnable
{
    private ClientProcess client;
    private int player1Id;
    private int player2Id;
    private long startTime;
    private int winnerToken;
    private int totalMoves;
    private ArrayList<Pair<String, Position>> moves;

    private boolean replayActive = true;
    private int currentMove;
    private long startReplayTime = 0;

    public GameReplayProcess(ClientProcess client, int player1Id, int player2Id, int totalMoves, int winnerToken, long startTime)
    {
        this.client = client;
        this.player1Id = player1Id;
        this.player2Id = player2Id;
        this.startTime = startTime;
        this.winnerToken = winnerToken;
        this.totalMoves = totalMoves;
        this.currentMove = 0;
        this.moves = new ArrayList<>();
    }

    public void addMove(Pair<String, Position> newMove)
    {
        moves.add(newMove);
    }

    @Override
    public void run()
    {
        try
        {
            while (replayActive)
            {
                boolean loadedMoves = moves.size() == totalMoves;
                if (loadedMoves) {
                    if (startReplayTime == 0)
                    {
                        client.getUi().clearBoard();
                        startReplayTime = System.currentTimeMillis();
                    }

                    while (currentMove < totalMoves && replayActive)
                    {
                        long replayTimePosition = System.currentTimeMillis() - startReplayTime;

                        Pair<String, Position> nextMove = moves.get(currentMove);
                        Long timestamp = Long.parseLong(nextMove.getFirst());
                        long moveTimePosition = timestamp - startTime;

                        if (replayTimePosition > moveTimePosition) {
                            int token;
                            if (currentMove % 2 == 0)
                                token = 1;
                            else
                                token = 2;
                            Position pos = nextMove.getSecond();
                            client.getUi().changeUIBoardToken(pos.getRow(), pos.getCol(), token, 0);
                            currentMove++;
                        }
                    }
                    replayActive = false;
                    client.getUi().updateBoardUI(1, winnerToken, 0);
                }
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void killReplay() {this.replayActive = false;}
}
