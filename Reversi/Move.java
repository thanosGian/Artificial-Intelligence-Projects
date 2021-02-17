public class Move {
    private int row;
    private int col;
    private int value;

    public Move(){
        row = -1;
        col = -1;
        value = 0;
    }

    public Move(int row, int col)
    {
        this.row = row;
        this.col = col;
        this.value = 0;
    }

    public Move(int value)
    {
        this.row = -1;
        this.col = -1;
        this.value = value;
    }

    public Move(int row, int col, int value)
    {
        this.row = row;
        this.col = col;
        this.value = value;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return col;
    }

    public void setColumn(int col) {
        this.col = col;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "["+this.row+','+this.col+"]"+value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Move move = (Move) o;
        return row == move.row &&
                col == move.col;
    }

    @Override
    public int hashCode() {
        return this.getRow() + this.getColumn();
    }
}
