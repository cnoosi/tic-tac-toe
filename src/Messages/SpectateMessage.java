package Messages;

import java.util.HashMap;
import java.util.Map;

public class SpectateMessage extends Message {
    private String gameId;

    public SpectateMessage(String gameId)
    {
        this.gameId = gameId;
    }

    @Override
    public Map<String, Object> toMap() {
        HashMap<String, Object> newMap = new HashMap<>();
        newMap.put("MessageType", this.getClass().getSimpleName());
        newMap.put("GameId", gameId);
        return newMap;
    }
}
