package Observers;

import java.util.ArrayList;

public class ObserverMessage
{
    private String messageType;
    private ArrayList<String> message;

    public ObserverMessage(String messageType, ArrayList<String> message)
    {
        this.messageType = messageType;
        this.message = message;
    }

    public ObserverMessage(String messageType)
    {
        this.messageType = messageType;
        this.message = new ArrayList<>();
    }

    public String getMessageType()
    {
        return messageType;
    }

    public void setMessageType(String messageType)
    {
        this.messageType = messageType;
    }

    public ArrayList<String> getMessage()
    {
        return message;
    }

    public void setMessage(ArrayList<String> message)
    {
        this.message = message;
    }

    @Override
    public String toString()
    {
        String output;

        output = messageType + ":\n";
        for(String str : message)
            output += str + "\n";

        return output;
    }
}
