package Networking;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;

public class ServerProcess implements Runnable
{
    private ServerSocket server;
    private boolean keepRunning = true;
    private ArrayList<ClientConnection> connections;
    private BlockingQueue<ChatMessage> chatMessagesToProcess;

    private void messagingProcess()
    {
        try
        {
            while (true) {
                //Introduce topic so you only send messages to the intended connections
                ChatMessage message = chatMessagesToProcess.take();
                if (message != null)
                {
                    String chat = message.getData();
                    for (ClientConnection connection : connections) {
                        connection.writeChatMessage(new Packet("GlobalChat", new ChatMessage(chat)));
                    }
                }
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private void startProcessThreads()
    {
        Thread messagingProcessThread = new Thread(this::messagingProcess);
        messagingProcessThread.start();
    }

    @Override
    public void run()
    {
        connections = new ArrayList<ClientConnection>();
        chatMessagesToProcess = new SynchronousQueue<ChatMessage>();
        startProcessThreads();

        try
        {
            server = new ServerSocket(8000);
            System.out.println("Server opened, waiting for new connections...");
            while (keepRunning)
            {
                Socket clientSocketConnection = server.accept();
                ClientConnection newConnection = new ClientConnection(connections.size() + 1,
                                                                        clientSocketConnection, chatMessagesToProcess);
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
