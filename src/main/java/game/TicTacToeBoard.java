package game;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TicTacToeBoard implements Board, Position {
    private static final Map<Cell, String> CELL_TO_STRING = Map.of(
            Cell.E, ".",
            Cell.X, "X",
            Cell.O, "0"
    );

    private final Cell[][] field;
    private Cell turn;

    private final Integer k;
    private final int m;
    private final int n;

    private boolean currentPlayerHasObtainedValidMove = false;
    public TicTacToeBoard(int m, int n, int k) {
        this.k = k;
        this.m = m;
        this.n = n;
        field = new Cell[m][n];
        for (Cell[] row : field) {
            Arrays.fill(row, Cell.E);
        }
        turn = Cell.X;
    }

    @Override
    public Coordinates getMaxValues() {
        return new Coordinates(m, n);
    }

    @Override
    public Position getPosition() {
        return this;
    }

    @Override
    public GameResult makeMove(Move move) {
        if (!isValid(move, ValidityCheckMode.MOVE,true)) {
            return GameResult.LOOSE;
        }

        field[move.getRow()][move.getCol()] = move.getValue();

        if (checkWin()) {
            return GameResult.WIN;
        }

        if (checkDraw()) {
            return GameResult.DRAW;
        }
        switchTurn();
        return GameResult.UNKNOWN;
    }

    @Override
    public void switchTurn() {
        turn = turn == Cell.X ? Cell.O : Cell.X;
    }

    private boolean checkDraw() {
        int count = 0;
        for (int r = 0; r < m; r++) {
            for (int c = 0; c < n; c++) {
                if (field[r][c] == Cell.E) {
                    count++;
                }
            }
        }
        return count == 0;
    }

    private boolean checkWin() {
        if (checkWinByRows()) return true;
        if (checkWinByColumns()) return true;
        return findMaxLengthOfAllDiagonals() >= k;
    }

    private boolean checkWinByColumns() {
        for (int c = 0; c < n; c++) {
            if (findMaxLengthForLine(c, 1, m) >= k) {
                return true;
            }
        }
        return false;
    }

    private boolean checkWinByRows() {
        for (int r = 0; r < m; r++) {
            if (findMaxLengthForLine(r, 2, n) >= k) {
                return true;
            }
        }
        return false;
    }

    private int findMaxLengthForLine(int fixedIndex, int dimensionToIterate, int limit) {
        int maxLength = 0;
        int currentPosition = 0;
        int currentLength = 0;
        while (currentPosition < limit) {
            Cell cellToCheck = dimensionToIterate == 2 ? field[fixedIndex][currentPosition] : field[currentPosition][fixedIndex];
            if (cellToCheck.equals(turn)) {
                currentLength++;
            } else {
                if (currentLength >= k) {
                    return currentLength;
                }
                if (currentLength > maxLength) {
                    maxLength = currentLength;
                }
                currentLength = 0;
            }
            currentPosition++;
        }
        if (currentLength >= k) {
            return currentLength;
        }
        if (currentLength > maxLength) {
            maxLength = currentLength;
        }
        return maxLength;
    }

    public int findMaxLengthOfAllDiagonals() {
        int result = 0;
        for (int r = k - 1; r < m; r++) {
            int maxResultForDiagonal = findMaxResultForSecondaryDiagonal(r, 0);
            if (maxResultForDiagonal > result) {
                result = maxResultForDiagonal;
            }
        }

        for (int c = 1; c < n - k + 1; c++) {
            int maxResultForDiagonal = findMaxResultForSecondaryDiagonal(m - 1, c);
            if (maxResultForDiagonal > result) {
                result = maxResultForDiagonal;
            }
        }

        for (int r = m - k + 1; r >= 0; r--) {
            int maxResultForDiagonal = findMaxResultForMainDiagonal(r, 0);
            if (maxResultForDiagonal > result) {
                result = maxResultForDiagonal;
            }
        }

        for (int c = 1; c < n - k + 1; c++) {
            int maxResultForDiagonal = findMaxResultForMainDiagonal(0, c);
            if (maxResultForDiagonal > result) {
                result = maxResultForDiagonal;
            }
        }
        return result;
    }

    private int findMaxResultForMainDiagonal(int startRow, int startColumn) {
        int result = 0;
        int currentResult = 0;

        while (startRow < m && startColumn < n) {
            if (field[startRow][startColumn].equals(turn)) {
                currentResult++;
            } else {
                if (currentResult >= k) {
                    return currentResult;
                } else if (currentResult > result) {
                    result = currentResult;
                }
                currentResult = 0;
            }
            if (currentResult >= k) {
                return currentResult;
            } else if (currentResult > result) {
                result = currentResult;
            }
            startRow++;
            startColumn++;
        }
        return result;
    }

    private int findMaxResultForSecondaryDiagonal(int startRow, int startColumn) {
        Integer result = 0;
        Integer currentResult = 0;

        while (startRow >= 0 && startColumn < n) {
            if (field[startRow][startColumn].equals(turn)) {
                currentResult++;
            } else {
                if (currentResult >= k) {
                    return currentResult;
                } else if (currentResult > result) {
                    result = currentResult;
                }
                currentResult = 0;
            }
            if (currentResult >= k) {
                return currentResult;
            } else if (currentResult > result) {
                result = currentResult;
            }
            startRow--;
            startColumn++;
        }
        return result;
    }

    public boolean isValid(final Move move, ValidityCheckMode mode, boolean log) {
        if (!(0 <= move.getRow() && move.getRow() < m)) {
            if (log) System.out.printf("Row index %d is out of bounds (between 0 and %d)", move.getRow(), m);
            return false;
        }
        if (!(0 <= move.getCol() && move.getCol() < n)) {
            if (log) System.out.printf("Column index %d is out of bounds (between 0 and %d)", move.getCol(), n);
            return false;
        }
        if (!field[move.getRow()][move.getCol()].equals(Cell.E)) {
            if (log)
                System.out.printf("Cell with coordinates (%d, %d) is already filled with %s", move.getRow(), move.getCol(), field[move.getRow()][move.getCol()]);
            return false;
        }
        if (turn != move.getValue()) {
            if (log) System.out.printf("Expected turn %s, got %s", turn, move.getValue());
            return false;
        }
        if (ValidityCheckMode.PREPARE.equals(mode) && currentPlayerHasObtainedValidMove) {
            if (log) System.out.println("Player already has prepared a valid move, but has not used it yet!");
            return false;
        }
        currentPlayerHasObtainedValidMove = true;
        return true;
    }

    public void setCurrentPlayerHasObtainedValidMove(boolean currentPlayerHasObtainedValidMove) {
        this.currentPlayerHasObtainedValidMove = currentPlayerHasObtainedValidMove;
    }

    @Override
    public Cell getCell(int row, int column) {
        return field[row][column];
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(" " + IntStream
                .iterate(1, i -> i + 1)
                .limit(n)
                .mapToObj(String::valueOf)
                .collect(Collectors.joining())).append(System.lineSeparator());
        for (int r = 0; r < m; r++) {
            sb.append(r + 1);
            for (Cell cell : field[r]) {
                sb.append(CELL_TO_STRING.get(cell));
            }
            sb.append(System.lineSeparator());
        }
        sb.setLength(sb.length() - System.lineSeparator().length());
        return sb.toString();
    }
}
