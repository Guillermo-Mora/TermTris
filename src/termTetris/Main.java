package termTetris;

public class Main {
    static void main() {
        Pieces pieces = Pieces.getInstance();
        Messages messages = new Messages();
        TermTetris program = new TermTetris(pieces, messages);
        program.start();
    }
}