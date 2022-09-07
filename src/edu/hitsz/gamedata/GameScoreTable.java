package edu.hitsz.gamedata;

import edu.hitsz.application.GameThread;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Comparator;
import java.util.List;

public class GameScoreTable {


    public JPanel MainPanel;
    private JPanel bottomPanel;
    private JPanel topPanel;
    private JScrollPane gameScorePane;
    private JLabel headerLabel;
    private JTable scoreTable;
    private JButton deleteButton;
    private JLabel level;
    private GameDataDaoImpl gameDataDaoImpl;
    private List<GameData> gameDatas;

    public GameScoreTable() {

        level.setText("难度: " + GameThread.levelType);

        String[] columnName = {"排名", "用户名", "游戏得分", "游戏时间"};

        //读取游戏数据
        gameDataDaoImpl = new GameDataDaoImpl();

        //记录这次游戏数据
        if (GameThread.isInput) {
            gameDataDaoImpl.doAdd(GameThread.userName, GameThread.gameScore, GameThread.gameDateTime);
        }

        //更新游戏数据
        gameDataDaoImpl.setAllGameDatas();

        gameDatas = gameDataDaoImpl.getAllGameDatas();

        gameDatas.sort(Comparator.comparing(GameData::getGameScore, Comparator.reverseOrder()));

        gameDataDaoImpl.setAllGameDatas();

        String[][] tableData = new String[gameDatas.size()][4];

        int num = 0;
        for (GameData gameData : gameDatas) {
            tableData[num][0] = num + 1 + "";
            tableData[num][1] = gameData.getUserName();
            tableData[num][2] = gameData.getGameScore() + "";
            tableData[num][3] = gameData.getGameDateTime();
            num = num + 1;
        }

        //表格模型
        DefaultTableModel model = new DefaultTableModel(tableData, columnName) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        //JTable并不存储自己的数据，而是从表格模型那里获取它的数据
        scoreTable.setModel(model);
        gameScorePane.setViewportView(scoreTable);

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = scoreTable.getSelectedRow();
                System.out.println(row + 1);
                if (row != -1) {
                    int op = JOptionPane.showConfirmDialog(null, "是否确定删除选中的玩家？", "选择一个选项",JOptionPane.YES_NO_CANCEL_OPTION);
                    if (op == JOptionPane.YES_OPTION) {
                        gameDataDaoImpl.doDelete(scoreTable.getValueAt(scoreTable.getSelectedRow(), 1).toString(), scoreTable.getValueAt(scoreTable.getSelectedRow(), 3).toString());
                        model.removeRow(row);
                    }
                    //更新游戏数据
                    gameDataDaoImpl.setAllGameDatas();
                }
            }
        });
    }
}
