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

    //findGame.requestMove(client, row, col);

    public boolean requestMove(ClientConnection client, int row, int col)
    {
        game.setPosition(row, col, client.getToken());
        return true;
    }

    public void run()
    {
        System.out.println("Hello World");
    }

}
