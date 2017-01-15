package com.king.service.gdb.game.bean;

/**
 * Created by Administrator on 2017/1/13 0013.
 */

public class CoachBean extends GameBean {
    private String name;
    private String seasonIds;
    private int bestSeasonId;
    private int seasonCount;
    private String type;
    private String imagePath;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSeasonIds() {
        return seasonIds;
    }

    public void setSeasonIds(String seasonIds) {
        this.seasonIds = seasonIds;
    }

    public int getBestSeasonId() {
        return bestSeasonId;
    }

    public void setBestSeasonId(int bestSeasonId) {
        this.bestSeasonId = bestSeasonId;
    }

    public int getSeasonCount() {
        return seasonCount;
    }

    public void setSeasonCount(int seasonCount) {
        this.seasonCount = seasonCount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
