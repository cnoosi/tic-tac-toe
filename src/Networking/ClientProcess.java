package Networking;

import Game.Game;
import Game.Position;
import Linkers.ClientObserver;
import Linkers.ClientSubject;
import Linkers.GameObserver;
import Observers.Observer;
import Observers.ObserverMessage;
import Messages.*;
import UserInterface.UIProcess;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

public class ClientProcess implements Runnable, ClientObserver, Observer
{
    Socket client;
    boolean clientAlive = true;
    DataInputStream inputStream;
    DataOutputStream outputStream;

    private UIProcess ui;
    String username;
    private int userId;
    private String gameId;
    private String chatChannel;

    // Information for active replays
    private GameReplayProcess activeReplay;

    // Information for fetching live & historic games
    private int messagePreloadCount;
    private int messagePreloadCountCompleted;
    private ArrayList<String> liveGameList;
    private ArrayList<String> historyGameList;

    public ClientProcess(Stage primaryStage)
    {
        this.ui = new UIProcess(this, primaryStage);
        ui.openPage("Menu");
    }

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

    private void handleChatMessage(Map<String, Object> map)
    {
        String playerName = (String) map.get("PlayerName");
        String playerChat = (String) map.get("PlayerChat");
        ui.newChat(playerName, playerChat);
    }


    private void handleGameMessage(Map<String, Object> map)
    {
        long currentToken = (long) map.get("CurrentToken");
        long winner = (long) map.get("Winner");
        long newRow = (long) map.get("NewRow");
        long newCol = (long) map.get("NewCol");
        long newValue = (long) map.get("NewValue");
        long spectators = (long) map.get("Spectators");
        if (newRow != -1 && newCol != -1 && newValue != -1)
        {
            ui.changeUIBoardToken((int) newRow, (int) newCol, (int) newValue);
        }
        ui.updateBoardUI((int) currentToken, (int) winner, (int) spectators);
    }

    private void handleGameReplayMessage(Map<String, Object> map)
    {
        long player1Id = (long) map.get("Player1Id");
        long player2Id = (long) map.get("Player2Id");
        long winnerToken = (long) map.get("WinnerToken");
        long totalMoveCount = (long) map.get("TotalMoveCount");
        long startTime = (long) map.get("StartTime");
        activeReplay = new GameReplayProcess(this, (int) player1Id, (int) player2Id,
                                             (int) totalMoveCount, (int) winnerToken, startTime);

        Thread activeReplayThread = new Thread(activeReplay);
        activeReplayThread.start();
    }

    private void handleGameHistoryMessage(Map<String, Object> map)
    {
        String timestamp = (String) map.get("Timestamp");
        long row = (long) map.get("Row");
        long col = (long) map.get("Col");

        activeReplay.addMove(new Pair(timestamp, new Position((int) row, (int) col)));
    }

    private void handleFetchGameListMessage(Map<String, Object> map)
    {
        long gameListNum = (long) map.get("GameListType");
        GameListType gameListType = GameListType.values()[(int) gameListNum];

        if (gameListType == GameListType.MessageCountPreload)
        {
            long messageCount = (long) map.get("MessageCount");
            messagePreloadCount = (int) messageCount;
            messagePreloadCountCompleted = 0;
            liveGameList = new ArrayList<>();
            historyGameList = new ArrayList<>();
        }
        else if (gameListType == GameListType.Live)
        {
            String gameId = (String) map.get("GameId");
            liveGameList.add(gameId);
            messagePreloadCountCompleted++;
        }
        else if (gameListType == GameListType.History)
        {
            String gameId = (String) map.get("GameId");
            historyGameList.add(gameId);
            messagePreloadCountCompleted++;
        }

        if (messagePreloadCount == messagePreloadCountCompleted)
        {
            System.out.println("LIVE COUNT: " + liveGameList.size() + " || HISTORY COUNT: " + historyGameList.size());
            ui.gameHistoryRecieved(liveGameList, historyGameList);
        }
    }

