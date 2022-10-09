package gui;

import gui.listener.RestartListener;
import gui.listener.UndoListener;
import javafx.util.Pair;
import util.Board;
import util.Mode;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class MyGui extends JFrame {
    private final boolean white;
    private boolean chooseFrom;
    private Pair<Integer, Integer> fromWhere;
    private final MainPanel panel;
    private MyGui otherPlayer;
    private JLabel state;

    public MyGui(boolean white) {
        this.chooseFrom = false;
        this.white = white;
        this.setLayout(new GridLayout());
        MainPanel mainPanel = new MainPanel(this);
        this.add(mainPanel, BorderLayout.PAGE_END);
        panel = mainPanel;
        if (white) {
            this.setBounds(500, 50, 600, 600);
        }
        else {
            this.setBounds(200, 50, 600, 600);
        }
        setResizable(true);
        setTitle("国际象棋 by aokmy");

        if (white) {
            new MyDialog("请选择游戏模式", this);
        }

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

        String state = "白方回合";
        JLabel jlabel = new JLabel(state, JLabel.CENTER);
        if (Board.getBoard().isTwoPlayers()) {
            bar.add(jlabel);
            this.state = jlabel;
        }

        this.setJMenuBar(bar);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    public void setOtherPlayer(MyGui otherPlayer) {
        this.otherPlayer = otherPlayer;
    }

    public MyGui getOtherPlayer() {
        return otherPlayer;
    }

    public boolean isWhite() {
        return white;
    }

    public void setFromWhere(Pair<Integer, Integer> fromWhere) {
        this.fromWhere = fromWhere;
    }

    public void setChooseFrom(boolean chooseFrom) {
        this.chooseFrom = chooseFrom;
    }

    public Pair<Integer, Integer> getFromWhere() {
        return fromWhere;
    }

    public boolean isChooseFrom() {
        return chooseFrom;
    }

    public MainPanel getPanel() {
        return panel;
    }

    public void setState(boolean white) {
        if (white) {
            state.setText("白方回合");
        }
        else {
            state.setText("黑方回合");
        }
    }
}
