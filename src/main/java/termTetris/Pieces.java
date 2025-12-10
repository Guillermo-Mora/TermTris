package termTetris;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Pieces{
    //How coordinates work in the tetris board
    //+Y = down -> +12
    //-Y = up -> -12
    //+X = right -> +1
    //-X = left -> -1
    // First int[] of every piece -> initial spaces when droped in
    // Last 2 digits of every int[] of every piece rotation exlcuding first int[] ->
    // Y and X Coordinates of the next rotation piece
    private final ArrayList<List<int[]>> piecesTermTetris = new ArrayList<>(
            List.of(
                    Arrays.asList(
                            new int[]{0, 0, 0},
                            new int[]{
                                    1, 1, 1, 1,
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
                    ),
                    Arrays.asList(
                            new int[]{0, 0, 0},
                            new int[]{
                                    1, 1, 1, 0, 0, 0, 0,
                                    0, 0, 0, 0, 1,
                                    -1, 1

                            },
                            new int[]{
                                    1, 0, 0, 0, 0, 0, 0, 0, 0,
                                    1, 1, 0, 0, 0 ,0 ,0 ,0, 0, 0,
                                    0, 1,
                                    1, 0
                            },
                            new int[]{
                                    1, 0, 0, 0, 0, 0, 0, 0, 0,
                                    1, 1, 1,
                                    -1, 0
                            },
                            new int[]{
                                    1, 0, 0, 0, 0, 0, 0, 0, 0,
                                    0, 1, 1, 0, 0 ,0, 0, 0, 0, 0,
                                    0, 1,
                                    1, -1
                            }
                    ),
                    Arrays.asList(
                            new int[]{0, 0, 0},
                            new int[]{
                                    1, 1, 1, 0, 0, 0, 0,
                                    0, 0, 0, 1,
                                    -1, 0
                            },
                            new int[]{
                                    1, 1, 0, 0, 0, 0, 0, 0, 0, 0,
                                    0, 1, 0, 0, 0, 0, 0, 0, 0, 0,
                                    0, 1,
                                    1, 2
                            },
                            new int[]{
                                    1, 0, 0, 0, 0, 0, 0, 0,
                                    1, 1, 1,
                                    -1, -1
                            },
                            new int[]{
                                    1, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                                    1, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                                    1, 1,
                                    1, -1
                            }
                    ),
                    Arrays.asList(
                            new int[]{0, 0, 0},
                            new int[]{
                                    1, 1, 1, 0, 0, 0, 0,
                                    0, 0, 0, 0, 0, 1,
                                    -1, 1
                            },
                            new int[]{
                                    1, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                                    1, 0, 0, 0, 0, 0, 0, 0, 0,
                                    1, 1,
                                    1, -1

                            },
                            new int[]{
                                    1, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                                    1, 1, 1,
                                    -1, 1
                            },
                            new int[]{
                                    1, 1, 0, 0, 0, 0, 0, 0, 0, 0,
                                    1, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                                    1,
                                    1, -1
                            }
                    ),
                    Arrays.asList(
                            new int[]{0, 0, 0, 0},
                            new int[]{
                                    1, 1, 0, 0, 0, 0,
                                    0, 0, 0, 1, 1,
                                    -1, 0
                            },
                            new int[]{
                                    1, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                                    1, 1, 0, 0, 0, 0, 0, 0, 0, 0,
                                    0, 1,
                                    1, 0
                            },
                            new int[]{
                                    1, 1, 0, 0, 0, 0, 0, 0, 0,
                                    1, 1,
                                    -1, -1
                            },
                            new int[]{
                                    1, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                                    1, 1, 0, 0, 0, 0, 0, 0, 0, 0,
                                    0, 1,
                                    1, 1
                            }
                    ),
                    Arrays.asList(
                            new int[]{0, 0, 0},
                            new int[]{
                                    1, 1, 0, 0, 0, 0, 0,
                                    0, 0, 0, 0, 1, 1,
                                    -1, 2
                            },
                            new int[]{
                                    1, 0, 0, 0, 0, 0, 0, 0, 0,
                                    1, 1, 0, 0, 0, 0, 0, 0, 0, 0,
                                    1,
                                    1, -2
                            },
                            new int[]{
                                    1, 1, 0, 0, 0, 0, 0, 0, 0, 0,
                                    0, 1, 1,
                                    -1, 1
                            },
                            new int[]{
                                    1, 0, 0, 0, 0, 0, 0, 0, 0,
                                    1, 1, 0, 0, 0, 0, 0, 0, 0, 0,
                                    1,
                                    1, -1
                            }
                    ),
                    Arrays.asList(
                            new int[]{0, 0, 0, 0},
                            new int[]{
                                    1, 1, 0, 0, 0, 0,
                                    0, 0, 0, 0, 1, 1,
                                    0, 0
                            },
                            new int[]{
                                    1, 1, 0, 0, 0, 0, 0, 0, 0, 0,
                                    1, 1,
                                    0, 0
                            },
                            new int[]{
                                    1, 1, 0, 0, 0, 0, 0, 0, 0, 0,
                                    1, 1,
                                    0, 0
                            },
                            new int[]{
                                    1, 1, 0, 0, 0, 0, 0, 0, 0, 0,
                                    1, 1,
                                    0, 0
                            }
                    )
            )
    );
    private static Pieces pieces_instance = null;

    public List<int[]> randomPiece() {
        return piecesTermTetris.get((int) (Math.random() * piecesTermTetris.size()));
    }
    private Pieces() {}
    public synchronized static Pieces getInstance() {
        if (pieces_instance == null) pieces_instance = new Pieces();
        return pieces_instance;
    }
}
