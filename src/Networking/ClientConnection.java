package Networking;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;

public class ClientConnection implements Runnable
{
    Socket client;
    boolean continueConnection = true;
    int id;
    DataInputStream inputStream;
    DataOutputStream outputStream;
    Thread clientThread;
    BlockingQueue messageQueue;

    public ClientConnection(int id, Socket c, BlockingQueue messageQueue) throws Exception
    {
        this.id = id;
        this.client = c;
        this.outputStream = new DataOutputStream(c.getOutputStream());
        this.inputStream = new DataInputStream(c.getInputStream());
        this.messageQueue = messageQueue;

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

        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    @Override
    public void run()
    {
        try
        {
            while (continueConnection)
            {
                String jsonString = inputStream.readUTF();
                System.out.println(jsonString);
                JSONObject json = (JSONObject) JSONValue.parse(jsonString);
                if (json.containsKey("Message"))
                {
                    Object newMessage = json.get("Message");
                    if (newMessage instanceof Message)
                    {
                        System.out.println("Whoa...");
                        // They sent a message! Let's queue it up!!!!
                        messageQueue.add(newMessage);
                    }
                }
                else
                    System.out.println("Awwww...");
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
}
