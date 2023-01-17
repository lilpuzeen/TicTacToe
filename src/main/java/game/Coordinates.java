package game;

public class Coordinates {
    private final int rowIndex;
    private final int columnIndex;

    public Coordinates(int x, int y) {
        this.rowIndex = x;
        this.columnIndex = y;
    }

    public int getX() {
        return rowIndex;
    }

    public int getY() {
        return columnIndex;
    }
}
