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
    int id;
    DataInputStream inputStream;
    DataOutputStream outputStream;
    Thread clientThread;
    ServerProcess server;

    public ClientConnection(int id, Socket c, ServerProcess server) throws Exception
    {
        this.id = id;
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
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public int getId() {return id;}

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
            ex.printStackTrace();
        }
    }
}
