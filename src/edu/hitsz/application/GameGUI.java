package edu.hitsz.application;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static edu.hitsz.application.Main.LOCK;


public class GameGUI{
    public JPanel MainPanel;
    private JButton easyButton;
    private JButton commonButton;
    private JButton hardButton;
    private JComboBox soundEffectBox;
    private JLabel soundEffect;

    public GameGUI() {
        easyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GameThread.levelType = "easy";
                MainPanel.setVisible(false);
                synchronized (Main.LOCK){
                    Main.LOCK.notify();
                }
            }
        });
        commonButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GameThread.levelType = "common";
                MainPanel.setVisible(false);
                synchronized (LOCK){
                    Main.LOCK.notify();
                }
            }
        });
        hardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GameThread.levelType = "difficult";
                MainPanel.setVisible(false);
                synchronized (LOCK){
                    Main.LOCK.notify();
                }
            }
        });
        soundEffectBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if("关".equals(soundEffectBox.getSelectedItem().toString())){
                    GameThread.soundEffect = false;
                }
                else if("开".equals(soundEffectBox.getSelectedItem().toString())){
                    GameThread.soundEffect = true;
                }
            }
        });
    }

}
