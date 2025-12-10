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
    private final int[] termtetrisBoard = new int[252];
    private final char[] blockType = new char[]{'░', '▓', '█', '▒', ' '};
    private final String[] scoreText = new String[]{
            "▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒",
            " SCORE      0 ▒",
            "▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒",
    };
    private final String[] nextPieceText = new String[]{
            "              ▒",
            "              ▒",
            "              ▒",
            "              ▒",
            "▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒",
    };
    private final String[] linesClearedText = new String[]{
            " LINES      0 ▒",
            "▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒",
    };
    private final String[] levelText = new String[]{
            " LEVEL      0 ▒",
            "▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒",
    };
    private final Pieces pieces;
    private boolean newPiece;
    private final Messages messages;
    private int linesCleared;
    private int points;
    private int level;
    private int linesRequiredtoNextLevel;
    private int pieceFallSpeed;
    private List<int[]> nextRandomPiece;
    private List<int[]> currentRandomPiece;
    private int[] randomPieceCurrentState;
    private final DefaultTerminalFactory defaultTerminalFactory = new DefaultTerminalFactory();
    private final Terminal terminal;
    private final Screen screen;
    private final TextGraphics textGraphics;
    private KeyStroke keyStroke;
    {
        try {
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
            screen.close();
            terminal.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void gameStart() {
        boolean running = true;
        newPiece = true;
        fillBoard();
        String[] boardLines;
        int pieceInTimer = 0;
        linesCleared = 0;
        points = 0;
        level = 0;
        linesRequiredtoNextLevel = 5;
        //pieceFallSpeed = 85;
        pieceFallSpeed = 5;
        nextRandomPiece = pieces.randomPiece();
        screen.clear();
        //Show board top box
        textGraphics.putString(0, 0, "▒▒▒▒▒▒▒▒▒▒▒▒");
        //Show score message box
        for (int i = 0; i < scoreText.length; i++) textGraphics.putString(12, i, scoreText[i]);
        //Show next piece message box
        for (int i = 0; i < nextPieceText.length; i++) textGraphics.putString(12, i + 3, nextPieceText[i]);
        //Show lines cleared message box
        for (int i = 0; i < linesClearedText.length; i++) textGraphics.putString(12, i + 8, linesClearedText[i]);
        //Show level message box
        for (int i = 0; i < levelText.length; i++) textGraphics.putString(12, i + 10, levelText[i]);
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

                //Mover la pieza actual hacia abajo según el tiempo requerido por el nivel
                if (pieceInTimer >= pieceFallSpeed) {
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
                for (int i = 0; i < boardLines.length; i++) textGraphics.putString(0, i + 1, boardLines[i]);
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
            String[] gameOverMessage = messages.gameOverMessage(points, linesCleared, level);
            for (int i = 0; i < gameOverMessage.length; i++) textGraphics.putString(0, i, gameOverMessage[i]);
            screen.refresh();
            do {
                keyStroke = screen.readInput();
                if (keyStroke != null) {
                    if (keyStroke.getKeyType() == KeyType.Enter) gameStart();
                    else if (keyStroke.getCharacter() != null &&
                            (keyStroke.getCharacter() == 'q' || keyStroke.getCharacter() == 'Q')) {
                        screen.stopScreen();
                        screen.close();
                        terminal.close();
                        System.exit(0);
                    }
                }
            } while (true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void fillBoard() {
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

    private String[] showBoard() {
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
    private boolean randomPiceInBoard() {
        currentRandomPiece = nextRandomPiece;
        randomPieceCurrentState = currentRandomPiece.get(1);
        nextRandomPiece = pieces.randomPiece();
        StringBuilder[] nextPieceBlocks = new StringBuilder[2];
        nextPieceBlocks[0] = new StringBuilder();
        nextPieceBlocks[1] = new StringBuilder();

        int spacesLength = nextRandomPiece.getFirst().length;
        int spacesCount = spacesLength;
        int secondLineSpaces = 0;

        for (int i = 0; i < nextRandomPiece.get(1).length-2; i++) {
            if (spacesCount < 10) {
                if (nextRandomPiece.get(1)[i] == 1) nextPieceBlocks[0].append(blockType[nextRandomPiece.get(1)[i]]);
            } else {
                if (nextRandomPiece.get(1)[i] == 0 && spacesLength > 0) spacesLength--;
                else if (nextRandomPiece.get(1)[i] == 0) secondLineSpaces++;
                else if (nextRandomPiece.get(1)[i] == 1)
                    nextPieceBlocks[1].append(blockType[nextRandomPiece.get(1)[i]]);
            }
            spacesCount++;
        }
        secondLineSpaces -= spacesLength;

        //Clear previous next piece
        for (int i = 0; i < 2; i++) textGraphics.putString(12+1, 4+i, "             ");

        //Show next piece
        for (int i = 0; i < nextPieceBlocks.length; i++)
            textGraphics.putString(12+6 + (i == 1 ? secondLineSpaces : 0), 4+i, nextPieceBlocks[i].toString());

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

    private boolean movePieceDown() {
        ArrayList<Integer> oldPositions = new ArrayList<>();
        ArrayList<Integer> newPositions = new ArrayList<>();

        for (int i = 0; i < termtetrisBoard.length; i++) {
            if (termtetrisBoard[i] == 1) {
                oldPositions.add(i);

                if (termtetrisBoard[i + 12] == 2 || termtetrisBoard[i + 12] == 3) return false;

                newPositions.add(i + 12);
            }
        }
        if (oldPositions.isEmpty()) return false;

        for (Integer oldPosition : oldPositions) termtetrisBoard[oldPosition] = 0;
        for (Integer newPosition : newPositions) termtetrisBoard[newPosition] = 1;
        return true;
    }

    private void showPieceBottomPosition() {
        int blocksQuantity = 0;
        int minEqualPosition = 0;

        for (int i = 0; i < randomPieceCurrentState.length - 2; i++)
            if (randomPieceCurrentState[i] == 1) blocksQuantity++;

        int[] maxReached = new int[blocksQuantity];
        int[] blocksPositions = new int[blocksQuantity];

        for (int i = 0, currentPiece = -1; i < termtetrisBoard.length; i++) {
            if (termtetrisBoard[i] == 1) {
                currentPiece++;
                if (currentPiece > blocksPositions.length-1) return;
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

    private void movePieceHorizontal(boolean isRightDirection) {
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

    private void rotatePieceClockwise() {
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
            if (i >= termtetrisBoard.length) return;

            //Piece cant rotate becuase is at the top of the board
            if (i < 0) {
                k = 0;
                nextPieceStartPosition += 12;
                i = nextPieceStartPosition - 1;
                continue;
            }

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

                    if (!triedRealignment || realignmentTries == 3) return;
                    k = 0;
                    continue;
                }
            }
            if (termtetrisBoard[i] == 2 && randomPieceNextState[k] == 1) return;


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

    private void boardLinesFilled() {
        int clearedLinesThisTime = 0;
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
                clearedLinesThisTime++;

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

        if (clearedLinesThisTime > 0) {
            points += (clearedLinesThisTime * 100) * (level + 1);
            textGraphics.putString(12 + drawPositionX(points), 1, String.valueOf(points));

            linesCleared += clearedLinesThisTime;
            textGraphics.putString(12 + drawPositionX(linesCleared), 8, String.valueOf(linesCleared));

            if (linesCleared >= linesRequiredtoNextLevel) {
                linesRequiredtoNextLevel = ++level * 5 + 5;
                textGraphics.putString(12 + drawPositionX(level), 10, String.valueOf(level));
                if (level <= 16) pieceFallSpeed -= 5;
                else if (level <= 18) pieceFallSpeed -= 1;
            }
        }
    }

    private int drawPositionX(int value) {
        if (value <= 9) return 12;
        else if (value <= 99) return 11;
        else if (value <= 999) return 10;
        else if (value <= 9999) return 9;
        else if (value <= 99999) return 8;
        else return 7;
    }

    private void transformBlockToAnotherBlock(int block, int newBlock) {
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