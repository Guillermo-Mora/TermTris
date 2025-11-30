package termTris;

import java.util.*;

public class Pieces {
    private final HashMap<Integer, int[]> pieces;

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
}
