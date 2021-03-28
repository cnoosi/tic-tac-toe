package Networking;

import java.net.ServerSocket;
import java.util.ArrayList;

public class ServerProcess implements Runnable {
    private ServerSocket server;
    private boolean keepRunning = true;
    private ArrayList<ClientConnection> connections;


    @Override
    public void run()
    {
        
    }
}
