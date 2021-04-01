package Messages;

import java.util.HashMap;
import java.util.Map;

public class SubscribeMessage extends Message {
    private String topic;
    private boolean subscribe;

    public SubscribeMessage(String topic, boolean subscribe)
    {
        this.topic = topic;
        this.subscribe = subscribe;
    }

    @Override
    public Map<String, Object> toMap()
    {
        HashMap<String, Object> newMap = new HashMap<>();
        newMap.put("MessageType", this.getClass().getSimpleName());
        newMap.put("Topic", topic);
        newMap.put("Subscribe", subscribe);
        return newMap;
    }
}
