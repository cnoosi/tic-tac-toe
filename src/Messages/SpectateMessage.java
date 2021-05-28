package Messages;

import java.util.HashMap;
import java.util.Map;

public class SpectateMessage extends Message {
    private boolean spectate;
    private String gameId;

    public SpectateMessage(boolean spectate, String gameId)
    {
        this.spectate = spectate;
        this.gameId = gameId;
    }

    @Override
    public Map<String, Object> toMap() {
        HashMap<String, Object> newMap = new HashMap<>();
        newMap.put("MessageType", this.getClass().getSimpleName());
        newMap.put("Spectate", spectate);
        newMap.put("GameId", gameId);
        return newMap;
    }
}
