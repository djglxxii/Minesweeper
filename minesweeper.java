//********************************************************************
//  minesweeper.java       Author: original unknown
//                         David J. Gardner
//
//  Represents one die (singular of dice) with faces showing values
//  between 1 and 6 with both integer value and graphical image.
//
//  4/29/18 David J. Gardner - Implemented required methods
//********************************************************************

public class minesweeper implements GameData {

    /**
     * mine and clue values, 9 - mine, 0-8 clue values
     */
    private int[][] mines;

    /**
     * tile values 0 - open, 1 - closed,<br>
     * 2 - question, 3 - mine
     */
    private int[][] tiles;

    /**
     * Level 2 - game status win, lose, play
     */
    private String status;

    /**
     * default constructor<br>
     * board size 9 x 9<br>
     * create mines and tile arrays<br>
     * place mines<br>
     * calculate clues<br>
     * (*)set game status to play<br>
     */
    public minesweeper() {
        this(9, 9);
    }

    /**
     * alternate constructor
     * use specifies board size<br>
     * create mines and tile arrays<br>
     * place mines<br>
     * calculate clues<br>
     * (*)set game status to play<br>
     *
     * @param newRows number of rows for grid<br>
     * @param newCols number of columns for grid<br>
     */
    public minesweeper(int newRows, int newCols) {
        initGame(newRows, newCols);
    }

    /**
     * Level 2 - game status
     *
     * @return "play", "win", or "lose"
     */
    public String getStatus() {
        return status;
    }

    /**
     * number of rows for board
     *
     * @return number of rows
     */
    public int getRows() {
        return mines.length;
    }

    /**
     * number of columns for board
     *
     * @return number of columns
     */
    public int getCols() {
        return mines[0].length;
    }

    /**
     * value of the mines array at r,c<br>
     * -1 is returned if invalid r,c
     *
     * @param row row index
     * @param col column index
     * @return value of mines array, -1 if invalid
     */
    public int getMines(int row, int col) {
        return validIndex(row, col) ? mines[row][col] : -1;
    }

    /**
     * value of the tiles array at r,c
     * -1 is returned if invalid r,c<br>
     *
     * @param row row index
     * @param col column index
     * @return value of tiles array, -1 if invalid
     */
    public int getTiles(int row, int col) {
        return validIndex(row, col) ? tiles[row][col] : -1;
    }

    /**
     * mark tile - open tile, close tile, <br>
     * flag tile as mine, set tile as question mark, close tile<br>
     * <br>
     * Level 1 - Requirements<br>
     * - invalid row,c values must be ignored<br>
     * - a tile that is opened must stay open<br>
     * - a tile that is marked as a flag (ie. tile[][] value 3) can not be opened<br>
     * <br>
     * Level 2 - Requirements<br>
     * - tile values can only change when game status is "play"<br>
     * - game status must be updated after a tile is opened<br>
     * <br>
     *
     * @param row  row index<br>
     * @param col  column index<br>
     * @param tile 0 - open, 1 - close, 2 - question, 3 - flag<br>
     */
    public void markTile(int row, int col, int tile) {
        if (validIndex(row, col)) {
            int currentTileValue = this.tiles[row][col];

            if (this.status.equals(Constants.PLAY)) {
                if (tile == Constants.OPENED) {
                    if (currentTileValue != Constants.OPENED && currentTileValue != Constants.FLAG) {
                        this.tiles[row][col] = Constants.OPENED;
                        if (gameWon()) {
                            this.status = Constants.WIN;
                        } else if (this.mines[row][col] == Constants.MINE) {
                            this.status = Constants.LOSE;
                        } else if (this.mines[row][col] == Constants.OPENED) {
                            // recursively mark the tiles
                            markTile(row - 1, col, Constants.OPENED); // n
                            markTile(row - 1, col + 1, Constants.OPENED); // ne
                            markTile(row, col + 1, Constants.OPENED); // e
                            markTile(row + 1, col + 1, Constants.OPENED); // se
                            markTile(row + 1, col, Constants.OPENED); // s
                            markTile(row - 1, col - 1, Constants.OPENED); // sw
                            markTile(row, col - 1, Constants.OPENED); // w
                            markTile(row + 1, col - 1, Constants.OPENED); // nw
                        }
                    }
                } else if (currentTileValue != Constants.OPENED) {
                    this.tiles[row][col] = tile;
                }
            }
        }

        checkGameStatus();
    }

