package edu.hitsz.application;

import edu.hitsz.BombPropPublish.BombPropPublisher;
import edu.hitsz.aircraftfactory.BossEnemyFactory;
import edu.hitsz.aircraftfactory.EliteEnemyFactory;
import edu.hitsz.aircraftfactory.MobEnemyFactory;
import edu.hitsz.aircraft.*;
import edu.hitsz.bullet.*;
import edu.hitsz.prop.*;
import edu.hitsz.basic.AbstractFlyingObject;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.util.concurrent.*;

import static edu.hitsz.application.ImageManager.*;
import static edu.hitsz.application.Main.LOCK;

/**
 * 游戏主面板，游戏启动
 *
 * @author hitsz
 */
public abstract class Game extends JPanel{

    /**
     * int levelUp 游戏难度随时间提升
     * String gamelevel 游戏难度
     * boolean soundopen 音效打开
     * BombPropPublisher bombPropPublisher 发布者
     */
    protected double levelUp = 0;
    protected final String gamelevel;
    protected final boolean soundopen;
    protected final BombPropPublisher bombPropPublisher;
    protected int backGroundTop = 0;

    /**
     * Scheduled 线程池，用于任务调度
     */
    protected final ScheduledExecutorService gameExecutorService;
    protected final MusicThread gameBackgroundMusic;
    protected final MusicThread bossBackgroundMusic;

    /**
     * 时间间隔(ms)，控制刷新频率
     */
    protected final int timeInterval = 20;

    protected final HeroAircraft heroAircraft;
    protected final List<AbstractAircraft> enemyAircrafts;
    protected final List<BaseBullet> heroBullets;
    protected final List<BaseBullet> enemyBullets;
    protected final List<AbstractProp> Props;

    protected int enemyMaxNumber;

    protected boolean gameOverFlag = false;
    protected boolean bossValid = false;
    protected int scorePast = 0;
    protected int score = 0;
    protected int time = 0;
    /**
     * 周期（ms)
     * 指示子弹的发射、敌机的产生频率
     */
    protected int cycleDuration;
    protected int enemyCycleDuration = 600;
    protected int cycleTime = 0;
    protected int enemyCycleTime = 0;


