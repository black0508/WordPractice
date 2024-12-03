package Data;

import Util.Tool;

import java.sql.*;
import java.util.ArrayList;

public class DataManagerSQL {
    public static UserData Login(String userName,String password){
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        try{
            //Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(Tool.url,"root","123456");
            PreparedStatement st = conn.prepareStatement(sql);
            st.setString(1,userName);
            st.setString(2,password);

            ResultSet rs = st.executeQuery();
            if(rs.next()){
                int ID = rs.getInt("user_ID");
                ArrayList<WordData> wordList = loadWords(conn, ID);
                return new UserData(userName,password,wordList,ID);
            }
        } catch (SQLException  e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    private static ArrayList<WordData> loadWords(Connection conn, int userId) throws SQLException {
        String wordsSql = "SELECT w.word FROM user_words uw " +
                "JOIN words w ON w.word_ID = uw.word_id " +
                "WHERE uw.user_id = ?";
        PreparedStatement wordsSt = conn.prepareStatement(wordsSql);
        wordsSt.setInt(1, userId);
        ResultSet wordsRs = wordsSt.executeQuery();

        // 创建一个 ArrayList 来存储用户的单词
        ArrayList<WordData> wordList = new ArrayList<>();
        while (wordsRs.next()) {
            WordData word = new WordData(wordsRs.getString("word"));
            wordList.add(word);
        }
        return wordList;  // 返回加载的单词列表
    }


    public static boolean Register(String userName,String password){

        String sql = "INSERT INTO users (username, password) VALUES (?, ?)";
        try {
            //Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(Tool.url, "root", "123456");
            if(!checkRegister(userName,conn)) return false;
            // 插入新的用户
            PreparedStatement st = conn.prepareStatement(sql);
            st.setString(1, userName);
            st.setString(2, password);

            int rowAffected = st.executeUpdate();
            if (rowAffected > 0) {
                return true;  // 注册成功
            }
        } catch (SQLException  e) {
            e.printStackTrace();
        }
        return false;  // 注册失败
    }

    private static boolean checkRegister(String userName,Connection conn) {
        String sql = "SELECT * FROM users WHERE username = ?";
        try {
            PreparedStatement st = conn.prepareStatement(sql);
            st.setString(1, userName);

            ResultSet user = st.executeQuery();

            if(user.next()){
                return false;
            }else{
                return true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
