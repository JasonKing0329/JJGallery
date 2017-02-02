package com.jing.app.jjgallery.gdb.bean;

import com.king.service.gdb.bean.Star;
import com.king.service.gdb.game.bean.PlayerBean;

/**
 * Created by Administrator on 2017/2/1 0001.
 */

public class GamePlayerBean extends PlayerBean {
    private Star star;
    private boolean hasBeenTop;
    private boolean hasBeenBottom;

    public Star getStar() {
        return star;
    }

    public void setStar(Star star) {
        this.star = star;
    }

    public void setHasBeenTop(boolean hasBeenTop) {
        this.hasBeenTop = hasBeenTop;
    }

    public boolean isHasBeenBottom() {
        return hasBeenBottom;
    }

    public void setHasBeenBottom(boolean hasBeenBottom) {
        this.hasBeenBottom = hasBeenBottom;
    }

    public boolean isHasBeenTop() {
        return hasBeenTop;
    }
}
