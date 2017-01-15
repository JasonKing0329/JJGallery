package com.king.service.gdb.game.bean;

/**
 * Created by 景阳 on 2017/1/10.
 */

public class SeasonBean extends GameBean {

    private int sequence;
    private String name;
    private int matchRule;
    private int coachId1;
    private int coachId2;
    private int coachId3;
    private int coachId4;
    private String coverPath;

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMatchRule() {
        return matchRule;
    }

    public void setMatchRule(int matchRule) {
        this.matchRule = matchRule;
    }

    public int getCoachId1() {
        return coachId1;
    }

    public void setCoachId1(int coachId1) {
        this.coachId1 = coachId1;
    }

    public int getCoachId2() {
        return coachId2;
    }

    public void setCoachId2(int coachId2) {
        this.coachId2 = coachId2;
    }

    public int getCoachId3() {
        return coachId3;
    }

    public void setCoachId3(int coachId3) {
        this.coachId3 = coachId3;
    }

    public int getCoachId4() {
        return coachId4;
    }

    public void setCoachId4(int coachId4) {
        this.coachId4 = coachId4;
    }

    public String getCoverPath() {
        return coverPath;
    }

    public void setCoverPath(String coverPath) {
        this.coverPath = coverPath;
    }
}
