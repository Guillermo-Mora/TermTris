import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class TermTris {
    //Variables
    private final int[] termtetrisBoard;
    private final char[] blockType;
    private final Pieces pieces;
    private final Messages messages;

    //Start the program
    public void start() {
        if (!messages.menuMessage().equalsIgnoreCase("Q")) {
            messages.clearScreen();
            gameStart();
        }
    }

    //Start a game
    public void gameStart() {
        fillBoard();
        buildPieces();
        if (!randomPiceInBoard()) {
            messages.gameOverMessage();
        }
        showBoard();

        boolean pieceContinueFall = true;
        do {
            if (!movePieceDown()) {
                pieceContinueFall = false;
                transformPieceToStatic();
            }
            showBoard();
        } while (pieceContinueFall);
    }

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

    //Show current board
    public void showBoard() {
        for (int i = 0; i < termtetrisBoard.length; i++) {
            if (i % 12 == 0) {
                System.out.println();
            }
            System.out.print(blockType[termtetrisBoard[i]]);
        }
    }

    //Build pieces
    public void buildPieces() {
        pieces.storePieces();
    }


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

    public boolean movePieceDown() {
        ArrayList<Integer> oldPositions = new ArrayList<>();
        ArrayList<Integer> newPositions = new ArrayList<>();

        for (int i = 0; i < termtetrisBoard.length; i++) {
            if (termtetrisBoard[i] == 1) {
                oldPositions.add(i);

                if (termtetrisBoard[i + 12] == 2 || termtetrisBoard[i + 12] == 3) {
                    return false;
                }

                newPositions.add(i + 12);
            }
        }

        for (int i = 0; i < oldPositions.size(); i++) {
            boolean dontOverride = false;
            for (Integer newPosition : newPositions) {
                if (Objects.equals(oldPositions.get(i), newPosition)) {
                    dontOverride = true;
                    break;
                }
            }
            if (!dontOverride) termtetrisBoard[oldPositions.get(i)] = 0;
            termtetrisBoard[newPositions.get(i)] = 1;
        }
        return true;
    }

    public boolean movePieceHorizontal(boolean isRightDirection) {
        int direction;

        if (isRightDirection) direction = 1;
        else direction = -1;

        ArrayList<Integer> oldPositions = new ArrayList<>();
        ArrayList<Integer> newPositions = new ArrayList<>();

        for (int i = 0; i < termtetrisBoard.length; i++) {
            if (termtetrisBoard[i] == 1) {
                oldPositions.add(i);

                if (termtetrisBoard[i + direction] == 2 || termtetrisBoard[i + direction] == 3) {
                    return false;
                }

                newPositions.add(i + direction);
            }
        }

        for (int i = 0; i < oldPositions.size(); i++) {
            boolean dontOverride = false;
            for (Integer newPosition : newPositions) {
                if (Objects.equals(oldPositions.get(i), newPosition)) {
                    dontOverride = true;
                    break;
                }
            }
            if (!dontOverride) termtetrisBoard[oldPositions.get(i)] = 0;
            termtetrisBoard[newPositions.get(i)] = 1;
        }
        return true;
    }

    public void transformPieceToStatic() {
        for (int i = 0; i < termtetrisBoard.length; i++) {
            if (termtetrisBoard[i] == 1) termtetrisBoard[i] = 2;
        }
    }

    //Get random piece
    public int[] randomPice() {
        return pieces.randomPiece();
    }

    //Constructor
    public TermTris(Pieces pieces, Messages messages) {
        this.termtetrisBoard = new int[252];
        this.blockType = new char[]{'□', '■', '▣', '▨'};
        this.pieces = pieces;
        this.messages = messages;
    }
}
