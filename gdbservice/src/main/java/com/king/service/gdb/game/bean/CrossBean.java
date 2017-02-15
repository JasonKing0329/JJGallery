package com.king.service.gdb.game.bean;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/2/8 11:22
 */
public class CrossBean extends BattleBean {
    private int coach2Id;
    private int rematchFlag;

    public int getCoach2Id() {
        return coach2Id;
    }

    public void setCoach2Id(int coach2Id) {
        this.coach2Id = coach2Id;
    }

    public int getRematchFlag() {
        return rematchFlag;
    }

    public void setRematchFlag(int rematchFlag) {
        this.rematchFlag = rematchFlag;
    }
}
