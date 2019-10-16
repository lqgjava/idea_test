package com.lkd.shoot;

import java.util.Random;

/**
 * 封装英雄机的属性和功能类
 */
public class Hero extends Flyer {

    private int doubleFire; //双倍火力子弹数
    private int life; //生命值
    private int score; //得分

    //对外提供读取生命值的方法
    public int getLife(){
        return life;
    }

    //对外提供的获取得分的方法
    public int getScore(){
        return score;
    }

    /**
     * 英雄机对象的无参构造方法
     */
    public Hero(){
        image = ShootGame.hero0;
        height = image.getHeight();
        width = image.getWidth();
        x = 127;
        y = 388;
        doubleFire = 0;
        life = 3;
        score = 0;
    }

    /**
     * 实现英雄机的动画效果的方法
     * 让英雄机的图片在hero0和hero1之斗切换
     */
    @Override
    public void step() {
        Random r = new Random();
        if(r.nextInt(2) == 0){
            image = ShootGame.hero0;
        }else{
            image = ShootGame.hero1;
        }
    }

    @Override
    public boolean outOfBounds() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void moveTo(int x, int y) {

    }

    /**
     * 英雄机随鼠标移动的方法
     * 要求传入鼠标当前的位置
     * @param x 鼠标位置的x坐标
     * @param y 鼠标位置的y坐标
     */
    public void move(int x,int y){
        //传入的x，y是鼠标的坐标
        //move的作用是让英雄机的中心位置和鼠标位置一致
        this.x = x - width / 2;
        this.y = y - height / 2;
    }

    /**
     * 英雄机获得分数或奖励的方法
     * @param f 是一个飞行物父类方法，可以指向敌机或者大飞机
     */
    public void getScore_Award(Flyer f){
        //先判断敌人对象的类型
        if(f instanceof Airplane){ //如果敌人是敌机
            //获得敌机对象中的分数，加到当现分数上
            score += ((Airplane)f).getScore();
        }else{ //如果对象是大飞机
            //继续判断大飞机对象中保存的奖励类型
            if(((BigPlane)f).getAwardType() == BigPlane.DOUBLE_FIRE){
                //如果保存的是双倍火力
                doubleFire += 20;
            }else{
                //如果保存的是生命值奖励
                life += 1;
            }
        }

    }

    /**
     * 英雄机发射子弹的方法
     * @return 新创建出来的子弹对名
     * 			可能是一发，也可能 是两发，用数组保存
     */
    public Bullet[] shoot(){
        Bullet[] bullets = null;
        //何时开启双倍火力：
        if(doubleFire != 0){ //创建双倍火力
            bullets = new Bullet[2];
            Bullet b1 = new Bullet(x + width/4 - ShootGame.bullet.getWidth()/2,y + ShootGame.bullet.getWidth());
            Bullet b2 = new Bullet(x + width*3/4 - ShootGame.bullet.getWidth()/2,y + ShootGame.bullet.getWidth());
            bullets[0] = b1;
            bullets[1] = b2;
            //每创建一个双倍火力，doubleFire-1
            doubleFire -= 1;
        }else{
            //单倍火力：
            //子弹x坐标：x+英雄机宽度/2-子弹宽度/2
            //子弹y坐标：y-子弹高度
            bullets = new Bullet[1];
            bullets[0] = new Bullet(x + width/2 - ShootGame.bullet.getWidth()/2,y - ShootGame.bullet.getHeight());
        }
        return bullets;
    }

    /**
     * 英雄机自带和敌人碰撞检测方法
     * @param f 可能发生碰撞的敌人
     * 			可能是敌机也可能是大飞机
     * @return 是否碰撞
     */
    public boolean hit(Flyer f){
        //调用碰撞检测方法，检测是否碰撞
        boolean r = Flyer.boom(this, f);
        if(r){ //如果碰撞
            life--;
            doubleFire = 0;
        }
        return r;
    }

}