package com.lkd.shoot;


import java.util.Random;

/**
 * 封装大飞机属性和功能的类
 */
public class BigPlane extends Flyer {

    /*定义奖励类型的备选项常量*/
    public static final int DOUBLE_FIRE = 0; //奖励类型是0，说明奖励双倍火力
    public static final int FILE = 1; //奖励类型是1，说明奖励一次生命

    /*大飞机类私有成员*/
    private int xspeed = 1; //水平移动的速度为1
    private int yspeed = 2; //垂直移动的速度为2
    private int awardType; //当前大飞机保存的奖励类型

    //对外提供的读取大飞机奖励类型的方法
    public int getAwardType(){
        return awardType;
    }

    /**
     * 大飞机的无参构造方法
     */
    public BigPlane(){
        //step1: 从主程序中获取大飞机图片的静态变量——bigplane
        image = ShootGame.bigplane;
        //step2: 使用图片宽高设置对象宽高
        width = image.getWidth();
        height= image.getHeight();
        //step3: 设置大飞机开始下落的高度
        y = -height;
        //step4:  大飞机对象开始下落的x坐标在0~（界面宽度 - 大飞机图片宽度）之前随机
        Random r = new Random();
        x = r.nextInt(ShootGame.WIDTH - width);
        //在0和1之间随机先择一种奖励类型
        awardType = r.nextInt(2);
    }

    @Override
    public void step() {
        //每次x移动一个xspeed，y移动一个yspeed
        x += xspeed;
        y += yspeed;
        //大飞机不能起出边界，一旦超出那么xspeed*（-1），相当于反向移动
        if(x < 0 || x > ShootGame.WIDTH - width){
            xspeed *= -1;
        }
    }

    @Override
    public boolean outOfBounds() {
        //大飞机的y坐标>游戏界面，越界
        return y > ShootGame.HEIGHT;
    }

    @Override
    public void moveTo(int x, int y) {

    }
}
