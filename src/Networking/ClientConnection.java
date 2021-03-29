package Networking;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;

public class ClientConnection implements Runnable
{
    Socket client;
    int id;
    ObjectInputStream inputStream;
    ObjectOutputStream outputStream;
    Thread clientThread;
    BlockingQueue chatQueue;

    public ClientConnection(int id, Socket c, BlockingQueue chatQueue) throws Exception
    {
        this.id = id;
        this.client = c;
        this.outputStream = new ObjectOutputStream(c.getOutputStream());
        this.inputStream = new ObjectInputStream(c.getInputStream());
        this.chatQueue = chatQueue;

        this.clientThread = new Thread(this);
        clientThread.start();
    }

    public ObjectOutputStream getOutputStream() {
        return outputStream;
    }

    public ObjectInputStream getInputStream()
    {
        return inputStream;
    }

    public void writeChatMessage(Packet chatPacket)
    {
        try
        {
            outputStream.writeObject(chatPacket);
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
            while (true)
            {
                Packet newPacket = (Packet) inputStream.readObject();
                ChatMessage message = (ChatMessage) newPacket.getMessage();
                chatQueue.add(message);
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
}
