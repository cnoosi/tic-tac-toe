package Messages;

import java.util.HashMap;
import java.util.Map;

public class MoveMessage extends Message {
    private String gameId;
    private int row;
    private int col;

    public MoveMessage(String gameId, int row, int col)
    {
        this.gameId = gameId;
        this.row = row;
        this.col = col;
    }

    @Override
    public Map<String, Object> toMap() {
        HashMap<String, Object> newMap = new HashMap<>();
        newMap.put("MessageType", this.getClass().getSimpleName());
        newMap.put("GameId", gameId);
        newMap.put("Row", row);
        newMap.put("Col", col);
        return newMap;
    }
}
