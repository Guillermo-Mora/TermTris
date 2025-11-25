public class Main {
    public static void main(String[] args) {
        Pieces pieces = new Pieces();
        Messages messages = new Messages();
        TermTris program = new TermTris(pieces, messages);
        program.start();
    }
}