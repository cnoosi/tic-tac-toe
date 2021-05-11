package Game;

public class MovesHistory {
    private int row;
    private int col;

    public MovesHistory(int row, int col){
        setRow(row);
        setCol(col);
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
