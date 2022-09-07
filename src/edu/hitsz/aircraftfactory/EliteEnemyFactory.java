package edu.hitsz.aircraftfactory;

import edu.hitsz.aircraft.EliteEnemy;
import edu.hitsz.application.ImageManager;
import edu.hitsz.application.Main;
import edu.hitsz.shootstragegy.EnemyStraightShootStrategy;

/**
 * ELITE敌机的创建工厂
 * 创建ELITE敌机
 *
 * @author me
 */
public class EliteEnemyFactory implements AircraftFactory {
    @Override
    public EliteEnemy createAircraft(){
        if(Math.random()<0.5){
            return new EliteEnemy(new EnemyStraightShootStrategy(),
                    ImageManager.ELITE_ENEMY_IMAGE.getWidth()/2 + (int) (Math.random() * (Main.WINDOW_WIDTH - ImageManager.ELITE_ENEMY_IMAGE.getWidth())),
                    (int) (Math.random() * Main.WINDOW_HEIGHT * 0.2),
                    1,
                    3,
                    20
            );
        }
        else{
            return new EliteEnemy(new EnemyStraightShootStrategy(),
                    ImageManager.ELITE_ENEMY_IMAGE.getWidth()/2 + (int) (Math.random() * (Main.WINDOW_WIDTH - ImageManager.ELITE_ENEMY_IMAGE.getWidth())),
                    (int) (Math.random() * Main.WINDOW_HEIGHT * 0.2),
                    -1,
                    3,
                    20
            );
        }
    }
}
