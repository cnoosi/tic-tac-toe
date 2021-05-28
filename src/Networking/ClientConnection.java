package Networking;

import Messages.ChatMessage;
import Messages.Message;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.HashMap;

public class ClientConnection implements Runnable
{
    Socket client;
    boolean continueConnection = true;
    String gameId;
    int id;
    DataInputStream inputStream;
    DataOutputStream outputStream;
    Thread clientThread;
    ServerProcess server;

    public ClientConnection(Socket c, ServerProcess server) throws Exception
    {
        this.id = -1;
        this.client = c;
        this.outputStream = new DataOutputStream(c.getOutputStream());
        this.inputStream = new DataInputStream(c.getInputStream());
        this.server = server;

        this.clientThread = new Thread(this);
        clientThread.start();
    }

    public DataOutputStream getOutputStream() {
        return outputStream;
    }

    public DataInputStream getInputStream()
    {
        return inputStream;
    }

    public void writeMessage(Message message)
    {
        try
        {
            outputStream.writeUTF(JSON.encode(message));
        }
        catch (Exception ex) {}
    }



    @Override
    public void run()
    {
        try
        {
            while (continueConnection)
            {
                String jsonString = inputStream.readUTF();
                HashMap<String, Object> map = JSON.decode(jsonString);
                if (map != null)
                {
                    map.put("Client", this);
                    server.processMessage(map);
                }
            }
        }
        catch (Exception ex)
        {
            killClient();
            server.disconnectClient(this);
        }
    }

    public void killClient() {this.continueConnection = false;}
    public void setGameId(String gameId) {this.gameId = gameId;}
    public void setId(int id) {this.id = id;}

    public String getGameId() {return this.gameId;}
    public int getId() {return id;}
    public boolean isConnected() {return this.continueConnection;}
}
