package gui;

import gui.listener.CellListener;
import util.Board;

import javax.swing.JButton;
import java.awt.Color;
import java.awt.Font;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class ChessCell extends JButton {
    private final int posX;
    private final int posY;
    private final CellListener listener;

    public ChessCell(int index, MyGui frame, Player player, Computer computer, Lock lock,
                     Condition playerCondition, Condition computerCondition) {
        posX = index % 8 + 1;
        posY = 8 - index / 8;
        if ((index % 2) == (index / 8 % 2)) {
            this.setBackground(Color.lightGray);
        }
        else {
            this.setBackground(Color.GRAY);
        }
        this.setBorder(null);
        String[] elements = Board.getBoard().getElements();
        this.setText(elements[index]);
        this.setFont(new Font("Segoe UI Symbol", Font.BOLD, 50));
        listener = new CellListener(index, frame, player, computer, lock,
                playerCondition, computerCondition);
        this.addActionListener(listener);
    }

    public void init() {
        listener.init();
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }
}
