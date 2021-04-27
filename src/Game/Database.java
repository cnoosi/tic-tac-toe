package Game;
import javax.xml.transform.Result;
import java.sql.*;

public class Database {

    public static Connection connect() {
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

    public void insert(String userName, String firstName, String lastName, String password) {
        String sql = "INSERT INTO User(UserName,FirstName,LastName,Password) VALUES(?,?,?,?)";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1,userName);
            pstmt.setString(2,firstName);
            pstmt.setString(3,lastName);
            pstmt.setString(4,password);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void readAll(){
        String sql = "SELECT * FROM User";
        ResultSet rs = null;
        try(Connection conn = this.connect();
        PreparedStatement ps = conn.prepareStatement(sql)){

            rs = ps.executeQuery();
            System.out.println("All Users");
            while(rs.next()){
                int    id        = rs.getInt("Id");
                String userName  = rs.getString("UserName");
                String firstName = rs.getString("FirstName");
                String lastName  = rs.getString("LastName");
                String passwrod  = rs.getString("Password");

                System.out.println("Id: " + id);
                System.out.println("Username: " + userName);
                System.out.println("First Name: " + firstName);
                System.out.println("Last Name: " + lastName);
                System.out.println("Password: " + passwrod);
                System.out.println("\n\n");
            }

        } catch(SQLException e) { System.out.println(e.getMessage()); }
    }

    public void deleteRow(String userName) {
        String sql = "delete from User WHERE UserName = ?";

        try (Connection conn = this.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, userName);
            ps.execute();
            System.out.println("User " + userName + " Has been DELETED!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
