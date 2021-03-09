package Game;

public class Position
{
    private int col;
    private int row;

    public Position()
    {
        this.row = -1;
        this.col = -1;
    }

    public Position(int row, int col)
    {
        this.row = row;
        this.col = col;
    }

    public int getCol()
    {
        return col;
    }

    public void setCol(int col)
    {
        this.col = col;
    }

    public int getRow()
    {
        return row;
    }

    public void setRow(int row)
    {
        this.row = row;
    }

    @Override
    public String toString()
    {
        return "Row: " + row + "\nCol: " + col;
    }
}
