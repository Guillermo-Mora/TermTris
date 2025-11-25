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
        String option;
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

        do {
            option = lector.nextLine();
            if (!option.equalsIgnoreCase("q") && !option.isEmpty()) {
                System.out.print( "║ Invalid option [ ! ]\n" +
                        "║ Press Enter to start a new Game [ ▶ ]\n" +
                        "║ Type [ Q ] to exit [ ✖ ]\n" +
                        "║ ➤ ] ");
                continue;
            }
            break;
        } while (true);
        return option;
    }
}
