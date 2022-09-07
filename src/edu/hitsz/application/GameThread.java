package edu.hitsz.application;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.gamedata.GameScoreTable;

import javax.swing.*;
import java.awt.*;

import static edu.hitsz.application.ImageManager.*;
import static edu.hitsz.application.Main.LOCK;


public class GameThread implements Runnable{

    public static final GameThread L = new GameThread();

    public static String levelType;
    public static Boolean soundEffect = true;
    public static String userName;
    public static int gameScore;
    public static String gameDateTime;
    public static Boolean isInput = false;
    public JFrame frame;

    @Override
    public void run() {

        GameGUI gameGUI = new GameGUI();
        frame.add(gameGUI.MainPanel);
        frame.setVisible(true);

        synchronized (LOCK){
            while(gameGUI.MainPanel.isVisible()){
                try{
                    LOCK.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        frame.remove(gameGUI.MainPanel);

        Game game;
        switch (levelType){
            case"easy":
                game = new EasyGame(levelType,soundEffect);
                break;
            case"common":
                game = new CommonGame(levelType,soundEffect);
                break;
            case"difficult":
                game = new DifficultGame(levelType,soundEffect);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + levelType);
        }
        frame.add(game);
        game.action();
        frame.setVisible(true);

        synchronized (LOCK){
            while(!game.gameOverFlag){
                try{
                    LOCK.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        frame.remove(game);

        GameDataInput gameDataInput = new GameDataInput();
        frame.add(gameDataInput.MainPanel);
        frame.setVisible(true);

        synchronized (LOCK){
            while(gameDataInput.MainPanel.isVisible()){
                try{
                    LOCK.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        GameScoreTable gameScoreTable = new GameScoreTable();
        frame.add(gameScoreTable.MainPanel);
        frame.setVisible(true);

        synchronized (LOCK){
            while(gameScoreTable.MainPanel.isVisible()){
                try{
                    LOCK.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        frame.remove(gameScoreTable.MainPanel);
        frame.dispose();

    }

    public GameThread(){
        // 获得屏幕的分辨率，初始化 Frame
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame = new JFrame("Aircraft War");
        frame.setSize(Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT);
        frame.setResizable(false);
        //设置窗口的大小和位置,居中放置
        frame.setBounds(((int) screenSize.getWidth() - Main.WINDOW_WIDTH) / 2, ((int) screenSize.getHeight() - Main.WINDOW_HEIGHT) / 2,
                Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }
}


