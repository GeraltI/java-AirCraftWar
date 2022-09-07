package edu.hitsz.aircraft;

import edu.hitsz.BombPropPublish.BombPropPublisher;
import edu.hitsz.BombPropPublish.BombPropSubscriber;
import edu.hitsz.application.Game;
import edu.hitsz.application.ImageManager;
import edu.hitsz.application.Main;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.prop.*;
import edu.hitsz.propfactory.BloodPropFactory;
import edu.hitsz.propfactory.BombPropFactory;
import edu.hitsz.propfactory.BulletPropFactory;
import edu.hitsz.shootstragegy.EnemyStraightShootStrategy;
import edu.hitsz.shootstragegy.ShootStrategy;

import java.util.LinkedList;
import java.util.List;

/**
 * 精英敌机
 * 可以射击
 *
 * @author hitsz
 */
public class EliteEnemy extends AbstractAircraft{

    /**
     * locationX 精英敌机位置x坐标
     * locationY 精英敌机位置y坐标
     * speedX 精英敌机的基准速度
     * speedY 精英敌机的基准速度
     * hp 精英敌机初始生命值
     * shoot strategy 精英敌机射击策略
     */

    public EliteEnemy(ShootStrategy shootstrategy,int locationX, int locationY, int speedX, int speedY, int hp) {
        super(shootstrategy,locationX, locationY, speedX, speedY, hp);
    }

    @Override
    public void forward() {
        super.forward();
        // 横向超出边界后反向
        if (locationX - ImageManager.ELITE_ENEMY_IMAGE.getWidth()/2 <= 0 || locationX + ImageManager.ELITE_ENEMY_IMAGE.getWidth()/2 >= Main.WINDOW_WIDTH) {
            speedX = -speedX;
        }
        // 判定 y 轴向下飞行出界
        if (locationY >= Main.WINDOW_HEIGHT ) {
            vanish();
        }
    }

    @Override
    public List<BaseBullet> shoot() {
        return shootstrategy.shootStrategy(1,locationX,locationY,speedX,speedY,10);
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
        if(Math.random() < 0.3){
            BloodPropFactory bloodPropFactory = new BloodPropFactory(locationX,locationY,speeds,3);
            res.add(bloodPropFactory.createProp());
        }
        else if(Math.random() < 0.2 / 0.7){
            BulletPropFactory bulletPropFactory = new BulletPropFactory(locationX,locationY,speeds,3);
            res.add(bulletPropFactory.createProp());
        }
        else if(Math.random() < 0.1 / 0.8 / 0.7){
            BombPropFactory bombPropFactory = new BombPropFactory(locationX,locationY,speeds,3);
            res.add(bombPropFactory.createProp());
        }
        return res;
    }

    @Override
    public void response() {
        if(this.isValid){
            this.vanish();
            BombPropPublisher.getBombPropPublisher().scoreChange = BombPropPublisher.getBombPropPublisher().scoreChange + 20;
        }
    }
}