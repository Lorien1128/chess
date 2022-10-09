package gui.listener;

import gui.MainPanel;
import util.Board;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RestartListener implements ActionListener {
    private final MainPanel panel;

    public RestartListener(MainPanel panel) {
        this.panel = panel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Board board = Board.getBoard();
        if (board.isCurMoveWhite()) {
            board.init();
            panel.init();
            panel.getComputer().init();
            panel.render();
        }
    }
}
