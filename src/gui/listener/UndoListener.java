package gui.listener;

import gui.MainPanel;
import util.Board;
import util.Point;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class UndoListener implements ActionListener {
    private final MainPanel panel;

    public UndoListener(MainPanel panel) {
        this.panel = panel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ArrayList<Point> changes = Board.getBoard().undo();
        if (changes != null) {
            panel.showUndo(changes);
            panel.init();
            panel.reduceCompCount();
        }
        panel.render();
    }
}
