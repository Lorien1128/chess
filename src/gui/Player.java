package gui;

import javafx.util.Pair;
import piece.ChessPiece;
import util.Board;
import util.PieceEvent;
import util.Point;

import javax.swing.JFrame;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class Player extends Thread {
    private final JFrame frame;
    private Pair<ChessPiece, Point> move;
    private final Lock lock;
    private final Condition condition;
    private Condition toWakeUp = null;
    private final Condition computerCondition;
    private boolean needComputer;

    public Player(JFrame frame, Lock lock, Condition condition,
                  Condition computerCondition) {
        this.frame = frame;
        this.lock = lock;
        this.condition = condition;
        this.computerCondition = computerCondition;
    }

    public void setToWakeUp(Condition toWakeUp) {
        synchronized (this) {
            this.toWakeUp = toWakeUp;
        }
    }

    public Board getBoard() {
        return Board.getBoard();
    }

    public void init() {
        setNeedComputer(true);
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
            //System.out.println("player is awake");
            if (getBoard().isCurMoveWhite()) {
                Pair<ChessPiece, Point> m = getMove();
                PieceEvent whiteEvent = getBoard().acMove(m.getKey(), m.getValue());
                MainPanel.render();
                handleWhiteEvent(whiteEvent, m.getKey());
            }
            toWakeUp.signal();
            //System.out.println("玩家释放锁");
            lock.unlock();
        }
    }

    public void handleWhiteEvent(PieceEvent event, ChessPiece piece) {
        setNeedComputer(true);
        if (event == PieceEvent.PAWN_PROMOTION) {
            setNeedComputer(false);
            new MyDialog("请选择兵升变目标", frame, piece, lock, computerCondition);
        }
        else if (event == PieceEvent.IN_CHECK) {
            MainPanel.showCheck(false);
        }
        else if (event == PieceEvent.CHECKMATED) {
            new MyDialog("对方被将死！",frame, true);
        }
        else if (event == PieceEvent.DRAW) {
            new MyDialog("对方被逼和！",frame, true);
        }
    }

    public void setMove(ChessPiece chessPiece, Point point) {
        synchronized (this) {
            this.move = new Pair<>(chessPiece, point);
        }
    }

    public Pair<ChessPiece, Point> getMove() {
        synchronized (this) {
            return move;
        }
    }

    public void setNeedComputer(boolean needComputer) {
        synchronized (this) {
            this.needComputer = needComputer;
        }
    }

    public boolean isNeedComputer() {
        synchronized (this) {
            return needComputer;
        }
    }
}
