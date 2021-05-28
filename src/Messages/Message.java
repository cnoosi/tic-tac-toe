package Messages;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public abstract class Message implements Serializable {
    public Map<String, Object> toMap()
    {
        HashMap<String, Object> newMap = new HashMap<>();
        newMap.put("MessageType", null);
        newMap.put("Data", null);
        return newMap;
    }
}
