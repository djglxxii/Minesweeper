//********************************************************************
//  MinesweeperFX.java
//
//  Author: David J. Gardner
//  Date: 4/22/18
//
//  Game GUI driver for the minesweeper game.
//********************************************************************

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class MinesweeperFX {
    // the minesweeper game. reference to this object cannot be changed (final).
    private final minesweeper minesweeper;

    // timeout duration in minutes
    private int timeout;
    // timer object
    private Timeline timer;

    // text field for game status, game instructions, and game timer
    private Text gameStatusText, instructionsText, timerText;

    // the game's grid that represents the array.
    private GridPane grid;

    //-----------------------------------------------------------------
    //  Constructor.  Accepts the minesweeper game.
    //-----------------------------------------------------------------
    public MinesweeperFX(minesweeper minesweeper, int timeout) {
        this.minesweeper = minesweeper;
        this.timeout = timeout;

        // initialize the various UI controls...
        this.gameStatusText = getText(25);
        this.instructionsText = getInstructionsText();
        this.timerText = getText(15);

        this.grid = getGrid();

        int cols = minesweeper.getCols();
        int rows = minesweeper.getRows();

        for (int col = 0; col < cols; col++) {
            for (int row = 0; row < rows; row++) {
                TileButton tb = getTileButton(col, row, minesweeper);
                this.grid.add(tb, col, row);
            }
        }

        this.timer = new Timeline();
        timer.setCycleCount(Timeline.INDEFINITE);
        // timer will call refreshTimer every minute.
        KeyFrame frame = new KeyFrame(Duration.minutes(1), this::refreshTimer);
        timer.getKeyFrames().addAll(frame);
        timer.playFromStart();

        // draw the display
        refresh();
    }

    //-----------------------------------------------------------------
    //  GameUI accessor.  Returns JavaFX Parent object.
    //-----------------------------------------------------------------
    public Parent getGameUI() {
        VBox vbox = new VBox();
        vbox.setAlignment(Pos.TOP_CENTER);

        vbox.getChildren().addAll(
                this.instructionsText,
                this.gameStatusText,
                this.grid,
                this.timerText
        );

        return vbox;
    }

    //-----------------------------------------------------------------
    //  Refresh the game's display.
    //-----------------------------------------------------------------
    private void refresh() {
        String status = getStatus();

        Color playColor = Color.GREEN;
        Color winColor = Color.BLUE;
        Color loseColor = Color.DARKRED;

        this.timerText.setText(
                "Time's up in " + this.timeout
                        + (this.timeout == 1 ? " minute..." : " minutes...")
        );

        if (status.equals(Constants.PLAY)) {
            this.gameStatusText.setFill(playColor);
            this.gameStatusText.setText("GAME IN PROGRESS");
        }

        if (status.equals(Constants.LOSE)) {
            this.gameStatusText.setFill(loseColor);
            this.gameStatusText.setText("YOU HAVE LOST");

            endGame();
        }

        if (status.equals(Constants.WIN)) {
            this.gameStatusText.setFill(winColor);
            this.gameStatusText.setText("YOU HAVE WON!");

            endGame();
        }

        // notify all tiles to refresh their state.
        for (Node node : this.grid.getChildren()) {
            ((TileButton) node).refresh();
        }
    }

    //-----------------------------------------------------------------
    //  Refresh the game's timeout.
    //-----------------------------------------------------------------
    private void refreshTimer(ActionEvent event) {
        this.timeout--;

        if (this.timeout < 0) {
            this.timeout = 0;
        }

        refresh();
    }

    //-----------------------------------------------------------------
    //  Get the game status.
    //-----------------------------------------------------------------
    private String getStatus() {
        return this.timeout <= 0 ? Constants.LOSE : this.minesweeper.getStatus();
    }

    //-----------------------------------------------------------------
    //  End the game by stopping the timer and disabling game grid.
    //-----------------------------------------------------------------
    private void endGame() {
        this.timer.stop();
        this.grid.setDisable(true);
    }

    //-----------------------------------------------------------------
    //  Create and return new JavaFX GridPane
    //-----------------------------------------------------------------
    private GridPane getGrid() {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(5));
        grid.setHgap(2);
        grid.setVgap(2);
        grid.setAlignment(Pos.CENTER);

        return grid;
    }

    //-----------------------------------------------------------------
    //  Create and return new JavaFX TileButton
    //-----------------------------------------------------------------
    private TileButton getTileButton(int col, int row, GameData gameData) {
        final int SIZE = 32; // matches image dimensions

        TileButton tb = new TileButton(col, row, gameData);
        tb.setPrefSize(SIZE, SIZE);
        tb.setOnTileMarked(event -> refresh());

        return tb;
    }

    //-----------------------------------------------------------------
    //  Create instructions text control
    //-----------------------------------------------------------------
    private Text getInstructionsText() {
        Text text = getText(12);
        text.setText(
                "Left click - OPEN          Right click - FLAG          Middle click - QUESTION"
        );

        return text;
    }

    //-----------------------------------------------------------------
    //  Create and return new JavaFX Text with the specified font size
    //-----------------------------------------------------------------
    private Text getText(int fontSize) {
        Font font = Font.font("Arial", fontSize);

        Text text = new Text();
        text.setFont(font);

        return text;
    }

    //-----------------------------------------------------------------
    //  Returns a string representation of the minesweeper game.
    //-----------------------------------------------------------------
    public String toString() {
        return this.minesweeper.toString();
    }
}