package Data;

import Util.Tool;

import java.sql.*;
import java.util.ArrayList;

public class WordData {
    public int ID;
    public String word;

    public String wordMening;

    public WordData(String word) {
        newWord(word);
        this.word = word;
    }

    private boolean newWord(String word) {
        if(CheckWord(word)) return false;
        String sql = "INSERT INTO words (word ,word_meaning) VALUES (?, ?)";
        try {
            //Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(Tool.url, "root", "123456");
            System.out.println(666);
            PreparedStatement st = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            st.setString(1, word);
            st.setString(2, "");

            int rowAffected = st.executeUpdate();
            if (rowAffected > 0) {
                try (ResultSet generatedKeys = st.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        ID = generatedKeys.getInt(1);  // 获取自增的 user_id
                        return true;
                    }
                }
            } else {
                return false;
            }
        } catch (SQLException  e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    private boolean CheckWord(String word) {
        String sql = "SELECT word,word_ID FROM words WHERE word = ?";
        try {
            //Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(Tool.url, "root", "123456");
            System.out.println(666);
            PreparedStatement st = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            st.setString(1, word);

            ResultSet wordRS = st.executeQuery();
            if (wordRS.next()) {
                ID = wordRS.getInt("word_ID");
                return true;
            }else{
                return false;
            }
        } catch ( SQLException e) {
            System.out.println(e);
        }
        return false;
    }
}