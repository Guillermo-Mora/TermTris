import java.util.*;

public class TermTris {
    //Variables
    private final int[] termtetrisBoard;
    private final char[] blockType;
    private Pieces pieces;
    //

    //Start the program
    public void program() {
        fillBoard();
        showBoard();
    }
    //

    //Fill the board
    public void fillBoard() {
        for (int i = 0; i < termtetrisBoard.length; ) {
            if (i >= termtetrisBoard.length - 12) {
                termtetrisBoard[i] = 2;
                i++;
            } else {
                for (int j = 0; j < 12; j++, i++) {
                    if (j == 0 || j == 11) {
                        termtetrisBoard[i] = 2;
                    } else {
                        termtetrisBoard[i] = 0;
                    }
                }
            }
        }
    }
    //

    public void showBoard() {
        for (int i = 0; i < termtetrisBoard.length; i++) {
            if (i % 12 == 0) {
                System.out.println();
            }
            System.out.print(blockType[termtetrisBoard[i]]);
        }
    }


    //Constructor
    public TermTris() {
        this.termtetrisBoard = new int[252];
        this.blockType = new char[]{'□', '■', '▨'};
        this.pieces = new Pieces();
    }
    //

    //Getters && Setters
    public int[] getTermtetrisBoard() {
        return termtetrisBoard;
    }

    public char[] getBlockType() {
        return blockType;
    }

    public Pieces getPieces() {
        return pieces;
    }

    public void setPieces(Pieces pieces) {
        this.pieces = pieces;
    }
    //

}
