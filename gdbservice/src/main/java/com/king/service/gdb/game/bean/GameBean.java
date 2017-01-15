package com.king.service.gdb.game.bean;

/**
 * Created by Administrator on 2017/1/15 0015.
 */

public class GameBean {

    /**
     * -1 是区分未添加和已添加。0属于id的范畴
     */
    private int id = -1;
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
