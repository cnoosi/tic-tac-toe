package Messages;

import Networking.AccountAction;

import java.util.HashMap;
import java.util.Map;

public class AccountMessasge extends Message {
    private AccountAction accountAction;
    private String username;
    private String password;

    public AccountMessasge(AccountAction accountAction, String username, String password)
    {
        this.accountAction = accountAction;
        this.username = username;
        this.password = password;
    }

    @Override
    public Map<String, Object> toMap()
    {
        HashMap<String, Object> newMap = new HashMap<>();
        newMap.put("MessageType", this.getClass().getSimpleName());
        newMap.put("Action", accountAction);
        newMap.put("Username", username);
        newMap.put("Password", password);
        return newMap;
    }
}
