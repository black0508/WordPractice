package Data;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class DataManager implements Serializable {

    private static Map<String,UserData> users = new HashMap<String,UserData>(); //这里存储了所有的数据(暂时根据username来获取)
    private static String filePath = "Data/userData.dat";
    public static void InitUsers() {
        users.clear();
        File file = new File(filePath);
        if (!file.exists() || file.length() == 0) {
            System.err.println("文件不存在或为空：" + filePath);
            return; // 文件不存在直接返回
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            Object obj = ois.readObject();

            if (obj instanceof Map) {
                users = (Map<String, UserData>) obj;
            } else {
                System.err.println("文件内容格式错误，无法转换为用户数据");
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("加载失败：" + e);
        }
    }

    public static boolean SaveUsers(){
        //TODO: 改为数据库储存方式

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            System.out.println(users);
            oos.writeObject(users); // 写入当前对象

            return true;
        } catch (Exception e) {
            System.err.println("保存失败： " + e.getMessage());
            return false;
        }
    }
    //所有的增删改操作都在这里完成，而单词表在不在这里
    public static boolean AddUser(UserData newUser){
        if(users.get(newUser.userName) == null){
            users.put(newUser.userName,newUser);
            SaveUsers();
            return true;
        }
        return false;
    }
    public static boolean RemoveUser(String userName){
        if(users.get(userName) == null){
            return false;
        }
        users.remove(userName);
        return true;
    }
    public static UserData getUser(String userName){
        return users.get(userName);
    }
    public static String getID(){
        //TODO:为数据库做准备
        return users.size() + "";
    }
}
