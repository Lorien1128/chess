package gui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class MyGUI extends JFrame {

    public MyGUI() {
        this.setLayout(new GridLayout());
        JLabel label = new JLabel("lab");
        MainPanel mainPanel = new MainPanel(this);
        this.add(mainPanel, BorderLayout.PAGE_END);
        this.setBounds(400, 50, 600, 600);
        setResizable(true);
        setTitle("国际象棋 by aokmy");

        Toolkit kit = Toolkit.getDefaultToolkit();
        //Image image = kit.getImage("src//1.png"); //设置窗口图标路径
        //setIconImage(image); //换掉窗体样式
        JEditorPane jee = new JEditorPane();
        JMenuBar jr = new JMenuBar();//创建菜单条对象

        JMenu jm = new JMenu("文件");//新建项
        JMenuItem ji = new JMenuItem();//创建菜单项对象
        ji.setText("新建");
        ji.setFont(new Font("楷体", Font.BOLD, 20));
        ji.addActionListener(e -> {
            jee.setText("请输入内容: ");//提示
            jee.setSize(836, 908);//设置宽高
            add(jee);//添加当前对象
        });
        this.add(ji);


        JMenu j1 = new JMenu(); //创建菜单对象
        j1.setText("编辑");
        JMenuItem jit = new JMenuItem();//创建菜单项对象
        jit.setText("查找");

        JMenu j2 = new JMenu(); //创建菜单格式对象
        j2.setText("格式");
        JMenuItem zt = new JMenuItem();//创建菜单项对象
        zt.setText("字体");

        JMenu j3 = new JMenu(); //创建菜单查看对象
        j3.setText("查看");
        JMenuItem ztl = new JMenuItem();//创建菜单项对象
        ztl.setText("状态栏");
        JMenu j4 = new JMenu(); //创建菜单帮助对象
        j4.setText("帮助");
        JMenuItem jt = new JMenuItem();//创建菜单项对象
        jt.setText("关于记事本");

        jm.add(ji);//把新建添加到菜单
        jr.add(jm);//把菜单加载到菜单条
        jr.add(j1);//把编辑添加到菜单条
        j1.add(jit);//将查找添加到编辑
        jr.add(j2);//把格式添加到菜单条
        jr.add(j3);//把查看添加到菜单条
        jr.add(j4);//把帮助添加到菜单条
        j4.add(jt);//把关于记事本添加j4
        j2.add(zt);//将字体加入到j2
        j3.add(ztl);//将菜单项加入到菜单
        setDefaultCloseOperation(EXIT_ON_CLOSE);// 用户点击窗口关闭
        //this.setJMenuBar(jr);
        this.setVisible(true);//设置是否窗口显示
    }
}
