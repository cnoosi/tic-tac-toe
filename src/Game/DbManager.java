package Game;
import java.util.*;
import java.sql.*;

public class DbManager {

    private static DbManager instance = new DbManager();
    private ArrayList<User> userList = new ArrayList<>();
    private int currentUser;

    private DbManager()
    {
        String sql = "SELECT * FROM User";
        ResultSet rs = null;
        userList.add(new User());
        currentUser = 0;
        try(Connection conn = this.connect();
            PreparedStatement ps = conn.prepareStatement(sql)){
            rs = ps.executeQuery();
            while(rs.next()){
                int    id        = rs.getInt("Id");
                String userName  = rs.getString("UserName");
                String firstName = rs.getString("FirstName");
                String lastName  = rs.getString("LastName");
                String password  = rs.getString("Password");
                int    deleted   = rs.getInt("Deleted");

                userList.add(new User(id,userName,firstName,lastName,password,deleted));
            }

        } catch(SQLException e) { System.out.println(e.getMessage()); }
    }

    public static DbManager getInstance()
    {
        return instance;
    }

    //********************************************
    //SEARCHES THROUGH THE LIST OF USERS TO SEE
    //IF USER EXISTS
    //********************************************
    public boolean userFound (String user, String pass)
    {
        boolean found = false;
        for (User u: userList)
        {
            if((u.getUserName().equals(user)) && (u.getPassword().equals(pass)))
            {
                found = true;
                break;
            }
        }
        return found;
    }

    //********************************************
    //CHECKS IF USERNAME IS TAKEN OR NOT
    //********************************************
    public boolean userAvailable(String user)
    {
        boolean available = true;
        for (User u: userList)
        {
            if(u.getUserName().equals(user))
            {
                available = false;
                break;
            }
        }
        return available;
    }

    public void setCurrentUser(String userName)
    {
        for(User u: userList)
        {
            if(u.getUserName().equals(userName)){
                currentUser = userList.indexOf(u);
                System.out.println("current index: " + currentUser);
            }
        }
        System.out.println("current index: " + currentUser);
    }

    public void setCurrentUser(int id)
    {
        for(User u: userList)
        {
            if(u.getId() == id) {
                currentUser = userList.indexOf(u);
                System.out.println(currentUser);
            }
        }
    }

    public void setCurrentUserIndex(int index){
        currentUser = index;
    }

    public User getCurrentUser()
    {
        return userList.get(currentUser);
    }

    public void addUser(String userName, String firstName, String lastName, String password)
    {
        String sql = "INSERT INTO User(UserName,FirstName,LastName,Password,Deleted) VALUES(?,?,?,?,?)";


        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1,userName);
            pstmt.setString(2,firstName);
            pstmt.setString(3,lastName);
            pstmt.setString(4,password);
            pstmt.setInt(5,0);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        sql = "SELECT * FROM User WHERE UserName = ?";
        ResultSet rs = null;
        int id = -2;
        int deleted = -2;
        try (Connection conn = this.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1,userName);
            rs = ps.executeQuery();
            while(rs.next()){
                id = rs.getInt("Id");
                deleted = rs.getInt("Deleted");
            }
        } catch (SQLException e) { System.out.println(e.getMessage()); }

        if(id == -2 || deleted == -2)
        {
            System.out.println("bruh it went south");
        }
        else
        {
            userList.add(new User(id,userName,firstName,lastName,password,deleted));
//            setCurrentUser(id,userName,firstName,lastName,password,deleted);
            setCurrentUser(id);
            System.out.println("new user added successfully! id: " + id + " username: " + userName + " deleted: " + deleted);
        }

    }

    public static Connection connect()
    {
        Connection conn = null;
        try{
            // db parameters
            String url = "jdbc:sqlite:src/resources/images/TTTDb.db";
            // create a connection to the database
            conn = DriverManager.getConnection(url);

            System.out.println("Connection to SQLite has been established.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public void printAllUsers()
    {
        for (User u: userList){
            System.out.println(u.toString());
        }
    }

}
