package edu.hitsz.application;


/**
 * 程序入口
 * @author hitsz
 */
public class Main {

    public static final Main LOCK = new Main();

    public static final int WINDOW_WIDTH = 512;
    public static final int WINDOW_HEIGHT = 768;

    public static void main(String[] args) {

        System.out.println("Hello Aircraft War");

        GameThread gameThread = new GameThread();
        Runnable r = () -> {
            try {
                gameThread.run();
                Thread.sleep(0);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };
        // 启动线程
        new Thread(r, "aircraftWarThread").start();
    }
}
