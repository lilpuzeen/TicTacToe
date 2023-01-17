package game;

public class SequentialPlayer implements Player {

    private Cell sign;

    @Override
    public Cell getSign() {
        return sign;
    }

    public void setSign(Cell sign) {
        this.sign = sign;
    }

    public SequentialPlayer(Cell sign) {
        this.sign = sign;
    }

    public SequentialPlayer() {}

    @Override
    public PlayerType getType() {
        return PlayerType.SEQUENTIAL;
    }

    @Override
    public Move prepareMove(Position position, Cell activePlayerSign) {
        Coordinates fieldDimensions = position.getMaxValues();
        for (int r = 0; r < fieldDimensions.getX(); r++) {
            for (int c = 0; c < fieldDimensions.getY(); c++) {
                final Move move = new Move(r, c, activePlayerSign);
                if (position.isValid(move)) {
                    return move;
                }
            }
        }
        throw new AssertionError("No valid moves");
    }

    public String toString() {
        return "Sequential player";
    }
}
