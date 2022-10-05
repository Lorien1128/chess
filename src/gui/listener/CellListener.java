package gui.listener;

import gui.ChessCell;
import gui.Computer;
import gui.MainPanel;
import gui.MyDialog;
import gui.Player;
import javafx.util.Pair;
import piece.ChessPiece;
import util.Board;
import util.Point;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class CellListener extends Thread implements ActionListener {
    private final int posX;
    private final int posY;
    private final JFrame frame;
    private static boolean chooseFrom;
    private static Pair<Integer, Integer> fromWhere;
    private final Player player;
    private final Computer computer;
    private final Lock lock;
    private final Condition playerCondition;
    private final Condition computerCondition;
    private final Condition myCondition;

    public CellListener(int index, JFrame frame, Player player, Computer computer, Lock lock,
                        Condition playerCondition, Condition computerCondition) {
        super();
        posX = index % 8 + 1;
        posY = 8 - index / 8;
        this.frame = frame;
        this.player = player;
        this.computer = computer;
        this.lock = lock;
        this.playerCondition = playerCondition;
        this.computerCondition = computerCondition;
        myCondition = lock.newCondition();
    }

    public void init() {
        chooseFrom = false;
        player.init();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!chooseFrom) {
            handleNotChosenFrom();
        }
        else {
            handleChosenFrom();
        }
    }

    public void handleNotChosenFrom() {
        Board board = Board.getBoard();
        ChessPiece chessPiece = board.getChess(posX, posY);
        if (computer.getState() == State.RUNNABLE) {
            return;
        }
        if (chessPiece == null) {
            MyDialog dialog = new MyDialog("该处不存在棋子！", 2, frame);
            Thread thread = new Thread(dialog);
            thread.start();
        }
        else if (!chessPiece.isWhite()) {
            MyDialog dialog = new MyDialog("请走本方棋子！", 2, frame);
            Thread thread = new Thread(dialog);
            thread.start();
        }
        else {
            ArrayList<Point> points = chessPiece.searchAllDrop(false);
            if (points.isEmpty()) {
                MyDialog dialog = new MyDialog("该棋子无法移动！", 2, frame);
                Thread thread = new Thread(dialog);
                thread.start();
            }
            else {
                for (ChessCell chessCell : MainPanel.getChess()) {
                    chessCell.setBorder(null);
                }
                for (Point point : points) {
                    ChessCell cell = MainPanel.getChess().get(getIndex(point));
                    cell.setBorder(BorderFactory.createLineBorder(Color.GREEN, 3));
                }
                chooseFrom = true;
                fromWhere = new Pair<>(posX, posY);
            }
        }
    }

    public void handleChosenFrom() {
        Board board = Board.getBoard();
        if (posX == fromWhere.getKey() && posY == fromWhere.getValue()) {
            for (ChessCell chessCell : MainPanel.getChess()) {
                chessCell.setBorder(null);
            }
            chooseFrom = false;
        }
        else if (MainPanel.getChess().get(getIndex(posX, posY)).getBorder() != null) {
            ChessPiece piece = board.getChess(fromWhere.getKey(), fromWhere.getValue());
            player.setMove(piece, new Point(posX, posY));
            player.setToWakeUp(myCondition);

            lock.lock();
            board.setCurMoveWhite(true);
            playerCondition.signal();
            try {
                myCondition.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            lock.unlock();

            for (ChessCell chessCell : MainPanel.getChess()) {
                chessCell.setBorder(null);
            }
            lock.lock();
            if (player.isNeedComputer()) {
                board.setCurMoveWhite(false);
                computerCondition.signal();
            }
            lock.unlock();
            chooseFrom = false;
        }
        else if (board.getChess(posX, posY) != null &&
                board.getChess(posX, posY).isWhite()) {
            handleNotChosenFrom();
        }
        else {
            MyDialog dialog = new MyDialog("无法移动到该处！", 2, frame);
            Thread thread = new Thread(dialog);
            thread.start();
        }
    }

    public int getIndex(Point point) {
        return 8 * (8 - point.getPy()) + point.getPx() - 1;
    }

    public int getIndex(int x, int y) {
        return 8 * (8 - y) + x - 1;
    }
}
