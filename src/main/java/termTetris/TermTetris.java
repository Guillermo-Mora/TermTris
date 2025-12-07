package termTetris;

import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TermTetris {
    //Variables
    private final int[] termtetrisBoard = new int[252];
    //Perfect characters for font type 'Hack'
    //I don't use them becuase I have to use a separate terminal emulator window in order to change font type
    //private final char[] blockType = new char[]{'□', '◼', '▣', '■', '▨'};

    //These work right for most fonts I think
    private final char[] blockType = new char[]{'░', '▓', '█', '▒', ' '};
    private final String topBoardText = "▒▒▒▒▒▒▒▒▒▒▒▒";

    private final String[] scoreText = new String[]{
            "▒▒▒▒▒▒▒▒▒▒▒",
            "SCORE-0000▒",
            "▒▒▒▒▒▒▒▒▒▒▒",
    };
    private final String[] linesClearedText = new String[] {
            "▒▒▒▒▒▒▒▒▒▒▒",
            "LINES-0000▒",
            "▒▒▒▒▒▒▒▒▒▒▒",
    };

    private final String[] levelText = new String[]{
            "▒▒▒▒▒▒▒▒▒▒▒",
            "LEVEL-0001▒",
            "▒▒▒▒▒▒▒▒▒▒▒",
    };

    private final String[] nextPieceText = new String[]{
            "▒▒▒▒▒▒▒▒▒▒▒",
            "          ▒",
            "          ▒",
            "          ▒",
            "          ▒",
            "▒▒▒▒▒▒▒▒▒▒▒",
    };
    private final Pieces pieces;
    private final Messages messages;
    private int linesCleared;
    private int points;

    private int level;

    private List<int[]> nextrandomPiece;
    private List<int[]> currentRandomPiece;

    private int[] randomPieceCurrentState;

    //private final Font termTetrisFont = new Font("Hack", Font.PLAIN, 30);
    //private final AWTTerminalFontConfiguration termTetrisFontConfiguration =
    //        AWTTerminalFontConfiguration.newInstance(termTetrisFont);

    private final DefaultTerminalFactory defaultTerminalFactory = new DefaultTerminalFactory();
    private final Terminal terminal;
    private final Screen screen;

    private final TextGraphics textGraphics;
    private KeyStroke keyStroke;

    {
        try {
            //defaultTerminalFactory.setForceAWTOverSwing(true);
            //defaultTerminalFactory.setPreferTerminalEmulator(true);
            //defaultTerminalFactory.setTerminalEmulatorFontConfiguration(termTetrisFontConfiguration);
            terminal = defaultTerminalFactory.createTerminal();
            screen = new TerminalScreen(terminal);
            textGraphics = screen.newTextGraphics();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    //Start the program
    public void start() {
        try {
            screen.startScreen();
            screen.clear();
            screen.setCursorPosition(null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String[] menuMessage = messages.menuMessage();
        for (int i = 0; i < menuMessage.length; i++) textGraphics.putString(0, i, menuMessage[i]);
        try {
            screen.refresh();
            do {
                keyStroke = screen.readInput();
                if (keyStroke != null) {
                    if (keyStroke.getKeyType() == KeyType.Enter) gameStart();
                    else if (keyStroke.getCharacter() != null &&
                            (keyStroke.getCharacter() == 'q' || keyStroke.getCharacter() == 'Q')) break;
                }
            } while (true);
            screen.stopScreen();
            terminal.exitPrivateMode();
            screen.close();
            terminal.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void gameStart() {
        boolean running = true;
        boolean newPiece = true;
        fillBoard();
        String[] boardLines;
        int pieceInTimer = 0;
        linesCleared = 0;
        points = 0;
        screen.clear();
        //Show lines cleared message box
        textGraphics.putString(0, 0, topBoardText);
        for (int i = 0; i < linesClearedText.length; i++) textGraphics.putString(12, i, linesClearedText[i]);

        try {
            do {
                //Detectar entrada por teclado de manera no bloqueante
                keyStroke = screen.pollInput();
                if (keyStroke != null) {
                    if (keyStroke.getKeyType() == KeyType.ArrowRight) movePieceHorizontal(true);
                    else if (keyStroke.getKeyType() == KeyType.ArrowLeft) movePieceHorizontal(false);
                    else if (keyStroke.getKeyType() == KeyType.ArrowUp) rotatePieceClockwise();
                    else if (keyStroke.getCharacter() != null && keyStroke.getCharacter() == ' ') {
                        do {
                            if (!movePieceDown()) {
                                transformBlockToAnotherBlock(1, 2);
                                //Comprobar si hay líneas completas, y si hay, mover las piezas 1 nivel más abajo
                                boardLinesFilled();
                                newPiece = true;
                                break;
                            }
                        } while (true);
                    } else if (keyStroke.getKeyType() == KeyType.ArrowDown) {
                        if (!movePieceDown()) {
                            transformBlockToAnotherBlock(1, 2);
                            //Comprobar si hay líneas completas, y si hay, mover las piezas 1 nivel más abajo
                            boardLinesFilled();
                            newPiece = true;
                        }
                    }
                }

                //Añadir nueva pieza al tablero si la última ha llegado al fondo
                if (newPiece) {
                    newPiece = false;
                    if (!randomPiceInBoard()) running = false;
                }

                //Mover la pieza actual hacia abajo cada 1.5 segundos
                if (pieceInTimer == 90) {
                    pieceInTimer = 0;
                    if (!movePieceDown()) {
                        transformBlockToAnotherBlock(1, 2);
                        //Comprobar si hay líneas completas, y si hay, mover las piezas 1 nivel más abajo
                        boardLinesFilled();
                        newPiece = true;
                    }
                }

                //Limpiar rastro anterior de dónde caerá la pieza
                transformBlockToAnotherBlock(4, 0);
                //Mostrar dónde caerá la pieza al bajar al final del tablero
                showPieceBottomPosition();

                //Mostrar el tablero actual
                boardLines = showBoard();
                for (int i = 0; i < boardLines.length; i++) textGraphics.putString(0, i+1, boardLines[i]);
                screen.refresh();

                //El bucle del juego se ejecuta cada 16ms (62,5 veces por segundo)
                try {
                    Thread.sleep(16);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                //Cada iteración
                pieceInTimer++;
            } while (running);

            screen.clear();
            String[] gameOverMessage = messages.gameOverMessage();
            for (int i = 0; i < gameOverMessage.length; i++) textGraphics.putString(0, i, gameOverMessage[i]);
            screen.refresh();
            do {
                keyStroke = screen.readInput();
                if (keyStroke != null) {
                    if (keyStroke.getKeyType() == KeyType.Enter) gameStart();
                    else if (keyStroke.getCharacter() != null &&
                            (keyStroke.getCharacter() == 'q' || keyStroke.getCharacter() == 'Q'))
                        System.exit(0);
                }
            } while (true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void fillBoard() {
        for (int i = 0; i < termtetrisBoard.length; ) {
            if (i >= termtetrisBoard.length - 12) {
                termtetrisBoard[i] = 3;
                i++;
            } else {
                for (int j = 0; j < 12; j++, i++) {
                    if (j == 0 || j == 11) {
                        termtetrisBoard[i] = 3;
                    } else {
                        termtetrisBoard[i] = 0;
                    }
                }
            }
        }
    }

    public String[] showBoard() {
        String[] lines = new String[21];
        int currentLine = 0;
        StringBuilder currentString = new StringBuilder();

        for (int i = 0; i < termtetrisBoard.length; i++) {

            currentString.append(blockType[termtetrisBoard[i]]);

            if ((i + 1) % 12 == 0) {
                lines[currentLine] = currentString.toString();
                currentLine++;
                currentString = new StringBuilder();
            }
        }
        return lines;
    }

    //Introduce random piece in the board
    public boolean randomPiceInBoard() {
        currentRandomPiece = pieces.randomPiece();
        randomPieceCurrentState = currentRandomPiece.get(1);

        int i = 0, j = 0;
        int initialSpace = currentRandomPiece.getFirst().length;

        while (j < randomPieceCurrentState.length - 2) {
            if (termtetrisBoard[i] != 3) {
                while (initialSpace > 0) {
                    termtetrisBoard[i] = 0;
                    initialSpace--;
                    i++;
                }
                if (termtetrisBoard[i] == 2 && randomPieceCurrentState[j] == 1) {
                    return false;
                } else termtetrisBoard[i] = randomPieceCurrentState[j];
                i++;
                j++;
            } else {
                i++;
            }
        }
        return true;
    }

    public boolean movePieceDown() {
        ArrayList<Integer> oldPositions = new ArrayList<>();
        ArrayList<Integer> newPositions = new ArrayList<>();

        for (int i = 0; i < termtetrisBoard.length; i++) {
            if (termtetrisBoard[i] == 1) {
                oldPositions.add(i);

                if (termtetrisBoard[i + 12] == 2 || termtetrisBoard[i + 12] == 3) return false;

                newPositions.add(i + 12);
            }
        }

        for (Integer oldPosition : oldPositions) termtetrisBoard[oldPosition] = 0;
        for (Integer newPosition : newPositions) termtetrisBoard[newPosition] = 1;
        return true;
    }

    public void showPieceBottomPosition() {
        int blocksQuantity = 0;
        int minEqualPosition = 0;

        for (int i = 0; i < randomPieceCurrentState.length - 2; i++)
            if (randomPieceCurrentState[i] == 1) blocksQuantity++;

        int[] maxReached = new int[blocksQuantity];
        int[] blocksPositions = new int[blocksQuantity];

        for (int i = 0, currentPiece = -1; i < termtetrisBoard.length; i++) {
            if (termtetrisBoard[i] == 1) {
                currentPiece++;
                blocksPositions[currentPiece] = i;
                do {
                    if (i + maxReached[currentPiece] + 12 < termtetrisBoard.length) {
                        if (termtetrisBoard[i + maxReached[currentPiece] + 12] == 0 ||
                                termtetrisBoard[i + maxReached[currentPiece] + 12] == 1) {
                            maxReached[currentPiece] += 12;
                            continue;
                        }
                    }
                    break;
                } while (true);
            }
        }

        for (int i = 0; i < maxReached.length; i++) {
            if (i == 0) {
                minEqualPosition = maxReached[i];
                continue;
            }
            minEqualPosition = Math.min(minEqualPosition, maxReached[i]);
        }

        if (minEqualPosition < 12) return;

        for (int i = 0; i < blocksPositions.length; i++) {
            if (termtetrisBoard[blocksPositions[i] + minEqualPosition] == 0)
                termtetrisBoard[blocksPositions[i] + minEqualPosition] = 4;
        }
    }

    public void movePieceHorizontal(boolean isRightDirection) {
        int direction;

        if (isRightDirection) direction = 1;
        else direction = -1;

        ArrayList<Integer> oldPositions = new ArrayList<>();
        ArrayList<Integer> newPositions = new ArrayList<>();

        for (int i = 0; i < termtetrisBoard.length; i++) {
            if (termtetrisBoard[i] == 1) {
                oldPositions.add(i);

                if (termtetrisBoard[i + direction] == 2 || termtetrisBoard[i + direction] == 3) {
                    return;
                }

                newPositions.add(i + direction);
            }
        }

        for (int i = 0; i < oldPositions.size(); i++) {
            boolean dontOverride = false;
            for (Integer newPosition : newPositions) {
                if (Objects.equals(oldPositions.get(i), newPosition)) {
                    dontOverride = true;
                    break;
                }
            }
            if (!dontOverride) termtetrisBoard[oldPositions.get(i)] = 0;
            termtetrisBoard[newPositions.get(i)] = 1;
        }
    }

    public void rotatePieceClockwise() {
        int nextRotationPositionY = randomPieceCurrentState[randomPieceCurrentState.length - 2];
        int nextRotationPositionX = randomPieceCurrentState[randomPieceCurrentState.length - 1];
        ArrayList<Integer> oldRotationPositions = new ArrayList<>();
        ArrayList<Integer> newRotationPositions = new ArrayList<>();
        int[] randomPieceNextState;
        int nextPieceStartPosition = 0;
        int directionToRealign = -1;
        int realignmentTries = 0;
        int currentPieceRotation = currentRandomPiece.indexOf(randomPieceCurrentState);

        if (currentPieceRotation == 4) randomPieceNextState = currentRandomPiece.get(1);
        else randomPieceNextState = currentRandomPiece.get(currentPieceRotation + 1);

        //Obtengo la posición inicial del primer bloque de la pieza actual y elimino la pieza
        boolean firstBlockFound = false;
        for (int i = 0; i < termtetrisBoard.length; i++) {
            if (termtetrisBoard[i] == 1) {
                oldRotationPositions.add(i);

                if (!firstBlockFound) {
                    nextPieceStartPosition = i;
                    firstBlockFound = true;
                }
            }
        }

        if (nextRotationPositionY < 0) for (int i = 0; i > nextRotationPositionY; i--) nextPieceStartPosition -= 12;
        else for (int i = 0; i < nextRotationPositionY; i++) nextPieceStartPosition += 12;
        nextPieceStartPosition += nextRotationPositionX;

        //Añado la pieza actual con la nueva rotación a partir de la posición del primer bloque
        // de la pieza anterior junto con sus coordenadas añadidas
        for (int i = nextPieceStartPosition, k = 0; k < randomPieceNextState.length - 2; i++) {
            if (i >= termtetrisBoard.length || i < 0) {
                //System.out.println("No se puede girar, fuera del rango del tablero");
                return;
            }

            //System.out.println(termtetrisBoard[i] + " | " + randomPieceNextState[k]);
            if (k - 1 >= 0) {
                if ((termtetrisBoard[i] == 2 || termtetrisBoard[i] == 3) && randomPieceNextState[k] == 1
                        && randomPieceNextState[k - 1] == 1) {
                    boolean triedRealignment = false;

                    //Realign to right
                    if (i + 1 < termtetrisBoard.length && (directionToRealign == -1 || directionToRealign == 0)) {
                        if (termtetrisBoard[i + 1] != 3 && termtetrisBoard[i + 1] != 2) {
                            nextPieceStartPosition++;
                            i = nextPieceStartPosition - 1;
                            triedRealignment = true;
                            directionToRealign = 0;
                            realignmentTries++;
                            newRotationPositions.clear();
                        }
                    }

                    //Realign to left
                    if (i - 1 >= 0 && !triedRealignment && (directionToRealign == -1 || directionToRealign == 1)) {
                        if (termtetrisBoard[i - 1] != 3 && termtetrisBoard[i - 1] != 2) {
                            nextPieceStartPosition--;
                            i = nextPieceStartPosition - 1;
                            triedRealignment = true;
                            directionToRealign = 1;
                            realignmentTries++;
                            newRotationPositions.clear();
                        }
                    }

                    if (!triedRealignment || realignmentTries == 3) {
                        //System.out.println("Tras realinear la pieza a izquierda/derecha sigue chocando");
                        return;
                    }
                    k = 0;
                    continue;
                }
            }
            if (termtetrisBoard[i] == 2 && randomPieceNextState[k] == 1) {
                //System.out.println("No se puede girar, choca con otra pieza existente o con el tablero");
                return;
            }

            if (termtetrisBoard[i] != 3) {
                newRotationPositions.add(i);
                k++;
            }
        }

        //Si la rotación es posible, elimino la pieza antigua
        oldRotationPositions.forEach(i -> termtetrisBoard[i] = 0);

        //Y añado la pieza nueva rotada al tablero
        for (int i = 0; i < newRotationPositions.size(); i++) {
            if (termtetrisBoard[newRotationPositions.get(i)] == 2 && randomPieceNextState[i] == 0)
                termtetrisBoard[newRotationPositions.get(i)] = 2;
            else termtetrisBoard[newRotationPositions.get(i)] = randomPieceNextState[i];
        }

        //Le asigno su siguiente posición de rotación de pieza
        randomPieceCurrentState = randomPieceNextState;
    }

    public void boardLinesFilled() {
        int filledBlocksInLine = 0;
        for (int i = 0; i < termtetrisBoard.length; i++) {
            if (termtetrisBoard[i] == 2) filledBlocksInLine++;
            else {
                filledBlocksInLine = 0;
                continue;
            }
            if (filledBlocksInLine == 10) {
                //Vaciar línea detectada
                for (int j = 0; j < 10; j++) {
                    termtetrisBoard[i - j] = 0;
                }
                //Mostrar las líneas cleared actuales
                linesCleared++;
                int drawPosition;
                if (linesCleared <= 9) drawPosition = 9;
                else if (linesCleared <= 99) drawPosition = 8;
                else if (linesCleared <= 999) drawPosition = 7;
                else drawPosition = 6;
                textGraphics.putString(12 + drawPosition, 1, String.valueOf(linesCleared));

                //Mover todas las piezas estáticas 1 nivel más abajo de abajo hacia arriba a partir
                // de la siguiente fila arriba de la vaciada
                for (int j = i - 12; j > 0; j--) {
                    if (termtetrisBoard[j] == 2) {
                        termtetrisBoard[j] = 0;
                        termtetrisBoard[j + 12] = 2;
                    }
                }
            }
        }
    }

    public void transformBlockToAnotherBlock(int block, int newBlock) {
        for (int i = 0; i < termtetrisBoard.length; i++) {
            if (termtetrisBoard[i] == block) termtetrisBoard[i] = newBlock;
        }
    }

    //Constructor
    public TermTetris(Pieces pieces, Messages messages) {
        this.pieces = pieces;
        this.messages = messages;
    }
}