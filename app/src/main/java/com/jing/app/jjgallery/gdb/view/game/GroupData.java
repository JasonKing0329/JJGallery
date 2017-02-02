package com.jing.app.jjgallery.gdb.view.game;

import com.king.service.gdb.game.bean.CoachBean;
import com.king.service.gdb.game.bean.PlayerBean;
import com.king.service.gdb.game.bean.SeasonBean;

import java.util.List;

/**
 * Created by Administrator on 2017/1/28 0028.
 */

public class GroupData {
    private SeasonBean season;
    private CoachBean coach1;
    private CoachBean coach2;
    private CoachBean coach3;
    private CoachBean coach4;
    private List<PlayerBean> playerListTop1;
    private List<PlayerBean> playerListTop2;
    private List<PlayerBean> playerListTop3;
    private List<PlayerBean> playerListTop4;
    private List<PlayerBean> playerListBottom1;
    private List<PlayerBean> playerListBottom2;
    private List<PlayerBean> playerListBottom3;
    private List<PlayerBean> playerListBottom4;

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

    public List<PlayerBean> getPlayerListBottom1() {
        return playerListBottom1;
    }

    public void setPlayerListBottom1(List<PlayerBean> playerListBottom1) {
        this.playerListBottom1 = playerListBottom1;
    }

    public List<PlayerBean> getPlayerListBottom2() {
        return playerListBottom2;
    }

    public void setPlayerListBottom2(List<PlayerBean> playerListBottom2) {
        this.playerListBottom2 = playerListBottom2;
    }

    public List<PlayerBean> getPlayerListBottom3() {
        return playerListBottom3;
    }

    public void setPlayerListBottom3(List<PlayerBean> playerListBottom3) {
        this.playerListBottom3 = playerListBottom3;
    }

    public List<PlayerBean> getPlayerListBottom4() {
        return playerListBottom4;
    }

    public void setPlayerListBottom4(List<PlayerBean> playerListBottom4) {
        this.playerListBottom4 = playerListBottom4;
    }

    public List<PlayerBean> getPlayerListTop1() {
        return playerListTop1;
    }

    public void setPlayerListTop1(List<PlayerBean> playerListTop1) {
        this.playerListTop1 = playerListTop1;
    }

    public List<PlayerBean> getPlayerListTop2() {
        return playerListTop2;
    }

    public void setPlayerListTop2(List<PlayerBean> playerListTop2) {
        this.playerListTop2 = playerListTop2;
    }

    public List<PlayerBean> getPlayerListTop3() {
        return playerListTop3;
    }

    public void setPlayerListTop3(List<PlayerBean> playerListTop3) {
        this.playerListTop3 = playerListTop3;
    }

    public List<PlayerBean> getPlayerListTop4() {
        return playerListTop4;
    }

    public void setPlayerListTop4(List<PlayerBean> playerListTop4) {
        this.playerListTop4 = playerListTop4;
    }

    public SeasonBean getSeason() {
        return season;
    }

    public void setSeason(SeasonBean season) {
        this.season = season;
    }
}
