package com.jing.app.jjgallery.gdb.model.game;

import com.king.service.gdb.game.bean.PlayerBean;

import java.util.List;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/2/15 11:24
 */
public class CrossData extends BattleData {
    private List<PlayerBean> coach1TopList;
    private List<PlayerBean> coach2TopList;
    private List<PlayerBean> coach3TopList;
    private List<PlayerBean> coach4TopList;
    private List<PlayerBean> coach1BottomList;
    private List<PlayerBean> coach2BottomList;
    private List<PlayerBean> coach3BottomList;
    private List<PlayerBean> coach4BottomList;

    public List<PlayerBean> getCoach1BottomList() {
        return coach1BottomList;
    }

    public void setCoach1BottomList(List<PlayerBean> coach1BottomList) {
        this.coach1BottomList = coach1BottomList;
    }

    public List<PlayerBean> getCoach1TopList() {
        return coach1TopList;
    }

    public void setCoach1TopList(List<PlayerBean> coach1TopList) {
        this.coach1TopList = coach1TopList;
    }

    public List<PlayerBean> getCoach2BottomList() {
        return coach2BottomList;
    }

    public void setCoach2BottomList(List<PlayerBean> coach2BottomList) {
        this.coach2BottomList = coach2BottomList;
    }

    public List<PlayerBean> getCoach2TopList() {
        return coach2TopList;
    }

    public void setCoach2TopList(List<PlayerBean> coach2TopList) {
        this.coach2TopList = coach2TopList;
    }

    public List<PlayerBean> getCoach3BottomList() {
        return coach3BottomList;
    }

    public void setCoach3BottomList(List<PlayerBean> coach3BottomList) {
        this.coach3BottomList = coach3BottomList;
    }

    public List<PlayerBean> getCoach3TopList() {
        return coach3TopList;
    }

    public void setCoach3TopList(List<PlayerBean> coach3TopList) {
        this.coach3TopList = coach3TopList;
    }

    public List<PlayerBean> getCoach4BottomList() {
        return coach4BottomList;
    }

    public void setCoach4BottomList(List<PlayerBean> coach4BottomList) {
        this.coach4BottomList = coach4BottomList;
    }

    public List<PlayerBean> getCoach4TopList() {
        return coach4TopList;
    }

    public void setCoach4TopList(List<PlayerBean> coach4TopList) {
        this.coach4TopList = coach4TopList;
    }
}
