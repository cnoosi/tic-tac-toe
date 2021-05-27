package Messages;

import java.util.HashMap;
import java.util.Map;

public class AccountMessage extends Message {
    private AccountAction accountAction;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String response;

    public AccountMessage(AccountAction accountAction, String username, String password,
                          String firstName, String lastName, String response)
    {
        this.accountAction = accountAction;
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.response = response;
    }

    public AccountMessage(AccountAction accountAction)
    {
        this.accountAction = accountAction;
    }

    public AccountMessage(AccountAction accountAction, String username, String password,
                          String firstName, String lastName)
    {
        this.accountAction = accountAction;
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Override
    public Map<String, Object> toMap()
    {
        HashMap<String, Object> newMap = new HashMap<>();
        newMap.put("MessageType", this.getClass().getSimpleName());
        newMap.put("Action", accountAction);
        newMap.put("Username", username);
        newMap.put("Password", password);
        newMap.put("FirstName", firstName);
        newMap.put("LastName", lastName);
        newMap.put("Response", response);
        return newMap;
    }
}
