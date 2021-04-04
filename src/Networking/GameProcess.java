package Networking;
import Game.Game;
import Messages.GameMessage;

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
    {
        //This is where the game (fun) begins!
        while (true)
        {

        }
        //System.out.println("Hello World");
    }
}
