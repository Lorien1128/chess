package gui;

import gui.listener.PromotionListener;
import piece.ChessPiece;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.WindowConstants;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class MyDialog extends JDialog implements Runnable {
    private final JLabel note;
    private final int time;

    public MyDialog(String text, int time, Component frame) {
        setVisible(false);
        this.time = time;
        setBounds(200, 200, 200, 120);
        setResizable(false);
        setLocationRelativeTo(frame);
        setTitle("提示");
        Container container = getContentPane();
        container.setLayout(new GridLayout(2, 1, 0, 0));
        JLabel label = new JLabel(text, JLabel.CENTER);
        label.setFont(new Font("微软雅黑", Font.BOLD, 20));
        label.setBackground(Color.RED);
        container.add(label);
        note = new JLabel("", JLabel.CENTER);
        note.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        container.add(note);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }

    public MyDialog(String text, JFrame frame, ChessPiece chessPiece,
                    Lock lock, Condition toWakeUp) {
        super(frame);
        note = new JLabel();
        time = 0;
        setResizable(false);
        setVisible(false);
        setBounds(200, 200, 360, 180);
        setLocationRelativeTo(frame);
        setTitle("提示");
        Container container = getContentPane();
        //container.setLayout(new GridLayout(2, 1, 0, 0));
        container.setLayout(null);
        JLabel label = new JLabel(text, JLabel.CENTER);
        label.setFont(new Font("微软雅黑", Font.BOLD, 20));
        label.setBounds(0, 10, 340, 50);
        container.add(label);
        String[] texts = {"1.马", "2.象", "3.车", "4.后"};
        JButton[] buttons = new JButton[4];
        for (int i = 0; i < 4; i++) {
            buttons[i] = new JButton();
            buttons[i].setBounds(20 + 80 * i, 70, 60, 40);
            buttons[i].setText(texts[i]);
            buttons[i].addActionListener(new PromotionListener(i + 1, this, chessPiece,
                    lock, toWakeUp));
            container.add(buttons[i]);
        }
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setVisible(true);
        //System.out.println("完成构建");
    }

    public MyDialog(String text, Component frame, boolean exit) {
        setVisible(false);
        this.time = 0;
        setBounds(200, 200, 200, 120);
        setResizable(false);
        setLocationRelativeTo(frame);
        setTitle("提示");
        Container container = getContentPane();
        container.setLayout(new GridLayout(2, 1, 0, 0));
        //container.setLayout(null);
        JLabel label = new JLabel(text, JLabel.CENTER);
        label.setFont(new Font("微软雅黑", Font.BOLD, 20));
        label.setBackground(Color.RED);
        label.setBounds(20, 0, 160, 60);
        container.add(label);
        note = null;
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        JButton buttons = new JButton();
        buttons.setBounds(80, 100, 40, 40);
        buttons.setText("确定");
        buttons.setFont(new Font("微软雅黑", Font.BOLD, 15));
        if (exit) {
            buttons.addActionListener(e -> System.exit(0));
        }
        container.add(buttons);
        setVisible(true);
    }

    @Override
    public void run() {
        setVisible(true);
        int remain = time;
        while (remain != 0) {
            note.setText("还剩" + remain + "秒自动关闭");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            remain--;
        }
        setVisible(false);
        dispose();
    }
}
