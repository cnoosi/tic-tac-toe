package Networking;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class ClientProcess implements Runnable
{
    Socket client;
    ObjectInputStream inputStream;
    ObjectOutputStream outputStream;

    private void updateGlobalChatMessages()
    {
        try
        {
            while (true) {
                Packet newPacket = (Packet) inputStream.readObject();
                if (newPacket.getTopic().equals("GlobalChat"))
                {
                    ChatMessage message = (ChatMessage) newPacket.getMessage();
                    System.out.println("Message from other client: " + message.getData());
                }
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    @Override
    public void run()
    {
        try {
            client = new Socket("localhost", 8000);
            outputStream = new ObjectOutputStream(client.getOutputStream());
            inputStream = new ObjectInputStream(client.getInputStream());

            Thread updateChatThread = new Thread(this::updateGlobalChatMessages);
            updateChatThread.start();

            boolean continueMessaging = true;
            Scanner input = new Scanner(System.in);
            while (continueMessaging) {
                //System.out.print("Enter a message: ");
                String newMessage = input.nextLine();
                outputStream.writeObject(new Packet("GlobalChat", new ChatMessage(newMessage)));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