    public Game(String levelType,boolean soundEffect) {

        soundopen = soundEffect;
        gamelevel = levelType;
        heroAircraft = HeroAircraft.getHeroAircraft();
        bombPropPublisher = BombPropPublisher.getBombPropPublisher();

        enemyAircrafts = new LinkedList<>();
        heroBullets = new LinkedList<>();
        enemyBullets = new LinkedList<>();
        Props = new LinkedList<>();

        //Scheduled 线程池，用于定时任务调度
        ThreadFactory gameThread = new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r);
                t.setName("game thread");
                return t;
            }
        };
        gameBackgroundMusic = new MusicThread("src/videos/bgm.wav",true,false);
        bossBackgroundMusic = new MusicThread("src/videos/bgm_boss.wav",true,true);
        gameExecutorService = new ScheduledThreadPoolExecutor(15,gameThread);
        gameExecutorService.schedule(() -> {
            try {
                gameBackgroundMusic.run();
                Thread.sleep(0);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },0,TimeUnit.SECONDS);
        gameExecutorService.schedule(() -> {
            try {
                bossBackgroundMusic.run();
                Thread.sleep(0);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },0,TimeUnit.SECONDS);

        //获取当时日期时间  format日期时间结构
        LocalDateTime dateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        GameThread.gameDateTime = dateTime.format(formatter);


        //启动英雄机鼠标监听
        new HeroController(this, heroAircraft);

    }

    /**
     * 游戏启动入口，执行游戏逻辑
     */
    public void action() {

        // 定时任务：绘制、对象产生、碰撞判定、击毁及结束判定
        Runnable task = () -> {

            time += timeInterval;

            // 周期性执行（控制频率）
            if (timeCountAndNewCycleJudge()) {
                if(time % (8 * cycleDuration) <= 10){
                    timeLevelup();
                }
                // 英雄飞机射出子弹
                heroShootAction();
            }
            if(timeCountAndNewEnemyCycleJudge()){
                // 新敌机产生
                createEnemyAircraft();
                // 敌机射出子弹
                enemyShootAction();
            }

            // 子弹移动
            bulletsMoveAction();

            // 飞机移动
            aircraftsMoveAction();

            // 道具移动
            propsMoveAction();

            // 撞击检测
            crashCheckAction();

            // 后处理
            postProcessAction();

            //每个时刻重绘界面
            repaint();

            // 游戏结束检查
            if (heroAircraft.getHp() <= 0) {

                //音效
                gameExecutorService.schedule(() -> {
                    try {
                        new MusicThread("src/videos/game_over.wav",false,false).run();
                        Thread.sleep(0);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                },0,TimeUnit.SECONDS);
                gameBackgroundMusic.setStop(true);
                bossBackgroundMusic.setStop(true);

                GameThread.gameScore = score;

                //游戏结束标志
                gameOverFlag = true;

                System.out.println("Game Over!");

                // 游戏结束
                gameExecutorService.shutdown();

                synchronized (LOCK){
                    Main.LOCK.notify();
                }
            }
        };

        /**
         * 以固定延迟时间进行执行
         * 本次任务执行完成后，需要延迟设定的延迟时间，才会执行新的任务
         */

        gameExecutorService.scheduleWithFixedDelay(task, timeInterval, timeInterval, TimeUnit.MILLISECONDS);

    }

    //***********************
    //      Action 各部分
    //***********************

    protected boolean timeCountAndNewCycleJudge() {
        cycleTime += timeInterval;
        if (cycleTime >= cycleDuration && cycleTime - timeInterval < cycleDuration) {
            // 跨越到新的周期
            cycleTime %= cycleDuration;
            return true;
        }
        else {
            return false;
        }
    }

    protected boolean timeCountAndNewEnemyCycleJudge() {
        enemyCycleTime += timeInterval;
        if (enemyCycleTime >= enemyCycleDuration / (1 + levelUp)) {
            // 跨越到新的周期
            enemyCycleTime %= enemyCycleDuration / (1 + levelUp);
            return true;
        }
        else {
            return false;
        }
    }

    protected void timeLevelup(){
        levelUp = levelUp + 0;
    }

    protected void createEnemyAircraft(){
        if (enemyAircrafts.size() < enemyMaxNumber)
        {
            if(score - scorePast >= 200 && !bossValid){
                gameBackgroundMusic.setStop(true);
                bossBackgroundMusic.setStop(false);
                BossEnemyFactory aircraftFactory = new BossEnemyFactory();
                AbstractAircraft abstractAircraft = aircraftFactory.createAircraft();
                bombPropPublisher.addBombPropSubscriber(abstractAircraft);
                enemyAircrafts.add(abstractAircraft);
                bossValid = true;
            }
            else if(Math.random() < 0.2) {
                EliteEnemyFactory aircraftFactory = new EliteEnemyFactory();
                AbstractAircraft abstractAircraft = aircraftFactory.createAircraft();
                bombPropPublisher.addBombPropSubscriber(abstractAircraft);
                enemyAircrafts.add(abstractAircraft);
            }
            else {
                MobEnemyFactory aircraftFactory = new MobEnemyFactory();
                AbstractAircraft abstractAircraft = aircraftFactory.createAircraft();
                bombPropPublisher.addBombPropSubscriber(abstractAircraft);
                enemyAircrafts.add(abstractAircraft);
            }
        }
    }

    private void heroShootAction() {
        // 英雄射击
        heroBullets.addAll(heroAircraft.shoot());
        //音效
        gameExecutorService.schedule(() -> {
            try {
                if(soundopen){
                    new MusicThread("src/videos/bullet.wav",false,false).run();
                }
                Thread.sleep(0);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },0,TimeUnit.SECONDS);
    }

    private void enemyShootAction(){
        // TODO 敌机射击
        for (AbstractAircraft enemyAircraft: enemyAircrafts) {
            List<BaseBullet> shootBullets = enemyAircraft.shoot();
            enemyBullets.addAll(shootBullets);
            for(BaseBullet shootBullet:shootBullets){
                bombPropPublisher.addBombPropSubscriber(shootBullet);
            }
        }
    }

    private void bulletsMoveAction() {
        for (BaseBullet bullet : heroBullets) {
            bullet.forward();
        }
        for (BaseBullet bullet : enemyBullets) {
            bullet.forward();
        }
    }

    private void aircraftsMoveAction() {
        for (AbstractAircraft enemyAircraft : enemyAircrafts) {
            enemyAircraft.forward();
        }
    }

    private void propsMoveAction() {
        for (AbstractProp prop : Props) {
            prop.forward();
        }
    }

    /**
     * 碰撞检测：
     * 1. 敌机攻击英雄
     * 2. 英雄攻击/撞击敌机
     * 3. 英雄获得补给
     */
    private void crashCheckAction() {
        // TODO 敌机子弹攻击英雄
        for (BaseBullet enemyBullet : enemyBullets) {
            if (enemyBullet.notValid()) {
                continue;
            }
            if (heroAircraft.crash(enemyBullet)) {
                // 英雄机撞击到敌机子弹
                // 英雄机损失一定生命值
                heroAircraft.decreaseHp(enemyBullet.getPower());
                enemyBullet.vanish();
                bombPropPublisher.removeBombPropSubscriber(enemyBullet);
            }
        }
        // 英雄子弹攻击敌机
        for (BaseBullet heroBullet : heroBullets) {
            if (heroBullet.notValid()) {
                continue;
            }
            for (AbstractAircraft enemyAircraft : enemyAircrafts) {
                if (enemyAircraft.notValid()) {
                    // 已被其他子弹击毁的敌机，不再检测
                    // 避免多个子弹重复击毁同一敌机的判定
                    continue;
                }
                if (enemyAircraft.crash(heroBullet)) {
                    //音效
                    gameExecutorService.schedule(() -> {
                        try {
                            if(soundopen){
                                new MusicThread("src/videos/bullet_hit.wav",false,false).run();
                            }
                            Thread.sleep(0);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    },0,TimeUnit.SECONDS);
                    // 敌机撞击到英雄机子弹
                    // 敌机损失一定生命值
                    enemyAircraft.decreaseHp(heroBullet.getPower());
                    heroBullet.vanish();
                    if (enemyAircraft.notValid()) {
                        // TODO 获得分数，产生道具补给
                        if (enemyAircraft instanceof MobEnemy) {
                            score += 10;
                        }
                        if (enemyAircraft instanceof EliteEnemy) {
                            score += 20;
                            Props.addAll(enemyAircraft.reward());
                        }
                        if (enemyAircraft instanceof BossEnemy) {
                            score += 50;
                            scorePast = score;
                            bossValid = false;
                            gameBackgroundMusic.setStop(false);
                            bossBackgroundMusic.setStop(true);
                            Props.addAll(enemyAircraft.reward());
                        }
                        bombPropPublisher.removeBombPropSubscriber(enemyAircraft);
                    }
                }

                // 英雄机 与 敌机 相撞，均损毁
                if (enemyAircraft.crash(heroAircraft) || heroAircraft.crash(enemyAircraft)) {
                    enemyAircraft.vanish();
                    bombPropPublisher.removeBombPropSubscriber(enemyAircraft);
                    heroAircraft.decreaseHp(Integer.MAX_VALUE);
                }
            }
        }

        // Todo: 我方获得道具，道具生效
        for (AbstractProp prop : Props) {
            if (prop.notValid()) {
                continue;
            }
            if(heroAircraft.crash(prop)){

                prop.effect();

                if (prop instanceof BloodProp) {
                    //音效
                    gameExecutorService.schedule(() -> {
                        try {
                            if(soundopen){
                                new MusicThread("src/videos/get_supply.wav",false,false).run();
                            }
                            Thread.sleep(0);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    },0,TimeUnit.SECONDS);
                }

                else if(prop instanceof BulletProp){
                    //音效
                    gameExecutorService.schedule(() -> {
                        try {
                            if(soundopen){
                                new MusicThread("src/videos/get_supply.wav",false,false).run();
                            }
                            Thread.sleep(0);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    },0,TimeUnit.SECONDS);
                }

                else if(prop instanceof BombProp){
                    //音效
                    gameExecutorService.schedule(() -> {
                        try {
                            if(soundopen){
                                new MusicThread("src/videos/bomb_explosion.wav",false,false).run();
                            }
                            Thread.sleep(0);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    },0,TimeUnit.SECONDS);
                    score = score + BombPropPublisher.getBombPropPublisher().scoreChange;
                }

                prop.vanish();

            }
        }
    }

    /**
     * 后处理：
     * 1. 删除无效的子弹
     * 2. 删除无效的敌机
     * 3. 检查英雄机生存
     * <p>
     * 无效的原因可能是撞击或者飞出边界
     */
    private void postProcessAction() {
        enemyBullets.removeIf(AbstractFlyingObject::notValid);
        heroBullets.removeIf(AbstractFlyingObject::notValid);
        enemyAircrafts.removeIf(AbstractFlyingObject::notValid);
        Props.removeIf(AbstractFlyingObject::notValid);
        for(AbstractAircraft abstractAircraft:enemyAircrafts){
            if(abstractAircraft.notValid()){
                bombPropPublisher.removeBombPropSubscriber(abstractAircraft);
            }
        }
        for(BaseBullet baseBullet:enemyBullets){
            if(baseBullet.notValid()){
                bombPropPublisher.removeBombPropSubscriber(baseBullet);
            }
        }
    }

    //***********************
    //      Paint 各部分
    //***********************

    /**
     * 重写paint方法
     * 通过重复调用paint方法，实现游戏动画
     *
     * @param  g
     */
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Image background;
        switch(gamelevel){
            case"easy":
                background = BACKGROUND_EASY_IMAGE;
                break;
            case"common":
                background = BACKGROUND_COMMON_IMAGE;
                break;
            case"difficult":
                background = BACKGROUND_DIFFICULT_IMAGE;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + gamelevel);
        }

        // 绘制背景,图片滚动
        g.drawImage(background, 0, this.backGroundTop - Main.WINDOW_HEIGHT, null);
        g.drawImage(background, 0, this.backGroundTop, null);
        this.backGroundTop += 1;
        if (this.backGroundTop == Main.WINDOW_HEIGHT) {
            this.backGroundTop = 0;
        }

        // 先绘制子弹，后绘制飞机
        // 这样子弹显示在飞机的下层
        paintImageWithPositionRevised(g, enemyBullets);
        paintImageWithPositionRevised(g, heroBullets);

        paintImageWithPositionRevised(g, enemyAircrafts);

        paintImageWithPositionRevised(g, Props);

        g.drawImage(HERO_IMAGE, heroAircraft.getLocationX() - HERO_IMAGE.getWidth() / 2,
                heroAircraft.getLocationY() - HERO_IMAGE.getHeight() / 2, null);

        //绘制得分和生命值
        paintScoreAndLife(g);

    }

    private void paintImageWithPositionRevised(Graphics g, List<? extends AbstractFlyingObject> objects) {
        if (objects.size() == 0) {
            return;
        }

        for (AbstractFlyingObject object : objects) {
            BufferedImage image = object.getImage();
            assert image != null : objects.getClass().getName() + " has no image! ";
            g.drawImage(image, object.getLocationX() - image.getWidth() / 2,
                    object.getLocationY() - image.getHeight() / 2, null);
        }
    }

    private void paintScoreAndLife(Graphics g) {
        int x = 10;
        int y = 25;
        g.setColor(new Color(16711680));
        g.setFont(new Font("SansSerif", Font.BOLD, 22));
        g.drawString("SCORE:" + this.score, x, y);
        y = y + 20;
        g.drawString("LIFE:" + this.heroAircraft.getHp(), x, y);
    }


}
