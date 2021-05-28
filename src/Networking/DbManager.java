package Networking;

import java.util.*;
import java.sql.*;

public class DbManager {
    private ArrayList<User> userList = new ArrayList<>();
    private ArrayList<GameHistory> gameList = new ArrayList<>();
    private ArrayList<MovesHistory> movesList = new ArrayList<>();

    public DbManager() {
        String sql = "SELECT * FROM User";
        ResultSet rs = null;
        userList.add(new User());
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


        } catch (SQLException e) { System.out.println(e.getMessage()); }

        String gameSql = "Select * FROM Game";
        ResultSet gameRS = null;
        gameList.add(new GameHistory());
        try (Connection gameConn = this.connect();
             PreparedStatement gamePS = gameConn.prepareStatement(gameSql)) {
            gameRS = gamePS.executeQuery();
            while (gameRS.next()) {
                String gameId = gameRS.getString("Id");
                Long startTime = gameRS.getLong("StartTime");
                Long endTime = gameRS.getLong("EndTime");
                int player1Id = gameRS.getInt("Player1Id");
                int player2Id = gameRS.getInt("Player2Id");
                int startingPlayerId = gameRS.getInt("StartingPlayerId");
                int winnerToken = gameRS.getInt("WinnerToken");

                gameList.add(new GameHistory(gameId, startTime, endTime, player1Id, player2Id, startingPlayerId, winnerToken));

            }

        } catch (SQLException e) { System.out.println(e.getMessage()); }


        String movesSql = "Select * FROM Moves";
        ResultSet movesRS = null;
        movesList.add(new MovesHistory());
        try (Connection movesConn = this.connect();
             PreparedStatement movesPS = movesConn.prepareStatement(movesSql)) {
            movesRS = movesPS.executeQuery();
            while (movesRS.next()) {
                String gameId = movesRS.getString("Id");
                int playerId = movesRS.getInt("PlayerId");
                int row = movesRS.getInt("Row");
                int col = movesRS.getInt("Col");
                String time = movesRS.getString("Time");
                int moveIndex = movesRS.getInt("MoveIndex");

                movesList.add(new MovesHistory(gameId, playerId, row, col, time, moveIndex));

            }

        } catch (SQLException e) { System.out.println(e.getMessage()); }

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
            if((u.getUserName().equals(user)) && (u.getPassword().equals(pass)) && !u.isDeleted())
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

    public int getUserId(String username)
    {
        for (User u: userList)
        {
            if (u.getUserName().equals(username))
                return u.getId();
        }
        return -1;
    }

    public String getUsername(int userId)
    {
        for (User u: userList)
        {
            if (u.getId() == userId)
                return u.getUserName();
        }
        return "";
    }

    public GameHistory getGameHistory(String gameId)
    {
        for (GameHistory game : gameList)
        {
            if (game.getGameId().equals(gameId))
            {
                return game;
            }
        }
        return null;
    }

    public ArrayList<GameHistory> getGameHistoryForUser(int userId)
    {
        ArrayList<GameHistory> history = new ArrayList<>();
        for (GameHistory game : gameList)
        {
            if (game.getPlayer1Id() == userId || game.getPlayer2Id() == userId)
                history.add(game);
        }
        return history;
    }

    public ArrayList<MovesHistory> getMoveHistory(String gameId)
    {
        Map<Integer, MovesHistory> moveMap = new HashMap<Integer, MovesHistory>();

        for (MovesHistory move : movesList)
        {
            if (move.getGameId().equals(gameId)) {
                moveMap.put(move.getMoveIndex(), move);
            }
        }

        ArrayList<MovesHistory> moves = new ArrayList<>();
        for (Map.Entry<Integer, MovesHistory> entry: moveMap.entrySet())
        {
            moves.add(entry.getValue());
        }
        return moves;
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
            System.out.println("new user added successfully! id: " + id + " username: " + userName + " deleted: " + deleted);
        }

    }

    public void addGame(String gameId, long startTime, long endTime, int player1Id, int player2Id, int startingPlayerId, int winnerToken)
    {
        String gameSql = "INSERT INTO Game(Id,StartTime,EndTime,Player1Id,Player2Id,StartingPlayerId,WinnerToken) VALUES(?,?,?,?,?,?,?)";
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
        String gameSql = "INSERT INTO Moves(Id,PlayerId,Row,Col,Time,MoveIndex) VALUES(?,?,?,?,?,?)";
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

    public void printAllGames(){
        for(GameHistory g: gameList){
            System.out.println(g.toString());
        }
    }

}
