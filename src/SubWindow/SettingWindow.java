package SubWindow;


import Util.Tool;

import javax.swing.*;
import java.awt.*;

public class SettingWindow extends JFrame {

    public JCheckBox randomSelected; //随机选词
    public JLabel randomTimeLabel;
    public JTextField randomTime; //随机时间
    public SettingWindow(){
        setTitle("设置");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(null);

        randomSelected = new JCheckBox("随机选词是否写入输写框");
        randomSelected.setFont(Tool.font3_kt);
        randomSelected.setHorizontalTextPosition(SwingConstants.LEFT);
        randomSelected.setBounds(20, 25, 400, 25);
        randomSelected.setOpaque(false);
        mainPanel.add(randomSelected);

        randomTimeLabel = new JLabel("随机选词时间(-1为不限时间)");
        randomTimeLabel.setFont(Tool.font3_kt);
        randomTimeLabel.setBounds(20, 60, 400, 25);
        mainPanel.add(randomTimeLabel);

        randomTime = new JTextField(5);
        randomTime.setFont(Tool.font2);
        randomTime.setBounds(300, 60, 30, 25);
        mainPanel.add(randomTime);

        add(mainPanel);
    }
    public boolean getSelectedRandom(){
        return randomSelected.isSelected();
    }
    public int getrandomTime(){
        if(!randomTime.getText().isEmpty())
            return Integer.parseInt(randomTime.getText());
        return -1;
    }
}
