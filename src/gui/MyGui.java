package gui;

import gui.listener.RestartListener;
import gui.listener.UndoListener;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.JFrame;
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

        JMenuBar bar = new JMenuBar();
        bar.setBounds(0, 0, 600, 100);

        JMenuItem undo = new JMenuItem("悔棋");
        undo.addActionListener(new UndoListener(mainPanel));
        bar.add(undo);

        JMenuItem restart = new JMenuItem("新游戏");
        restart.addActionListener(new RestartListener(mainPanel));
        bar.add(restart);

        this.setJMenuBar(bar);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);
    }
}
