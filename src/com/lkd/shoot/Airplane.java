package com.lkd.shoot;

import java.util.Random;

/**
 * 封装敌机属性和功能的类
 */
public class Airplane extends Flyer {

    private int speed = 2; //敌机每次下落2个单位长度
    private int score = 5; //敌机包含的奖励分数

    //对外提供的读取敌机奖励分数的方法
    public int getScore(){
        return score;
    }

    /**
     * 敌机类的无参构造方法
     */
    public Airplane(){
        image = ShootGame.airplane;
        width = image.getWidth();
        height = image.getHeight();
        y = -height;
        Random r = new Random();
        x = r.nextInt(ShootGame.WIDTH - width);
    }

    @Override
    public void step() {
        //敌机每次向下移动一个speed长度
        y += speed;
    }

    @Override
    public boolean outOfBounds() {
        //敌机y坐标>游戏界面，越界
        return y > ShootGame.HEIGHT;
    }

    @Override
    public void moveTo(int x, int y) {

    }

}
