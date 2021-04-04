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
    private String gameId;
    private Pair<ClientConnection, ClientConnection> players;
    private ServerProcess server;

    public GameProcess(ServerProcess server, String gameId, Pair players)
    {
        game = new Game();
        this.server = server;
        this.gameId = gameId;
        this.players = players;
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
        }
        else
            System.out.println("A move was BLOCKED by token: " + token);
    }

    public void run()
    { ;
        while(true)
        {
            if(game.checkWin() != 0)
            {
                Map<String, Object> message = new HashMap<>();
                message.put("MessageType", "SubscribeMessage");
                message.put("Client", players.getFirst());
                message.put("Topic", "CHAT_" + gameId);
                message.put("TopicType", "Chat");
                message.put("Subscribe", false);
                server.processMessage(message);

                message.replace("Client", players.getSecond());
                server.processMessage(message);

                message.replace("Topic", "GAME_" + gameId);
                message.replace("TopicType", "Game");
                server.processMessage(message);

                message.replace("Client", players.getFirst());
                server.processMessage(message);

                System.out.println("Hello World");
            }
            break;
        }
    }
}
