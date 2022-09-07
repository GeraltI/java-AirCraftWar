package edu.hitsz.prop;

import edu.hitsz.BombPropPublish.BombPropPublisher;
import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.application.Game;
import edu.hitsz.application.ImageManager;
import edu.hitsz.application.Main;
/**
 * 清除敌机子弹道具
 * 清除当时游戏内敌机子弹
 *
 * @author me
 */
public class BombProp extends AbstractProp{

    @Override
    public void forward() {
        super.forward();
        // 横向超出边界后反向
        if (locationX - ImageManager.BOMB_PROP_IMAGE.getWidth()/2 <= 0 || locationX + ImageManager.BOMB_PROP_IMAGE.getWidth()/2 >= Main.WINDOW_WIDTH) {
            speedX = -speedX;
        }
        // 判定 y 轴向下飞行出界
        if (locationY >= Main.WINDOW_HEIGHT ) {
            vanish();
        }
    }

    public BombProp(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }

    @Override
    public void effect(){
        System.out.println("BombSupply active!");
        BombPropPublisher.getBombPropPublisher().bombPropEffect();
    }

}
