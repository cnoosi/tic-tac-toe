package Networking;

import Messages.*;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;

public class ServerProcess implements Runnable
{
    private ServerSocket server;
    private boolean keepServerRunning = true;
    private ArrayList<ClientConnection> connections;
    private Map<String, ArrayList<ClientConnection>> subscriptions;
    private Map<String, GameProcess> games;
    private BlockingQueue<ClientConnection> matchmakingQueue;
    private BlockingQueue<Map<String, Object>> messagesToProcess;
    private UserDbManager userDatabase;

    private void handleSubscribeMessage(Map<String, Object> map)
    {
        ClientConnection client = (ClientConnection) map.get("Client");
        String topic = (String) map.get("Topic");
        String topicType = (String) map.get("TopicType");
        boolean subscribe = (boolean) map.get("Subscribe");
        if (subscribe)
            subscribe(topic, topicType, client);
        else
            unsubscribe(topic, topicType, client);
    }

    private void handleQueueMessage(Map<String, Object> map)
    {
        ClientConnection client = (ClientConnection) map.get("Client");
        boolean joinQueue = (boolean) map.get("InQueue");
        System.out.println("JOIN QUEUE: " + joinQueue);
        if (joinQueue)
            matchmakingQueue.add(client);
        else
            matchmakingQueue.remove(client);
    }

    private void handleMoveMessage(Map<String, Object> map)
    {
        ClientConnection client = (ClientConnection) map.get("Client");
        String gameId = (String) map.get("GameId");
        long row = (long) map.get("Row");
        long col = (long) map.get("Col");
        GameProcess findGame = games.get(gameId);
        if (findGame != null)
            findGame.requestMove(client, (int) row, (int) col);
    }

    private void handleChatMessage(Map<String, Object> map)
    {
        ClientConnection client = (ClientConnection) map.get("Client");
        String playerName = "" + client.getId(); //Eventually, change this to their actual name!
        String playerChat = (String) map.get("PlayerChat");
        String channelName = (String) map.get("ChatChannel");
        sendToSubscribedClients(channelName, new ChatMessage(playerName, playerChat, channelName), client);
    }

