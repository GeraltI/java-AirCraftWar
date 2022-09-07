package edu.hitsz.shootstragegy;

import edu.hitsz.bullet.BaseBullet;

import java.util.List;

/**
 * 根据子弹数量的射击策略接口
 * @author me
 */
public interface ShootStrategy {
    /**
     * 飞机射击策略，可射击对象必须实现
     * 可射击对象需实现，返回子弹
     * 非可射击对象空实现，返回null
     * @param shootNum 表示子弹数量
     * @param locationX 表示子弹初始横坐标
     * @param locationY 表示子弹初始纵坐标
     * @param speedX 表示子弹横速度
     * @param speedY 表示子弹纵速度
     * @param power 表示子弹伤害
     * @return <BaseBullet>
     */
    List<BaseBullet> shootStrategy(int shootNum, int locationX, int locationY, int speedX, int speedY, int power);

}
