package edu.hitsz.prop;

import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.application.Main;
import edu.hitsz.basic.AbstractFlyingObject;
import edu.hitsz.shootstragegy.HeroScatteredShootStrategy;
import edu.hitsz.shootstragegy.HeroStraightShootStrategy;

import static java.lang.Thread.sleep;

/**
 * 所有种类掉落道具奖励抽象父类
 * 道具奖励(BLOOD,BOMB,BULLET)
 *
 * @author me
 */
public abstract class AbstractProp extends AbstractFlyingObject {

    public AbstractProp(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }

    @Override
    public void forward() {
        super.forward();
        // 判定 y 轴向下飞行出界
        if (locationY >= Main.WINDOW_HEIGHT ) {
            vanish();
        }
    }

    public void effect(){};

}
