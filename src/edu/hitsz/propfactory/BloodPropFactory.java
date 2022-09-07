package edu.hitsz.propfactory;

import edu.hitsz.prop.BloodProp;
import edu.hitsz.propfactory.PropFactory;

/**
 * 回血道具奖励的创建工厂
 * 创建回血道具奖励
 *
 * @author me
 */
public class BloodPropFactory implements PropFactory {

    private int locationX;
    private int locationY;
    private int speedX;
    private int speedY;

    public BloodPropFactory(int locationX,int locationY,int speedX,int speedY){
        this.locationX = locationX;
        this.locationY = locationY;
        this.speedX = speedX;
        this.speedY = speedY;
    }

    @Override
    public BloodProp createProp(){
        return new BloodProp(
                this.locationX,
                this.locationY,
                this.speedX,
                this.speedY
        );
    }
}
