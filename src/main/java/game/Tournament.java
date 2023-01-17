package game;

import java.util.Optional;
import java.util.stream.Stream;

public class Tournament {
    private int targetScore;
    private Statistics player1Statistics;
    private Statistics player2Statistics;

    public Tournament (int targetScore, Player player1, Player player2) {
        this.targetScore = targetScore;
        this.player1Statistics = new Statistics(player1);
        this.player2Statistics = new Statistics(player2);
    }

    public void incrementScore(Player winner) {
        Stream.of(player1Statistics, player2Statistics)
        .filter(stats -> stats.getPlayer() == winner)
        .forEach(Statistics::upscore);
    }

    public void incrementScoreToEnemy(Player looser) {
        Stream.of(player1Statistics, player2Statistics)
        .filter(stats -> stats.getPlayer() != looser)
        .forEach(Statistics::upscore);
    }

    public boolean isFinished() {
        return getWinner().isPresent();
    }

    public Optional<Player> getWinner() {
        int currentScorePlayer1 = player1Statistics.getScore();
        int currentScorePlayer2 = player2Statistics.getScore();
        if (currentScorePlayer1 >= targetScore) {
            return Optional.of(player1Statistics.getPlayer());
        } else if (currentScorePlayer2 >= targetScore) {
            return Optional.of(player2Statistics.getPlayer());
        }
        return Optional.empty();
        // return Stream.of(player1Statistics, player2Statistics).anyMatch(stats -> stats.getScore() >= targetScore);
    }

    public String getTotalScore() {
        return player1Statistics.getScore() + " : " + player2Statistics.getScore();
    }


    private static class Statistics {
        private Player player;
        private int score;

        public Statistics(Player player) {
            this.player = player;
            this.score = 0;
        }

        public Player getPlayer() {
            return player;
        }

        public void upscore() {
            score++;
        }

        public int getScore() {
            return score;
        }
    }
}
