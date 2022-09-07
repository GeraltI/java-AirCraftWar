package edu.hitsz.shootstragegy;

import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.EnemyBullet;

import java.util.LinkedList;
import java.util.List;

/**
 * 敌机散射射击方法
 * @author me
 */
public class EnemyScatteredShootStrategy implements ShootStrategy {


    @Override
    public List<BaseBullet> shootStrategy(int shootNum, int locationX, int locationY, int speedX, int speedY, int power){
        List<BaseBullet> res = new LinkedList<>();
        BaseBullet baseBullet;
        if (shootNum % 2 == 1) {
            for(int i=0; i<shootNum; i++){
                // 子弹发射位置相对飞机位置向前偏移
                // 多个子弹横向分散
                baseBullet = new EnemyBullet(locationX, locationY, (2 * i - shootNum + 1)/2, 5, power);
                res.add(baseBullet);
            }
        }
        else{
            for(int i=0; i<shootNum; i++){
                // 子弹发射位置相对飞机位置向前偏移
                // 多个子弹横向分散
                if(i < shootNum / 2)
                {
                    baseBullet = new EnemyBullet(locationX, locationY, (2 * i - shootNum)/2, 5, power);
                    res.add(baseBullet);
                }
                else{
                    baseBullet = new EnemyBullet(locationX, locationY, (2 * i - shootNum + 2)/2, 5, power);
                    res.add(baseBullet);
                }
            }
        }
        return res;
    }
}