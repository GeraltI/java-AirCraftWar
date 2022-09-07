package edu.hitsz.propfactory;

import edu.hitsz.prop.AbstractProp;

/**
 * 所有种类掉落道具奖励工厂的抽象父类
 * 创建道具奖励(BLOOD,BOMB,BULLET)
 *
 * @author me
 */
public interface PropFactory {

    /**
     * 创建掉落奖励方法
     * @return AbstractProp
     */
    AbstractProp createProp();
}
