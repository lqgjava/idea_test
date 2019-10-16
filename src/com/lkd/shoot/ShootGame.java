package com.lkd.shoot;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class ShootGame extends JPanel {

    private static final long serialVersionUID = 1L;

    //背景图片的大小320*568
    public static final int WIDTH = 320;
    public static final int HEIGHT = 568;
    //游戏界面固定大小336*607
    public static final int FRAME_WIDTH = 336;

    public static final int FRAME_HEIGHT = 607;

    /*
     * 游戏启动第一件事是从硬盘加载所有要用到的图片到内存当中
     * 而且仅在启动时加载一次——静态块
     * 缓存在程序中的所有图片，都会反复使用，仅保存一份——静态变量
     * 下面，为每张图片加载一个静态变量，然后在静态块加加载每张图片
     */
    public static BufferedImage background; //背景图片
    public static BufferedImage start; //开始图片
    public static BufferedImage airplane; //敌机图片
    public static BufferedImage bigplane; //大飞机
    public static BufferedImage hero0; //英雄机状态0
    public static BufferedImage hero1; //英雄机状态1
    public static BufferedImage bullet; //子弹
    public static BufferedImage pause; //暂停图片
    public static BufferedImage gameover; //游戏结束

    //静态块，在类加载到方法区时执行一次，专门加载静态资源
    static{
        /*
         * java从硬盘中加载图片到内存中：
         * ImageIO.read方法：专门从硬盘中加载图片的静态方法
         * 不用实例化，直接调用
         * ShootGame.class:获得当前类的加载器所在路径
         * ShootGame.class.getRerource("文件名"); 从当前类所在路径加载指定文件到程序中
         */
        try {
            background = ImageIO.read(ShootGame.class.getResource("/com/lkd/images/background.png"));
            airplane = ImageIO.read(ShootGame.class.getResource("/com/lkd/images/airplane.png"));
            bigplane = ImageIO.read(ShootGame.class.getResource("/com/lkd/images/bigplane.png"));
            bullet = ImageIO.read(ShootGame.class.getResource("/com/lkd/images/bullet.png"));
            start = ImageIO.read(ShootGame.class.getResource("/com/lkd/images/start.png"));
            pause = ImageIO.read(ShootGame.class.getResource("/com/lkd/images/pause.png"));
            hero0 = ImageIO.read(ShootGame.class.getResource("/com/lkd/images/hero01.png"));
            hero1 = ImageIO.read(ShootGame.class.getResource("/com/lkd/images/hero02.png"));
            gameover = ImageIO.read(ShootGame.class.getResource("/com/lkd/images/gameover.png"));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /*
     * 为游戏中的角色定义数据结构，包括：
     * 1个英雄机对象
     * 1个存储所有敌人（敌机和大飞机）的对象数组
     * 1个存储所有子弹的对象数组
     */
    public Hero hero = new Hero();
    public Flyer[] flyers = {}; //存储所有敌人对象的数组
    public Bullet[] bullets = {}; //存储所有子弹对象的数组

    //定义游戏状态：当前状态变量：默认为开始状态
    private int state = START;
    //定义游戏状态的备选项常量：
    public static final int START = 0;
    public static final int RUNNING = 1;
    public static final int PAUSE = 2;
    public static final int GAME_OVER = 3;
    public Timer timer;
    private int intervel = 1000 / 100;//时间间隔(毫秒)

    public Flyer[] getFlyings() {
        return flyers;
    }

    public void setFlyings(Flyer[] flyings) {
        this.flyers = flyers;
    }

    public static void main(String[] args) {

        /*
         * java中绘制窗体：JFrame对象——窗框
         * 要想在窗体中绘制内容，还需要嵌入背景面板——JPanel
         */
        JFrame frame = new JFrame("ShootGame");
        frame.setSize(FRAME_WIDTH,FRAME_HEIGHT);//(336, 607);
        frame.setAlwaysOnTop(true); //设置窗体置顶
        //设置窗体关闭同时，退出程序
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null); //设置窗体的位置，null表示居中

        /*在窗体中嵌入背景面板对象——JPanel*/
        ShootGame game = new ShootGame(); //创建背景面板对象
        frame.add(game); //将背景面板对象嵌入到窗体对象中
        /*窗体默认不可见！必须调用setVisible方法才能显示窗体*/
        frame.setVisible(true); //自动调用窗体的paint方法
        game.action();
    }
    /**
     * 游戏启动时要做的事
     */
    public void action() {
        FlyMouseAdapter flyAdapter = new FlyMouseAdapter(this);
        this.addMouseListener(flyAdapter);      //处理鼠标点击操作
        this.addMouseMotionListener(flyAdapter);//处理鼠标滑动操作

        timer = new Timer();//主流程控制
        FlyGameTimerTask flyTask = new FlyGameTimerTask(this);//定时器任务
        timer.schedule(flyTask, intervel, intervel);
    }
    	 int flyEnteredIndex = 0; // 飞行物入场计数
    	 /** 飞行物入场 */
    	 


/*游戏开始时，要定义鼠标事件的监听*/

        //step1: 创建MouseAdapter匿名内部类——事件的响应程序
//        MouseAdapter l = new MouseAdapter(){
//            //step2: 重写希望的鼠标事件——鼠标移动
//            @Override
//            public void mouseMoved(MouseEvent e) {
//                //只有在RUNNING状态下英雄机才跟随鼠标移动
//                if(state == RUNNING){
//                    //step3: 获得鼠标新位置
//                    int x = e.getX();
//                    int y = e.getY();
//                    //step4: 将鼠标位置传给英雄机的move方法
//                    hero.move(x, y);
//                }
//            }
//
//            @Override
//            public void mouseClicked(MouseEvent e) {
//                if(state == START || state == PAUSE){ //START或者PAUSE状态，单击才会改改为RUNNING状态
//                    state = RUNNING;
//                }else if(state == RUNNING){ //游戏点击暂停
//                    state = PAUSE;
//                }else if(state == GAME_OVER){ //游戏结束后单击，游戏初始化
//                    state = START;
//                    //从GAME_OVER到START，要重新初始化游戏数据
//                    flyers = new Flyer[0];
//                    bullets = new Bullet[0];
//                    hero = new Hero();
//                }
//            }
//
//
//            @Override
//            public void mouseExited(MouseEvent e) {
//                if(state == RUNNING){
//                    //仅在处于RUNNING状态下，鼠标移出才暂停
//                    state = PAUSE;
//                }
//            }
//
//            @Override
//            public void mouseEntered(MouseEvent e) {
//                if(state == PAUSE){
//                    state = RUNNING;
//                }
//            }
//
//        }; //匿名内部类要以分号结尾

/*step5: 要响应鼠标事件，必须将鼠标事件添加到程序的监听器中*/
//
//        this.addMouseMotionListener(l); //支持鼠标的移动事件，不支持鼠标单击事件
//        this.addMouseListener(l);; //支持鼠标单击事件
//
//        //step1: 创建定时器
//        Timer timer = new Timer();
//        //step2: 调用定时器对象的schedule方法，做计划
//        //       第一个参数：TimerTask类型的匿名内部类
//        //               必须重写run方法——核心——要做什么事
//        timer.schedule(new TimerTask(){
//            //首先定义一个计时器变量index，记录run方法运行的次数
//            private int runTimes = 0;
//
//            @Override
//            public void run() {
//                //除了repaint方法，其余功能只在RUNNING状态下执行
//                if(state == RUNNING){
//                    //每执行一次run方法，runTimes就+1
//                    runTimes++;
//
//                    //每500亳秒生成一次敌人
//                    if(runTimes % 50 == 0){
//                        nextOne(); //自动随机创建敌人对象
//                    }
//                    //遍历每一个对象，调用对象的step方法，移动一次对象的位置
//                    for(int i = 0;i < flyers.length;i++){
//                        flyers[i].step();
//                    }
//
//                    //每300亳秒生成一次子弹
//                    if(runTimes % 30 == 0){
//                        shoot(); //创建一次子弹
//                    }
//                    //遍历子弹数组的每一个对象，移动位置
//                    for(int i = 0;i < bullets.length;i++){
//                        bullets[i].step();
//                    }
//
//                    //英雄机动画效果
//                    hero.step();
//
//                    //添加子弹和敌人的碰撞检测
//                    boom();
//
//                    //英雄机碰撞检测
//                    hit();
//
//                    //添加越界检测
//                    outOfBounds();
//                }
//
///*强调：只要界面发生变化，必须调用repaint方法重新绘制界面*/
//
//                repaint();
//            }
//
//        }, 10,10); //界面每隔10亳秒变化一次
//
//    }


    @Override
    public void paint(Graphics g) {
        //step1: 绘制背景图片
        g.drawImage(background, 0, 0, null);
        //step2: 绘制英雄机
        paintHero(g);
        //step3: 批量绘制敌人数组
        paintFlyers(g);
        //step4: 批量绘制子弹数组
        paintBullets(g);
        //绘制分数和生命值
        paintScore_Life(g);

        //根据游戏状态绘制不同图片
        if(state == START){
            g.drawImage(start, 0, 0, null);
        }else if(state == PAUSE){
            g.drawImage(pause, 0, 0, null);
        }else if(state == GAME_OVER){
            g.drawImage(gameover, 0, 0, null);
        }

    }

    /**
     * 绘制英雄机对象的方法
     * @param g 画笔
     */
    public void paintHero(Graphics g){
        g.drawImage(hero.image, hero.x, hero.y, null);
    }

    /**
     * 遍历敌人数组，批量绘制所有敌人的方法
     * @param g
     */
    public void paintFlyers(Graphics g){
        for(int i = 0;i < flyers.length;i++){
            g.drawImage(flyers[i].image, flyers[i].x, flyers[i].y, null);
        }
    }

    /**
     * 遍历子弹数组，批量绘制所有子弹的方法
     * @param g
     */
    public void paintBullets(Graphics g){
        for(int i = 0;i < bullets.length;i++){
            g.drawImage(bullets[i].image, bullets[i].x, bullets[i].y, null);
        }
    }

    /**
     * 随机生成1个敌人对象
     * 每生成一个新敌人， flyers数组就要扩容1
     * 然后将新敌人放入数组最后一个元素
     */
    public void nextOne(){
        Random r = new Random();
        Flyer f = null;
        if(r.nextInt(20) == 0){ //只有随机数取0时才创建大飞机
            f = new BigPlane();
        }else{ //其余全部生成敌机
            f = new Airplane();
        }
        //对flyers数组扩容1
        flyers = Arrays.copyOf(flyers, flyers.length + 1);
        //将新敌人放入数组末尾
        flyers[flyers.length - 1] = f;
    }

    /**
     * 获得英雄机对象发射的子弹对象
     * 将新的子弹对象保存到子弹数组中，统一管理
     */
    public void shoot(){
        Bullet[] newBullets = hero.shoot(); //获得英雄机返回的新子弹数组
        //根据返回新子弹的数量，扩容子弹数组
        bullets = Arrays.copyOf(bullets, bullets.length + newBullets.length);
        //从newBullets数组中拷贝所有元素到bullets数组末尾
        System.arraycopy(newBullets, 0, bullets, bullets.length - newBullets.length, newBullets.length);

    }

    /**
     * 遍历子弹数组和敌人数组，进行碰撞检测
     * 一旦发生碰撞，子弹和敌人都减少一个
     */
    public void boom(){
        for(int i = 0;i < bullets.length;i++){
            for(int j = 0;j < flyers.length;j++){
                if(Flyer.boom(bullets[i], flyers[j])){
                    //为英雄机获得分数和奖励
                    hero.getScore_Award(flyers[j]);
                    //从敌人数组中删除被击中的敌机
                    //step1： 使用敌人数组最后一个元素替换被击中的敌机
                    flyers[j] = flyers[flyers.length - 1];
                    //step2: 压缩数组
                    flyers = Arrays.copyOf(flyers, flyers.length - 1);
                    //从子弹数组中删除击中敌机的子弹
                    bullets[i] = bullets[bullets.length - 1];
                    bullets = Arrays.copyOf(bullets, bullets.length -1);
                    i--; //第发现一次碰撞，子弹就要退一个元素，重新检测当前位置
                    break; //只要发现碰撞就退出当前敌人数组的循环
                }
            }
        }
    }

    /**
     * 绘制分数和生命值的方法
     * @param g
     */
    public void paintScore_Life(Graphics g){
        int x = 10; //文字在左上角的x坐标
        int y = 15; //文字在左上角的y坐标
        Font font = new Font(Font.SANS_SERIF,Font.BOLD,16);
        g.setFont(font); //设置字体的画笔对象
        //绘制第一行:分数
        g.drawString("分数: " + hero.getScore(), x, y);
        //绘制第二行：生命值，y坐标下移20个单位
        y += 20;
        g.drawString("生命: " + hero.getLife(), x, y);
    }

    /**
     * 检查所有飞行物是否越界
     */
    public void outOfBounds(){
        //检查所有敌人是否越界
        Flyer[] Flives = new Flyer[flyers.length];
        //遍历敌人数组，将存活的敌人对象存到新数组中
        //设置Flives数组的计数器index: 1.标示下一个存活对象的位置
        //						  2.统计Flives中一共有多少元素
        int index = 0;
        for(int i = 0;i < flyers.length;i++){
            if(!flyers[i].outOfBounds()){ //没有越界的对象
                Flives[index] = flyers[i];
                index++;
            } //遍历结束后：
            //index是存活对象的个数
            //Flives数组里是存活的对象，个数为index
            //把Flives数组压缩为index大小
            //压缩后的新数组 应替换回flyers数组
        }
        flyers = Arrays.copyOf(Flives, index);

        //检测所有子弹是否越界
        Bullet[] Blives = new Bullet[bullets.length];
        index = 0;
        for(int i = 0;i < bullets.length;i++){
            if(!bullets[i].outOfBounds()){
                Blives[index] = bullets[i];
                index++;
            }
        }
        bullets = Arrays.copyOf(Blives, index);
    }

    /**
     * 遍历敌人数组，判断英雄机和每个敌人是否碰撞
     */
    public void hit(){
        Flyer[] lives = new Flyer[flyers.length];
        //记录存活的敌人
        int index = 0;
        for(int i = 0;i < flyers.length;i++){
            if(!hero.hit(flyers[i])){
                lives[index] = flyers[i];
                index++;
            }
        }
        if(hero.getLife() <= 0){ //如果英雄机生命值小于等于0，游戏结束
            state = GAME_OVER;
        }
        //压缩敌人数组，并替换数组
        flyers = Arrays.copyOf(lives, index);

    }

    public int getState() {
        return 1;
    }

    public void setState(int i) {
    }

    public void checkGameOverAction() {
    }

    public void outOfBoundsAction() {
    }

    public void bangAction() {
    }

    public void shootAction() {
    }

    public void stepAction() {
    }

    public void enterAction() {
    }

    public Hero getHero() {
        return hero;
    }

    public void setHero(Hero hero) {
        this.hero = hero;
    }

    public Flyer[] getFlyers() {
        return flyers;
    }

    public void setFlyers(Flyer[] flyers) {
        this.flyers = flyers;
    }

    public Bullet[] getBullets() {
        return bullets;
    }

    public void setBullets(Bullet[] bullets) {
        this.bullets = bullets;
    }

    public static int getSTART() {
        return START;
    }

    public static int getRUNNING() {
        return RUNNING;
    }

    public static int getPAUSE() {
        return PAUSE;
    }

    public static int getGameOver() {
        return GAME_OVER;
    }

    public Timer getTimer() {
        return timer;
    }

    public void setTimer(Timer timer) {
        this.timer = timer;
    }

    public int getIntervel() {
        return intervel;
    }

    public void setIntervel(int intervel) {
        this.intervel = intervel;
    }

    public int getRunning() {
        return 0;
    }
}
