package Messages;

import Game.Game;

import java.util.HashMap;
import java.util.Map;

public class GameStatusMessage extends Message {
    private Game gameData;

    public GameStatusMessage(Game gameData)
    {
        this.gameData = gameData;
    }

    @Override
    public Map<String, Object> toMap() {
        HashMap<String, Object> newMap = new HashMap<>();
        newMap.put("MessageType", this.getClass().getSimpleName());
        newMap.put("Game", gameData);
        return newMap;
    }
}
