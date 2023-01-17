package game;

public interface Position {

    Coordinates getMaxValues();

    boolean isValid(Move move, ValidityCheckMode mode, boolean log);

    default boolean isValid(Move move) {
        return isValid(move, ValidityCheckMode.PREPARE, false);
    };



    Cell getCell(int row, int column);
}
