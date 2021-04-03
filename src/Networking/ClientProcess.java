package Networking;

import Messages.ChatMessage;
import Messages.Message;
import Messages.MoveMessage;
import Messages.QueueMessage;

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

    private String gameId;
    private String chatChannel;

    public void handleMessagingProcess()
    {
        try
        {
            while (clientAlive)
            {
                String jsonString = inputStream.readUTF();
                HashMap<String, Object> map = JSON.decode(jsonString);
                if (map != null) {
                    String messageType = (String) map.get("MessageType");
                    if (messageType.equals("SubscribeMessage")) {
                        String topic = (String) map.get("Topic");
                        String topicType = (String) map.get("TopicType");
                        boolean isSubscribed = (boolean) map.get("Subscribe");
                        if (topicType.equals("Chat")) {
                            if (isSubscribed)
                            {
                                chatChannel = topic;
                            }
                        }
                    }
                    else if (messageType.equals("QueueMessage")) {
                        boolean inQueue = (boolean) map.get("InQueue");
                        String gameId = (String) map.get("GameId");
                        if (!inQueue && gameId != null)
                        {
                            this.gameId = gameId;
                        }
                    }

                    else if (messageType.equals("GameMessage")) {
                        long currentToken = (long) map.get("CurrentToken");
                        long winner = (long) map.get("Winner");
                        long newRow = (long) map.get("NewRow");
                        long newCol = (long) map.get("NewCol");
                        long newValue = (long) map.get("NewValue");
                        System.out.println(newRow + "," + newCol + " = " + newValue);
                        System.out.println("CURRENT PLAYER: " + currentToken + " WINNER: " + winner);
                    }
                    else if (messageType.equals("ChatMessage")) {
                        String playerName = (String) map.get("PlayerName");
                        String playerChat = (String) map.get("PlayerChat");
                        System.out.println(playerName + ": " + playerChat);
                    }
                }
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public void writeMessage(Message newMessage)
    {
        try
        {
            outputStream.writeUTF(JSON.encode(newMessage));
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

            Thread messagingProcessThread = new Thread(this::handleMessagingProcess);
            messagingProcessThread.start();

            //Test stuff
            //Put the user into queue
            writeMessage(new QueueMessage(true));

            Scanner input = new Scanner(System.in);
            while (clientAlive)
            {
                int row = input.nextInt();
                int col = input.nextInt();
                MoveMessage moveRequest = new MoveMessage(gameId, row, col);
                writeMessage(moveRequest);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
