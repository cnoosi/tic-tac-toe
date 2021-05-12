package Game;

public class MovesHistory {
    private String gameId;
    private int playerId;
    private int row;
    private int col;
    private long time;
    private int moveIndex;

    public MovesHistory(String gameId,int playerId, int row, int col, int time, int moveIndex){
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

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
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
}
