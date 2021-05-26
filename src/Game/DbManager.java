package Game;
import javax.xml.stream.events.StartDocument;
import java.util.*;
import java.sql.*;

public class DbManager {

    private static DbManager instance = new DbManager();
    private ArrayList<User> userList = new ArrayList<>();
    private ArrayList<GameHistory> gameList = new ArrayList<>();
    private ArrayList<MovesHistory> movesList = new ArrayList<>();
    private int currentUser;
    private int currentGame;
    private int currentMoves;

    private DbManager() {
        String sql = "SELECT * FROM User";
        ResultSet rs = null;
        userList.add(new User());
        currentUser = 0;
        try (Connection conn = this.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("Id");
                String userName = rs.getString("UserName");
                String firstName = rs.getString("FirstName");
                String lastName = rs.getString("LastName");
                String password = rs.getString("Password");
                int deleted = rs.getInt("Deleted");

                userList.add(new User(id, userName, firstName, lastName, password, deleted));
            }


        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        String gameSql = "Select * FROM GAME";
        ResultSet gameRS = null;
        gameList.add(new GameHistory());
        currentGame = 0;
        try (Connection gameConn = this.connect();
             PreparedStatement gamePS = gameConn.prepareStatement(gameSql)) {
            gameRS = gamePS.executeQuery();
            while (gameRS.next()) {
                String gameId = gameRS.getString("GameId");
                Long startTime = gameRS.getLong("StartTime");
                Long endTime = gameRS.getLong("EndTime");
                int player1Id = gameRS.getInt("Player1Id");
                int player2Id = gameRS.getInt("Player2Id");
                int startingPlayerId = gameRS.getInt("StartingPlayerId");
                int winnerToken = gameRS.getInt("WinningPlayerId");

                gameList.add(new GameHistory(gameId, startTime, endTime, player1Id, player2Id, startingPlayerId, winnerToken));

            }

        } catch (SQLException e) { System.out.println(e.getMessage()); }


        String movesSql = "Select * FROM Moves";
        ResultSet movesRS = null;
        movesList.add(new MovesHistory());
        currentMoves = 0;
        try (Connection movesConn = this.connect();
             PreparedStatement movesPS = movesConn.prepareStatement(gameSql)) {
            movesRS = movesPS.executeQuery();
            while (movesRS.next()) {
                String gameId = movesRS.getString("GameId");
                int playerId = movesRS.getInt("PlayerId");
                int row = movesRS.getInt("Row");
                int col = movesRS.getInt("Column");
                String time = movesRS.getString("Time");
                int moveIndex = movesRS.getInt("MoveIndex");

                movesList.add(new MovesHistory(gameId, playerId, row, col, time, moveIndex));

            }

        } catch (SQLException e) { System.out.println(e.getMessage()); }

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

    public void setCurrentGameIndex(int gameIndex) {currentGame = gameIndex;}

    public GameHistory getCurrentGame() {return gameList.get(currentGame);}

    public void setCurrentMovesIndex(int movesIndex) {currentGame = movesIndex;}

    public MovesHistory getCurrentMoves() {return movesList.get(currentMoves);}

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

    public void addGame(String gameId, long startTime, long endTime, int player1Id, int player2Id, int startingPlayerId, int winnerToken)
    {
        String gameSql = "INSERT INTO Game(GameId,StartTime,EndTime,Player1Id,Player2Id,StartingPlayerId,WinningPlayerId) VALUES(?,?,?,?,?,?,?)";
        try (Connection gameConn = this.connect();
             PreparedStatement gamePstmt = gameConn.prepareStatement(gameSql)) {
            gamePstmt.setString(1,gameId);
            gamePstmt.setLong(2,startTime);
            gamePstmt.setLong(3,endTime);
            gamePstmt.setInt(4,player1Id);
            gamePstmt.setInt(5,player2Id);
            gamePstmt.setInt(6,startingPlayerId);
            gamePstmt.setInt(7,winnerToken);
            gamePstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        gameList.add(new GameHistory(gameId, startTime, endTime, player1Id, player2Id, startingPlayerId, winnerToken));

    }

    public void addMoves(String gameId, int playerId, int row, int col, String time, int moveIndex)
    {
        String gameSql = "INSERT INTO Game(GameId,PlayerId,Row,Col,Time,MoveIndex) VALUES(?,?,?,?,?,?)";
        try (Connection gameConn = this.connect();
             PreparedStatement gamePstmt = gameConn.prepareStatement(gameSql)) {
            gamePstmt.setString(1,gameId);
            gamePstmt.setInt(2,playerId);
            gamePstmt.setInt(3,row);
            gamePstmt.setInt(4,col);
            gamePstmt.setString(5,time);
            gamePstmt.setInt(6,moveIndex);
            gamePstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        movesList.add(new MovesHistory(gameId, playerId, row, col, time, moveIndex));

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
