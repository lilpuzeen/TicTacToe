package game;


import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.stream.Stream;

import static game.PlayerType.createPlayerOfType;


public class GraphicBoard extends Application {
    private Stage stage;
    private Scene dimensionsScene;
    private TwoPlayerGame game;
    private Player activePlayer;
    private Player player1;
    private Player player2;
    private GridPane gridPane;
    private Label messageLabel;
    private Pane pane;
    private Label resultsLabel = new Label();
    private int m;
    private int n;
    private int k;

    @Override
    public void start(Stage stage) {
        this.stage = stage;
        stage.setTitle("TicTacToe");

        messageLabel = new Label("Enter game dimensions");

        HBox inputBox = new HBox();
        inputBox.setMinHeight(50);
        inputBox.setAlignment(Pos.CENTER);
        TextField mInput = new TextField("3");
        TextField nInput = new TextField("3");
        TextField kInput = new TextField("3");
        inputBox.getChildren().setAll(new Label("m: "), mInput,
                new Label("n: "), nInput,
                new Label("k: "), kInput);
        inputBox.setMinHeight(50);

        HBox playersBox = new HBox();

        ChoiceBox<PlayerType> player1ChoiceBox = new ChoiceBox<>();
        player1ChoiceBox.getItems().setAll(PlayerType.values());
        player1ChoiceBox.setValue(PlayerType.HUMAN);

        ChoiceBox<PlayerType> player2ChoiceBox = new ChoiceBox<>();
        player2ChoiceBox.getItems().setAll(PlayerType.values());
        player2ChoiceBox.setValue(PlayerType.RANDOM);

        playersBox.getChildren().setAll(
                new Label("Choose player 1 type: "), player1ChoiceBox,
                new Label("Choose player 2 type: "), player2ChoiceBox);


        playersBox.setAlignment(Pos.CENTER);
        Stream.of(inputBox, playersBox, player1ChoiceBox, player2ChoiceBox, mInput, nInput, kInput).forEach(elem ->
                HBox.setMargin(elem, new Insets(0, 20, 0, 20)));

        HBox buttonWrapper = prepareButtonWrapper();
        Button startButton = new Button("Start");
        startButton.setOnAction(e -> {
            try {
                validateInputs(mInput, nInput, kInput);
            } catch (IllegalArgumentException ex) {
                return;
            }
            messageLabel.setText("Enter game dimensions");
            messageLabel.getStyleClass().remove("warning");
            startGameWithInputValues(mInput, nInput, kInput, player1ChoiceBox.getValue(), player2ChoiceBox.getValue());
        });
        buttonWrapper.getChildren().setAll(startButton);
        VBox root = prepareRoot(new VBox(messageLabel, inputBox, playersBox, buttonWrapper));
        dimensionsScene = new Scene(root);
        dimensionsScene.getStylesheets().setAll("style.css");
        stage.setWidth(810);
        stage.setScene(dimensionsScene);
        stage.show();
    }

    private void validateInputs(TextField mInput, TextField nInput, TextField kInput) {
        int m = extractFromInput(mInput, "m");
        int n = extractFromInput(nInput, "n");
        int k = extractFromInput(kInput, "k");

        if (k > Math.min(n, m)) {
            if (!messageLabel.getStyleClass().contains("warning")) {
                messageLabel.getStyleClass().add("warning");
            }
            messageLabel.setText("K-Dimension should be less than number of rows and columns!");
            throw new IllegalArgumentException();
        }
    }

    private int extractFromInput(TextField mInput, String literal) {
        try {
            int dimension = Integer.parseInt(mInput.getText());
            if (dimension < 1) {
                if (!messageLabel.getStyleClass().contains("warning")) {
                    messageLabel.getStyleClass().add("warning");
                }
                messageLabel.setText(literal + " should be positive!");
                throw new IllegalArgumentException();
            }
            return dimension;
        } catch (NumberFormatException e) {
            if (!messageLabel.getStyleClass().contains("warning")) {
                messageLabel.getStyleClass().add("warning");
            }
            messageLabel.setText(literal + " should be number!");
            throw new IllegalArgumentException();
        }
    }

    private static HBox prepareButtonWrapper() {
        HBox buttonWrapper = new HBox();
        buttonWrapper.setAlignment(Pos.CENTER);
        buttonWrapper.setMinHeight(50);
        return buttonWrapper;
    }

