package util;

import javafx.util.Pair;
import piece.ChessPiece;

import java.util.ArrayList;
import java.util.Random;

public class Strategy {
    private static int count;

    public static Pair<ChessPiece, Point> decide(int piles) {
        count = 0;
        Board board = Board.getBoard();
        ArrayList<Pair<ChessPiece, Point>> choices = board.getAllChoices(false);
        int index = simulate(piles, true, 0);
        System.out.println("index: " + index);
        System.out.println("count: " + count);
        return choices.get(index);
    }

    private static int simulate(int piles, boolean top, int maxScore) {
        count++;
        Board board = Board.getBoard();
        if (piles == 0) {
            return board.sumOfValue();
        }
        else if (piles % 2 == 1) {
            ArrayList<Pair<ChessPiece, Point>> choices = board.getAllChoices(true);
            if (choices.isEmpty()) {
                return Integer.MAX_VALUE;
            }
            int min = Integer.MAX_VALUE;
            ArrayList<ChessPiece> snapshot = Tools.copy(board.getChessPieces());
            board.setChessPieces(Tools.copy(snapshot));
            for (Pair<ChessPiece, Point> choice : choices) {
                board.move(choice);
                int r = simulate(piles - 1, false, 0);
                if (r <= maxScore) {
                    return Integer.MIN_VALUE;
                }
                if (r < min) {
                    min = r;
                }
                board.setChessPieces(Tools.copy(snapshot));
            }
            return min;
        }
        else {
            ArrayList<Pair<ChessPiece, Point>> choices = board.getAllChoices(false);
            if (choices.isEmpty()) {
                return Integer.MIN_VALUE;
            }
            ArrayList<ChessPiece> snapshot = Tools.copy(board.getChessPieces());
            board.setChessPieces(Tools.copy(snapshot));
            int max = Integer.MIN_VALUE;
            int maxIndex = 0;
            for (Pair<ChessPiece, Point> choice : choices) {
                board.move(choice);
                int r = simulate(piles - 1, false, max);
                if (r > max) {
                    maxIndex = choices.indexOf(choice);
                    max = r;
                }
                board.setChessPieces(Tools.copy(snapshot));
            }
            if (top) {
                return maxIndex;
            }
            else {
                return max;
            }
        }
    }

    private static int monteCarlo() {
        return 0;
    }

    public static Pair<ChessPiece, Point> randomDecide() {
        Board board = Board.getBoard();
        ArrayList<Pair<ChessPiece, Point>> choices = board.getAllChoices(false);
        Random r = new Random();
        int index = r.nextInt(choices.size());
        return choices.get(index);
    }
}
