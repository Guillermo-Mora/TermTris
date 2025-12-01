package termTris;

import java.util.*;

public class Pieces {
    private final HashMap<Integer, int[]> pieces;
    private final ArrayList<int[]> piecePositions = new ArrayList<>(Arrays.asList(
            //How coordinates work in the tetris board
            //+Y = down -> +12
            //-Y = up -> -12
            //+X = right -> +1
            //-X = left -> -1

            //Piece initial spaces when droped in
            new int[] {
                    0, 0, 0
            },
            new int[]{
                    1, 1, 1, 1,
                    //Y position and X position (Coordinates)
                    -2, 2
            },
            new int[]{
                    1, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                    1, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                    1, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                    1,
                    2, -2
            },
            new int[]{
                    1, 1, 1, 1,
                    -2, 1
            },
            new int[]{
                    1, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                    1, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                    1, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                    1,
                    2, -1
            }
    ));

    public void storePieces() {
        pieces.put(0, new int[]{0, 0, 0, 1, 1, 1, 1});
        pieces.put(1, new int[]{0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1});
        pieces.put(2, new int[]{0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 1});
        pieces.put(3, new int[]{0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1});
        pieces.put(4, new int[]{0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 1, 1});
        pieces.put(5, new int[]{0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1});
        pieces.put(6, new int[]{0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1});
    }

    public int[] randomPiece() {
        return pieces.get((int) (Math.random() * pieces.size()));
    }
    //

    //Constructor
    public Pieces() {
        this.pieces = new HashMap<>();
    }

    public ArrayList<int[]> getPiecePositions() {
        return piecePositions;
    }
}
