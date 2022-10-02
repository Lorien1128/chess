package gui;

import util.Board;

import javax.swing.JButton;
import javax.swing.JFrame;
import java.awt.Color;
import java.awt.Font;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class ChessCell extends JButton {
    private final int posX;
    private final int posY;

    public ChessCell(int index, JFrame frame, Player player, Computer computer, Lock lock,
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
        this.addActionListener(new CellListener(index, frame, player, computer, lock,
                playerCondition, computerCondition));
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }
}
