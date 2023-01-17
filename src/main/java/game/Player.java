package game;

public interface Player {
    Cell getSign();

    void setSign(Cell sign);

    PlayerType getType();

    Move prepareMove(Position position, Cell activePlayerSign);
}
