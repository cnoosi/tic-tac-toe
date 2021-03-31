package Networking;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;

public class ClientConnection implements Runnable
{
    Socket client;
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
            while (true)
            {

            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
}
