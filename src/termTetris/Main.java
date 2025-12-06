package termTetris;

public class Main {
    public static void main(String[] args) {
        Pieces pieces = Pieces.getInstance();
        Messages messages = new Messages();
        TermTetris program = new TermTetris(pieces, messages);
        program.start();
    }
}