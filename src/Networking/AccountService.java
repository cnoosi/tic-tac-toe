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
        String newValue = (String) map.get("NewValue");
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
                client.writeMessage(new AccountMessage(AccountAction.Register, null, null,
                        null, null, "fail"));
        }
        else if (action == AccountAction.Logout)
        {
            client.setId(-1);
            client.writeMessage(new AccountMessage(AccountAction.Logout, null, null,
                    null, null, "success"));
        }
        else if (action == AccountAction.ChangeName)
        {
            if (client.getId() != -1)
            {
                String currentUsername = database.getUsername(client.getId());
                if (database.userFound(currentUsername, password))
                {
                    database.changeInfo("FirstName", firstName, currentUsername);
                    database.changeInfo("LastName", lastName, currentUsername);
                    client.writeMessage(new AccountMessage(AccountAction.ChangeName, "success"));
                }
                else
                {
                    client.writeMessage(new AccountMessage(AccountAction.ChangeName, "fail"));
                }
            }
        }
        else if (action == AccountAction.ChangeUsername)
        {
            if (client.getId() != -1)
            {
                String currentUsername = database.getUsername(client.getId());
                if (database.userFound(currentUsername, password))
                {
                    database.changeInfo("Username", newValue, currentUsername);
                    client.writeMessage(new AccountMessage(AccountAction.ChangeUsername, "success"));
                }
                else
                {
                    client.writeMessage(new AccountMessage(AccountAction.ChangeUsername, "fail"));
                }
            }
        }
        else if (action == AccountAction.ChangePassword)
        {
            if (client.getId() != -1)
            {
                String currentUsername = database.getUsername(client.getId());
                if (database.userFound(currentUsername, password))
                {
                    database.changeInfo("Password", newValue, currentUsername);
                    client.writeMessage(new AccountMessage(AccountAction.ChangePassword, "success"));
                }
                else
                {
                    client.writeMessage(new AccountMessage(AccountAction.ChangePassword, "fail"));
                }
            }
        }
        else if (action == AccountAction.DeleteUser)
        {
            System.out.println("Server knows whats up");
            if (client.getId() != -1)
            {
                if (database.userFound(username, password))
                {
                    System.out.println("Server knows whats up 2");
                    database.deleteUser(username);
                    client.setId(-1);
                    client.writeMessage(new AccountMessage(AccountAction.DeleteUser, "success"));
                }
                else
                {
                    client.writeMessage(new AccountMessage(AccountAction.DeleteUser, "fail"));
                }
            }
        }
    }
}
