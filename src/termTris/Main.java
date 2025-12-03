package termTris;

public class Main {
    static void main() {
        Pieces pieces = new Pieces();
        Messages messages = new Messages();
        TermTris program = new TermTris(pieces, messages);
        program.start();
    }
}