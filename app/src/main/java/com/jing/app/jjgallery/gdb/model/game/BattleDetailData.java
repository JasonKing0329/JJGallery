package com.jing.app.jjgallery.gdb.model.game;

import com.king.service.gdb.game.bean.BattleBean;
import com.king.service.gdb.game.bean.CoachBean;
import com.king.service.gdb.game.bean.PlayerBean;
import com.king.service.gdb.game.bean.SeasonBean;

import java.util.List;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/2/8 15:02
 */
public class BattleDetailData {
    private SeasonBean season;
    private CoachBean coach;
    private List<PlayerBean> playerListTop;
    private List<PlayerBean> playerListBottom;
    private List<BattleBean> battleList;

    public CoachBean getCoach() {
        return coach;
    }

    public void setCoach(CoachBean coach) {
        this.coach = coach;
    }

    public List<PlayerBean> getPlayerListBottom() {
        return playerListBottom;
    }

    public void setPlayerListBottom(List<PlayerBean> playerListBottom) {
        this.playerListBottom = playerListBottom;
    }

    public List<PlayerBean> getPlayerListTop() {
        return playerListTop;
    }

    public void setPlayerListTop(List<PlayerBean> playerListTop) {
        this.playerListTop = playerListTop;
    }

    public SeasonBean getSeason() {
        return season;
    }

    public void setSeason(SeasonBean season) {
        this.season = season;
    }

    public List<BattleBean> getBattleList() {
        return battleList;
    }

    public void setBattleList(List<BattleBean> battleList) {
        this.battleList = battleList;
    }
}
