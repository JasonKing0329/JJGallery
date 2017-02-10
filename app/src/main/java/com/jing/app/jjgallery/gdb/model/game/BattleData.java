package com.jing.app.jjgallery.gdb.model.game;

import com.king.service.gdb.game.bean.CoachBean;
import com.king.service.gdb.game.bean.SeasonBean;

import java.util.List;

/**
 * 描述: Battle页面的初始化参数
 * <p/>作者：景阳
 * <p/>创建时间: 2017/2/8 11:20
 */
public class BattleData {

    private SeasonBean season;
    private CoachBean coach1;
    private CoachBean coach2;
    private CoachBean coach3;
    private CoachBean coach4;

    public CoachBean getCoach1() {
        return coach1;
    }

    public void setCoach1(CoachBean coach1) {
        this.coach1 = coach1;
    }

    public CoachBean getCoach2() {
        return coach2;
    }

    public void setCoach2(CoachBean coach2) {
        this.coach2 = coach2;
    }

    public CoachBean getCoach3() {
        return coach3;
    }

    public void setCoach3(CoachBean coach3) {
        this.coach3 = coach3;
    }

    public CoachBean getCoach4() {
        return coach4;
    }

    public void setCoach4(CoachBean coach4) {
        this.coach4 = coach4;
    }

    public SeasonBean getSeason() {
        return season;
    }

    public void setSeason(SeasonBean season) {
        this.season = season;
    }
}
