package edu.hitsz.gamedata;

import edu.hitsz.application.GameThread;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static java.nio.file.Files.readAllLines;


/**
 * 实现游戏数据访问对象DAO接口的实现类
 * @author xiao hao
 */
public class GameDataDaoImpl implements GameDataDao{
    private Path path;
    private final List<GameData> gameDatas;
    private List<String> lines;

    public GameDataDaoImpl() {

        if("easy".equals(GameThread.levelType)){
            path = Paths.get("src/edu/hitsz/gamedata"+"/EasyGamedatas.txt");
        }
        else if("common".equals(GameThread.levelType)){
            path = Paths.get("src/edu/hitsz/gamedata"+"/CommonGamedatas.txt");
        }
        else if("difficult".equals(GameThread.levelType)){
            path = Paths.get("src/edu/hitsz/gamedata"+"/DifficultGamedatas.txt");
        }
        gameDatas = new ArrayList<>();
        try {
            if (path != null) {
                lines = Files.readAllLines(path);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (lines != null) {
            for (String str : lines) {
                String[] datas = str.split("%");
                if(datas.length !=3){
                    break;
                }
                gameDatas.add(new GameData(datas[0],Integer.parseInt(datas[1]),datas[2]));
            }
        }
    }

    @Override
    public List<GameData> getAllGameDatas() {
        return gameDatas;
    }

    @Override
    public void doAdd(String userName,int gameScore,String gameDateTime) {
        this.gameDatas.add(new GameData(userName,gameScore,gameDateTime));
    }

    @Override
    public void doDelete(String userName,String gameDateTime) {
        gameDatas.removeIf(gameData -> Objects.equals(gameData.getUserName(), userName) && Objects.equals(gameData.getGameDateTime(), gameDateTime));
    }

    @Override
    public void setAllGameDatas() {
        lines = new ArrayList<>();
        for(GameData gameData : gameDatas){
            String s = new String(gameData.getUserName() + "%" + gameData.getGameScore() + "%" + gameData.getGameDateTime());
            lines.add(s);
        }
        try {
            Files.write(path, lines);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
