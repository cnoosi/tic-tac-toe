package Networking;

import Messages.ChatMessage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class ClientProcess implements Runnable
{
    Socket client;
    boolean clientAlive = true;
    DataInputStream inputStream;
    DataOutputStream outputStream;

    public void handleMessagingProcess()
    {
        try
        {
            String jsonString = inputStream.readUTF();
            HashMap<String, Object> map = JSON.decode(jsonString);
            if (map != null)
            {
                String messageType = (String) map.get("MessageType");
                if (messageType.equals("SubscribeMessage"))
                {
                    String topic = (String) map.get("Topic");
                    String topicType = (String) map.get("TopicType");
                    boolean isSubscribed = (boolean) map.get("Subscribe");
                    if (topicType.equals("Chat"))
                    {
                        //Change chat channel
                    }
                }
                else if (messageType.equals("ChatMessage"))
                {
                    String playerName = (String) map.get("PlayerName");
                    String playerMessage = (String) map.get("PlayerMessage");
                    //Put it in our local chat!
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
        Thread messagingProcessThread = new Thread(this::handleMessagingProcess);
        messagingProcessThread.start();

        try {
            client = new Socket("localhost", 8000);
            outputStream = new DataOutputStream(client.getOutputStream());
            inputStream = new DataInputStream(client.getInputStream());

            Scanner input = new Scanner(System.in);
            while (clientAlive) {
                //System.out.print("Enter a message: ");
                String newMessage = input.nextLine();
                ChatMessage chatMessage = new ChatMessage(newMessage, "CHAT_GLOBAL");
                outputStream.writeUTF(JSON.encode(chatMessage));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
