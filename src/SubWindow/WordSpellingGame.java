package SubWindow;

import Data.DataManager;
import Data.UserData;
import Util.Tool;

import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import javax.swing.*;

public class WordSpellingGame extends JFrame implements FocusListener, KeyListener {

    public static WordSpellingGame instance; //单例模式，可以学下
    private TextField word; // 输入单词
    private JLabel tipInput; // 提示输入："请输入一个英文单词"
    private JButton enterButton; // 确定按钮，添加监听
    private JPanel panelNorth;
    private JLabel tipDown; // 下方操作提示语
    private JLabel tipCount; // 提示次数
    private JPanel panelSouth;
    private String strInput;

    private int n = 0;
    private int moveCount = 0; //用了几次
    private JPanel panelCenter;
    private JButton[] bx = new JButton[15]; // 存放打乱后的单词字母

    private JMenuBar menubar;
    private JMenu menu;
    private JMenuItem openDic;
    private JMenuItem openSet;
    private JMenuItem randomWord; //随机获取单词
    // 创建字体
    DicWindow dicWindow;
    SettingWindow setWindow;

    WordSpellingGame() {
        instance = this;
        menubar = new JMenuBar();
        setJMenuBar(menubar);
        menu = new JMenu("菜单");
        menu.setFont(Tool.font2);
        openDic = new JMenuItem("单词字典");
        openDic.setFont(Tool.font2);
        openSet = new JMenuItem("设置");
        openSet.setFont(Tool.font2);
        randomWord = new JMenuItem("随机单词");
        randomWord.setFont(Tool.font2);
        menu.add(openDic);
        menu.add(openSet);
        menu.add(randomWord);
        menubar.add(menu);
        //为菜单们添加点击监听
        addActionInMenu();
        //启用定时器来更新时间
        Timer timer = new Timer(1000, e -> updateTime());
        timer.start();

        word = new TextField(15);
        word.setFont(Tool.font2);
        //为word添加按键
        addWordKey();
        tipInput = new JLabel("请输入一个英文单词");
        tipInput.setFont(Tool.font1);
        enterButton = new JButton("确定");
        enterButton.setFont(Tool.font1);
        enterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startChange(); // 点击确定打乱单词顺序
            }
        });
        panelNorth = new JPanel();
        panelNorth.add(tipInput);
        panelNorth.add(word);
        panelNorth.add(enterButton);
        add(panelNorth, BorderLayout.NORTH); // 放在布局北侧

        tipDown = new JLabel("用鼠标单击字母，按左右箭头交换字母，将其排列成所输入的单词");
        tipDown.setFont(Tool.font2);
        tipCount = new JLabel("    当前移动次数：" + moveCount);
        tipCount.setFont(Tool.font1);
        panelSouth = new JPanel();
        panelSouth.add(tipDown,BorderLayout.CENTER);
        panelSouth.add(tipCount,BorderLayout.EAST);
        add(panelSouth, BorderLayout.SOUTH); // 放在布局南侧

        dicWindow = new DicWindow();
        setWindow = new SettingWindow();


        addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {

            }

            @Override
            public void windowClosing(WindowEvent e) {
                if(DataManager.SaveUsers()){
                    System.out.printf("窗口关闭，保存成功");
                }else{
                    System.out.printf("窗口关闭，保存失败");
                }
            }

            @Override
            public void windowClosed(WindowEvent e) {
                System.out.println(666);
            }

            @Override
            public void windowIconified(WindowEvent e) {

            }

            @Override
            public void windowDeiconified(WindowEvent e) {

            }

            @Override
            public void windowActivated(WindowEvent e) {

            }

            @Override
            public void windowDeactivated(WindowEvent e) {

            }
        });
    }

    private void addActionInMenu() {
        openDic.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(dicWindow == null){
                    dicWindow = new DicWindow();
                    setWindow.setVisible(true);
                }else{
                    dicWindow.setVisible(true);
                }
            }
        });
        openSet.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(setWindow == null){
                    setWindow = new SettingWindow();
                    setWindow.setVisible(true);
                }else{
                    setWindow.setVisible(true);
                }
            }
        });
        randomWord.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(Tool.wordArr.isEmpty()){
                    Tool.showMessage("请你为字典添加单词，里面的单词是空的哦",instance);
                }else{
                    //封装好的洗牌算法来随机选
                    int[] p = new int[Tool.getInd()];
                    Tool.shuffleArray(p);
                    //直接写入文本框
                    if(setWindow.getSelectedRandom())
                        word.setText(Tool.wordArr.get(p[0]).word);
                    else { //不写入文本框
                        word.setText(Tool.wordArr.get(p[0]).word);
                        startChange();
                        Tool.showMessage("当前单词是>>>  " + Tool.wordArr.get(p[0]).word + "  <<<请你快速记忆他！",setWindow.getrandomTime(),instance);
                    }
                }
            }
        });
    }

    //主面板(输入框)按键
    private void addWordKey() {
        word.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    startChange(); // 按下Enter键进入打乱
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
    }

    //开始打乱
    public void startChange() {

        try { // 这里是为了输入新的单词时，移除旧的单词
            for (int i = 0; i < n; i++) {
                panelCenter.remove(bx[i]);
            }

        } catch (Exception e2) {
            e2.printStackTrace();
        }
        setMoveCount(0);
        strInput = word.getText(); // "happy"
        n = strInput.length(); // 单词长度：5
        if(panelCenter == null)
            panelCenter = new JPanel();

        panelCenter.removeAll();
        // 打乱顺序
        // 创建一个乱序数组 p
        int[] p = new int[n];
        //洗牌算法随机排序
        Tool.shuffleArray(p);



        // 遍历乱序数组，生成按钮
        for (int i = 0; i < n; i++) {
            char c = strInput.charAt(p[i]); // 根据乱序索引获取字符
            bx[i] = new JButton("" + c); // 创建按钮并设置文本
            bx[i].setPreferredSize(new Dimension(80, 80)); // 设置按钮大小
            bx[i].setFont(Tool.font); // 设置字体
            bx[i].setForeground(Color.black); // 设置字体颜色
            bx[i].addFocusListener(this); // 添加焦点监听器
            bx[i].addKeyListener(this); // 添加按键监听器
            panelCenter.add(bx[i]); // 添加按钮到面板
            add(panelCenter,BorderLayout.CENTER);
        }
        word.setText(null);

        panelCenter.revalidate(); // 重新布局
        panelCenter.repaint(); // 重绘
        setVisible(true);

    }


    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        for (int i = 0; i < strInput.length(); i++) {
            if (bx[i] == (JButton) e.getSource()) {
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    swapButton(i, "left");
                    char[] str2 = new char[n];
                    for (int k = 0; k < n; k++) {
                        str2[k] = bx[k].getText().charAt(0);
                    }
                    String s1 = new String(str2);
                    judgeSuccess();
                } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    swapButton(i, "right");
                    judgeSuccess();
                }
            }
        }
    }

    private void judgeSuccess() {
        char[] str2 = new char[n];
        for (int k = 0; k < n; k++) {
            str2[k] = bx[k].getText().charAt(0);
        }
        String s1 = new String(str2);
        if (s1.equals(strInput)) {
            tipDown.setText("恭喜你，你成功了！");
            for (int p = 0; p < n; p++) {
                bx[p].setForeground(Color.green);
            }
        }
    }


    @Override
    public void keyReleased(KeyEvent e) {

    }

    public void swapButton(int i, String st1) {
        setMoveCount(moveCount + 1);; //移动次数+1
        if (st1.equals("left") && i >= 1) {
            String temp = bx[i].getText();
            String temp1 = bx[i - 1].getText();
            bx[i].setText(temp1);
            bx[i - 1].setText(temp);
            bx[i - 1].requestFocus();
        } else if (st1.equals("right") && i <= n - 2) {
            String temp = bx[i].getText();
            String temp1 = bx[i + 1].getText();
            bx[i].setText(temp1);
            bx[i + 1].setText(temp);
            bx[i + 1].requestFocus();
        }
    }

    //当焦点获得时
    @Override
    public void focusGained(FocusEvent e) {
        for (int i = 0; i < strInput.length(); i++) {
            if (bx[i] == (JButton) e.getSource()) {
                bx[i].setForeground(Color.red);
            }
        }
        judgeSuccess();
    }

    //当焦点丢失时
    @Override
    public void focusLost(FocusEvent e) {
        for (int i = 0; i < strInput.length(); i++) {
            if (bx[i] == (JButton) e.getSource()) {
                bx[i].setForeground(Color.black);
            }
        }
        judgeSuccess();
    }
    //更新时间
    private void updateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(java.util.TimeZone.getTimeZone("GMT+8"));
        String currentTime = sdf.format(new java.util.Date());
        setTitle("英语单词拼写训练"+ currentTime);
    }

    private void setMoveCount(int c){
        moveCount = c;
        tipCount.setText("    当前移动次数：" + moveCount);
    }

}