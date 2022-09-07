package edu.hitsz.aircraft;

import edu.hitsz.BombPropPublish.BombPropSubscriber;
import edu.hitsz.application.ImageManager;
import edu.hitsz.application.Main;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.prop.AbstractProp;
import edu.hitsz.propfactory.BloodPropFactory;
import edu.hitsz.propfactory.BombPropFactory;
import edu.hitsz.propfactory.BulletPropFactory;
import edu.hitsz.shootstragegy.EnemyScatteredShootStrategy;
import edu.hitsz.shootstragegy.HeroStraightShootStrategy;
import edu.hitsz.shootstragegy.ShootStrategy;

import java.util.LinkedList;
import java.util.List;

/**
 * 老大敌机
 * 可以射击
 *
 * @author hitsz
 */
public class BossEnemy extends AbstractAircraft{


    /**
     * locationX 老大敌机位置x坐标
     * locationY 老大敌机位置y坐标
     * speedX 老大敌机的基准速度
     * speedY 老大敌机的基准速度
     * hp 老大敌机初始生命值
     * shoot strategy 老大敌机的射击策略
     */


    public BossEnemy(ShootStrategy shootstrategy,int locationX, int locationY, int speedX, int speedY, int hp) {
        super(shootstrategy,locationX, locationY, speedX, speedY, hp);
    }

    @Override
    public void forward() {
        super.forward();
        // 判定 y 轴向下飞行出界
        if (locationY >= Main.WINDOW_HEIGHT ) {
            vanish();
        }
    }

    @Override
    public List<BaseBullet> shoot() {
        return shootstrategy.shootStrategy(3,locationX,locationY,speedX,speedY,10);
    }

    @Override
    public List<AbstractProp> reward(){
        List<AbstractProp> res = new LinkedList<>();
        int speeds;
        if(Math.random() < 0.5){
            speeds = 1;
        }
        else{
            speeds = -1;
        }
        if(Math.random() < 0.4){
            BloodPropFactory bloodPropFactory = new BloodPropFactory(locationX,locationY,speeds,3);
            res.add(bloodPropFactory.createProp());
        }
        else if(Math.random() < 0.3 / 0.6){
            BulletPropFactory bulletPropFactory = new BulletPropFactory(locationX,locationY,speeds,3);
            res.add(bulletPropFactory.createProp());
        }
        else if(Math.random() < 0.2 / 0.7 / 0.6){
            BombPropFactory bombPropFactory = new BombPropFactory(locationX,locationY,speeds,3);
            res.add(bombPropFactory.createProp());
        }
        return res;
    }

}
