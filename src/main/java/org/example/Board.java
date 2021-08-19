package org.example;

import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Board {

    // Initialisierung Spalten und Reihen und Scheiben
    public final static int COLUMN = 7; // Board dimensions
    public final static int ROW = 6;

    // The piece
    enum Disk {

        RED(+1, Color.RED), YELLOW(-1, Color.YELLOW), EMPTY(0, Color.WHITE);

        private final int fieldScore;
        private final Color color;

        Disk(int fieldScore, Color color) {
            this.fieldScore = fieldScore;
            this.color = color;
        }

        int getFieldScore() {

            return fieldScore;
        }

        Color getColor() {
            return color;
        }

    }

    /**
     * 4 Gewinnt Reihen
     */
    class Line {
        class Field {

            private final int row, col;

            Field(int col, int row) {
                this.row = row;
                this.col = col;
            }

            int getFieldValue() {
                return board[col][row];
            }

        }

        final private List<Field> fields = new ArrayList<>(4);

        // Winning Line wird erstellt
        private Line(int col, int row, int cols, int rows) {
            for (int i = 0; i < 4; i++) {
                fields.add(new Field(col + i * cols, row + i * rows));
            }
        }
        //Anzahl der Scheiben wird gezählt
        int count() {
            int s = 0;
            for (Field f : fields) {
                int sf = f.getFieldValue();
                if (s * sf < 0) return 0;
                s += sf;
            }
            return s;
        }

    }

    private int[][] board = new int[COLUMN][ROW];
    private int[] numDisk = new int[COLUMN]; // Anzahl der Scheiebn in einer Spalte
    private int getDisc = 0;
    private List<Line> lines; // Array list aller 4gweinnt möglichkeiten

    Board() {
        for (int c = 0; c < COLUMN; c++) {
            numDisk[c] = 0;
            for (int r = 0; r < ROW; r++) {
                board[c][r] = Disk.EMPTY.fieldScore;
            }
        }
        checkWIn();
    }



    void putDisk(int col, int p) {
        board[col][numDisk[col]++] = p;
        getDisc = getGetDisc() + 1;
    }
    boolean placeDisk(int col, Disk disk) {
        if (numDisk[col]>= ROW) return false;
        putDisk(col, disk.getFieldScore());
        return true;
    }

    void removeDIsc(int col) {
        board[col][--numDisk[col]] = 0;
        getDisc = getGetDisc() - 1;
    }

    int getColPieces(int col) {
        return numDisk[col];
    }

    int getGetDisc() {
        return getDisc;
    }

    List<Line> getLines() {
        return lines;
    }


    /**
     * Überprüfen aler Gewinnmöglichkeiten
     */
    //row -3  bc including scheibe
    private void checkWIn() {
        lines = new ArrayList<Line>();
        for (int r = 0; r < ROW; r++) {
            for (int c = 0; c < COLUMN; c++) {
                if (r + 4 <= ROW)
                    lines.add(new Line(c, r, 0, 1)); // Vertikal
                if (c + 4 <= COLUMN)
                    lines.add(new Line(c, r, 1, 0)); // Horizontal
                if (r + 4 <= ROW && c + 4 <= COLUMN)
                    lines.add(new Line(c, r, 1, 1)); // Diagonal
                if (r + 4 <= ROW && c - 3 >= 0)
                    lines.add(new Line(c, r, -1, 1));
            }
        }
    }



    // Schaut wann die erste Winning Combination gesetzt wurde
    private Optional<Line> getWinningLine() {
        return lines.stream().filter(l->(Math.abs(l.count())==4)).findFirst();
    }

    boolean gameOver() {
        return gameEnded() || getGetDisc() >= ROW * COLUMN;
    }

    boolean gameEnded() {
        return getWinningLine().isPresent();
    }
}

