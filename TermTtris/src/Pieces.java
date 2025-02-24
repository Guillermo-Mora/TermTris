import java.util.*;

public class Pieces {
    //Variables
    private final int[] blockPiece;
    private final int[] tShapedPiece;
    private final int[] lShapedPiece;
    private final int[] reverseLshapedPiece;
    private final int[] straightPiece;
    private final int[] leftZigZagPiece;
    private final int[] rightZigZagPiece;
    //

    //Constructor
    public Pieces() {
        this.blockPiece = new int[]{1,1,0,0,0,0,0,0,0,0,1,1};
        this.tShapedPiece = new int[]{1,1,1,0,0,0,0,0,0,0,0,1};
        this.lShapedPiece = new int[]{1,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,1,1};
        this.reverseLshapedPiece = new int[]{0,1,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,1,1};
        this.straightPiece = new int[]{1,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0};
        this.leftZigZagPiece = new int[]{1,1,0,0,0,0,0,0,0,0,0,1,1};
        this.rightZigZagPiece = new int[]{0,1,1,0,0,0,0,0,0,0,1,1};
    }
    //

    //Getters && Setters
    public int[] getBlockPiece() {
        return blockPiece;
    }

    public int[] gettShapedPiece() {
        return tShapedPiece;
    }

    public int[] getlShapedPiece() {
        return lShapedPiece;
    }

    public int[] getReverseLshapedPiece() {
        return reverseLshapedPiece;
    }

    public int[] getStraightPiece() {
        return straightPiece;
    }

    public int[] getLeftZigZagPiece() {
        return leftZigZagPiece;
    }

    public int[] getRightZigZagPiece() {
        return rightZigZagPiece;
    }
    //
}
