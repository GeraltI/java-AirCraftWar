package edu.hitsz.propfactory;

import edu.hitsz.prop.BombProp;
import edu.hitsz.propfactory.PropFactory;

/**
 * 清除子弹道具奖励的创建工厂
 * 创建清除子弹道具奖励
 *
 * @author me
 */
public class BombPropFactory implements PropFactory {

    private int locationX;
    private int locationY;
    private int speedX;
    private int speedY;

    public BombPropFactory(int locationX,int locationY,int speedX,int speedY){
        this.locationX = locationX;
        this.locationY = locationY;
        this.speedX = speedX;
        this.speedY = speedY;
    }

    @Override
    public BombProp createProp(){
        return new BombProp(
                this.locationX,
                this.locationY,
                this.speedX,
                this.speedY
        );
    }
}
