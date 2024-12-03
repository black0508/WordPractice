package Util;

import Data.DataManager;
import Data.UserData;
import Data.WordData;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Tool {
    public static UserData myUser;
    public static String url = "jdbc:mysql://localhost:3306/wordpractice?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    //这是一个工具类，里面封装了所有的静态方法静态对象，包括我的角色
    public static Font font = new Font("SimSun", Font.PLAIN, 46);

    public static Font font1 = new Font("SimSun", Font.PLAIN, 26);
    public static Font font2 = new Font("SimSun", Font.PLAIN, 18);
    public static Font font2_kt =new Font("楷体",Font.BOLD,26);
    public static Font font3_kt =new Font("宋体",Font.BOLD,20);

    public static ArrayList<WordData> wordArr = new ArrayList<>();
    public static int getInd(){
        return wordArr.size();
    } //列表的index

    //洗牌算法
    public static void shuffleArray(int[] p){
        shuffleArray(p,0);
    }
    public static void shuffleArray(int[] p,int k){ //到下标几停止洗牌
        int n = p.length;
        // 初始化数组 p，使其内容为 [1, 2, ..., n]
        for (int i = 0; i < n; i++) {
            p[i] = i;
        }

        // 使用 洗牌算法 打乱数组 p 的顺序
        for (int i = n - 1; i > k; i--) {
            int r = (int) (Math.random() * (i + 1)); // 从 [0, i] 随机选一个索引
            int temp = p[i];
            p[i] = p[r];
            p[r] = temp;
        }
    }

    //对单词表的修改，因为单词表就一个，而且也是单线程操作，所以在Tool类里面修改即可
    public static boolean AddWord(WordData wordData){
        if(myUser == null) return false;

        //数据库参数
        String sql = "INSERT INTO user_words (user_id,word_id) VALUES (?, ?)";
        try{
            //Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url,"root","123456");
            PreparedStatement st = conn.prepareStatement(sql);
            st.setInt(1,Tool.myUser.ID);
            st.setInt(2,wordData.ID);
            int rowAffected = st.executeUpdate();
            System.out.printf(String.valueOf(rowAffected));
            if(rowAffected > 0){
                wordArr.add(wordData);
                return true;
            }else{
                return false;
            }
        } catch (SQLException  e) {
            System.out.println(e);
        }
        return false;
    }
    public static boolean RemoveWord(int index){
        if(myUser == null) return false;
        //数据库参数
        String sql = "DELETE FROM user_words WHERE word_id = ? AND user_id = ?";
        try(Connection conn = DriverManager.getConnection(url,"root","123456");
            PreparedStatement st = conn.prepareStatement(sql)) {

            st.setInt(1,wordArr.get(index).ID);
            st.setInt(2,myUser.ID);

            int rowAffected = st.executeUpdate();
            if(rowAffected > 0){
                wordArr.remove(index);
                return true;
            }else{
                return false;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void loadWord(){
        //TODO: 后期要改成数据库来加载(登陆系统)

        wordArr.clear();
        if (myUser != null) {
            wordArr = myUser.wordList;
        }
//        File dictionaryFile = new File("dictionary.txt");
//
//        // 读取字典文件
//        try (BufferedReader reader = new BufferedReader(new FileReader(dictionaryFile))) {
//            String word;
//            while ((word = reader.readLine()) != null) {
//                wordArr.add(word);
//            }
//        } catch (IOException e) {
//            System.out.println(e);
//        }
    }



    /**
     * 显示提示,loc是显示位置
     * */
    public static void showMessage(String msg,JFrame loc){
//        JOptionPane.showMessageDialog(this, msg, "信息",
//                JOptionPane.INFORMATION_MESSAGE);
        showMessage(msg,-1,loc);
    }

    public static void showMessage(String msg,int time,JFrame loc){
        // 弹出消息框并显示信息
        JDialog dialog = new JDialog(loc,"信息");
        dialog.setResizable(false);
        JPanel jp = new JPanel();
        loc.setEnabled(false);
        dialog.setLocationRelativeTo(loc);
        dialog.setLayout(new BorderLayout(100,100));
        dialog.setSize(280,time == -1 ? 80 : 100);
        dialog.add(jp);

        JLabel tips = new JLabel(msg);
        jp.add(tips,BorderLayout.NORTH);
        dialog.setVisible(true);

        JLabel remainTime = new JLabel();
        jp.add(remainTime,BorderLayout.NORTH);


        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    int t = time;
                    while (t >= 0) {
                        // 每秒更新消息
                        String message = " 剩余时间: " + t + "秒";
                        System.out.println(t);
                        // 更新消息框中的信息

                        SwingUtilities.invokeLater(() -> {
                            remainTime.setText(message);
                            dialog.repaint();  // 强制重绘对话框
                        });
                        // 等待1秒钟
                        Thread.sleep(1000);
                        t--;
                    }

                    // 时间结束后关闭对话框
                    SwingUtilities.invokeLater(() -> {
                        dialog.setVisible(false);  // 关闭对话框
                        loc.setEnabled(true);
                    });
                } catch (InterruptedException e) {
                    // 线程被中断时处理
                    System.out.println("倒计时线程被中断");
                }
            }
        });
        dialog.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {

            }
            @Override
            public void windowClosing(WindowEvent e) {
                th.interrupt();
                loc.setEnabled(true);
            }
            @Override
            public void windowClosed(WindowEvent e) {
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

       if(time != -1) th.start();


    }

}
