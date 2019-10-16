package com.lkd.shoot;

import java.util.TimerTask;

public class FlyGameTimerTask  extends TimerTask {
    private ShootGame mainFrm;
    public FlyGameTimerTask(ShootGame jp){
        mainFrm=jp;
    }
    @Override
    public void run() {
        if (mainFrm.getState()== ShootGame.RUNNING){
            mainFrm.enterAction();
            mainFrm.stepAction();
            mainFrm.shootAction();
            mainFrm.bangAction();
            mainFrm.outOfBoundsAction();
            mainFrm.checkGameOverAction();
        }
        mainFrm.repaint();//重绘
    }
}
