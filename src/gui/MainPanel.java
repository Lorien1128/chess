package gui;

import piece.ChessPiece;
import util.Board;
import util.Point;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MainPanel extends JPanel {
    private static final ArrayList<ChessCell> chess = new ArrayList<>();
    private final Computer computer;

    public MainPanel(JFrame frame) {
        setLayout(new GridLayout(9, 9, 0, 0));
        Lock lock = new ReentrantLock();
        Condition playerCondition = lock.newCondition();
        Condition computerCondition = lock.newCondition();
        Player player = new Player(frame, lock, playerCondition, computerCondition);
        player.start();
        Computer computer = new Computer(frame, lock, computerCondition);
        computer.start();
        this.computer = computer;
        for (int i = 0; i < 64; i++) {
            ChessCell chessCell = new ChessCell(i, frame, player, computer, lock,
                    playerCondition, computerCondition);
            chessCell.setBorder(null);
            add(chessCell);
            chess.add(chessCell);
        }
    }

    public Computer getComputer() {
        return computer;
    }

    public void reduceCompCount() {
        computer.countReduce();
    }

    public void init() {
        for (ChessCell chessCell : chess) {
            chessCell.init();
        }
    }

    public static ArrayList<ChessCell> getChess() {
        return chess;
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(800, 800);
    }

    public static void render() {
        for (ChessCell chessCell : chess) {
            int x = chessCell.getPosX();
            int y = chessCell.getPosY();
            Board board = Board.getBoard();
            ChessPiece chessPiece = board.getChess(x, y);
            if (chessPiece != null) {
                chessCell.setText(chessPiece.toString());
            }
            else {
                chessCell.setText(null);
            }
        }
    }

    public static int getIndex(Point point) {
        return 8 * (8 - point.getPy()) + point.getPx() - 1;
    }

    public void showUndo(ArrayList<Point> changes) {
        for (ChessCell chessCell : chess) {
            chessCell.setBorder(null);
        }
        for (Point point : changes) {
            int index = getIndex(point);
            chess.get(index).setBorder(BorderFactory.createLineBorder(Color.ORANGE, 3));
        }
    }

    public static void showCheck(boolean white) {
        Board board = Board.getBoard();
        Point point = board.findKing(white).getPoint();
        ChessCell cell = chess.get(getIndex(point));
        cell.setBorder(BorderFactory.createLineBorder(Color.RED, 3));
    }
}
