import java.util.Scanner;

public class Messages {
    public void clearScreen() {
        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n" +
                "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n" +
                "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n" +
                "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n" +
                "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n" +
                "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n" +
                "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
    }

    public void gameOverMessage() {
        Scanner lector = new Scanner(System.in);
        System.out.println("Game over ☠\n" +
                           "Press Enter to return to Menu ▶");
        lector.next();
    }

    public String menuMessage() {
        Scanner lector = new Scanner(System.in);
        System.out.println("\n" +
                "████████╗███████╗██████╗ ███╗   ███╗████████╗███████╗████████╗██████╗ ██╗███████╗\n" +
                "╚══██╔══╝██╔════╝██╔══██╗████╗ ████║╚══██╔══╝██╔════╝╚══██╔══╝██╔══██╗██║██╔════╝\n" +
                "   ██║   █████╗  ██████╔╝██╔████╔██║   ██║   █████╗     ██║   ██████╔╝██║███████╗\n" +
                "   ██║   ██╔══╝  ██╔══██╗██║╚██╔╝██║   ██║   ██╔══╝     ██║   ██╔══██╗██║╚════██║\n" +
                "   ██║   ███████╗██║  ██║██║ ╚═╝ ██║   ██║   ███████╗   ██║   ██║  ██║██║███████║\n" +
                "   ╚═╝   ╚══════╝╚═╝  ╚═╝╚═╝     ╚═╝   ╚═╝   ╚══════╝   ╚═╝   ╚═╝  ╚═╝╚═╝╚══════╝\n");
        System.out.print("║ Welcome to TermTetris! ▙ \n" +
                           "║ Press Enter to start a new Game [ ▶ ]\n" +
                           "║ Type [ Q ] to exit [ ✖ ]\n" +
                           "║ ➤ ] ");
        return lector.nextLine();
    }
}
