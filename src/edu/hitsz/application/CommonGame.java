package edu.hitsz.application;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.aircraftfactory.BossEnemyFactory;
import edu.hitsz.aircraftfactory.EliteEnemyFactory;
import edu.hitsz.aircraftfactory.MobEnemyFactory;

import static java.lang.String.format;

/**
 * @author xiao hao
 */
public class CommonGame extends Game{

    public CommonGame(String levelType, boolean soundEffect) {
        super(levelType, soundEffect);
        cycleDuration = 350;
        enemyMaxNumber = 10;
        HeroAircraft.getHeroAircraft().decreaseHp(500);
    }

    @Override
    protected void timeLevelup(){
        levelUp = levelUp + 0.05;
        System.out.println("提高难度!精英机概率:" + format("%.2f",0.2 * (1 + levelUp)) + ",敌机周期:" + format("%.2f",30 / (1 + levelUp)) + ",敌机属性提升倍率:" + format("%.2f",(1 + levelUp)));
    }

    @Override
    protected void createEnemyAircraft(){
        if (enemyAircrafts.size() < enemyMaxNumber)
        {
            if(score - scorePast >= 300 && !bossValid){
                gameBackgroundMusic.setStop(true);
                bossBackgroundMusic.setStop(false);
                BossEnemyFactory aircraftFactory = new BossEnemyFactory();
                AbstractAircraft abstractAircraft = aircraftFactory.createAircraft();
                abstractAircraft.decreaseHp(-50);
                abstractAircraft.decreaseHp((int) (-abstractAircraft.getHp() * levelUp));
                bombPropPublisher.addBombPropSubscriber(abstractAircraft);
                enemyAircrafts.add(abstractAircraft);
                bossValid = true;
                System.out.println("产生Boss敌机！");
            }
            else if(Math.random() < 0.2 * (1 + levelUp)) {
                EliteEnemyFactory aircraftFactory = new EliteEnemyFactory();
                AbstractAircraft abstractAircraft = aircraftFactory.createAircraft();
                abstractAircraft.decreaseHp((int) (-abstractAircraft.getHp() * levelUp));
                abstractAircraft.setSpeedX((int)(abstractAircraft.getSpeedX() * (1 + levelUp)));
                abstractAircraft.setSpeedY((int)(abstractAircraft.getSpeedY() * (1 + levelUp)));
                bombPropPublisher.addBombPropSubscriber(abstractAircraft);
                enemyAircrafts.add(abstractAircraft);
            }
            else {
                MobEnemyFactory aircraftFactory = new MobEnemyFactory();
                AbstractAircraft abstractAircraft = aircraftFactory.createAircraft();
                abstractAircraft.decreaseHp((int) (-abstractAircraft.getHp() * levelUp));
                abstractAircraft.setSpeedX((int)(abstractAircraft.getSpeedX() * (1 + levelUp)));
                abstractAircraft.setSpeedY((int)(abstractAircraft.getSpeedY() * (1 + levelUp)));
                bombPropPublisher.addBombPropSubscriber(abstractAircraft);
                enemyAircrafts.add(abstractAircraft);
            }
        }
    }
}