    private static VBox prepareRoot(VBox root) {
        root.getStyleClass().setAll("root");
        root.setAlignment(Pos.CENTER);
        return root;
    }

    private void startGameWithInputValues(TextField mInput, TextField nInput, TextField kInput, PlayerType playerType1, PlayerType playerType2) {

        m = Integer.parseInt(mInput.getText());
        n = Integer.parseInt(nInput.getText());
        k = Integer.parseInt(kInput.getText());
        this.player1 = createPlayerOfType(playerType1, Cell.X);
        this.player2 = createPlayerOfType(playerType2, Cell.O);
        this.activePlayer = player1;
        game = new TwoPlayerGame(m, n, k);
        gridPane = new GridPane();
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                gridPane.add(generateButton(), j, i);
            }
        }
        VBox root = new VBox();
        HBox horizontalRoot = new HBox();
        root.setAlignment(Pos.CENTER);
        horizontalRoot.setAlignment(Pos.CENTER);
        pane = new Pane(gridPane);
        root.getChildren().add(horizontalRoot);
        horizontalRoot.getChildren().add(pane);
        Scene gameScene = new Scene(root);
        gameScene.getStylesheets().setAll("style.css");
        stage.setScene(gameScene);
        stage.setHeight(55 * m + 40);
        stage.setWidth(55 * n);
        stage.show();


        while (!(activePlayer instanceof HumanPlayer) && GameResult.UNKNOWN.equals(game.getGameResult())) {
            moveByNonHumanPlayer();
            switchActivePlayer();
        }
    }

    private Button generateButton() {
        Button button = new Button();
        button.setMaxWidth(50);
        button.setMinWidth(50);
        button.setMinHeight(50);
        button.setMaxHeight(50);
        button.setOnAction(e -> {
            if (activePlayer instanceof HumanPlayer) {
                moveByHumanPlayer(button);
                switchActivePlayer();
                if (!(activePlayer instanceof HumanPlayer) && game.getGameResult().equals(GameResult.UNKNOWN)) {
                    moveByNonHumanPlayer();
                    switchActivePlayer();
                }
            }
        });
        return button;
    }

    private void moveByHumanPlayer(Button button) {
        Coordinates coordinates = new Coordinates(GridPane.getRowIndex(button), GridPane.getColumnIndex(button));
        Move move = game.prepareMove(activePlayer, coordinates);
        game.makeMove(move);
        button.setText(move.getValue().toString());
        if (!game.getGameResult().equals(GameResult.UNKNOWN)) {
            displayResult();
        }
    }

    private void moveByNonHumanPlayer() {
        Move move = game.prepareMove(activePlayer);
        game.makeMove(move);
        Button affectedButton = (Button) gridPane.getChildren().get(n * move.getRow() + move.getCol());
        affectedButton.setText(move.getValue().toString());
        if (!game.getGameResult().equals(GameResult.UNKNOWN)) {
            displayResult();
        }
    }

    private void displayResult() {
        switch (game.getGameResult()) {
            case WIN ->
                    resultsLabel.setText("Player " + (activePlayer == player1 ? "1" : "2") + "(" + activePlayer.getType() + ")" + " won!");
            case LOOSE ->
                    resultsLabel.setText("Player " + (activePlayer == player1 ? "1" : "2") + "(" + activePlayer.getType() + ")" + " lost!");
            case DRAW -> resultsLabel.setText("Draw!");
        }
        HBox.setMargin(resultsLabel, new Insets(50));
        VBox resultsVBox = prepareRoot(new VBox(resultsLabel, generateRestartButton()));
        HBox.setMargin(resultsVBox, new Insets(50));
        ((VBox) stage.getScene().getRoot()).getChildren().add(resultsVBox);
        stage.setHeight(stage.getHeight() + 50);
    }


    private void switchActivePlayer() {
        activePlayer = activePlayer == player1 ? player2 : player1;
    }

    private Button generateRestartButton() {
        Button restartButton = new Button("Start new game");
        restartButton.setOnAction(e -> {
            stage.setScene(dimensionsScene);
            stage.setWidth(810);
            stage.setHeight(200);
        });
        return restartButton;
    }
}
