package Networking;
import Game.*;
import Messages.GameMessage;
import Messages.GameStatusMessage;
import Messages.Message;

import java.util.ArrayList;

public class GameProcess
{
    private Game game;
    private boolean gameActive = true;
    private String gameId;
    private Pair<ClientConnection, ClientConnection> players;
    private ArrayList<ClientConnection> spectators;
    private long startTime = System.currentTimeMillis();
    private long endTime;
    private ServerProcess server;
    private ClientProcess client;

    private final int MOVE_TIME_LIMIT = 30;

    public GameProcess(ServerProcess server, String gameId, Pair players)
    {
        game = new Game();
        this.server = server;
        this.gameId = gameId;
        this.players = players;
        this.spectators = new ArrayList<>();
    }

    public GameProcess(ClientProcess client)
    {
        game = new Game(2);
        this.client = client;
        this.spectators = new ArrayList<>();
    }

    public void gameEnded()
    {
        System.out.println("Game process ended!");
        gameActive = false;
        endTime = System.currentTimeMillis();
        if (server != null)
        {
            GameMessage newMessage = new GameMessage(game.getToken(), game.getWinner(), getSpectatorCount(),
                                                    -1, -1, -1);

            players.getFirst().setGameId(null);
            players.getSecond().setGameId(null);

            server.sendToSubscribedClients("GAME_" + gameId, newMessage, null);
            server.unsubscribe("GAME_" + gameId, "Game", players.getFirst());
            server.unsubscribe("GAME_" + gameId, "Game", players.getSecond());
            server.unsubscribe("CHAT_" + gameId, "Chat", players.getFirst());
            server.unsubscribe("CHAT_" + gameId, "Chat", players.getSecond());
            server.subscribe("CHAT_GLOBAL", "Chat", players.getFirst());
            server.subscribe("CHAT_GLOBAL", "Chat", players.getSecond());

            // Call database function
            // db.saveGameHistory(this);

            server.killGameProcess(gameId);
        }
        else if (client != null)
        {
            GameMessage newMessage = new GameMessage(game.getToken(), game.getWinner(), getSpectatorCount(),
                                                    -1, -1, -1);
        }
    }

    public int getToken(ClientConnection client)
    {
        // had to make this more specific so non-players cant request moves
        int token = 0;
        if (players.getFirst() == client)
            token = 1;
        else if (players.getSecond() == client)
            token = 2;
        return token;
    }

    public void requestMove(ClientConnection client, int row, int col)
    {
        int token = getToken(client);
        if (token != 0)
        {
            boolean moveMade = game.requestPosition(row, col, token);
            if (moveMade) 
            {
                GameMessage newMessage = new GameMessage(game.getToken(), game.getWinner(), getSpectatorCount(),
                        row, col, token);
                server.sendToSubscribedClients("GAME_" + gameId, newMessage, null);
                int winner = game.checkWin(); //Update winner
                if (winner != 0)
                    gameEnded();
            }
        }
    }

    public void requestMoveSinglePlayer(int row, int col)
    {
        int token = 1;
        boolean moveMade = game.requestPosition(row, col, token);
        if (moveMade)
        {
            GameMessage newMessage = new GameMessage(game.getToken(), game.getWinner(), getSpectatorCount(),
                                                     row, col, token);
            server.sendToSubscribedClients("GAME_" + gameId, newMessage, null);
            int winner = game.checkWin(); //Update winner
            if (winner != 0)
                gameEnded();
        }
    }

    public void addSpectator(ClientConnection spectator)
    {
        spectators.add(spectator);
        Message gameStatus = new GameStatusMessage(this.game);
        spectator.writeMessage(gameStatus);
    }

    public void removeSpectator(ClientConnection spectator)
    {
        spectators.remove(spectator);
    }

    public String getId() {return this.gameId;}
    public Game getGame() {return this.game;}
    public long getStartTime() {return this.startTime;}
    public long getEndTime() {return this.endTime;}
    public Pair getPlayers() {return this.players;}
    public int getSpectatorCount() {return this.spectators.size();}
}
