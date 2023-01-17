package game;

import java.util.Random;

public class RandomPlayer implements Player {

    private Cell sign;

    @Override
    public Cell getSign() {
        return sign;
    }

    public void setSign(Cell sign) {
        this.sign = sign;
    }

    public RandomPlayer(Cell sign) {
        this.sign = sign;
    }

    public RandomPlayer() {}
    

    private final Random random = new Random();

    @Override
    public PlayerType getType() {
        return PlayerType.RANDOM;
    }

    @Override
    public Move prepareMove(Position position, Cell activePlayerSign) {
        while (true) {
            final Move move = new Move(
                    random.nextInt(position.getMaxValues().getX()),
                    random.nextInt(position.getMaxValues().getY()),
                    activePlayerSign
            );
            if (position.isValid(move)) {
                return move;
            }
        }
    }

    public String toString() {
        return "Random player";
    }
}
