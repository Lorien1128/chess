package gui.listener;

import gui.ChessCell;
import gui.Computer;
import gui.MyDialog;
import gui.MyGui;
import gui.Player;
import javafx.util.Pair;
import piece.ChessPiece;
import util.Board;
import util.Mode;
import util.Point;

import javax.swing.BorderFactory;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class CellListener extends Thread implements ActionListener {
    private final int posX;
    private final int posY;
    private final MyGui frame;
    private final Player player;
    private final Computer computer;
    private final Lock lock;
    private final Condition playerCondition;
    private final Condition computerCondition;
    private final Condition myCondition;

    public CellListener(int index, MyGui frame, Player player, Computer computer, Lock lock,
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
        frame.setChooseFrom(false);
        player.init();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!frame.isChooseFrom()) {
            handleNotChosenFrom();
        }
        else {
            handleChosenFrom();
        }
    }

    public void handleNotChosenFrom() {
        Board board = Board.getBoard();
        ChessPiece chessPiece = board.getChess(posX, posY);
        if (computer.getState() == State.RUNNABLE ||
                board.isCurMoveWhite() != frame.isWhite()) {
            return;
        }
        if (chessPiece == null) {
            MyDialog dialog = new MyDialog("该处不存在棋子！", 2, frame);
            Thread thread = new Thread(dialog);
            thread.start();
        }
        else if (chessPiece.isWhite() != frame.isWhite()) {
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
                for (ChessCell chessCell : frame.getPanel().getChess()) {
                    chessCell.setBorder(null);
                }
                for (Point point : points) {
                    ChessCell cell = frame.getPanel().getChess().get(getIndex(point));
                    cell.setBorder(BorderFactory.createLineBorder(Color.GREEN, 3));
                }
                frame.setChooseFrom(true);
                frame.setFromWhere(new Pair<>(posX, posY));
            }
        }
    }

    public void handleChosenFrom() {
        Board board = Board.getBoard();
        if (posX == frame.getFromWhere().getKey() &&
                posY == frame.getFromWhere().getValue()) {
            for (ChessCell chessCell : frame.getPanel().getChess()) {
                chessCell.setBorder(null);
            }
            frame.setChooseFrom(false);
        }
        else if (frame.getPanel().getChess().get(getIndex(posX, posY)).getBorder() != null) {
            ChessPiece piece = board.getChess(
                    frame.getFromWhere().getKey(), frame.getFromWhere().getValue());
            player.setMove(piece, new Point(posX, posY));
            player.setToWakeUp(myCondition);

            lock.lock();
            board.setCurMoveWhite(frame.isWhite());
            playerCondition.signal();
            try {
                myCondition.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            lock.unlock();

            for (ChessCell chessCell : frame.getPanel().getChess()) {
                chessCell.setBorder(null);
            }
            if (Computer.getMode() != Mode.BOTH_PLAYER) {
                lock.lock();
                if (player.isNeedComputer()) {
                    board.setCurMoveWhite(!frame.isWhite());
                    computerCondition.signal();
                }
                lock.unlock();
            }
            else {
                board.setCurMoveWhite(!frame.isWhite());
            }
            frame.setChooseFrom(false);
        }
        else if (board.getChess(posX, posY) != null &&
                board.getChess(posX, posY).isWhite() == frame.isWhite()) {
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
