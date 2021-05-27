package Messages;

import java.util.HashMap;
import java.util.Map;

public class GameMessage extends Message {
    private int newRow;
    private int newCol;
    private int newValue;
    private int winner;
    private int currentToken;
    private int spectators;

    public GameMessage(int currentToken, int winner, int spectators, int newRow, int newCol, int newValue)
    {
        this.currentToken = currentToken;
        this.winner = winner;
        this.newRow = newRow;
        this.newCol = newCol;
        this.newValue = newValue;
        this.spectators = spectators;
    }

    @Override
    public Map<String, Object> toMap() {
        HashMap<String, Object> newMap = new HashMap<>();
        newMap.put("MessageType", this.getClass().getSimpleName());
        newMap.put("CurrentToken", currentToken);
        newMap.put("Winner", winner);
        newMap.put("NewRow", newRow);
        newMap.put("NewCol", newCol);
        newMap.put("NewValue", newValue);
        newMap.put("Spectators", spectators);
        return newMap;
    }
}