    /*
    Checks the game status by inspecting the arrays.
     */
    private void checkGameStatus() {

        // if game status is set to 'PLAY', we will verify
        // that the game is still in a playable state by
        // inspecting the array to confirm there are still
        // closed/question tiles.
        if (this.status.equals(Constants.PLAY)) {
            boolean allTilesOpened = true;

            for (int row = 0; row < this.tiles.length; row++) {
                for (int col = 0; col < this.tiles[row].length; col++) {
                    int tile = getTiles(row, col);

                    if (tile == Constants.CLOSED || tile == Constants.QUESTION) {
                        allTilesOpened = false;
                    }
                }
            }

            // all tiles have been opened, so the game is not in a playable
            // state and needs to be set to an end-game status.
            if (allTilesOpened) {
                boolean hasWon = true;

                for (int row = 0; row < this.tiles.length; row++) {
                    for (int col = 0; col < this.tiles[row].length; col++) {
                        int tile = getTiles(row, col);
                        int mine = getMines(row, col);

                        // if player Flagged a tile that wasn't a mine then they lose.
                        if (tile == Constants.FLAG && mine != Constants.MINE) {
                            hasWon = false;
                        }
                    }
                }

                this.status = hasWon ? Constants.WIN : Constants.LOSE;
            }
        }
    }

    /**
     * mines array as String
     *
     * @return mines array as a String
     */
    public String toStringMines() {
        String result = Constants.LINEFEED;

        for (int row = 0; row < mines.length; row++) {
            for (int col = 0; col < mines[row].length; col++) {
                result = result + mines[row][col];
            }

            result += Constants.LINEFEED;
        }

        return result;
    }

    /**
     * tiles array as String
     *
     * @return mines array as a String
     */
    public String toStringTiles() {
        String result = Constants.LINEFEED;

        for (int row = 0; row < mines.length; row++) {
            for (int col = 0; col < mines[row].length; col++) {
                result = result + tiles[row][col];
            }

            result += Constants.LINEFEED;
        }

        return result;
    }

    /**
     * game board array as String
     *
     * @return game board as String
     */
    public String toStringBoard() {
        String result = "";

        for (int row = 0; row < tiles.length; row++) {

            for (int col = 0; col < tiles[row].length; col++) {
                result += this.getBoard(row, col);
            }

            result += Constants.LINEFEED; //advance to next line
        }

        return result;
    }

    /**
     * getBoard - determines current game board character for row,col position <br>
     * using the value of the mines[][] and tiles[][]array<br>
     * Note:  Level 2 values are returned when <br>
     * game is over (ie. status is "win" or "lose")<br>
     * <br><br>
     * Level 1 values<br>
     * '1'-'8'  opened tile showing clue value<br>
     * ' '      opened tile blank<br>
     * 'X'      tile closed<br>
     * '?'      tile closed marked with ?<br>
     * 'F'      tile closed marked with flag<br>
     * '*'      mine<br>
     * <br><br>
     * Level 2 values<br>
     * '-'      if game lost, mine that was incorrectly flagged<br>
     * '!'      if game lost, mine that ended game<br>
     * 'F'      if game won, all mines returned with F
     * <br>
     *
     * @return char representing game board at row,col
     */
    public char getBoard(int row, int col) {
        char chr = ' ';

        int tile = this.tiles[row][col];
        int mine = this.mines[row][col];

        if (tile == Constants.OPENED) {
            if (mine == Constants.OPENED) {
                chr = ' ';
            } else {
                chr = Integer.toString(mine).charAt(0);
            }
        } else if (tile == Constants.CLOSED) {
            chr = 'X';
        } else if (tile == Constants.QUESTION) {
            chr = '?';
        } else if (tile == Constants.FLAG) {
            chr = 'F';
        }

        if (!this.status.equals(Constants.PLAY)) {
            if (this.status.equals(Constants.WIN)) {
                if (mine == Constants.FLAG) {
                    chr = 'F';
                }
            } else if (mine == Constants.MINE && tile == Constants.OPENED) {
                chr = '!';
            } else if (mine == Constants.MINE) {
                chr = '*';
            } else if (tile == Constants.FLAG) {
                chr = '-';
            }
        }

        return chr;
    }

