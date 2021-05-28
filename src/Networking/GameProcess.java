package Networking;
import Game.*;
import Messages.GameMessage;
import Messages.Message;
import Messages.SpectateMessage;

import java.util.ArrayList;
import java.util.Map;

public class GameProcess
{
    private Game game;
    private boolean gameActive = true;
    private String gameId;
    private Pair<ClientConnection, ClientConnection> players;
    private ArrayList<ClientConnection> spectators;
    private long startTime = System.currentTimeMillis();
    private long endTime;
    private GamesService gamesService;
    private ServerProcess server;
    private ClientProcess client;

    private final int MOVE_TIME_LIMIT = 30;

    public GameProcess(GamesService gamesService, ServerProcess server, String gameId, Pair<ClientConnection, ClientConnection> players)
    {
        game = new Game();
        this.server = server;
        this.gamesService = gamesService;
        this.gameId = gameId;
        this.players = players;
        this.spectators = new ArrayList<>();

        //Subscribe the players
        players.getFirst().setGameId(gameId);
        players.getSecond().setGameId(gameId);

        //Subscribe clients to the game AND game chat channel
        server.unsubscribe("CHAT_GLOBAL", "Chat", players.getFirst());
        server.unsubscribe("CHAT_GLOBAL", "Chat", players.getSecond());
        server.subscribe("CHAT_" + gameId, "Chat", players.getFirst());
        server.subscribe("CHAT_" + gameId, "Chat", players.getSecond());
        //Subscribe clients to the game
        server.subscribe("GAME_" + gameId, "Game", players.getFirst());
        server.subscribe("GAME_" + gameId, "Game", players.getSecond());
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

            // Unsubscribe all spectators
            for (int i = 0; i < spectators.size(); i++)
            {
                ClientConnection client = spectators.get(i);
                server.unsubscribe("GAME_" + gameId, "Game", client);
                server.unsubscribe("CHAT_" + gameId, "Chat", client);
                server.subscribe("CHAT_GLOBAL", "Chat", client);
            }

            // Call database function
            gamesService.saveGameData(this);

            gamesService.killGameProcess(gameId);
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
        if (gameActive)
        {
            int token = getToken(client);
            if (token != 0) {
                boolean moveMade = game.requestPosition(row, col, token);
                if (moveMade) {
                    GameMessage newMessage = new GameMessage(game.getToken(), game.getWinner(), getSpectatorCount(),
                            row, col, token);
                    server.sendToSubscribedClients("GAME_" + gameId, newMessage, null);
                    int winner = game.checkWin(); //Update winner
                    if (winner != 0)
                        gameEnded();
                }
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
        int currentToken = game.getToken();
        int winnerToken = game.getWinnerToken();
        int spectators = getSpectatorCount();
        Map<String, Pair<Integer, Position>> moves = this.game.getMoves();
        for (Map.Entry<String, Pair<Integer, Position>> entry : moves.entrySet())
        {
            int moveIndex = entry.getValue().getFirst();
            int token;
            if (moveIndex % 2 == 0)
                token = 1;
            else
                token = 2;
            Position pos = entry.getValue().getSecond();
            GameMessage gameMessage = new GameMessage(currentToken, winnerToken, spectators,
                                                      pos.getRow(), pos.getCol(), token);
            spectator.writeMessage(gameMessage);
        }
        server.unsubscribe("CHAT_GLOBAL", "Chat", spectator);
        server.subscribe("GAME_" + gameId, "Game", spectator);
        server.subscribe("CHAT_" + gameId, "Chat", spectator);
    }

    public void removeSpectator(ClientConnection spectator)
    {
        spectators.remove(spectator);
        server.unsubscribe("GAME_" + gameId, "Game", spectator);
        server.unsubscribe("CHAT_" + gameId, "Chat", spectator);
        server.subscribe("CHAT_GLOBAL", "Chat", spectator);
    }

    public String getId() {return this.gameId;}
    public Game getGame() {return this.game;}
    public long getStartTime() {return this.startTime;}
    public long getEndTime() {return this.endTime;}
    public Pair getPlayers() {return this.players;}
    public int getSpectatorCount() {return this.spectators.size();}
}