    private void handleAccountMessage(Map<String, Object> map)
    {
        long accountActionNum = (long) map.get("Action");
        AccountAction accountAction = AccountAction.values()[(int) accountActionNum];
        String response = (String) map.get("Response");

//        if (accountAction == AccountAction.Login)
//        {
//            if (response.equals("success"))
//                //ui.loginSuccess(true);
//            else
//                ui.loginSuccess(false);
//        }
//        else if (accountAction == AccountAction.Logout)
//            ui.logoutSuccess(true);
//        else if (accountAction == AccountAction.Register)
//        {
//            if (response.equals("success"))
//                ui.registerSuccess(true);
//            else
//                ui.registerSuccess(false);
//        }
//        else if (accountAction == AccountAction.ChangeSettings)
//        {
//            if (response.equals("success"))
//                ui.settingChangedSuccess(true);
//            else
//                ui.settingChangedSuccess(false);
//        }
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
                        case "ChatMessage":
                            handleChatMessage(map);
                            break;
                        case "GameMessage":
                            handleGameMessage(map);
                            break;
                        case "GameReplayMessage":
                            handleGameReplayMessage(map);
                            break;
                        case "GameHistoryMessage":
                            handleGameHistoryMessage(map);
                            break;
                        case "FetchGameListMessage":
                            handleFetchGameListMessage(map);
                            break;
                        case "AccountMessage":
                            handleAccountMessage(map);
                            break;
                        default:
                            System.out.println("Failed to process message: " + messageType);
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
    public void clientUpdate(Position pos)
    {
        if(clientAlive)
        {
            MoveMessage moveRequest = new MoveMessage(gameId, pos.getRow(), pos.getCol());
            writeMessage(moveRequest);
        }
    }

    @Override
    public void update(ObserverMessage message)
    {
        String messageType = message.getMessageType();

        System.out.println("UI ATTEMPT MESSAGE: " + message);
        /***************/
        if(messageType.equals("Login"))
        {
            String username = message.getMessage().get(0); // holds username
            String password = message.getMessage().get(1); // holds password
            writeMessage(new AccountMessage(AccountAction.Login, username, password));
        }

        else if(messageType.equals("CreateAccount"))
        {
            String username  = message.getMessage().get(0); // holds username
            String firstName = message.getMessage().get(1); // holds first name
            String lastName  = message.getMessage().get(2); // holds second name
            String password  = message.getMessage().get(3); // holds password
            // writeMessage(createAccount)   // has username, first name, last name, password
        }

        else if(messageType.equals("MultiPlayer"))
        {
            writeMessage(new QueueMessage(true));
            // Client has requested multi player game
            // add the client to the game queue
            // join queue
        }

        else if(messageType.equals("Move"))
        {
            int row = Integer.parseInt(message.getMessage().get(0)); // contains row index
            int col = Integer.parseInt(message.getMessage().get(1)); // contains col index
            System.out.println(row);
            System.out.println(col);
            System.out.println(clientAlive);
            if(clientAlive)
            {
                MoveMessage moveRequest = new MoveMessage(gameId, row, col);
                writeMessage(moveRequest);
            }
        }

        else if(messageType.equals("Spectate") || messageType.equals("ReplayGame"))
        {
            String gameId = message.getMessage().get(0); // holds the gameId they want to spectate
            writeMessage(new SpectateMessage(true, gameId));
        }

        else if (messageType.equals("GameHistoryUpdate"))
        {
            writeMessage(new FetchGameListMessage());
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

            //Test stuff ******
            Scanner input = new Scanner(System.in);
            while (clientAlive)
            {
                String newGameId = input.nextLine();
                writeMessage(new SpectateMessage(true, newGameId));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setUsername(String username) {this.username = username;}
    public void setId(int userId) {this.userId = userId;}

    public String getUsername() {return this.username;}
    public int getUserId() {return this.userId;}
    public UIProcess getUi() {return this.ui;}
}
