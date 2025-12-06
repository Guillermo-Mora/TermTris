package termTetris;

public class Messages {

    public String[] gameOverMessage() {
       return new String[]{
               "[Game over ☠]",
               "║ Type Q to exit",
               "║Press Enter to start a new Game [ ▶ ]"
       };
    }

    public String[] menuMessage() {
        return new String[]{
                "████████╗███████╗██████╗ ███╗   ███╗████████╗███████╗████████╗██████╗ ██╗███████╗",
                "╚══██╔══╝██╔════╝██╔══██╗████╗ ████║╚══██╔══╝██╔════╝╚══██╔══╝██╔══██╗██║██╔════╝",
                "   ██║   █████╗  ██████╔╝██╔████╔██║   ██║   █████╗     ██║   ██████╔╝██║███████╗",
                "   ██║   ██╔══╝  ██╔══██╗██║╚██╔╝██║   ██║   ██╔══╝     ██║   ██╔══██╗██║╚════██║",
                "   ██║   ███████╗██║  ██║██║ ╚═╝ ██║   ██║   ███████╗   ██║   ██║  ██║██║███████║",
                "   ╚═╝   ╚══════╝╚═╝  ╚═╝╚═╝     ╚═╝   ╚═╝   ╚══════╝   ╚═╝   ╚═╝  ╚═╝╚═╝╚══════╝",
                "║ Welcome to TermTetris! ▙",
                "║ Type Q to exit",
                "║ Press Enter to start a new Game [ ▶ ]"
        };
    }
}
