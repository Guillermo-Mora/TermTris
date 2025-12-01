package termTris;

import java.util.ArrayList;
import java.util.Arrays;

public class Piece {

    //A partir del primer bloque, + hacia arriba + hacia adelante

    private final ArrayList<int[]> piecePositions = new ArrayList<>(Arrays.asList(
            new int[] {
                    0, 0, 0
            },
            new int[]{
                    1, 1, 1, 1,
                    2, 2
            },
            new int[]{
                    1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                    1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                    1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                    1,
                    -2, -2
            },
            new int[]{
                    1, 1, 1, 1,
                    3, 2, 1
            },
            new int[]{
                    1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                    1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                    1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                    1,
                    -2, -1
            }
    ));

    public ArrayList<int[]> getPiecePositions() {
        return piecePositions;
    }
}
