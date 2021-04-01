package Networking;

import java.util.HashMap;
import java.util.Map;

public class ChatMessage extends Message {
    private String data;

    public ChatMessage(String data)
    {
        this.data = data;
    }

    public String getData()
    {
        return data;
    }

    @Override
    public Map<String, String> toMap()
    {
        HashMap<String, String> newMap = new HashMap<>();
        newMap.put("MessageType", this.getClass().getSimpleName());
        newMap.put("Data", data);
        return newMap;
    }
}
