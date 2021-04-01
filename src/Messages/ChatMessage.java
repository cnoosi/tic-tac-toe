package Messages;

import java.util.HashMap;
import java.util.Map;

public class ChatMessage extends Message {
    private String playerName;
    private String chatChannel;
    private String playerChat;

    public ChatMessage(String playerChat, String chatChannel)
    {
        this.playerName = "";
        this.chatChannel = chatChannel;
        this.playerChat = playerChat;
    }

    public ChatMessage(String playerName, String playerChat, String chatChannel)
    {
        this.playerName = playerName;
        this.chatChannel = chatChannel;
        this.playerChat = playerChat;
    }

    @Override
    public Map<String, Object> toMap()
    {
        HashMap<String, Object> newMap = new HashMap<>();
        newMap.put("MessageType", this.getClass().getSimpleName());
        newMap.put("PlayerName", playerName);
        newMap.put("PlayerChat", playerChat);
        return newMap;
    }
}
