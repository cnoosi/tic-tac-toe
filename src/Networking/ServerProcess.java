package Networking;

import Game.*;
import Messages.*;
import UserInterface.ServerUIProcess;
import UserInterface.UIProcess;
import javafx.stage.Stage;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Array;
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
    private BlockingQueue<Map<String, Object>> messagesToProcess;
    private DbManager database;
    private ServerUIProcess serverUIProcess;

    // Microservices
    private AccountService accountsService;
    private GamesService gamesService;

    public ServerProcess(Stage primaryStage)
    {
        this.serverUIProcess = new ServerUIProcess(this, primaryStage);
        serverUIProcess.openPage("Menu");
    }

    // Temp
    public ServerProcess() {}

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

    private void handleChatMessage(Map<String, Object> map)
    {
        ClientConnection client = (ClientConnection) map.get("Client");
        String playerName = "" + client.getId(); //Eventually, change this to their actual name!
        String playerChat = (String) map.get("PlayerChat");
        String channelName = (String) map.get("ChatChannel");
        sendToSubscribedClients(channelName, new ChatMessage(playerName, playerChat, channelName), client);
    }

    private void handleFetchGameListMessage(Map<String, Object> map)
    {
        ClientConnection client = (ClientConnection) map.get("Client");
        if (client.getId() != 0) //SHOULD BE -1!
        {
            // Get live games
            Map<String, GameProcess> liveGamesMap = gamesService.getLiveGames();
            ArrayList<String> liveGames = new ArrayList<>();
            for (Map.Entry<String, GameProcess> entry : liveGamesMap.entrySet()) {
                liveGames.add(entry.getKey());
            }
            // Get history games for user
            ArrayList<GameHistory> gameHistoryList = database.getGameHistoryForUser(client.getId());
            ArrayList<String> historyGames = new ArrayList<>();
            for (int i = 0; i < gameHistoryList.size(); i++) {
                historyGames.add(gameHistoryList.get(i).getGameId());
            }
            client.writeMessage(new FetchGameListMessage(GameListType.MessageCountPreload, liveGames.size() + historyGames.size()));
            // Send each individual string
            for (int i = 0; i < liveGames.size(); i++)
                client.writeMessage(new FetchGameListMessage(GameListType.Live, liveGames.get(i)));
            for (int i = 0; i < historyGames.size(); i++)
                client.writeMessage(new FetchGameListMessage(GameListType.History, historyGames.get(i)));
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
                    case "ChatMessage":
                        handleChatMessage(map);
                        break;
                    case "QueueMessage":
                        gamesService.handleQueueMessage(map);
                        break;
                    case "MoveMessage":
                        gamesService.handleMoveMessage(map);
                        break;
                    case "SpectateMessage":
                        gamesService.handleSpectateMessage(map);
                        break;
                    case "AccountMessage":
                        accountsService.handleAccountMessage(map);
                        break;
                    case "FetchGameListMessage":
                        handleFetchGameListMessage(map);
                        break;
                    case "GetGameDataMessage":
                        gamesService.handleGetGameDataMessage(map);
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
        System.out.println(client.getId() + " subscribed to: " + topic);
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
        System.out.println(client.getId() + " unsubscribed from: " + topic);
        ArrayList<ClientConnection> clients = subscriptions.get(topic);
        if (clients != null)
            return;
        client.writeMessage(new SubscribeMessage(topic, topicType, false));
    }

    public void disconnectClient(ClientConnection client)
    {
        gamesService.endClientGamesOnLeave(client);

        for (Map.Entry<String, ArrayList<ClientConnection>> entry : subscriptions.entrySet())
        {
            ArrayList<ClientConnection> clients = entry.getValue();
            for (int i = 0; i < clients.size(); i++)
            {
                if (clients.get(i) == client)
                    unsubscribe(entry.getKey(), null, client);
            }
        }
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
        database = new DbManager();

        //Microservices
        accountsService = new AccountService(this, database);
        gamesService = new GamesService(this, database);

        Thread messagingProcessThread = new Thread(this::handleClientMessagesProcess);
        messagingProcessThread.start();

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

    public DbManager getDatabase() {return this.database;}
    public ArrayList<ClientConnection> getConnections() {return this.connections;}
    public GamesService getGamesService() {return this.gamesService;}
}
