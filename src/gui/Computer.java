package gui;

import connection.MySocket;
import javafx.util.Pair;
import piece.ChessPiece;
import util.Board;
import util.Mode;
import util.PieceEvent;
import util.Point;
import util.Strategy;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import java.awt.Color;
import java.io.IOException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class Computer extends Thread {
    private final Lock lock;
    private final Condition condition;
    private final JFrame frame;
    private int count = 0;
    private static final MySocket socket = new MySocket();
    private static Mode mode;

    public Computer(JFrame frame, Lock lock, Condition condition) throws IOException {
        this.lock = lock;
        this.condition = condition;
        this.frame = frame;
    }

    public void init() {
        count = 0;
    }

    public static void setMode(Mode mode) {
        Computer.mode = mode;
        if (mode != Mode.OUTER_AI) {
            socket.close();
        }
    }

    public static Mode getMode() {
        return mode;
    }

    public void countReduce() {
        count--;
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
                Pair<ChessPiece, Point> blackMove = new Pair<>(null, null);
                long startTime = System.currentTimeMillis();
                if (mode == Mode.INNER_AI) {
                    if (count % 80 == 0) {
                        blackMove = Strategy.randomDecide();
                    }
                    else if (count % 60 == 0) {
                        blackMove = Strategy.decide(3);
                    }
                    else {
                        blackMove = Strategy.decide(4);
                    }
                    if (getBoard().isCurMoveWhite()) {
                        blackMove = Strategy.monteCarloDecide(2, 2);
                    }
                }
                else if (mode == Mode.OUTER_AI) {
                    try {
                        blackMove = socket.queryStep(getBoard().getLastMove());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                long endTime = System.currentTimeMillis();
                if (endTime - startTime < 2000) {
                    try {
                        sleep(2000 - (endTime - startTime));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                ChessPiece piece = blackMove.getKey();
                Point target = blackMove.getValue();

                ChessCell cell = MainPanel.getChess().get(getIndex(piece.getPoint()));
                cell.setBorder(BorderFactory.createLineBorder(Color.BLUE, 3));
                cell = MainPanel.getChess().get(getIndex(target));
                cell.setBorder(BorderFactory.createLineBorder(Color.BLUE, 3));
                PieceEvent blackEvent = getBoard().acMove(piece, target);
                count++;
                handleBlackEvent(blackEvent);

                MainPanel.render();
                Board.getBoard().setCurMoveWhite(true);
            }
            lock.unlock();
        }
    }

    public void handleBlackEvent(PieceEvent event) {
        if (event == PieceEvent.IN_CHECK) {
            MainPanel.showCheck(true);
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
