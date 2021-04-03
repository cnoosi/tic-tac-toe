package Networking;

import Messages.ChatMessage;
import Messages.Message;
import Messages.QueueMessage;
import Messages.SubscribeMessage;

import java.lang.reflect.Array;
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

    private void handleClientMessagesProcess()
    {
        try
        {
            while (keepServerRunning) {
                Map<String, Object> map = messagesToProcess.take();
                String messageType = (String) map.get("MessageType");
                if (messageType.equals("SubscribeMessage"))
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
                else if (messageType.equals("QueueMessage"))
                {
                    ClientConnection client = (ClientConnection) map.get("Client");
                    boolean joinQueue = (boolean) map.get("InQueue");
                    System.out.println("JOIN QUEUE: " + joinQueue);
                    if (joinQueue)
                        matchmakingQueue.add(client);
                    else
                        matchmakingQueue.remove(client);
                }
                else if (messageType.equals("MoveMessage"))
                {
                    ClientConnection client = (ClientConnection) map.get("Client");
                    String gameId = (String) map.get("GameId");
                    long row = (long) map.get("Row");
                    long col = (long) map.get("Col");
                    GameProcess findGame = games.get(gameId);
                    if (findGame != null)
                        findGame.requestMove(client, (int) row, (int) col);
                }
                else if (messageType.equals("ChatMessage"))
                {
                    ClientConnection client = (ClientConnection) map.get("Client");
                    String playerName = "" + client.getId(); //Eventually, change this to their actual name!
                    String channelName = (String) map.get("ChatChannel");
                    String playerChat = (String) map.get("PlayerChat");
                    sendToSubscribedClients(channelName, new ChatMessage(playerName, playerChat, channelName), client);
                    // Send message to their topic!
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

    public void sendToSubscribedClients(String topic, Message newMessage, ClientConnection ignoreClient)
    {
        ArrayList<ClientConnection> subs = subscriptions.get(topic);
        for (ClientConnection client : subs)
        {
            if (ignoreClient == null)
                client.writeMessage(newMessage);
            else if (client != ignoreClient)
                client.writeMessage(newMessage);
        }
    }

    private void subscribe(String topic, String topicType, ClientConnection client)
    {
        System.out.println("SUBSCRIBE: " + topic + " CLIENT: " + client.getId());
        ArrayList<ClientConnection> clients = subscriptions.get(topic);
        if (clients == null)
        {
            subscriptions.put(topic, new ArrayList<>());
            clients = subscriptions.get(topic);
        }
        clients.add(client);
        client.writeMessage(new SubscribeMessage(topic, topicType, true));
    }

    private void unsubscribe(String topic, String topicType, ClientConnection client)
    {
        System.out.println("UNSUBSCRIBE: " + topic + " CLIENT: " + client.getId());
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
                ClientConnection newConnection = new ClientConnection(connections.size() + 1,
                                                                        clientSocketConnection, this);
                connections.add(newConnection);
                InetAddress inetAddress = clientSocketConnection.getInetAddress();
                System.out.println("Accepted connection from " + inetAddress.getHostAddress());
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
