package edu.hitsz.application;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.aircraftfactory.BossEnemyFactory;
import edu.hitsz.aircraftfactory.EliteEnemyFactory;
import edu.hitsz.aircraftfactory.MobEnemyFactory;

import java.util.concurrent.TimeUnit;

import static edu.hitsz.application.Main.LOCK;

public class EasyGame extends Game{

    public EasyGame(String levelType, boolean soundEffect) {
        super(levelType, soundEffect);
        cycleDuration = 300;
        enemyMaxNumber = 5;
    }

    @Override
    protected void createEnemyAircraft(){
        if (enemyAircrafts.size() < enemyMaxNumber)
        {
            if(Math.random() < 0.2 * (1 + levelUp)) {
                EliteEnemyFactory aircraftFactory = new EliteEnemyFactory();
                AbstractAircraft abstractAircraft = aircraftFactory.createAircraft();
                abstractAircraft.decreaseHp((int) (-abstractAircraft.getHp() * levelUp));
                bombPropPublisher.addBombPropSubscriber(abstractAircraft);
                enemyAircrafts.add(abstractAircraft);
            }
            else {
                MobEnemyFactory aircraftFactory = new MobEnemyFactory();
                AbstractAircraft abstractAircraft = aircraftFactory.createAircraft();
                abstractAircraft.decreaseHp((int) (-abstractAircraft.getHp() * levelUp));
                bombPropPublisher.addBombPropSubscriber(abstractAircraft);
                enemyAircrafts.add(abstractAircraft);
            }
        }
    }
}
