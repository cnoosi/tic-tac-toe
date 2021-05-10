package Networking;

import Messages.ChatMessage;
import Messages.Message;
import Messages.MoveMessage;
import Messages.QueueMessage;
import UserInterface.UIProcess;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Map;
import java.util.Scanner;

public class ClientProcess implements Runnable
{
    Socket client;
    boolean clientAlive = true;
    DataInputStream inputStream;
    DataOutputStream outputStream;

    private UIProcess ui;
    private String gameId;
    private String chatChannel;

    private void handleSubscribeMessage(Map<String, Object> map)
    {
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

    private void handleQueueMessage(Map<String, Object> map)
    {
        boolean inQueue = (boolean) map.get("InQueue");
        String gameId = (String) map.get("GameId");
        if (!inQueue && gameId != null)
        {
            ui.startGame();
            this.gameId = gameId;
        }
        else if (inQueue)
            ui.joinQueue();
        else
            ui.leaveQueue();
    }

    private void handleGameMessage(Map<String, Object> map)
    {
        long currentToken = (long) map.get("CurrentToken");
        long winner = (long) map.get("Winner");
        long newRow = (long) map.get("NewRow");
        long newCol = (long) map.get("NewCol");
        long newValue = (long) map.get("NewValue");
        if (newRow != -1 && newCol != -1 && newValue != -1)
        {
            System.out.println(newRow + " , " + newCol + " = " + newValue);
            ui.changeUIBoardToken((int) newRow, (int) newCol, (int) newValue);
        }
        System.out.println("Current token: " + currentToken + " || Winner: " + winner);
        ui.updateBoardUI((int) currentToken, (int) winner);
    }

    private void handleChatMessage(Map<String, Object> map)
    {
        String playerName = (String) map.get("PlayerName");
        String playerChat = (String) map.get("PlayerChat");
        ui.newChat(playerName, playerChat);
    }

    public void handleMessagingProcess()
    {
        try
        {
            while (clientAlive)
            {
                String jsonString = inputStream.readUTF();
                Map<String, Object> map = JSON.decode(jsonString);
                if (map != null) {
                    String messageType = (String) map.get("MessageType");

                    switch (messageType)
                    {
                        case "SubscribeMessage":
                            handleSubscribeMessage(map);
                            break;
                        case "QueueMessage":
                            handleQueueMessage(map);
                            break;
                        case "GameMessage":
                            handleGameMessage(map);
                            break;
                        case "ChatMessage":
                            handleChatMessage(map);
                            break;
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

    public ClientProcess(Stage primaryStage)
    {
        this.ui = new UIProcess(this, primaryStage);
        ui.openPage("Board");
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

            //Test stuff *******
            //Put the user into queue
            writeMessage(new QueueMessage(true));

            Scanner input = new Scanner(System.in);
            while (clientAlive)
            {
//                String newChat = input.nextLine();
//                ChatMessage chatMessage = new ChatMessage(newChat, this.chatChannel);
//                writeMessage(chatMessage);

                int row = input.nextInt();
                int col = input.nextInt();
                MoveMessage moveRequest = new MoveMessage(gameId, row, col);
                writeMessage(moveRequest);
            }
            //Test stuff *******

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