    /**
     * create mines & tiles array
     * place mines<br>
     * update clues<br>
     *
     * @param newRows number of rows for grid
     * @param newCols number of columns for grid
     */
    private void initGame(int newRows, int newCols) {
        //allocate space for mines and tiles array
        if (newRows >= 1 && newCols >= 1) {
            mines = new int[newRows][newCols];
            tiles = new int[newRows][newCols];

            //init tiles array
            resetTiles();

            //place mines
            placeMines();

            //update clues
            calculateClues();

            //set game status
            status = Constants.PLAY;
        }
    }

    /**
     * Sets all tiles to 1 - closed
     */
    private void resetTiles() {
        for (int col = 0; col < this.tiles.length; col++) {
            for (int row = 0; row < this.tiles[col].length; row++) {
                this.tiles[col][row] = Constants.CLOSED;
            }
        }
    }

    /**
     * places mines randomly on grid
     * integer value 9 represents a mine<br>
     * number of mines = (1 + number of columns * number rows) / 10<br>
     * minimum number of mines = 1<br>
     */
    private void placeMines() {
        int rowTotal = this.mines.length;
        int colTotal = this.mines[0].length;

        int minesToPlace = rowTotal * colTotal / 10 + 1;

        // in case our division gives us 0 or less.
        // there is always one mine.
        if (minesToPlace < 1) {
            minesToPlace = 1;
        }

        int counter = 0;

        while (counter < minesToPlace) {

            int randomRow = (int) (Math.random() * rowTotal);
            int randomCol = (int) (Math.random() * colTotal);

            if (this.mines[randomRow][randomCol] != Constants.MINE) {
                this.mines[randomRow][randomCol] = Constants.MINE;
                counter++;
            }
        }
    }

    /**
     * calculates clue values and updates
     * clue values in mines array<br>
     * integer value 9 represents a mine<br>
     * clue values will be 0 ... 8<br>
     */
    private void calculateClues() {
        for (int row = 0; row < mines.length; row++) {

            for (int col = 0; col < mines[row].length; col++) {

                // if the current row+col is itself a mine, then skip
                // this iteration.
                if (mines[row][col] == Constants.MINE) {
                    continue;
                }

                int clue = 0;

                // we will examine the perimeter of the current row+col
                // to detect if mines exist.  as a mine is discovered,
                // increment the clue value.
                for (int y : new int[]{-1, 0, 1}) {
                    for (int x : new int[]{-1, 0, 1}) {

                        // if y and x are 0 then this is actually the
                        // current cell.  skip it.
                        if (y == 0 && x == 0) {
                            continue;
                        }

                        int thisRow = row + y;
                        int thisCol = col + x;

                        // skip iteration if we're outside the bounds of the array.
                        if (!validIndex(thisRow, thisCol)) {
                            continue;
                        }

                        // increment clue if this mine array element is a mine.
                        if (mines[thisRow][thisCol] == Constants.MINE) {
                            clue++;
                        }
                    }
                }

                // assign the clue value.
                mines[row][col] = clue;
            }
        }
    }

    /**
     * determines if row,col is valid position
     *
     * @param row row index
     * @param col column index
     * @return true if valid position on board,
     * false if not valid board position
     */
    public boolean validIndex(int row, int col) {
        boolean isValid;

        try {
            int test = this.mines[row][col];
            isValid = true;
        } catch (ArrayIndexOutOfBoundsException ex) {
            isValid = false;
        }

        return isValid;
    }

    /**
     * Level 2 - game won status
     *
     * @return true if game won
     * false if game not won
     */
    private boolean gameWon() {
        boolean hasWon = true;

        for (int[] row : this.tiles) {
            for (int col : row) {
                if (col != Constants.OPENED && col != Constants.MINE) {
                    hasWon = false;
                }
            }
        }

        return hasWon;
    }

    public String toString() {
        return this.toStringBoard();
    }
}