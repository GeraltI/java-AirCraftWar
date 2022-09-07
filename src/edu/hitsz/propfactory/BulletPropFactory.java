package edu.hitsz.propfactory;

import edu.hitsz.prop.BulletProp;
import edu.hitsz.propfactory.PropFactory;

/**
 * 增加子弹道具奖励的创建工厂
 * 创建增加子弹道具奖励
 *
 * @author me
 */
public class BulletPropFactory implements PropFactory {

    private int locationX;
    private int locationY;
    private int speedX;
    private int speedY;

    public BulletPropFactory(int locationX,int locationY,int speedX,int speedY){
        this.locationX = locationX;
        this.locationY = locationY;
        this.speedX = speedX;
        this.speedY = speedY;
    }

    @Override
    public BulletProp createProp(){
        return new BulletProp(
                this.locationX,
                this.locationY,
                this.speedX,
                this.speedY
        );
    }
}
