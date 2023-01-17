package game;

import java.util.InputMismatchException;
import java.util.Scanner;


public class Main {
    private static Tournament tournament;
    private static TwoPlayerGame game;
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        
        int mode = readModeFromTerminal(sc); 

        Player player1 = readPlayerFromTerminal(sc);
        player1.setSign(Cell.X);
        Player player2 = readPlayerFromTerminal(sc);
        player2.setSign(Cell.O);
        
        if (mode == 1) {
            System.out.println("Enter target score: ");
            int targetScore = sc.nextInt();
            tournament = new Tournament(targetScore, player1 , player2);
            while (!tournament.isFinished()) {

                SingleGameResult gameResult = playSingleGame(sc, player1, player2);
                if (gameResult.getResult().equals(GameResult.WIN)) {
                    tournament.incrementScore(gameResult.getLastMovedPlayer());
                } else if (gameResult.getResult().equals(GameResult.LOOSE)) {
                    tournament.incrementScoreToEnemy(gameResult.getLastMovedPlayer());
                }

                System.out.println("Current Score " + tournament.getTotalScore());
            
                if (!tournament.isFinished()) {
                    switchSigns(player2, player1);
                    gameResult = playSingleGame(sc, player2, player1);
                    if (gameResult.getResult().equals(GameResult.WIN)) {
                        tournament.incrementScore(gameResult.getLastMovedPlayer());
                    } else if (gameResult.getResult().equals(GameResult.LOOSE)) {
                        tournament.incrementScoreToEnemy(gameResult.getLastMovedPlayer());
                    }
                    switchSigns(player1, player2);
                }
                

                
            }
            System.out.println(tournament.getWinner().get() + " has won!");
        } else {
            playSingleGame(sc, player1, player2);
        }

    }

    private static SingleGameResult playSingleGame(Scanner sc, Player player1, Player player2) {
        System.out.println("Enter number of rows: ");
        int m = readDimension(sc);

        System.out.println("Enter number of columns: ");
        int n = readDimension(sc);

        System.out.println("Enter number of k-in-a-row streak: ");
        int k = readAndValidateK(sc, m, n);

        System.out.println("New game started!");

        game = new TwoPlayerGame(
                m, n, k,
                player1,
                player2
        );
        final GameResult result = game.playInTerminal();
        switch (result) {
            case WIN:
                System.out.println("Player " + game.getNumberOfLastMovedPlayer() + " won");
                break;
            case LOOSE:
                System.out.println("Player " + (3 - game.getNumberOfLastMovedPlayer()) + " won");
                break;
            case DRAW:
                System.out.println("Draw");
                break;
            default:
                throw new AssertionError("Unknown result " + result);
        }
        return new SingleGameResult(result, game.getNumberOfLastMovedPlayer() == 1 ? player1 : player2);
    }

    private static int readAndValidateK(Scanner scanner, int m, int n) {
        while (true) {
            int k = readDimension(scanner);
            if (k > Math.max(m, n)) {
                System.out.println("K-Dimension should be less than other two dimensions! Try again:");
                scanner.nextLine();
            } else {
                return k;
            }
        }
    }

    private static int readDimension(Scanner scanner) {
        while (true) {
            try {
                int result = scanner.nextInt();
                if (result < 1) {
                    System.out.println("Input should be positive! Try again:");
                    scanner.nextLine();
                } else {
                    return result;
                }
            } catch (InputMismatchException e) {
                System.out.println("Input should be a number! Try again:");
                scanner.nextLine();
            }
        }
    }

    private static int readModeFromTerminal(Scanner sc) {
        boolean modeEnteredCorrectly = false;
        int mode = -1;
        while(!modeEnteredCorrectly) {
            System.out.println("Please select the mode: ");
            System.out.println("0 - single game");
            System.out.println("1 - tournament");
            try {
                mode = sc.nextInt();
                if (mode == 0 || mode == 1) {
                    modeEnteredCorrectly = true;
                } else {
                    System.out.println("Mode should be integer: {0, 1}. Please, try again");    
                }
            } catch (InputMismatchException e) {
                System.out.println("Mode should be integer: {0, 1}. Please, try again");
            }
            sc.nextLine();
        }

        return mode;
    }

    private static Player readPlayerFromTerminal(Scanner sc) {
        boolean playerTypeEnteredCorrectly = false;
        String player1Type = "";
        while(!playerTypeEnteredCorrectly) {
            System.out.println("Allowed types: H -> Human; R -> Random; C -> Cheater; S -> Sequential");
            System.out.println("Enter first player Type: ");
            player1Type = sc.nextLine();
            if (!PlayerType.allowedTypesLiterals().contains(player1Type)) {
                System.out.println("Please, enter correct literal!");
            } else {
                playerTypeEnteredCorrectly = true;
            }
        }
        return PlayerType.getPlayerByLiteral(player1Type);
    }

    private static class SingleGameResult {
        private GameResult gameResult;
        private Player lastMovedPlayer;

        public SingleGameResult(GameResult gameResult, Player lastMovedPlayer) {
            this.gameResult = gameResult;
            this.lastMovedPlayer = lastMovedPlayer;
        }

        public Player getLastMovedPlayer() {
            return lastMovedPlayer;
        }

        public GameResult getResult() {
            return gameResult;
        }
    }

    private static void switchSigns(Player player1, Player player2) {
        Cell player1Sign = player1.getSign();
        Cell player2Sign = player2.getSign();
        player2.setSign(player1Sign);
        player1.setSign(player2Sign);
    }
}
