package Networking;

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
    private boolean keepRunning = true;
    private ArrayList<ClientConnection> connections;
    private Map<String, ArrayList<ClientConnection>> subscriptions;
    private BlockingQueue<Map<String, Object>> messagesToProcess;

    private void messagingProcess()
    {
        try
        {
            while (true) {
                Map<String, Object> map = messagesToProcess.take();
                String messageType = (String) map.get("MessageType");
                if (messageType.equals("SubscribeMessage"))
                {
                    String topic = (String) map.get("Topic");
                    ClientConnection client = (ClientConnection) map.get("Client");
                    boolean subscribe = (boolean) map.get("Subscribe");
                    if (subscribe)
                        subscribe(topic, client);
                    else
                        unsubscribe(topic, client);
                }
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private ArrayList<ClientConnection> getSubscribed(String topic)
    {
        return subscriptions.get(topic);
    }

    private void subscribe(String topic, ClientConnection client)
    {
        System.out.println("SUBSCRIBE: " + topic + " CLIENT: " + client.getId());
        ArrayList<ClientConnection> clients = subscriptions.get(topic);
        if (clients == null)
            clients = new ArrayList<>();
        clients.add(client);
    }

    private void unsubscribe(String topic, ClientConnection client)
    {
        System.out.println("UNSUBSCRIBE: " + topic + " CLIENT: " + client.getId());
        ArrayList<ClientConnection> clients = subscriptions.get(topic);
        if (clients != null)
            clients.remove(client);
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

        Thread messagingProcessThread = new Thread(this::messagingProcess);
        messagingProcessThread.start();

        try
        {
            server = new ServerSocket(8000);
            System.out.println("Server opened, waiting for new connections...");
            while (keepRunning)
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
