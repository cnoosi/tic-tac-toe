package Networking;

import Messages.AccountAction;
import Messages.AccountMessage;

import java.util.Map;

public class AccountService {

    ServerProcess server;
    DbManager database;

    public AccountService(ServerProcess server, DbManager database)
    {
        this.server = server;
        this.database = database;
    }

    public void handleAccountMessage(Map<String, Object> map)
    {
        ClientConnection client = (ClientConnection) map.get("Client");
        long actionNum = (long) map.get("Action");
        AccountAction action = AccountAction.values()[(int) actionNum];
        String username = (String) map.get("Username");
        String password = (String) map.get("Password");
        String firstName = (String) map.get("FirstName");
        String lastName = (String) map.get("LastName");
        if (action == AccountAction.Login)
        {
            boolean loginSuccess = database.userFound(username, password);
            if (loginSuccess)
            {
                client.setId(database.getUserId(username));
                client.writeMessage(new AccountMessage(AccountAction.Login, null, null,
                        null, null, "success"));
            }
            else
                client.writeMessage(new AccountMessage(AccountAction.Login, null, null,
                        null, null, "fail"));
        }
        else if (action == AccountAction.Register)
        {
            if (database.userAvailable(username))
            {
                database.addUser(username, firstName, lastName, password);
                client.writeMessage(new AccountMessage(AccountAction.Register, null, null,
                        null, null, "success"));
            }
            else
                client.writeMessage(new AccountMessage(AccountAction.Login, null, null,
                        null, null, "fail"));
        }
        else if (action == AccountAction.Logout)
        {
            client.setId(0);
            client.writeMessage(new AccountMessage(AccountAction.Logout, null, null,
                    null, null, "success"));
        }
        else if (action == AccountAction.ChangeSettings)
        {
            if (client.getId() != 1)
            {
                // Change password and username!
            }
        }
    }
}
