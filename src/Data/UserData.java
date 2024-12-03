package Data;

import Util.Tool;

import java.io.Serializable;
import java.util.ArrayList;

//这个是玩家数据，包含了账号密码和他的单词
public class UserData implements Serializable {
    private static final long serialVersionUID = 1L;
    public int ID; //userID 唯一
    public String userName; //用户名
    public String password; //密码
    public ArrayList<WordData> wordList; //单词表
    public UserData(String userName,String password,int id){
        this.userName = userName;
        this.password = password;
        this.wordList = new ArrayList<>();
        this.ID = id;
    }
    public UserData(String userName,String password,ArrayList<WordData> wordList,int id){
        this.userName = userName;
        this.password = password;
        this.wordList = wordList;
        this.ID = id;
    }
}
