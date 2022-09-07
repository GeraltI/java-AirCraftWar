package edu.hitsz.application;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static edu.hitsz.application.Main.LOCK;

public class GameDataInput {
    public JPanel MainPanel;
    private JPanel OptionPanel;
    private JTextArea Field;
    private JLabel Label1;
    private JLabel Image;
    private JLabel Label2;
    private JButton Button2;
    private JButton Button1;
    private JLabel Label3;

    public GameDataInput() {
        Label1.setText("游戏结束你的得分是 " + GameThread.gameScore + " ，");
        Label2.setText("请输入名字记录得分：");
        Button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainPanel.setVisible(false);
                GameThread.isInput = false;
                GameThread.userName = Field.getText();
                synchronized (LOCK){
                    LOCK.notify();
                }
            }
        });
        Button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainPanel.setVisible(false);
                GameThread.isInput = true;
                GameThread.userName = Field.getText();
                synchronized (LOCK){
                    LOCK.notify();
                }
            }
        });
    }

}
