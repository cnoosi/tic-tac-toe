package Networking;

import Game.*;
import Messages.GameHistoryMessage;
import Messages.GameReplayMessage;
import Messages.GetGameDataMessage;
import Messages.QueueMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;

public class GamesService {
    private ServerProcess server;
    private Map<String, GameProcess> games;
    private BlockingQueue<ClientConnection> matchmakingQueue;
    DbManager database;

    private boolean serverActive = true;

    public GamesService(ServerProcess server, DbManager database)
    {
        this.server = server;
        this.database = database;
        matchmakingQueue = new SynchronousQueue<ClientConnection>();
        games = new HashMap<String, GameProcess>();

        Thread handleGameProcessThread = new Thread(this::handleGameProcess);
        handleGameProcessThread.start();
    }

    public void handleQueueMessage(Map<String, Object> map)
    {
        ClientConnection client = (ClientConnection) map.get("Client");
        boolean joinQueue = (boolean) map.get("InQueue");
        if (joinQueue)
        {
            if (client.getId() != -1)
            {
                System.out.println("JOIN QUEUE: true");
                matchmakingQueue.add(client);
            }
        }
        else
            matchmakingQueue.remove(client);
    }

    public void handleMoveMessage(Map<String, Object> map)
    {
        ClientConnection client = (ClientConnection) map.get("Client");
        String gameId = (String) map.get("GameId");
        long row = (long) map.get("Row");
        long col = (long) map.get("Col");
        GameProcess findGame = games.get(gameId);
        if (findGame != null)
            findGame.requestMove(client, (int) row, (int) col);
    }

    public void handleSpectateMessage(Map<String, Object> map)
    {
        ClientConnection client = (ClientConnection) map.get("Client");
        boolean spectate = (boolean) map.get("Spectate");
        String gameId = (String) map.get("GameId");
        GameProcess findGame = games.get(gameId);
        if (findGame != null)
        {
            if (spectate)
                findGame.addSpectator(client);
            else
                findGame.removeSpectator(client);
        }
        else
        {
            GameHistory game = database.getGameHistory(gameId);
            if (game != null)
            {
                ArrayList<MovesHistory> moves = database.getMoveHistory(gameId);
                client.writeMessage(new GameReplayMessage(game.getPlayer1Id(), game.getPlayer1Id(),
                        game.getWinnerToken(), moves.size(), game.getStartTime()));
                for (int i = 0; i < moves.size(); i++)
                {
                    MovesHistory move = moves.get(i);
                    client.writeMessage(new GameHistoryMessage(i, move.getTime(), move.getRow(), move.getCol()));
                }
            }
        }
    }

    public void handleGetGameDataMessage(Map<String, Object> map)
    {
        ClientConnection client = (ClientConnection) map.get("Client");
        String gameId = (String) map.get("GameId");
        GameHistory findGame = database.getGameHistory(gameId);
        if (findGame != null)
        {
            String player1Name = database.getUsername(findGame.getPlayer1Id());
            String player2Name = database.getUsername(findGame.getPlayer2Id());
            int gameDuration = (int) (findGame.getEndTime() - findGame.getStartTime());
            int winnerToken = findGame.getWinnerToken();
            client.writeMessage(new GetGameDataMessage(gameId, player1Name, player2Name, gameDuration, winnerToken));
        }
    }

    private void handleGameProcess()
    {
        try
        {
            Pair<ClientConnection, ClientConnection> gamePlayers = new Pair<>();
            while (serverActive)
            {
                ClientConnection nextPlayer = matchmakingQueue.take();

                if (gamePlayers.getFirst() == null)
                    gamePlayers.setFirst(nextPlayer);
                else if (gamePlayers.getSecond() == null)
                    gamePlayers.setSecond(nextPlayer);

                if (gamePlayers.getFirst() != null && gamePlayers.getSecond() != null)
                {
                    //Start the game!
                    String newGameId = JSON.generateGUID();
                    System.out.println("NEW GAME STARTED: " + newGameId);
                    GameProcess newGameProcess = new GameProcess(this, server, newGameId, gamePlayers);
                    games.put(newGameId, newGameProcess);

                    //Let the players know they're out of the queue!
                    QueueMessage gameFoundMessage = new QueueMessage(false, newGameId);
                    gamePlayers.getFirst().writeMessage(gameFoundMessage);
                    gamePlayers.getSecond().writeMessage(gameFoundMessage);

                    //Clear pair for another matchmaking attempt
                    gamePlayers = new Pair<>();
                }
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public void killGameProcess(String gameId)
    {
        games.remove(gameId);
    }

    public void saveGameData(GameProcess game)
    {
        Game gameObj = game.getGame();
        Map<String, Pair<Integer, Position>> moves = gameObj.getMoves();
        String gameId = game.getId();
        long startTime = game.getStartTime();
        long endTime = game.getEndTime();
        Pair<ClientConnection, ClientConnection> players = game.getPlayers();
        int playerId1 = players.getFirst().getId();
        int playerId2 = players.getSecond().getId();
        int winnerToken = gameObj.getWinnerToken();
        database.addGame(gameId, startTime, endTime, playerId1, playerId2, playerId1, winnerToken);

        for (Map.Entry<String, Pair<Integer, Position>> entry : moves.entrySet())
        {
            String time = entry.getKey();
            int moveIndex = entry.getValue().getFirst();
            int playerId;
            if (moveIndex % 2 == 0)
                playerId = playerId1;
            else
                playerId = playerId2;
            Position pos = entry.getValue().getSecond();
            database.addMoves(gameId, playerId, pos.getRow(), pos.getCol(), time, moveIndex);
        }
    }

    public void endClientGamesOnLeave(ClientConnection client)
    {
        // Check if the player is in an active game
        String gameId = client.getGameId();
        if (gameId != null)
        {
            GameProcess findGame = games.get(gameId);
            findGame.gameEnded();
        }
    }

    public Map<String, GameProcess> getLiveGames() {return this.games;}
}
