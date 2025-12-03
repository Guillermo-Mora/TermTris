package termTris;

import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class TermTris {
    //Variables
    private final int[] termtetrisBoard;
    private final char[] blockType;
    private final Pieces pieces;
    private final Messages messages;
    private List<int[]> currentRandomPiece;

    private int[] randomPieceCurrentState;

    //Start the program
    public void start() {
        if (!messages.menuMessage().equalsIgnoreCase("Q")) {
            gameStart();
        }
    }

    public void gameStart() {
        boolean running = true;
        boolean newPiece = true;
        fillBoard();
        String[] boardLines;
        int pieceInTimer = 0;
        KeyStroke keyStroke;
        DefaultTerminalFactory defaultTerminalFactory = new DefaultTerminalFactory();
        Terminal terminal;

        try {
            terminal = defaultTerminalFactory.createTerminal();
            Screen screen = new TerminalScreen(terminal);
            TextGraphics textGraphics = screen.newTextGraphics();
            screen.startScreen();
            terminal.enterPrivateMode();
            screen.clear();
            screen.refresh();
            terminal.setCursorPosition(0, 0);
            screen.setCursorPosition(terminal.getCursorPosition());

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
                                transformPieceToStatic();
                                newPiece = true;
                                break;
                            }
                        } while (true);
                    } else if (keyStroke.getKeyType() == KeyType.ArrowDown) {
                        if (!movePieceDown()) {
                            transformPieceToStatic();
                            newPiece = true;
                        }
                    }
                }

                //Añadir nueva pieca al tablero si la última ha llegado al fondo
                if (newPiece) {
                    newPiece = false;
                    if (!randomPiceInBoard()) running = false;
                }

                //Mover la pieza actual hacia abajo cada 1.5 segundos
                if (pieceInTimer == 90) {
                    pieceInTimer = 0;
                    if (!movePieceDown()) {
                        transformPieceToStatic();
                        newPiece = true;
                    }
                }

                //Comprobar si hay líneas completas, y si hay, mover las piezas 1 nivel más abajo
                boardLinesFilled();

                //Mostrar el tablero actual
                boardLines = showBoard();
                for (int i = 0; i < boardLines.length; i++) textGraphics.putString(0, i, boardLines[i]);
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
            screen.stopScreen();
            terminal.exitPrivateMode();
            screen.close();
            terminal.close();

            messages.gameOverMessage();
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

                if (termtetrisBoard[i + 12] == 2 || termtetrisBoard[i + 12] == 3) {
                    return false;
                }

                newPositions.add(i + 12);
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
        return true;
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
//        System.out.println(nextRotationPositionY);
//        System.out.println(nextRotationPositionX);
        ArrayList<Integer> oldRotationPositions = new ArrayList<>();
        ArrayList<Integer> newRotationPositions = new ArrayList<>();
        boolean triedReAlignment = false;
        int[] randomPieceNextState;
        int nextPieceStartPosition = 0;
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
                System.out.println("No se puede girar, fuera del rango del tablero");
                return;
            }

            //Si la pieza choca a su izquierda con algo (al principio),
            // continuamos para que haga +1 a la derecha en el tablero
            //-> Modificamos su posición de inicio a 1 más
            System.out.println(termtetrisBoard[i] + " | " + randomPieceNextState[k]);
            if ((termtetrisBoard[i] == 2 || termtetrisBoard[i] == 3)
                    && randomPieceNextState[k] == 1 && !triedReAlignment) {

                System.out.println(i+1 + " | " + termtetrisBoard.length);
                if (i + 2 < termtetrisBoard.length) {
                    if (termtetrisBoard[i + 1] != 3 && termtetrisBoard[i + 1] != 2) {
                        System.out.println("Intento +1 derecha");
                        i = nextPieceStartPosition;
                        triedReAlignment = true;
                        newRotationPositions.clear();
                    }
                }

                System.out.println(i-1 + " | " + 0);
                if (i - 1 >= 0 && !triedReAlignment) {
                    if (termtetrisBoard[i - 1] != 3 && termtetrisBoard[i - 1] != 2) {
                        System.out.println("Intento -1 izquierda");
                        i = nextPieceStartPosition-2;
                        triedReAlignment = true;
                        newRotationPositions.clear();
                    }
                }

                if (!triedReAlignment) {
                    System.out.println("Tras realinear la pieza a izquierda/derecha sigue chocando");
                    return;
                }
                k = 0;
                continue;
            }

            //Si la pieza choca a su derecha con algo (al final), retrasamos todas sus posiciones -1 atrás en el tablero
            //-> Modificamos su posición de inico a 1 menos
//            if ((termtetrisBoard[i] == 2 || termtetrisBoard[i] == 3)
//                    && randomPieceNextState[k] == 1 && !triedReAlignment) {
//                triedReAlignment = true;
//                i = nextPieceStartPosition--;
//                k = 0;
//                continue;
//            }

//                newRotationPositions.replaceAll(integer -> integer - 1);
//                newRotationPositions.add(i-1);
//                for (int i1 = 0; i1 < newRotationPositions.size(); i1++) {
//                    if ()
//                }


            if ((termtetrisBoard[i] == 2 || termtetrisBoard[i] == 3) && randomPieceNextState[k] == 1) {
                System.out.println("No se puede girar, choca con otra pieza existente o con el tablero");
                return;
            }

//            if (k + 1 < randomPieceNextState.length - 2 && i + 1 < termtetrisBoard.length) {
//                //|| termtetrisBoard[i] != 2
//                if ((termtetrisBoard[i] != 3) && termtetrisBoard[i + 1] == 3 && randomPieceNextState[k] == 1
//                        && randomPieceNextState[k + 1] == 1) {
//                    System.out.println("No se puede girar, da la vuelta al tablero");
//                    return;
//                }
//            }

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
            else filledBlocksInLine = 0;
            if (filledBlocksInLine == 10) {
                //Vaciar línea detectada
                for (int j = 0; j < 10; j++) {
                    termtetrisBoard[i - j] = 0;
                }
                //Mover todas las piezas estáticas 1 nivel más abajo de abajo hacia arriba a partir
                // de la siguiente fila arriba de la vaciada
                for (int j = i - 2; j > 0; j--) {
                    if (termtetrisBoard[j] == 2) {
                        termtetrisBoard[j] = 0;
                        termtetrisBoard[j + 12] = 2;
                    }
                }
            }
        }
    }

    public void transformPieceToStatic() {
        for (int i = 0; i < termtetrisBoard.length; i++) {
            if (termtetrisBoard[i] == 1) termtetrisBoard[i] = 2;
        }
    }

    //Constructor
    public TermTris(Pieces pieces, Messages messages) {
        this.termtetrisBoard = new int[252];
        this.blockType = new char[]{'□', '■', '▣', '▨'};
        this.pieces = pieces;
        this.messages = messages;
    }
}