package gui.listener;

import gui.Computer;
import gui.MyDialog;
import util.Mode;

import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PvpRoleListener implements ActionListener {
    private final Mode mode;
    private final MyDialog dialog;

    public PvpRoleListener(int num, MyDialog dialog) {
        mode = Mode.values()[num];
        this.dialog = dialog;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Computer.setMode(mode);
        String [] possiblePorts = { "8080", "8081", "9090"};
        if (mode == Mode.PVP_SERVER) {
            int port = Integer.parseInt((String) JOptionPane.showInputDialog(dialog,
                    "选择房间创建端口", "",JOptionPane.QUESTION_MESSAGE, null,
                    possiblePorts, possiblePorts[0]));
            System.out.println(port);
            Computer.setPort(port);
            dialog.dispose();
        }
        else {
            String host = JOptionPane.showInputDialog(dialog, "输入连接房间IP地址"
                    , "", JOptionPane.QUESTION_MESSAGE);
            int port = Integer.parseInt((String) JOptionPane.showInputDialog(dialog,
                    "选择房间连接端口", "",JOptionPane.QUESTION_MESSAGE, null,
                    possiblePorts, possiblePorts[0]));
            System.out.println(host + " " + port);
            Computer.setHost(host);
            Computer.setPort(port);
            dialog.dispose();
        }
    }
}
