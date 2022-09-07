package edu.hitsz.aircraftfactory;

import edu.hitsz.aircraft.BossEnemy;
import edu.hitsz.application.ImageManager;
import edu.hitsz.application.Main;
import edu.hitsz.shootstragegy.EnemyScatteredShootStrategy;

/**
 * BOSS敌机的创建工厂
 * 创建BOSS敌机
 * @author me
 */
public class BossEnemyFactory implements AircraftFactory {

    @Override
    public BossEnemy createAircraft(){
        return new BossEnemy(new EnemyScatteredShootStrategy(),
                Main.WINDOW_WIDTH/2,
                ImageManager.BOSS_ENEMY_IMAGE.getHeight()/2,
                0,
                0,
                100
        );
    }

}
