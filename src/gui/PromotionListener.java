package gui;

import piece.ChessPiece;
import util.Board;
import util.Render;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PromotionListener implements ActionListener {
    private final int num;
    private final JDialog dialog;
    private final ChessPiece chessPiece;

    public PromotionListener(int num, JDialog dialog, ChessPiece chessPiece) {
        this.num = num;
        this.dialog = dialog;
        this.chessPiece = chessPiece;
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
        this.dialog.setVisible(false);
        this.dialog.dispose();
    }
}
