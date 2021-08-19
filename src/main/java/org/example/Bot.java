package org.example;

import java.util.Optional;

/**
 * Klasse für den Bot
 */

public class Bot extends Players {

    private final static int WIN_SCORE  = 1000;  // Score zur Optimalen Zug bestimmung
    private final static int[] colOrder = { 3, 4, 2, 1, 5, 0, 6 }; // Score der Spalten zur Optmierung der Züge von Bot

    private int maxDepth;
    private final int initialMaxDepth;
    private final int pieceValue;
    private final Game game;


    /**
     * Die Funktionen des Bot werden erstellt
     * @param game
     * @param p
     * @param name
     * @param maxDepth
     */
    Bot(Game game, Board.Disk p, String name, int maxDepth) {
        super(p,name);
        this.game = game;
        this.initialMaxDepth = this.maxDepth = maxDepth;
        this.pieceValue = disk.getFieldScore();
    }
    @Override
    boolean isComputer() {
        return true;
    }

    @Override
    Optional<Integer> computeMove(Board board) {
        setOptimalMaxDepth(board);
        int col = minmax(board, pieceValue, 0, -1000000, +1000000);
        if (col>=0) return Optional.of(col);
        else return Optional.empty();
    }

    /**
     * Die züge des Bot werden mit dem Minmax und alpha Beta Abschneiden ausgeführt
     * @param board
     * @param p
     * @param depth
     * @param alpha
     * @param beta
     * @return best move for actual board and player on level 0, // Return best move for actual board and player on level 0
     */
    private int minmax(Board board, int p, int depth, int alpha, int beta) {

        int s = gameState(board,p);
        if (board.getGetDisc() >= Board.ROW * Board.COLUMN) {
            assert(depth!=0);
            if (depth==0) throw new IllegalArgumentException();
            return s;
        }
        if (depth >= maxDepth || s == +WIN_SCORE || s == -WIN_SCORE) return s; // max depth reached or won

        int s_max = -1000000;
        int c_max = -1;
        for (int i = 0; i < Board.COLUMN; i++) {
            int c = colOrder[i];
            if (board.getColPieces(c) < Board.ROW) {
                board.putDisk(c,p);
                s = -minmax(board, -p, depth + 1, -beta, -alpha);
                board.removeDIsc(c);
                if (s > s_max) {
                    s_max = s;
                    c_max = c;
                }
                if (s > alpha) {
                    alpha = s;
                    if (alpha > beta && depth > 0)
                        break;
                }
            }
        }

        if (depth == 0) {
            if (s_max == +WIN_SCORE) {
            }
            else if (s_max == -WIN_SCORE) {
                if (maxDepth != 2) {
                    maxDepth = 2;
                    return minmax(board, p, 0, -1000000, +1000000);
                }

            }
            return c_max;
        }
        else {
            return s_max;
        }
    }


    /**
     * Fragt den Score nach einem Zug ab
     */
    private int gameState(Board board, int p) {
        int s = 0;
        for (Board.Line l : board.getLines()) {
            int s1 = l.count();
            if (s1 == -4 || s1 == +4) {
                return p * s1 * WIN_SCORE/4;
            }
            s += s1;
        }
        return p * s;
    }

    private void setOptimalMaxDepth(Board board) {
        int n = 0;
        for (int i = 0; i< Board.COLUMN; i++) if (board.getColPieces(i)>= Board.ROW) n++;
        maxDepth = initialMaxDepth;
        switch (n) {
            case 0:
            case 1:
                if (board.getGetDisc()>16) maxDepth += 1;
                break;
            case 2:
                maxDepth += 2;
                break;
            default:
                maxDepth = 18;
        }
        if (maxDepth> Board.COLUMN * Board.ROW -board.getGetDisc()) maxDepth = Board.COLUMN * Board.ROW -board.getGetDisc();
    }

}



