package gui;

import gui.listener.RestartListener;
import gui.listener.UndoListener;
import util.Mode;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class MyGui extends JFrame {

    public MyGui() {
        this.setLayout(new GridLayout());
        MainPanel mainPanel = new MainPanel(this);
        this.add(mainPanel, BorderLayout.PAGE_END);
        this.setBounds(400, 50, 600, 600);
        setResizable(true);
        setTitle("国际象棋 by aokmy");

        new MyDialog("请选择游戏模式", this);

        JMenuBar bar = new JMenuBar();
        bar.setLayout(new GridLayout(1, 7, 1, 1));
        bar.setBounds(0, 0, 600, 100);

        if (Computer.getMode() != Mode.OUTER_AI) {
            JMenuItem undo = new JMenuItem("悔棋", JMenuItem.CENTER);
            undo.addActionListener(new UndoListener(mainPanel));
            bar.add(undo);

            JMenuItem restart = new JMenuItem("新游戏", JMenuItem.CENTER);
            restart.addActionListener(new RestartListener(mainPanel));
            bar.add(restart);
        }

        String[] modes = new String[]{"内置AI", "外部AI", "双人对战"};
        String text = modes[Computer.getMode().ordinal()];
        JLabel label = new JLabel("模式：" + text, JLabel.CENTER);
        bar.add(label);

        this.setJMenuBar(bar);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);
    }
}
