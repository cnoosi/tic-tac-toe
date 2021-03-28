package Networking;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class ClientConnection implements Runnable
{
    Socket client;
    int id;
    DataInputStream inputStream;
    DataOutputStream outputStream;
    Thread clientThread;


    public ClientConnection(int id, Socket c) throws Exception
    {
        this.id = id;
        this.client = c;
        inputStream = new DataInputStream(c.getInputStream());
        outputStream = new DataOutputStream(c.getOutputStream());

        this.clientThread = new Thread(this);
        clientThread.start();
    }

    public DataOutputStream getOutputStream() {
        return outputStream;
    }

    public void writeMessage(String message)
    {
        try
        {
            outputStream.writeUTF(message);
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
            String message = inputStream.readUTF();
            writeMessage(message);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
}