    private void handleSpectateMessage(Map<String, Object> map)
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
    }

    private void handleAccountMessage(Map<String, Object> map)
    {
        ClientConnection client = (ClientConnection) map.get("Client");
        AccountAction action = (AccountAction) map.get("Action");
        String username = (String) map.get("Username");
        String password = (String) map.get("Password");
        String firstName = (String) map.get("FirstName");
        String lastName = (String) map.get("LastName");
        if (action == AccountAction.Login)
        {
            boolean loginSuccess = userDatabase.userFound(username, password);
            if (loginSuccess)
            {
                client.setId(userDatabase.getUserId(username));
                client.writeMessage(new AccountMessage(AccountAction.Login, null, null,
                                    null, null, "success"));
            }
        }
        else if (action == AccountAction.Register)
        {
            if (userDatabase.userAvailable(username))
            {
                userDatabase.addUser(username, firstName, lastName, password);
                client.writeMessage(new AccountMessage(AccountAction.Register, null, null,
                                    null, null, "success"));
            }
            else
                client.writeMessage(new AccountMessage(AccountAction.Login, null, null,
                                    null, null, "unavailable"));
        }
        else if (action == AccountAction.Logout)
        {
            client.setId(0);
            client.writeMessage(new AccountMessage(AccountAction.Logout, null, null,
                               null, null, "success"));
        }
    }

    private void handleClientMessagesProcess()
    {
        try
        {
            while (keepServerRunning) {
                Map<String, Object> map = messagesToProcess.take();
                String messageType = (String) map.get("MessageType");
                switch (messageType)
                {
                    case "SubscribeMessage":
                        handleSubscribeMessage(map);
                        break;
                    case "QueueMessage":
                        handleQueueMessage(map);
                        break;
                    case "MoveMessage":
                        handleMoveMessage(map);
                        break;
                    case "ChatMessage":
                        handleChatMessage(map);
                        break;
                    case "SpectateMessage":
                        handleSpectateMessage(map);
                        break;
                    case "AccountMessage":
                        handleAccountMessage(map);
                        break;
                    default:
                        System.out.println("Failed to process message: " + messageType);
                }
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private void handleGameProcess()
    {
        try
        {
            Pair<ClientConnection, ClientConnection> gamePlayers = new Pair<>();
            while (keepServerRunning)
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
                    GameProcess newGameProcess = new GameProcess(this, newGameId, gamePlayers);
                    games.put(newGameId, newGameProcess);

                    //Subscribe clients to the game AND game chat channel
                    unsubscribe("CHAT_GLOBAL", "Chat", gamePlayers.getFirst());
                    unsubscribe("CHAT_GLOBAL", "Chat", gamePlayers.getSecond());
                    subscribe("CHAT_" + newGameId, "Chat", gamePlayers.getFirst());
                    subscribe("CHAT_" + newGameId, "Chat", gamePlayers.getSecond());
                    //Subscribe clients to the game
                    subscribe("GAME_" + newGameId, "Game", gamePlayers.getFirst());
                    subscribe("GAME_" + newGameId, "Game", gamePlayers.getSecond());

                    //Let the players know they're out of the queue!
                    QueueMessage gameFoundMessage = new QueueMessage(false, newGameId);
                    gamePlayers.getFirst().writeMessage(gameFoundMessage);
                    gamePlayers.getSecond().writeMessage(gameFoundMessage);

                    Thread handleNewGameThread = new Thread(newGameProcess);
                    handleNewGameThread.start();

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
        GameProcess findGame = games.get(gameId);
        games.remove(findGame);
    }

    public void sendToSubscribedClients(String topic, Message newMessage, ClientConnection ignoreClient)
    {
        ArrayList<ClientConnection> subs = subscriptions.get(topic);
        if (subs != null)
        {
            for (ClientConnection client : subs) {
                if (ignoreClient == null)
                    client.writeMessage(newMessage);
                else if (client != ignoreClient)
                    client.writeMessage(newMessage);
            }
        }
    }

    public void subscribe(String topic, String topicType, ClientConnection client)
    {
        ArrayList<ClientConnection> clients = subscriptions.get(topic);
        if (clients == null)
        {
            subscriptions.put(topic, new ArrayList<>());
            clients = subscriptions.get(topic);
        }
        clients.add(client);
        client.writeMessage(new SubscribeMessage(topic, topicType, true));
    }

    public void unsubscribe(String topic, String topicType, ClientConnection client)
    {
        ArrayList<ClientConnection> clients = subscriptions.get(topic);
        if (clients != null)
            return;
        client.writeMessage(new SubscribeMessage(topic, topicType, false));
    }

    public void processMessage(Map<String, Object> newMessage)
    {
        messagesToProcess.add(newMessage);
    }

    @Override
    public void run()
    {
        connections = new ArrayList<ClientConnection>();
        messagesToProcess = new SynchronousQueue<Map<String, Object>>();
        subscriptions = new HashMap<String, ArrayList<ClientConnection>>();
        matchmakingQueue = new SynchronousQueue<ClientConnection>();
        games = new HashMap<String, GameProcess>();
        userDatabase = new UserDbManager();

        Thread messagingProcessThread = new Thread(this::handleClientMessagesProcess);
        messagingProcessThread.start();

        Thread handleGameProcessThread = new Thread(this::handleGameProcess);
        handleGameProcessThread.start();

        try
        {
            server = new ServerSocket(8000);
            System.out.println("Server opened, waiting for new connections...");
            while (keepServerRunning)
            {
                Socket clientSocketConnection = server.accept();
                ClientConnection newConnection = new ClientConnection(clientSocketConnection, this);
                connections.add(newConnection);
                InetAddress inetAddress = clientSocketConnection.getInetAddress();
                System.out.println("Accepted connection from " + inetAddress.getHostAddress());

                //Subscribe the user to the global chat automatically
                subscribe("GLOBAL_CHAT", "Chat", newConnection);
            }
            System.out.println("Server shutting down");
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        finally
        {
            try
            {
                server.close();
            }
            catch (Exception ex) {}
        }
    }
}
