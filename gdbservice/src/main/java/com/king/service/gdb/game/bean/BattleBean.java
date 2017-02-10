package com.king.service.gdb.game.bean;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/2/8 11:22
 */
public class BattleBean extends GameBean {
    private int seasonId;
    private int coachId;
    private int topPlayerId;
    private int bottomPlayerId;
    private int round;
    private String scene;
    private String score;

    public int getBottomPlayerId() {
        return bottomPlayerId;
    }

    public void setBottomPlayerId(int bottomPlayerId) {
        this.bottomPlayerId = bottomPlayerId;
    }

    public int getCoachId() {
        return coachId;
    }

    public void setCoachId(int coachId) {
        this.coachId = coachId;
    }

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
    }

    public String getScene() {
        return scene;
    }

    public void setScene(String scene) {
        this.scene = scene;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public int getSeasonId() {
        return seasonId;
    }

    public void setSeasonId(int seasonId) {
        this.seasonId = seasonId;
    }

    public int getTopPlayerId() {
        return topPlayerId;
    }

    public void setTopPlayerId(int topPlayerId) {
        this.topPlayerId = topPlayerId;
    }
}
