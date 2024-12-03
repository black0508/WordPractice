package Util;

public class Singleton {

    private static final Singleton instance = new Singleton();

    public Singleton() {}

    public synchronized static Singleton getInstance() {
        return instance;
    }
}