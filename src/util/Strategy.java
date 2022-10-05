package util;

import javafx.util.Pair;
import piece.ChessPiece;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

public class Strategy {
    private static int count;
    private static boolean curMoveWhite;

    public static Pair<ChessPiece, Point> decide(int piles) {
        count = 0;
        curMoveWhite = false;
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
        else if (curMoveWhite) {
            ArrayList<Pair<ChessPiece, Point>> choices = board.getAllChoices(true);
            if (choices.isEmpty() && board.kingIsAttacked(true)) {
                return Integer.MAX_VALUE;
            }
            else if (choices.isEmpty()) {
                return 0;
            }
            int min = Integer.MAX_VALUE;
            ArrayList<ChessPiece> snapshot = Tools.copy(board.getChessPieces());
            board.setChessPieces(Tools.copy(snapshot));
            for (Pair<ChessPiece, Point> choice : choices) {
                board.move(choice);
                curMoveWhite = false;
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
            if (choices.isEmpty() && board.kingIsAttacked(false)) {
                return Integer.MIN_VALUE;
            }
            else if (choices.isEmpty()) {
                return 0;
            }
            ArrayList<ChessPiece> snapshot = Tools.copy(board.getChessPieces());
            board.setChessPieces(Tools.copy(snapshot));
            int max = Integer.MIN_VALUE;
            int maxIndex = 0;
            for (int index = 0; index < choices.size(); index++) {
                Pair<ChessPiece, Point> choice = choices.get(index);
                board.move(choice);
                curMoveWhite = true;
                int r = simulate(piles - 1, false, max);
                if (r > max) {
                    maxIndex = index;
                    max = r;
                }
                board.setChessPieces(Tools.copy(snapshot));
            } if (top) {
                return maxIndex;
            } else {
                return max;
            }
        }
    }

    public static Pair<ChessPiece, Point> monteCarloDecide(int piles, int roads) {
        count = 0;
        curMoveWhite = false;
        Board board = Board.getBoard();
        ArrayList<Pair<ChessPiece, Point>> choices = board.getAllChoices(false);
        ArrayList<ChessPiece> snapshot = Tools.copy(board.getChessPieces());
        board.setChessPieces(Tools.copy(snapshot));
        int max = Integer.MIN_VALUE;
        int maxIndex = 0;
        for (Pair<ChessPiece, Point> choice : choices) {
            board.move(choice);
            ArrayList<Pair<ChessPiece, Point>> choices1 = board.getAllChoices(true);
            if (choices1.isEmpty() && board.kingIsAttacked(true)) {
                return choice;
            }
            else if (choices1.isEmpty()) {
                continue;
            }
            ArrayList<ChessPiece> snapshot1 = Tools.copy(board.getChessPieces());
            board.setChessPieces(Tools.copy(snapshot));
            int min = Integer.MAX_VALUE;
            for (Pair<ChessPiece, Point> choice1 : choices1) {
                int r;
                board.move(choice1);
                curMoveWhite = false;
                r = monteCarlo(piles, roads);
                if (r < min) {
                    min = r;
                }
                board.setChessPieces(Tools.copy(snapshot1));
            }
            if (min > max) {
                max = min;
                maxIndex = choices.indexOf(choice);
            }
            System.out.println(choice.getKey() + " " + choice.getValue() + " value: " + min);
            board.setChessPieces(Tools.copy(snapshot));
        }
        System.out.println("index: " + maxIndex);
        System.out.println("count: " + count);
        return choices.get(maxIndex);
    }

    private static int monteCarlo(int piles, int roads) {
        count++;
        Board board = Board.getBoard();
        if (piles == 0) {
            return board.sumOfValue();
        }
        if (curMoveWhite) {
            ArrayList<Pair<ChessPiece, Point>> choices = board.getAllChoices(true);
            if (choices.isEmpty() && board.kingIsAttacked(true)) {
                return 90000;
            }
            else if (choices.isEmpty()) {
                return 0;
            }
            Random r = new Random();
            int size = choices.size();
            HashSet<Integer> paths = new HashSet<>();
            if (choices.size() >= roads) {
                for (int i = 0; i < roads; i++) {
                    int index = r.nextInt(size);
                    while (paths.contains(index)) {
                        index = r.nextInt(size);
                    }
                    paths.add(index);
                }
            }
            else {
                for (int i = 0; i < size; i++) {
                    paths.add(i);
                }
            }
            ArrayList<ChessPiece> snapshot = Tools.copy(board.getChessPieces());
            board.setChessPieces(Tools.copy(snapshot));
            int sum = 0;
            for (Integer path : paths) {
                board.move(choices.get(path));
                curMoveWhite = false;
                int ret = monteCarlo(piles - 1, roads);
                sum += ret;
                board.setChessPieces(Tools.copy(snapshot));
            }
            return sum / paths.size();
        }
        else {
            return handleBlack(piles, roads);
        }
    }

    private static int handleBlack(int piles, int roads) {
        Board board = Board.getBoard();
        ArrayList<Pair<ChessPiece, Point>> choices = board.getAllChoices(false);
        if (choices.isEmpty() && board.kingIsAttacked(false)) {
            return -90000;
        }
        else if (choices.isEmpty()) {
            return 0;
        }
        Random r = new Random();
        int size = choices.size();
        HashSet<Integer> paths = new HashSet<>();
        if (choices.size() >= roads) {
            for (int i = 0; i < roads; i++) {
                int index = r.nextInt(size);
                while (paths.contains(index)) {
                    index = r.nextInt(size);
                }
                paths.add(index);
            }
        }
        else {
            for (int i = 0; i < size; i++) {
                paths.add(i);
            }
        }
        ArrayList<ChessPiece> snapshot = Tools.copy(board.getChessPieces());
        board.setChessPieces(Tools.copy(snapshot));
        int sum = 0;
        for (Integer path : paths) {
            board.move(choices.get(path));
            curMoveWhite = true;
            int ret = monteCarlo(piles - 1, roads);
            sum += ret;
            board.setChessPieces(Tools.copy(snapshot));
        }
        return sum / paths.size();
    }

    public static Pair<ChessPiece, Point> randomDecide() {
        Board board = Board.getBoard();
        ArrayList<Pair<ChessPiece, Point>> choices = board.getAllChoices(false);
        Random r = new Random();
        int index = r.nextInt(choices.size());
        return choices.get(index);
    }
}
