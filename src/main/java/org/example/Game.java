package org.example;

import javafx.scene.paint.Color;

import java.util.Optional;
import java.util.Stack;

public class Game {

    private final Players player1;
    private final Players player2;
    private Players nextPlayer;


    // Board
    private final Board board;

    // Game status
    boolean gameOver;

    // Column Stack für den Zug
    private Stack<Integer> moveStack = new Stack<Integer>();

    public static final int getRows() {
        return Board.ROW;
    }

    public static final int getCols() {
        return Board.COLUMN;
    }

    /**
     * Das Spielfeld wird erstellt und die zwei Spieler werden Initialisiert
     */

    public Game(boolean computer1, boolean computer2, BoardUpdateListener bl, StatusUpdateListener sl) {

        boardUpdateListener = Optional.of(bl);
        statusUpdateListener = Optional.of(sl);
        gameOver = false;

        // Create board
        board = new Board();

        // Create players
        if (computer1) {
            player1 = new Bot(this, Board.Disk.RED, "Red", 11);
        } else {
            player1 = new Player(Board.Disk.RED, "Red");
        }
        if (computer2) {
            player2 = new Bot(this, Board.Disk.YELLOW, "Yellow", 10);
        } else {
            player2 = new Player(Board.Disk.YELLOW, "Yellow");
        }
        nextPlayer = player1; // Mensch beginnt

    }

    /**
     * Stellt fest ob sich der Inhalt des Fledes geändert hat
     */
    public interface BoardUpdateListener {
        void Update(Color color, boolean isNew, boolean marker, int column, int row);
    }
    private final Optional<BoardUpdateListener> boardUpdateListener;
    private void boardUpdate(Board.Disk disk, boolean isNew, boolean marker, int col, int row) {
        boardUpdateListener.ifPresent(l -> l.Update(disk.getColor(), isNew, marker, col, row));
    }

    public interface StatusUpdateListener {
        void PrintStatus(String s);
    }
    private final Optional<StatusUpdateListener> statusUpdateListener;
    void statusUpdate(String s) {
        statusUpdateListener.ifPresent(l -> l.PrintStatus(s));
    }

    /**
     * Wechsel des SPielers nach einem Zug
     */
    private void nextPlayer() {
        if (!isOver()) {
            nextPlayer = (nextPlayer == player1) ? player2 : player1;
        }
    }


    /**
     * @return Spiel vorbei?
     */
    public boolean isOver() {
        return gameOver;
    }

    /**
     * @return Nächster Zug Bot?
     */
    public boolean nextIsComputer() {
        return nextPlayer.isComputer();
    }


    /**
     * @return Zug von Spieler
     */
    public boolean humanMove(int col) {
        if (!gameOver) {
            if (doMove(nextPlayer.getDisk(), col)) {
                nextPlayer();
                return true;
            }
        }
        return false;
    }

    /**
     * @return Compuer macht einen Zug
     */
    public boolean computerMove() {
        if (!gameOver) {
            Optional<Integer> col = nextPlayer.computeMove(board);
            if (col.isPresent()) {
                if (doMove(nextPlayer.getDisk(), col.get())) {
                    nextPlayer();
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * Zeigt an welcher zug gemacht wurde
     * überprüft Spielstatus
     */

    private boolean doMove(Board.Disk disk, int col) {
        if (gameOver) return false;
        if (!board.placeDisk(col, disk)) return false;
        System.out.println(disk.name() + ":" + col);
        boardUpdate(disk, true, false, col, board.getColPieces(col) - 1);
        moveStack.push(col);
        if (board.gameEnded()) {
            gameOver = true;
        } else if (board.gameOver()) {
            //TODO:Sout("Winnner")
            gameOver = true;
        }
        return true;
    }


}
