package edu.hitsz.aircraftfactory;

import edu.hitsz.aircraft.MobEnemy;
import edu.hitsz.application.ImageManager;
import edu.hitsz.application.Main;
import edu.hitsz.shootstragegy.EnemyStraightShootStrategy;

/**
 * MOB敌机的创建工厂
 * 创建MOB敌机
 *
 * @author me
 */
public class MobEnemyFactory implements AircraftFactory {

    @Override
    public MobEnemy createAircraft(){
        return new MobEnemy(new EnemyStraightShootStrategy(),
                (int) (Math.random() * (Main.WINDOW_WIDTH - ImageManager.MOB_ENEMY_IMAGE.getWidth())),
                (int) (Math.random() * Main.WINDOW_HEIGHT * 0.2),
                0,
                4,
                10
        );
    }

}
