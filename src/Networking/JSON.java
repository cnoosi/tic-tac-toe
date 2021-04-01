package Networking;

import Messages.Message;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.util.HashMap;

public interface JSON {
    public static HashMap<String, Object> decode(String jsonString)
    {
        JSONObject json = (JSONObject) JSONValue.parse(jsonString);
        HashMap<String, Object> newMap = new HashMap<>();
        for (Object keyStr: json.keySet())
        {
            String key = (String) keyStr;
            newMap.put(key, json.get(key));
        }
        return newMap;
    }

    public static String encode(Message message)
    {
        return JSONObject.toJSONString(message.toMap());
    }
}
