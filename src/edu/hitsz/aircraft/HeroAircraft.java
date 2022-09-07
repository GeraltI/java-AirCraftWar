package edu.hitsz.aircraft;

import edu.hitsz.application.ImageManager;
import edu.hitsz.application.Main;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.prop.AbstractProp;
import edu.hitsz.shootstragegy.HeroStraightShootStrategy;
import edu.hitsz.shootstragegy.ShootStrategy;

import java.util.LinkedList;
import java.util.List;

/**
 * 英雄飞机，游戏玩家操控
 * @author hitsz
 */
public class HeroAircraft extends AbstractAircraft{

    /**
     * 攻击方式
     * shootNum 子弹一次发射数量
     * power 子弹伤害
     * direction 子弹射击方向 (向上发射：-1，向下发射：1)
     * shoot strategy 射击策略
     * */
    private int shootNum = 2;
    private int power = 10;

    /**
     * locationX 英雄机位置x坐标
     * locationY 英雄机位置y坐标
     * speedX 英雄机射出的子弹的基准速度（英雄机无特定速度）
     * speedY 英雄机射出的子弹的基准速度（英雄机无特定速度）
     * hp 英雄机初始生命值
     */
    private volatile static HeroAircraft heroAircraft;
    private HeroAircraft (ShootStrategy shootstrategy,int locationX, int locationY, int speedX, int speedY, int hp){
        super(shootstrategy,locationX, locationY, speedX, speedY, hp);
    }
    public static HeroAircraft getHeroAircraft() {
        if (heroAircraft == null) {
            synchronized (HeroAircraft.class) {
                if (heroAircraft == null) {
                    heroAircraft = new HeroAircraft(new HeroStraightShootStrategy(),
                            Main.WINDOW_WIDTH / 2,
                            Main.WINDOW_HEIGHT - ImageManager.HERO_IMAGE.getHeight() ,
                            0, 0, 1000);
                }
            }
        }
        return heroAircraft;
    }

    public int getShootNum(){
        return shootNum;
    }

    public void setShootNum(int shootNum){
        this.shootNum = shootNum;
    }

    @Override
    public List<BaseBullet> shoot() {
        return shootstrategy.shootStrategy(shootNum,locationX,locationY,speedX,speedY,power);
    }

    @Override
    public void forward() {
        // 英雄机由鼠标控制，不通过forward函数移动
    }

    @Override
    public List<AbstractProp> reward(){
        return new LinkedList<>();
    }

}
