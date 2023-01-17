package game;

public class CheatingPlayer implements Player {

    private Cell sign;

    public CheatingPlayer(Cell sign) {
        this.sign = sign;
    }

    public CheatingPlayer() {}

    @Override
    public Move prepareMove(Position position, Cell activePlayerSign) {
        final TicTacToeBoard board = (TicTacToeBoard) position;
        Coordinates fieldDimensions = position.getMaxValues();
        Move first = null;
        for (int r = 0; r < fieldDimensions.getX(); r++) {
            for (int c = 0; c < fieldDimensions.getY(); c++) {
                final Move move = new Move(r, c, sign);
                if (position.isValid(move)) {
                    if (first == null) {
                        first = move;
                    } else {
                        board.makeMove(move);
                    }
                }
            }
        }
        return first;
    }

    @Override
    public Cell getSign() {
        return sign;
    }

    public void setSign(Cell sign) {
        this.sign = sign;
    }

    @Override
    public PlayerType getType() {
        return PlayerType.CHEATER;
    }

    public String toString() {
        return "Cheating player";
    }
}
