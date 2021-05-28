package Messages;

import java.util.HashMap;
import java.util.Map;

public class GetGameDataMessage extends Message
{
    private String gameId;
    private String player1Name;
    private String player2Name;
    private int gameDuration;
    private int winnerToken;

    public GetGameDataMessage(String gameId)
    {
        this.gameId = gameId;
    }

    public GetGameDataMessage(String gameId, String player1Name, String player2Name, int gameDuration, int winnerToken)
    {
        this.gameId = gameId;
        this.player1Name = player1Name;
        this.player2Name = player2Name;
        this.gameDuration = gameDuration;
        this.winnerToken = winnerToken;
    }

    @Override
    public Map<String, Object> toMap()
    {
        HashMap<String, Object> newMap = new HashMap<>();
        newMap.put("MessageType", this.getClass().getSimpleName());
        newMap.put("GameId", gameId);
        newMap.put("Player1Name", player1Name);
        newMap.put("Player2Name", player2Name);
        newMap.put("GameDuration", gameDuration);
        newMap.put("WinnerToken", winnerToken);
        return newMap;
    }
}
