package gui;

import javafx.util.Pair;
import piece.ChessPiece;
import util.Board;
import util.Point;
import util.Strategy;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class Computer extends Thread {
    private final Lock lock;
    private final Condition condition;

    public Computer(Lock lock, Condition condition) {
        this.lock = lock;
        this.condition = condition;
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
                getBoard().acMove(piece, target);
                MainPanel.render();
            }
            lock.unlock();
        }
    }

    public int getIndex(Point point) {
        return 8 * (8 - point.getPy()) + point.getPx() - 1;
    }
}
