package game;

public interface Board {
    Position getPosition();
    void setCurrentPlayerHasObtainedValidMove(boolean currentPlayerHasObtainedValidMove);
    GameResult makeMove(Move move);
    void switchTurn();
}
