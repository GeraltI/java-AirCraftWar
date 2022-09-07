package edu.hitsz.prop;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.application.ImageManager;
import edu.hitsz.application.Main;
/**
 * 回血道具
 * 回复血量
 *
 * @author me
 */
public class BloodProp extends AbstractProp{

    @Override
    public void forward() {
        super.forward();
        // 横向超出边界后反向
        if (locationX - ImageManager.BLOOD_PROP_IMAGE.getWidth()/2 <= 0 || locationX + ImageManager.BLOOD_PROP_IMAGE.getWidth()/2 >= Main.WINDOW_WIDTH) {
            speedX = -speedX;
        }
        // 判定 y 轴向下飞行出界
        if (locationY >= Main.WINDOW_HEIGHT ) {
            vanish();
        }
    }

    public BloodProp(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }

    @Override
    public void effect(){
        System.out.println("BloodSupply active!");
        if(HeroAircraft.getHeroAircraft().getHp() <= 990){
            HeroAircraft.getHeroAircraft().decreaseHp(-10);
        }
    };

}
