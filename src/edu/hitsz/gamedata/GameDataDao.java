package edu.hitsz.gamedata;


import java.util.List;

/**
 * 游戏数据访问对象DAO接口
 * @author xiao hao
 */
public interface GameDataDao {
    /**
     * 得到所有游戏数据
     * @return List<GameData>
     */

    List<GameData> getAllGameDatas();

    /**
     * 增加一条游戏数据
     * @param userName 增加游戏数据的用户名
     * @param gameScore 增加游戏数据的游戏得分
     * @param gameDateTime 增加游戏数据的游戏时间
     */

    void doAdd(String userName,int gameScore,String gameDateTime);

    /**
     * 删除用户游戏数据
     * @param userName 删除游戏数据的用户名
     * @param gameDateTime 删除游戏数据的游戏时间
     */

    void doDelete(String userName,String gameDateTime);

    /**
     * 更新游戏数据的变动
     */

    void setAllGameDatas();
}
