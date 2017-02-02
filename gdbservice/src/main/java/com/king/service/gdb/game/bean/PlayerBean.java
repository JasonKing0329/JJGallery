package com.king.service.gdb.game.bean;

/**
 * Created by Administrator on 2017/2/1 0001.
 */

public class PlayerBean extends GameBean {
    private String name;
    private String topRound;
    private String bottomRound;
    private int topSeasonId;
    private int bottomSeasonId;
    private int topCoachId;
    private int bottomCoachId;

    public int getBottomCoachId() {
        return bottomCoachId;
    }

    public void setBottomCoachId(int bottomCoachId) {
        this.bottomCoachId = bottomCoachId;
    }

    public String getBottomRound() {
        return bottomRound;
    }

    public void setBottomRound(String bottomRound) {
        this.bottomRound = bottomRound;
    }

    public int getBottomSeasonId() {
        return bottomSeasonId;
    }

    public void setBottomSeasonId(int bottomSeasonId) {
        this.bottomSeasonId = bottomSeasonId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTopCoachId() {
        return topCoachId;
    }

    public void setTopCoachId(int topCoachId) {
        this.topCoachId = topCoachId;
    }

    public String getTopRound() {
        return topRound;
    }

    public void setTopRound(String topRound) {
        this.topRound = topRound;
    }

    public int getTopSeasonId() {
        return topSeasonId;
    }

    public void setTopSeasonId(int topSeasonId) {
        this.topSeasonId = topSeasonId;
    }
}
