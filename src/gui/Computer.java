package gui;

import javafx.util.Pair;
import piece.ChessPiece;
import util.Board;
import util.PieceEvent;
import util.Point;
import util.Strategy;

import javax.swing.*;
import java.awt.Color;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class Computer extends Thread {
    private final Lock lock;
    private final Condition condition;
    private final JFrame frame;

    public Computer(JFrame frame, Lock lock, Condition condition) {
        this.lock = lock;
        this.condition = condition;
        this.frame = frame;
    }

    public Board getBoard() {
        return Board.getBoard();
    }

    @Override
    public void run() {
        while (true) {
            lock.lock();
            try {
                condition.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //System.out.println("computer is awake");
            if (!getBoard().isCurMoveWhite()) {
                Pair<ChessPiece, Point> blackMove = Strategy.decide(4);
                ChessPiece piece = blackMove.getKey();
                Point target = blackMove.getValue();

                ChessCell cell = MainPanel.getChess().get(getIndex(piece.getPoint()));
                cell.setBorder(BorderFactory.createLineBorder(Color.BLUE, 3));
                cell = MainPanel.getChess().get(getIndex(target));
                cell.setBorder(BorderFactory.createLineBorder(Color.BLUE, 3));
                PieceEvent blackEvent = getBoard().acMove(piece, target);
                handleBlackEvent(blackEvent);

                MainPanel.render();
                Board.getBoard().setCurMoveWhite(true);
            }
            lock.unlock();
        }
    }

    public void handleBlackEvent(PieceEvent event) {
        if (event == PieceEvent.IN_CHECK) {
            MyDialog dialog = new MyDialog("你被将军了！", 2, frame);
            Thread thread = new Thread(dialog);
            thread.start();
        }
        else if (event == PieceEvent.CHECKMATED) {
            new MyDialog("你被将死了！",frame, true);
        }
        else if (event == PieceEvent.DRAW) {
            new MyDialog("你将对方逼和了！",frame, true);
        }
    }

    public int getIndex(Point point) {
        return 8 * (8 - point.getPy()) + point.getPx() - 1;
    }
}
