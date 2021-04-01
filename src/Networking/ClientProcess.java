package Networking;

import Messages.ChatMessage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class ClientProcess implements Runnable
{
    Socket client;
    DataInputStream inputStream;
    DataOutputStream outputStream;

    private void updateGlobalChatMessages()
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
        try {
            client = new Socket("localhost", 8000);
            outputStream = new DataOutputStream(client.getOutputStream());
            inputStream = new DataInputStream(client.getInputStream());

            boolean continueMessaging = true;
            Scanner input = new Scanner(System.in);
            while (continueMessaging) {
                //System.out.print("Enter a message: ");
                String newMessage = input.nextLine();
                ChatMessage chatMessage = new ChatMessage(newMessage);
                outputStream.writeUTF(JSON.encode(chatMessage));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
