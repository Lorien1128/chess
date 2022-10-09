package gui;

import javafx.util.Pair;
import piece.ChessPiece;
import util.Board;
import util.Mode;
import util.PieceEvent;
import util.Point;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class Player extends Thread {
    private final MyGui frame;
    private Pair<ChessPiece, Point> move;
    private final Lock lock;
    private final Condition condition;
    private Condition toWakeUp = null;
    private final Condition computerCondition;
    private boolean needComputer;

    public Player(MyGui frame, Lock lock, Condition condition,
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
            if (getBoard().isCurMoveWhite() == frame.isWhite()) {
                Pair<ChessPiece, Point> m = getMove();
                if (Computer.getMode() == Mode.BOTH_PLAYER) {
                    frame.getPanel().showStepToOther(m);
                    frame.setState(!frame.isWhite());
                    frame.getOtherPlayer().setState(!frame.isWhite());
                }
                PieceEvent whiteEvent = getBoard().acMove(m.getKey(), m.getValue());
                frame.getPanel().render();
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
            frame.getPanel().showCheck(!frame.isWhite());
        }
        else if (event == PieceEvent.CHECKMATED) {
            new MyDialog("对方被将死！", frame, true);
            if (getBoard().isTwoPlayers()) {
                new MyDialog("你被将死了！", frame.getOtherPlayer(), true);
            }
        }
        else if (event == PieceEvent.DRAW) {
            new MyDialog("你被对方逼和了！", frame, true);
            if (getBoard().isTwoPlayers()) {
                new MyDialog("你将对方逼和了！", frame.getOtherPlayer(), true);
            }
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
