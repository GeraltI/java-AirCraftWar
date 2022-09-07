package edu.hitsz.shootstragegy;

import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.HeroBullet;

import java.util.LinkedList;
import java.util.List;

/**
 * 英雄机直射射击方法
 * @author me
 */
public class HeroStraightShootStrategy implements ShootStrategy {

    @Override
    public List<BaseBullet> shootStrategy(int shootNum, int locationX, int locationY, int speedX, int speedY, int power){
        List<BaseBullet> res = new LinkedList<>();
        BaseBullet baseBullet;
        for(int i=0; i<shootNum; i++){
            // 子弹发射位置相对飞机位置向前偏移
            // 多个子弹横向分散
            baseBullet = new HeroBullet(locationX + (i*2 - shootNum + 1)*24, locationY, 0, -6, power);
            res.add(baseBullet);
        }
        return res;
    }
}
