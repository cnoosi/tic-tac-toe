package Messages;

import java.util.HashMap;
import java.util.Map;

public class GameReplayMessage extends Message
{
    private int player1Id;
    private int player2Id;
    private int winnerToken;
    private int totalMoveCount;
    private long startTime;

    public GameReplayMessage(int player1Id, int player2Id, int winnerToken, int totalMoveCount, long startTime)
    {
        this.player1Id = player1Id;
        this.player2Id = player2Id;
        this.winnerToken = winnerToken;
        this.totalMoveCount = totalMoveCount;
        this.startTime = startTime;
    }

    @Override
    public Map<String, Object> toMap() {
        HashMap<String, Object> newMap = new HashMap<>();
        newMap.put("MessageType", this.getClass().getSimpleName());
        newMap.put("Player1Id", player1Id);
        newMap.put("Player2Id", player2Id);
        newMap.put("WinnerToken", winnerToken);
        newMap.put("TotalMoveCount", totalMoveCount);
        newMap.put("StartTime", startTime);
        return newMap;
    }
}
