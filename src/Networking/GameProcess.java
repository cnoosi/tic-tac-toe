package Networking;
import Game.*;
import Messages.GameMessage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.HashMap;
import java.util.Map;

public class GameProcess implements Runnable
{
    private Game game;
    private boolean gameActive = true;
    private String gameId;
    private Pair<ClientConnection, ClientConnection> players;
    private ServerProcess server;
    private ClientProcess client;

    private final int MOVE_TIME_LIMIT = 30;

    public GameProcess(ServerProcess server, String gameId, Pair players)
    {
        game = new Game();
        this.server = server;
        this.gameId = gameId;
        this.players = players;
    }

    public GameProcess(ClientProcess client)
    {
        game = new Game(2);
        this.client = client;
    }

    private void gameEnded()
    {
        gameActive = false;
        System.out.println("game is over!");
        if (server != null)
        {
            GameMessage newMessage = new GameMessage(game.getToken(), game.getWinner(),
                    -1, -1, -1);

            server.sendToSubscribedClients("GAME_" + gameId, newMessage, null);
            server.unsubscribe("GAME_" + gameId, "Game", players.getFirst());
            server.unsubscribe("GAME_" + gameId, "Game", players.getSecond());
            server.unsubscribe("CHAT_" + gameId, "Chat", players.getFirst());
            server.unsubscribe("CHAT_" + gameId, "Chat", players.getSecond());
            server.subscribe("CHAT_GLOBAL", "Chat", players.getFirst());
            server.subscribe("CHAT_GLOBAL", "Chat", players.getSecond());
            server.killGameProcess(gameId);
        }
        else if (client != null)
        {
            GameMessage newMessage = new GameMessage(game.getToken(), game.getWinner(),
                                                    -1, -1, -1);

        }
    }

    public int getToken(ClientConnection client)
    {
        return players.getFirst() == client ? 1 : 2;
    }

    public void requestMove(ClientConnection client, int row, int col)
    {
        int token = getToken(client);
        boolean moveMade = game.requestPosition(row, col, token);
        if (moveMade)
        {
            System.out.println("A move was MADE by token: " + token);
            GameMessage newMessage = new GameMessage(game.getToken(), game.getWinner(), row, col, token);
            server.sendToSubscribedClients("GAME_" + gameId, newMessage, null);
            int winner = game.checkWin(); //Update winner
            if (winner != 0)
                gameEnded();
        }
        else
            System.out.println("A move was BLOCKED by token: " + token);
        System.out.println(game);
    }

    public void requestMoveSinglePlayer(int row, int col)
    {
        int token = 1;
        boolean moveMade = game.requestPosition(row, col, token);
        if (moveMade)
        {
            System.out.println("A move was MADE by token: " + token);
            GameMessage newMessage = new GameMessage(game.getToken(), game.getWinner(), row, col, token);
            server.sendToSubscribedClients("GAME_" + gameId, newMessage, null);
            int winner = game.checkWin(); //Update winner
            if (winner != 0)
                gameEnded();
        }
        else
            System.out.println("A move was BLOCKED by token: " + token);
    }

    public void run()
    {
        while(gameActive)
        {
            //Countdown texts
            long lastMove = game.getLastMove() / 1000;
            long currentTime = System.currentTimeMillis() / 1000;
            if (currentTime - lastMove > MOVE_TIME_LIMIT)
            {
                // They ran out of time!
                //System.out.println("TIME LIMIT HIT! TOKEN SWITCHED!");
                //game.switchToken();
            }
        }
    }
}
