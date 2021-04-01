package Networking;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public interface JSON {
    public static HashMap<String, String> decode(String jsonString)
    {
        JSONObject json = (JSONObject) JSONValue.parse(jsonString);
        HashMap<String, String> newMap = new HashMap<>();
        newMap.put("MessageType", (String) json.get("MessageType"));
        newMap.put("Data", (String) json.get("Data"));
        return newMap;
    }

    public static String encode(Message message)
    {
        return JSONObject.toJSONString(message.toMap());
    }
}
