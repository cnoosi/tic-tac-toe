package Messages;

import java.util.HashMap;
import java.util.Map;

public class GameHistoryMessage extends Message {
    private int moveIndex;
    private String timestamp;
    private int row;
    private int col;

    public GameHistoryMessage(int moveIndex, String timestamp, int row, int col)
    {
        this.moveIndex = moveIndex;
        this.timestamp = timestamp;
        this.row = row;
        this.col = col;
    }

    @Override
    public Map<String, Object> toMap() {
        HashMap<String, Object> newMap = new HashMap<>();
        newMap.put("MessageType", this.getClass().getSimpleName());
        newMap.put("MoveIndex", moveIndex);
        newMap.put("Timestamp", timestamp);
        newMap.put("Row", row);
        newMap.put("Col", col);
        return newMap;
    }
}
