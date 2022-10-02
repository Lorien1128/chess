package gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import javax.swing.*;

public class MyGui extends JFrame {

    public MyGui() {
        this.setLayout(new GridLayout());
        MainPanel mainPanel = new MainPanel(this);
        this.add(mainPanel, BorderLayout.PAGE_END);
        this.setBounds(400, 50, 600, 600);
        setResizable(true);
        setTitle("国际象棋 by aokmy");

        Toolkit kit = Toolkit.getDefaultToolkit();
        /*Image image = kit.getImage("src//1.png"); //设置窗口图标路径
        setIconImage(image); //换掉窗体样式*/

        JMenuBar bar = new JMenuBar();
        bar.setBounds(0, 0, 600, 100);

        JMenuItem undo = new JMenuItem("撤销");
        undo.addActionListener(new UndoListener(mainPanel));
        bar.add(undo);

        this.setJMenuBar(bar);
        setDefaultCloseOperation(EXIT_ON_CLOSE);// 用户点击窗口关闭
        //this.setJMenuBar(jr);
        this.setVisible(true);//设置是否窗口显示
    }
}
