package Messages;

import java.util.HashMap;
import java.util.Map;

public class QueueMessage extends Message {
    private boolean inQueue;

    public QueueMessage(boolean inQueue)
    {
        this.inQueue = inQueue;
    }

    @Override
    public Map<String, Object> toMap() {
        HashMap<String, Object> newMap = new HashMap<>();
        newMap.put("MessageType", this.getClass().getSimpleName());
        newMap.put("InQueue", inQueue);
        return newMap;
    }
}
