package SubWindow;

import Data.UserData;
import Data.WordData;
import Util.Tool;
import javafx.scene.input.KeyCode;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.security.Key;
import java.util.ArrayList;

public class DicWindow extends JFrame {
    private JTextField wordInputField;
    private JButton addWordButton;
    private JList<String> wordList;
    private DefaultListModel<String> wordListModel;


    public DicWindow() {
        wordListModel = new DefaultListModel<>();
        InitWordList();
        // 设置字典窗口属性
        setTitle("单词目录");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // 创建输入面板
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout());

        JLabel wordLabel = new JLabel("输入一个单词 ");
        wordLabel.setFont(Tool.font1);
        wordInputField = new JTextField(25);
        wordInputField.setFont(Tool.font1);
        addWordButton = new JButton("添加单词");
        addWordButton.setFont(Tool.font1);

        inputPanel.add(wordLabel);
        inputPanel.add(wordInputField);
        inputPanel.add(addWordButton);

        add(inputPanel, BorderLayout.NORTH);

        // 创建字典目录显示区域
        wordList = new JList<>(wordListModel);
        wordList.setFont(Tool.font1);
        wordList.addKeyListener(new KeyListener() {
            public void keyTyped(KeyEvent e) {

            }

            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_DELETE){
                    removeWordToDictionary();
                }
            }

            public void keyReleased(KeyEvent e) {

            }
        });
        JScrollPane scrollPane = new JScrollPane(wordList);
        add(scrollPane, BorderLayout.CENTER);

        // 按钮点击事件：添加单词
        addWordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String word = wordInputField.getText().trim();
                if (!word.isEmpty()) {
                    WordData wordData = new WordData(word);
                    try {
                        addWordToDictionary(wordData);
                    }catch(Exception ec){
                        JOptionPane.showMessageDialog(DicWindow.this, "写入失败", "信息", JOptionPane.INFORMATION_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(DicWindow.this, "请输入正确的单词", "单词错误", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

    }

    // 添加单词到字典并更新显示
    private void addWordToDictionary(WordData wordData) {
        if(Tool.AddWord(wordData)){
            JOptionPane.showMessageDialog(this, "写入成功", "信息", JOptionPane.INFORMATION_MESSAGE);
            // 更新列表显示
            wordListModel.addElement(Tool.getInd() + " - " + wordData.word);

            // 清空输入框
            wordInputField.setText("");
        }else{
            JOptionPane.showMessageDialog(this, "写入失败", "信息", JOptionPane.INFORMATION_MESSAGE);
        }


    }
    private void InitWordList(){

        wordListModel.clear();
        int i = 1;
        for(WordData w : Tool.wordArr){
            wordListModel.addElement(i++ + " - " + w.word);
        }
    }

    private void removeWordToDictionary(){
        String selectedWord = wordList.getSelectedValue();
        if(selectedWord != null) {

            String[] kv = selectedWord.split("-");
            System.out.println(kv[0].trim());
            int indx = Integer.parseInt(kv[0].trim());
            Tool.RemoveWord(indx - 1);
            InitWordList();
        }else{
            JOptionPane.showMessageDialog(this, "请你选中要删除的", "信息", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}