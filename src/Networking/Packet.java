package Networking;

import java.io.Serializable;

public class Packet implements Serializable {
    String topic;
    Message message;

    public Packet(String topic, Message message)
    {
        this.topic = topic;
        this.message = message;
    }

    public String getTopic()
    {
        return topic;
    }

    public Message getMessage()
    {
        return message;
    }
}
