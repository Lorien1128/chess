package gui.listener;

import gui.Computer;
import gui.MyDialog;
import util.Mode;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ModeListener implements ActionListener {
    private final Mode mode;
    private final MyDialog dialog;

    public ModeListener(int num, MyDialog dialog) {
        mode = Mode.values()[num];
        this.dialog = dialog;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Computer.setMode(mode);
        dialog.dispose();
    }
}
