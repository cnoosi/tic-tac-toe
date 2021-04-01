package Networking;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public abstract class Message implements Serializable {
    private Object data;

    public Map<String, String> toMap()
    {
        HashMap<String, String> newMap = new HashMap<>();
        newMap.put("MessageType", null);
        newMap.put("Data", null);
        return newMap;
    }
}
