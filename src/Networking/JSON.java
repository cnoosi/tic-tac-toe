package Networking;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import java.util.ArrayList;

public interface JSON {
    public static Object decode(String key, String jsonString)
    {
        System.out.println(jsonString);
        JSONObject json = (JSONObject) JSONValue.parse(jsonString);
        if (json.containsKey(key))
        {
            return json.get(key);
        }
        return null;
    }

    public static String encode(String key, Object obj)
    {
        JSONObject json = new JSONObject();
        json.put(key, obj);
        return JSONObject.toJSONString(json);
    }

    public static String encode(String key, ArrayList objects)
    {
        JSONObject json = new JSONObject();
        for (Object obj : objects)
        {
            json.put(key, obj);
        }
        return JSONObject.toJSONString(json);
    }
}
