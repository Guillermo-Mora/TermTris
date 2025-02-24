import java.util.*;

public class TermTris {
    //Variables
    private final int[] termtetrisBoard;
    private final char[] blockType;
    private final Pieces pieces;
    private final Messages messages;
    //

    //Start the program
    public void program() {
        if (!messages.menuMessage().equalsIgnoreCase("Q")) {
            messages.clearScreen();
            gameStart();
        }
    }
    //

    //Start a game
    public void gameStart() {
        fillBoard();
        buildPieces();
        if (!randomPiceInBoard()) {
            messages.gameOverMessage();
        }
        showBoard();
    }
    //

    //Fill the board
    public void fillBoard() {
        for (int i = 0; i < termtetrisBoard.length; ) {
            if (i >= termtetrisBoard.length - 12) {
                termtetrisBoard[i] = 3;
                i++;
            } else {
                for (int j = 0; j < 12; j++, i++) {
                    if (j == 0 || j == 11) {
                        termtetrisBoard[i] = 3;
                    } else {
                        termtetrisBoard[i] = 0;
                    }
                }
            }
        }
    }
    //

    //Show current board
    public void showBoard() {
        for (int i = 0; i < termtetrisBoard.length; i++) {
            if (i % 12 == 0) {
                System.out.println();
            }
            System.out.print(blockType[termtetrisBoard[i]]);
        }
    }
    //

    //Build pieces
    public void buildPieces() {
        pieces.storePieces();
    }
    //

    //Introduce random piece in the board
    public boolean randomPiceInBoard() {
        int[] randomPiece = randomPice();
        int i = 0, j = 0;
        while (j < randomPiece.length) {
            if (termtetrisBoard[i] != 3) {
                if (termtetrisBoard[i] == 2 && randomPiece[j] == 1){
                    return false;
                }
                termtetrisBoard[i] = randomPiece[j];
                i++;
                j++;
            } else {
                i++;
            }
        }
        return true;
    }
    //

    //Get random piece
    public int[] randomPice() {
        return pieces.randomPiece();
    }
    //

    //Constructor
    public TermTris(Pieces pieces, Messages messages) {
        this.termtetrisBoard = new int[252];
        this.blockType = new char[]{'□', '■', '▣', '▨'};
        this.pieces = pieces;
        this.messages = messages;
    }
    //
}
