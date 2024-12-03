package SubWindow;

import Data.DataManager;
import Data.DataManagerSQL;
import Data.UserData;
import Util.Tool;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginWindow extends JFrame {
    public LoginWindow() {
        // 设置窗口标题和大小
        setTitle("登录窗口");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // 关闭此窗口不退出程序
        setLocationRelativeTo(null); // 居中显示

        // 创建面板
        JPanel panel = new JPanel();
        panel.setLayout(null);
        add(panel);

        // 用户名标签
        JLabel userLabel = new JLabel("用户名:");
        userLabel.setBounds(40, 50, 80, 25);
        userLabel.setFont(Tool.font3_kt);
        panel.add(userLabel);

        // 用户名输入框
        JTextField userText = new JTextField(20);
        userText.setBounds(140, 50, 200, 30);
        userText.setFont(Tool.font3_kt);
        panel.add(userText);

        // 密码标签
        JLabel passwordLabel = new JLabel("密码:");
        passwordLabel.setBounds(40, 100, 80, 25);
        passwordLabel.setFont(Tool.font3_kt);
        panel.add(passwordLabel);

        // 密码输入框
        JPasswordField passwordText = new JPasswordField(20);
        passwordText.setBounds(140, 100, 200, 30);
        panel.add(passwordText);

        // 登录按钮
        JButton loginButton = new JButton("登录");
        loginButton.setBounds(50, 160, 120, 25);
        loginButton.setFont(Tool.font3_kt);
        panel.add(loginButton);

        // 注册按钮
        JButton registerButton = new JButton("注册");
        registerButton.setBounds(210, 160, 120, 25);
        registerButton.setFont(Tool.font3_kt);
        panel.add(registerButton);

        // 按钮功能实现
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = userText.getText();
                String password = new String(passwordText.getPassword());
                UserData user = DataManagerSQL.Login(username,password);
                if (user != null) {
                    JOptionPane.showMessageDialog(panel, "登录成功！");
                    dispose();
                    Tool.myUser = user;
                    Tool.loadWord();
                    InitWordSpellingGame();

                } else {
                    JOptionPane.showMessageDialog(panel, "用户名或密码错误", "错误", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String newUserName = userText.getText();
                String newPassword = new String(passwordText.getPassword());
                if(!DataManagerSQL.Register(newUserName,newPassword)){
                    JOptionPane.showMessageDialog(panel, "用户名已被注册", "错误", JOptionPane.ERROR_MESSAGE);
                }else{
                    JOptionPane.showMessageDialog(panel, "注册成功！");
                }

            }
        });
    }

    public void InitWordSpellingGame(){
        WordSpellingGame game = new WordSpellingGame();
        game.setBounds(300, 200, 900, 350); // 设置窗体大小
        game.setResizable(false);  //设置窗体是否可以被调整大小
        game.setVisible(true);
        game.setTitle("英语单词拼写训练");
        game.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
