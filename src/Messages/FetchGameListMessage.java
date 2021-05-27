package Messages;

import java.util.HashMap;
import java.util.Map;

public class FetchGameListMessage extends Message {
    private GameListType gameListType;
    private String gameId;
    private int messageCount;

    public FetchGameListMessage()
    {
        this.gameListType = GameListType.MessageCountPreload;
    }

    public FetchGameListMessage(GameListType gameListType, String gameId)
    {
        this.gameListType = gameListType;
        this.gameId = gameId;
    }

    public FetchGameListMessage(GameListType gameListType, int messageCount)
    {
        this.gameListType = gameListType;
        this.messageCount = messageCount;
    }

    @Override
    public Map<String, Object> toMap() {
        HashMap<String, Object> newMap = new HashMap<>();
        newMap.put("MessageType", this.getClass().getSimpleName());
        newMap.put("GameListType", gameListType.ordinal());
        newMap.put("GameId", gameId);
        newMap.put("MessageCount", messageCount);
        return newMap;
    }
}
