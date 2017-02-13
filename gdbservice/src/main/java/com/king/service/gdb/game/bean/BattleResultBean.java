package com.king.service.gdb.game.bean;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/2/13 14:38
 */
public class BattleResultBean extends GameBean {
    private int seasonId;
    private int coachId;
    private int rank;
    private int score;
    private int playerId;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getCoachId() {
        return coachId;
    }

    public void setCoachId(int coachId) {
        this.coachId = coachId;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getSeasonId() {
        return seasonId;
    }

    public void setSeasonId(int seasonId) {
        this.seasonId = seasonId;
    }

    private int type
            ;
}
