package game;

import java.util.InputMismatchException;
import java.util.Scanner;

public class TwoPlayerGame {
    private final Board board;
    private GameResult gameResult;

    private Player player1;
    private Player player2;
    private Player activePlayer;

    public TwoPlayerGame(int m, int n, int k) {
        this.board = new TicTacToeBoard(m, n, k);
        this.gameResult = GameResult.UNKNOWN;
    }

    public TwoPlayerGame(int m, int n, int k, Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
        this.board = new TicTacToeBoard(m, n, k);
        this.gameResult = GameResult.UNKNOWN;
    }

    public GameResult playInTerminal() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            activePlayer = player1;
            prepareAndMakeMove(scanner);
            if (!gameResult.equals(GameResult.UNKNOWN)) {
                return gameResult;
            }
            activePlayer = player2;
            prepareAndMakeMove(scanner);
            if (!gameResult.equals(GameResult.UNKNOWN)) {
                return gameResult;
            }
        }
    }

    private void prepareAndMakeMove(Scanner scanner) {
        Move move;
        if (activePlayer instanceof HumanPlayer) {
            System.out.println();
            System.out.println("Current position");
            System.out.println(board.getPosition());
            System.out.println("Enter you move for " + activePlayer.getSign());
            move = prepareMove(activePlayer, readCoordinatesFromInput(scanner));
        } else {
            move = prepareMove(activePlayer);
        }
        makeMove(move);
        displayStatus(move);
    }

    private void displayStatus(Move move) {
        System.out.println();
        System.out.println("Player: " + getNumberOfLastMovedPlayer());
        System.out.println(move);
        System.out.println(board);
        System.out.println("Result: " + gameResult);
    }


    public void makeMove(Move move) {
        this.gameResult = board.makeMove(move);
        board.setCurrentPlayerHasObtainedValidMove(false);
    }

    public Move prepareMove(Player player, Coordinates... coordinates) {
        activePlayer = player;
        if (coordinates.length > 0 && player instanceof HumanPlayer) {
            ((HumanPlayer) player).setCoordinates(coordinates[0]);
        }
        return player.prepareMove(board.getPosition(), activePlayer.getSign());
    }

    public GameResult getGameResult() {
        return gameResult;
    }

    public boolean firstPlayerMadeLastMove() {
        return activePlayer == player1;
    }

    public int getNumberOfLastMovedPlayer() {
        return firstPlayerMadeLastMove() ? 1 : 2;
    }

    public Coordinates readCoordinatesFromInput(Scanner scanner) {
        int rowIndex = 0;
        int columnIndex = 0;
        boolean columnIsRead = false;
        while (!(columnIsRead)) {
            try {
                rowIndex = scanner.nextInt();
                columnIndex = scanner.nextInt();
                columnIsRead = true;
            } catch (InputMismatchException e) {
                System.out.println("Input should be a number! Try again:");
                scanner.nextLine();
            }
        }
        return new Coordinates(rowIndex - 1, columnIndex - 1);
    } 
}
