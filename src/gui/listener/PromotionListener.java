package gui.listener;

import gui.MainPanel;
import gui.MyDialog;
import piece.ChessPiece;
import util.Board;
import util.Render;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class PromotionListener implements ActionListener {
    private final int num;
    private final MyDialog dialog;
    private final ChessPiece chessPiece;
    private final Lock lock;
    private final Condition toWakeUp;
    private final MainPanel panel;

    public PromotionListener(int num, MyDialog dialog, ChessPiece chessPiece,
                             Lock lock, Condition toWakeUp, MainPanel panel) {
        this.num = num;
        this.dialog = dialog;
        this.chessPiece = chessPiece;
        this.lock = lock;
        this.toWakeUp = toWakeUp;
        this.panel = panel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Board board = Board.getBoard();
        board.pawnPromotion(num, chessPiece);
        Render render = new Render(panel);
        render.start();
        MyDialog dialog = new MyDialog("兵升变成功！", 2, this.dialog);
        Thread thread = new Thread(dialog);
        thread.start();
        if (!Board.getBoard().isTwoPlayers()) {
            lock.lock();
            board.setCurMoveWhite(!this.dialog.isWhite());
            toWakeUp.signal();
            lock.unlock();
        }
        this.dialog.setVisible(false);
        this.dialog.dispose();
    }
}
