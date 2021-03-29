package Networking;

public class ChatMessage extends Message {
    private String data;

    public ChatMessage(String data)
    {
        this.data = data;
    }

    public String getData()
    {
        return data;
    }
}
