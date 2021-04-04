package Messages;

import java.util.HashMap;
import java.util.Map;

public class QueueMessage extends Message {
    private boolean inQueue;
    private String gameId;

    public QueueMessage(boolean inQueue)
    {
        this.inQueue = inQueue;
        this.gameId = "";
    }

    public QueueMessage(boolean inQueue, String gameId)
    {
        this.inQueue = inQueue;
        this.gameId = gameId;
    }

    @Override
    public Map<String, Object> toMap() {
        HashMap<String, Object> newMap = new HashMap<>();
        newMap.put("MessageType", this.getClass().getSimpleName());
        newMap.put("InQueue", inQueue);
        newMap.put("GameId", gameId);
        return newMap;
    }
}
