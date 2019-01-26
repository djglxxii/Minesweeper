//********************************************************************
//  TileButton.java
//
//  Author: David J. Gardner
//  Date: 4/29/18
//
//  TileButton extends JavaFX Button
//  represents a Minesweeper grid Tile that is clickable.
//********************************************************************

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class TileButton extends Button {
    // coordinates of this tile button.
    private int col, row;
    // interface to the game data.
    private GameData gameData;
    // callback for when tile is marked.
    private EventHandler<ActionEvent> onTileMarked;

    //-----------------------------------------------------------------
    //  Constructor.  Requires col, row, and gameData interface.
    //-----------------------------------------------------------------
    public TileButton(int col, int row, GameData gameData) {
        this.col = col;
        this.row = row;
        this.gameData = gameData;

        this.setOnMouseClicked(this::handleOnMouseClicked);
    }

    //-----------------------------------------------------------------
    //  OnTileMarked EventHandler.
    //-----------------------------------------------------------------
    public void setOnTileMarked(EventHandler<ActionEvent> value) {
        this.onTileMarked = value;
    }

    //-----------------------------------------------------------------
    //  Refresh the tile's display.
    //-----------------------------------------------------------------
    public void refresh() {
        int tile = getTile();
        int mine = getMine();
        String status = getStatus();

        String url = null;

        if (status.equals(Constants.PLAY)) {

            switch (tile) {
                case Constants.OPENED:
                    url = mine == 0
                            ? "open.png"
                            : Integer.toString(mine).substring(0, 1) + ".png";
                    break;
                case Constants.CLOSED:
                    url = null; // no graphic
                    break;
                case Constants.QUESTION:
                    url = "question.png";
                    break;
                case Constants.FLAG:
                    url = "flag.png";
                    break;
            }
        }

        if (status.equals(Constants.LOSE)) {
            this.setDisable(true);

            if (tile == Constants.OPENED && mine == Constants.MINE) {
                url = "minered.png";
            } else if (tile > Constants.OPENED && mine == Constants.MINE) {
                url = "mine.png";
            } else if (tile == Constants.FLAG && mine < Constants.MINE) {
                url = "minex.png"; // user marked this as a mine but wasn't!
            }
        }

        if (status.equals(Constants.WIN)) {
            url = "smile.png";
        }

        if (url != null) {
            this.getChildren().clear();

            Image img = new Image(url);
            ImageView iv = new ImageView(img);
            this.getChildren().addAll(iv);
        }
    }

    //-----------------------------------------------------------------
    //  Tile accessor.
    //-----------------------------------------------------------------
    private int getTile() {
        return this.gameData.getTiles(this.row, this.col);
    }

    //-----------------------------------------------------------------
    //  Mine accessor.
    //-----------------------------------------------------------------
    private int getMine() {
        return this.gameData.getMines(this.row, this.col);
    }

    //-----------------------------------------------------------------
    //  Game status accessor.
    //-----------------------------------------------------------------
    private String getStatus() {
        return this.gameData.getStatus();
    }

    //-----------------------------------------------------------------
    //  Mouse click handler.
    //-----------------------------------------------------------------
    private void handleOnMouseClicked(MouseEvent event) {
        MouseButton button = event.getButton();

        if (button.equals(MouseButton.PRIMARY)) {
            this.gameData.markTile(this.row, this.col, Constants.OPENED);
        }

        if (button.equals(MouseButton.MIDDLE)) {
            this.gameData.markTile(this.row, this.col, Constants.QUESTION);
        }

        if (button.equals(MouseButton.SECONDARY)) {
            this.gameData.markTile(this.row, this.col, Constants.FLAG);
        }

        ActionEvent e = new ActionEvent(this, null);
        this.onTileMarked.handle(e);
    }

    //-----------------------------------------------------------------
    //  Return a string representation of this tile button's state.
    //-----------------------------------------------------------------
    public String toString() {
        String result = "This tile button's coordinates are ";
        result += "(" + this.col + "," + this.row + ")";
        result += Constants.LINEFEED;
        result += "Tile value: " + getTile();
        result += " Mine value: " + getMine();

        return result;
    }
}