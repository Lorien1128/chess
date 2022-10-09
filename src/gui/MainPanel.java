package gui;

import javafx.util.Pair;
import piece.ChessPiece;
import util.Board;
import util.Mode;
import util.Point;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MainPanel extends JPanel {
    private final ArrayList<ChessCell> chess = new ArrayList<>();
    private final Computer computer;
    private final MyGui frame;

    public MainPanel(MyGui frame) {
        this.frame = frame;
        setLayout(new GridLayout(9, 9, 0, 0));
        Lock lock = new ReentrantLock();
        Condition playerCondition = lock.newCondition();
        Condition computerCondition = lock.newCondition();
        Player player = new Player(frame, lock, playerCondition, computerCondition);
        player.start();
        Computer computer = null;
        try {
            computer = new Computer(frame, lock, computerCondition);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert computer != null;
        computer.start();
        this.computer = computer;
        for (int i = 0; i < 64; i++) {
            ChessCell chessCell = new ChessCell(i, frame, player, computer, lock,
                    playerCondition, computerCondition);
            chessCell.setBorder(null);
            chess.add(chessCell);
        }
        if (Computer.getMode() != Mode.BOTH_PLAYER || frame.isWhite()) {
            for (ChessCell cell : chess) {
                add(cell);
            }
        }
        else {
            for (int i = chess.size() - 1; i >= 0; i--) {
                add(chess.get(i));
            }
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

    public ArrayList<ChessCell> getChess() {
        return chess;
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(800, 800);
    }

    public void render() {
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
        if (Computer.getMode() == Mode.BOTH_PLAYER) {
            for (ChessCell chessCell : frame.getOtherPlayer().getPanel().getChess()) {
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
    }

    public static int getIndex(Point point) {
        return 8 * (8 - point.getPy()) + point.getPx() - 1;
    }

    public void showUndo(ArrayList<Point> changes) {
        for (ChessCell chessCell : chess) {
            chessCell.setBorder(null);
        }
        if (Computer.getMode() == Mode.BOTH_PLAYER) {
            for (ChessCell chessCell : frame.getOtherPlayer().getPanel().getChess()) {
                chessCell.setBorder(null);
            }
        }
        for (Point point : changes) {
            int index = getIndex(point);
            chess.get(index).setBorder(BorderFactory.createLineBorder(Color.ORANGE, 3));
            if (Computer.getMode() == Mode.BOTH_PLAYER) {
                frame.getOtherPlayer().getPanel().getChess().
                        get(index).setBorder(BorderFactory.createLineBorder(Color.ORANGE, 3));
            }
        }
    }

    public void showCheck(boolean white) {
        Board board = Board.getBoard();
        Point point = board.findKing(white).getPoint();
        ChessCell cell = chess.get(getIndex(point));
        cell.setBorder(BorderFactory.createLineBorder(Color.RED, 3));
        if (Computer.getMode() == Mode.BOTH_PLAYER) {
            cell = frame.getOtherPlayer().getPanel().getChess().get(getIndex(point));
            cell.setBorder(BorderFactory.createLineBorder(Color.RED, 3));
        }
    }

    public void showStepToOther(Pair<ChessPiece, Point> move) {
        ChessPiece piece = move.getKey();
        Point target = move.getValue();
        ChessCell cell = frame.getOtherPlayer().
                getPanel().getChess().get(getIndex(piece.getPoint()));
        cell.setBorder(BorderFactory.createLineBorder(Color.BLUE, 3));
        cell = frame.getOtherPlayer().
                getPanel().getChess().get(getIndex(target));
        cell.setBorder(BorderFactory.createLineBorder(Color.BLUE, 3));
    }
}
