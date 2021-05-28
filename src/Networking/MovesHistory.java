package Networking;

import Networking.GameProcess;

public class MovesHistory {
    private String gameId;
    private int playerId;
    private int row;
    private int col;
    private String time;
    private int moveIndex;


    public MovesHistory() {
        gameId = "-1";
        playerId = -1;
        row = -1;
        col = -1;
        time = "0";
        moveIndex = -1;
    }

    public MovesHistory(String gameId, int playerId, int row, int col, String time, int moveIndex) {
        setGameId(gameId);
        setPlayerId(playerId);
        setRow(row);
        setCol(col);
        setTime(time);
        setMoveIndex(moveIndex);

    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getMoveIndex() {
        return moveIndex;
    }

    public void setMoveIndex(int moveIndex) {
        this.moveIndex = moveIndex;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public String ToString()
    {
        return "\ngameid: " + this.gameId + "\nplayerid: " + this.playerId + "\nrow: " + this.row + "\ncol: " + this.col
                + "\ntime? " + this.time + "\nmoveindex? " + "\n\n";
    }
}
