package edu.hitsz.aircraft;

import edu.hitsz.BombPropPublish.BombPropPublisher;
import edu.hitsz.BombPropPublish.BombPropSubscriber;
import edu.hitsz.application.Game;
import edu.hitsz.application.Main;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.prop.AbstractProp;
import edu.hitsz.shootstragegy.EnemyStraightShootStrategy;
import edu.hitsz.shootstragegy.ShootStrategy;

import java.util.LinkedList;
import java.util.List;

/**
 * 普通敌机
 * 不可射击
 *
 * @author hitsz
 */
public class MobEnemy extends AbstractAircraft{

    /**
     * locationX 普通敌机位置x坐标
     * locationY 普通敌机位置y坐标
     * speedX 普通敌机的基准速度
     * speedY 普通敌机的基准速度
     * hp 普通敌机初始生命值
     * shoot strategy  普通敌机射击策略
     */


    public MobEnemy(ShootStrategy shootstrategy,int locationX, int locationY, int speedX, int speedY, int hp) {
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
        return shootstrategy.shootStrategy(0,locationX,locationY,speedX,speedY,10);
    }

    @Override
    public List<AbstractProp> reward(){
        return new LinkedList<>();
    }

    @Override
    public void response() {
        if(this.isValid){
            this.vanish();
            BombPropPublisher.getBombPropPublisher().scoreChange = BombPropPublisher.getBombPropPublisher().scoreChange + 10;
        }
    }
}
