//********************************************************************
//  minesweeperTest.java
//
//  Author: David J. Gardner
//  Date: 5/2/18
//
//  Unit testing class for minesweeper.java
//********************************************************************

public class minesweeperTest {

    // system under test (sut)
    private static minesweeper game;

    //-----------------------------------------------------------------
    //  Application entry method.
    //-----------------------------------------------------------------
    public static void main(String[] args) {

        System.out.println("*** Testing started ***");
        System.out.println();

        // ---------------------------------------------------------------------------------------------------
        resetSut();

        System.out.println("Confirming game status is set correctly...");
        System.out.println("\tGetting system status, expecting: play, got: " + game.getStatus());
        System.out.println();

        game.markTile(0, 0, Constants.OPENED);
        System.out.println("Confirming tile can be opened...");
        System.out.println("\tMarked tile at 0,0 as OPENED (0), expecting 0, got: " + game.getTiles(0, 0));

        System.out.println("Confirming opened tile cannot then be closed...");
        game.markTile(0, 0, Constants.CLOSED);
        System.out.println("\tMarked OPENED tile at 0,0 as CLOSED (1), expecting 0, got: " + game.getTiles(0, 0));

        System.out.println();
        // ---------------------------------------------------------------------------------------------------


        // ---------------------------------------------------------------------------------------------------
        resetSut();

        game.markTile(0, 2, Constants.QUESTION);
        System.out.println("Confirming tile can be question-marked...");
        System.out.println("\tMarked tile at 0,2 as QUESTION (2), expecting 2, got: " + game.getTiles(0, 2));

        System.out.println();
        // ---------------------------------------------------------------------------------------------------


        // ---------------------------------------------------------------------------------------------------
        resetSut();

        // confirm tile can be mark as flag
        System.out.println("Confirming tile can be flagged...");
        game.markTile(0, 3, Constants.FLAG);
        System.out.println("\tMarked tile at 0,3 as FLAG (3), expecting 3, got: " + game.getTiles(0, 3));

        System.out.println("Confirming flagged tile cannot then be opened...");
        game.markTile(0, 3, Constants.OPENED);
        System.out.println("\tMarked FLAGGED tile at 0,3 as OPENED (0), expecting 3, got: " + game.getTiles(0, 3));

        System.out.println("Confirming flagged tile can be question-marked...");
        game.markTile(0, 3, Constants.QUESTION);
        System.out.println("\tMarked tile at 0,3 as QUESTION (2), expecting 2, got: " + game.getTiles(0, 3));

        System.out.println("Confirming question-marked tile can then be opened...");
        game.markTile(0, 3, Constants.OPENED);
        System.out.println("\tMarked tile at 0,3 as OPENED (0), expecting 0, got: " + game.getTiles(0, 3));

        System.out.println();
        // ---------------------------------------------------------------------------------------------------

        // ---------------------------------------------------------------------------------------------------
        resetSut();

        System.out.println("Confirm out of out-of-bounds access does not throw exception...");
        int invalidRow = game.getRows() + 1;
        int invalidCol = game.getCols() + 1;
        System.out.println("\tMarking invalid tile opened at (" + invalidRow + "," + invalidCol + ")");
        try {
            game.markTile(invalidRow, invalidCol, Constants.OPENED);
            System.out.println("\t ... expected no exception, got: no exception.");

        } catch (ArrayIndexOutOfBoundsException ex) {
            System.out.println("\t ... expected no exception, got: " + ex.getMessage());
        }

        int mineCount = 0;
        for (int row = 0; row < game.getRows(); row++) {
            for (int col = 0; col < game.getCols(); col++) {
                if (game.getMines(row, col) == Constants.MINE) {
                    mineCount++;
                }
            }
        }
        System.out.println();
        // ---------------------------------------------------------------------------------------------------

        // ---------------------------------------------------------------------------------------------------
        resetSut();

        System.out.println("\rCounted mines, expecting 9 , got: " + mineCount);
        System.out.println();

        int mineAtRow = 0, mineAtCol = 0;
        for (int row = 0; row < game.getRows(); row++) {
            for (int col = 0; col < game.getCols(); col++) {
                if (game.getMines(row, col) == Constants.MINE) {
                    mineAtRow = row;
                    mineAtCol = col;
                    break;
                }
            }
        }

        game.markTile(mineAtRow, mineAtCol, Constants.OPENED);
        System.out.println("Marked tile that contains mine as OPEN (0), expecting gameStatus to be lose, got: "
                + game.getStatus());

        System.out.println();

        int closedAtRow = 0, closedAtCol = 0;
        for (int row = 0; row < game.getRows(); row++) {
            for (int col = 0; col < game.getCols(); col++) {
                if (game.getTiles(row, col) == Constants.CLOSED) {
                    closedAtRow = row;
                    closedAtCol = col;
                    break;
                }
            }
        }
        game.markTile(closedAtRow, closedAtCol, Constants.OPENED);
        System.out.println("Game is over, marked a CLOSED (1) tile as OPEN (0), expecting 1, got: "
                + game.getTiles(closedAtRow, closedAtCol));

        System.out.println();

        System.out.println("Calling toStringMines():");
        System.out.println(game.toStringMines());

        System.out.println("Calling toStringTiles():");
        System.out.println(game.toStringTiles());

        System.out.println("Calling toStringBoard():");
        System.out.println(game.toStringBoard());

        System.out.println("*** Testing completed ***");

        System.exit(0);
    }

    //-----------------------------------------------------------------
    //  create new instance of minesweeper game
    //-----------------------------------------------------------------
    private static void resetSut() {
        game = new minesweeper(9, 9);
    }

}//minesweeperTest