package Messages;

import java.util.HashMap;
import java.util.Map;

public class SubscribeMessage extends Message {
    private String topic;
    private String topicType;
    private boolean subscribe;

    public SubscribeMessage(String topic, String topicType, boolean subscribe)
    {
        this.topic = topic;
        this.topicType = topicType;
        this.subscribe = subscribe;
    }

    @Override
    public Map<String, Object> toMap()
    {
        HashMap<String, Object> newMap = new HashMap<>();
        newMap.put("MessageType", this.getClass().getSimpleName());
        newMap.put("Topic", topic);
        newMap.put("TopicType", topicType);
        newMap.put("Subscribe", subscribe);
        return newMap;
    }
}
