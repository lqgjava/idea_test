package com.lkd.shoot;

import com.lkd.shoot.Bullet;
import com.lkd.shoot.Hero;
import com.lkd.shoot.ShootGame;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;
/**
 * 鼠标适配器
 * @author Administrator
 *
 */
public class FlyMouseAdapter extends MouseAdapter {
    private ShootGame mainFrm;

    public FlyMouseAdapter(){}

    public FlyMouseAdapter(ShootGame jp) {
        mainFrm=jp;
    }

    // 鼠标移动
    @Override
    public void mouseMoved(MouseEvent e) {
        // 运行状态下移动英雄机--随鼠标位置
        if (mainFrm.getState() == ShootGame.getRUNNING() ) {
            int x = e.getX();
            int y = e.getY();
            mainFrm.getHero().moveTo(x, y);
        }
    }




    @Override
    public void mouseEntered(MouseEvent e) { // 鼠标进入
        if (mainFrm.getState() == ShootGame.PAUSE) { // 暂停状态下运行
            mainFrm.setState(ShootGame.RUNNING);
        }
    }


    @Override
    public void mouseExited(MouseEvent e) { // 鼠标退出
        if (mainFrm.getState() == ShootGame.getRUNNING()) { // 游戏未结束，则设置其为暂停
            mainFrm.setState(ShootGame.getPAUSE());
        }
    }


    @Override
    public void mouseClicked(MouseEvent e) { // 鼠标点击
        if(mainFrm.getState()== ShootGame.getSTART()) {
            mainFrm.setState(1);
        }else if(mainFrm.getState() == ShootGame.getGameOver()) {
            mainFrm.setState(mainFrm.getRunning());  // 启动状态下运行
            mainFrm.setFlyers(new Flyer[0]);    // 清空飞行物
            mainFrm.setBullets(new Bullet[0]); // 清空子弹
            mainFrm.setHero(new Hero());   // 重新创建英雄机
            // shootgame.setScore(0) ; // 清空成绩
            mainFrm.setState(mainFrm.getRunning()) ; // 状态设置为启动
        }

    }
}
