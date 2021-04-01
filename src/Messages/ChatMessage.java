package Messages;

import java.util.HashMap;
import java.util.Map;

public class ChatMessage extends Message {
    private String data;

    public ChatMessage(String data)
    {
        this.data = data;
    }

    @Override
    public Map<String, Object> toMap()
    {
        HashMap<String, Object> newMap = new HashMap<>();
        newMap.put("MessageType", this.getClass().getSimpleName());
        newMap.put("Data", data);
        return newMap;
    }
}
