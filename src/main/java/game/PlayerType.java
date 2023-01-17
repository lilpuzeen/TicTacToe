package game;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public enum PlayerType {
    HUMAN,
    CHEATER,
    SEQUENTIAL,
    RANDOM;

    private static Map<String, PlayerType> typesByLiteral = new HashMap<>() {{
        put("H", HUMAN);
        put("C", CHEATER);
        put("R", RANDOM);
        put("S", SEQUENTIAL);
    }};

    public static Player createPlayerOfType(PlayerType playerType, Cell sign) {
        switch (playerType) {
            case HUMAN -> {
                return new HumanPlayer(sign, Mode.GUI);
            }
            case RANDOM -> {
                return new RandomPlayer(sign);
            }
            case CHEATER -> {
                return new CheatingPlayer(sign);
            }
            case SEQUENTIAL -> {
                return new SequentialPlayer(sign);
            }
        }
        return null;
    }

    public static Player createPlayerOfType(PlayerType playerType) {
        switch (playerType) {
            case HUMAN -> {
                return new HumanPlayer();
            }
            case RANDOM -> {
                return new RandomPlayer();
            }
            case CHEATER -> {
                return new CheatingPlayer();
            }
            case SEQUENTIAL -> {
                return new SequentialPlayer();
            }
        }
        return null;
    }

    public static Player getPlayerByLiteral(String playerLiteral, Cell sign) {
        return createPlayerOfType(typesByLiteral.get(playerLiteral), sign);
    }

    public static Player getPlayerByLiteral(String playerLiteral) {
        return createPlayerOfType(typesByLiteral.get(playerLiteral));
    }
    

    public static Set<String> allowedTypesLiterals() {
        return typesByLiteral.keySet();
    }
}
