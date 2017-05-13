package com.jing.app.jjgallery.gdb.bean;

import com.king.service.gdb.bean.Star;

/**
 * Created by JingYang on 2016/8/2 0002.
 * Description:
 */
public class StarProxy {
    private Star star;
    private String imagePath;
    private int favor;

    public Star getStar() {
        return star;
    }

    public void setStar(Star star) {
        this.star = star;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public int getFavor() {
        return favor;
    }

    public void setFavor(int favor) {
        this.favor = favor;
    }
}
