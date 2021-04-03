package Networking;
import Game.Game;

public class GameProcess implements Runnable
{
    private Game game;
    private String gameId;
    private Pair<ClientConnection, ClientConnection> players;
    private ServerProcess server;

    public GameProcess()
    {
        game = new Game();
        gameId = null;
        players = new Pair<>();
        server = new ServerProcess();
    }

    public GameProcess(ServerProcess server, String gameId, Pair players)
    {
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
        boolean moveMade = game.requestPosition(row, col, getToken(client));
        if (moveMade)
            System.out.println("A move was MADE by token: " + getToken(client));
        else
            System.out.println("A move was BLOCKED by token: " + getToken(client));
    }

    public void run()
    {
        //This is where the game (fun) begins!
        System.out.println("Hello World");
    }
}
