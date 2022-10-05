package gui.listener;

import gui.MyDialog;
import piece.ChessPiece;
import util.Board;
import util.Render;

import javax.swing.JDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class PromotionListener implements ActionListener {
    private final int num;
    private final JDialog dialog;
    private final ChessPiece chessPiece;
    private final Lock lock;
    private final Condition toWakeUp;

    public PromotionListener(int num, JDialog dialog, ChessPiece chessPiece,
                             Lock lock, Condition toWakeUp) {
        this.num = num;
        this.dialog = dialog;
        this.chessPiece = chessPiece;
        this.lock = lock;
        this.toWakeUp = toWakeUp;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Board board = Board.getBoard();
        board.pawnPromotion(num, chessPiece);
        Render render = new Render();
        render.start();
        MyDialog dialog = new MyDialog("兵升变成功！", 2, this.dialog);
        Thread thread = new Thread(dialog);
        thread.start();
        lock.lock();
        board.setCurMoveWhite(false);
        toWakeUp.signal();
        lock.unlock();
        this.dialog.setVisible(false);
        this.dialog.dispose();
    }
}
