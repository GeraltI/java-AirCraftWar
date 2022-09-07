package edu.hitsz.prop;

import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.application.Game;
import edu.hitsz.application.ImageManager;
import edu.hitsz.application.Main;
import edu.hitsz.shootstragegy.HeroScatteredShootStrategy;
import edu.hitsz.shootstragegy.HeroStraightShootStrategy;

import java.util.concurrent.ScheduledExecutorService;

import static java.lang.Thread.sleep;

/**
 * 增加子弹道具
 * 增加英雄机子弹数量
 *
 * @author me
 */
public class BulletProp extends AbstractProp{

    @Override
    public void forward() {
        super.forward();
        // 横向超出边界后反向
        if (locationX - ImageManager.BULLET_PROP_IMAGE.getWidth()/2 <= 0 || locationX + ImageManager.BULLET_PROP_IMAGE.getWidth()/2 >= Main.WINDOW_WIDTH) {
            speedX = -speedX;
        }
        // 判定 y 轴向下飞行出界
        if (locationY >= Main.WINDOW_HEIGHT ) {
            vanish();
        }
    }

    public BulletProp(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }



    @Override
    public void effect() {
        System.out.println("BulletSupply active!");
        Runnable r = () -> {
            try {
                HeroAircraft.getHeroAircraft().setShootNum(HeroAircraft.getHeroAircraft().getShootNum() + 1);
                HeroAircraft.getHeroAircraft().setShootStrategy(new HeroScatteredShootStrategy());
                Thread.sleep(20000);
                System.out.println("BulletSupply not active!");
                HeroAircraft.getHeroAircraft().setShootNum(HeroAircraft.getHeroAircraft().getShootNum() - 1);
                if (HeroAircraft.getHeroAircraft().getShootNum() == 2) {
                    HeroAircraft.getHeroAircraft().setShootStrategy(new HeroStraightShootStrategy());
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };
        new Thread(r).start();
    }
}
