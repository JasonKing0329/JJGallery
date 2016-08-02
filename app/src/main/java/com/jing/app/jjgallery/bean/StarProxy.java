package com.jing.app.jjgallery.bean;

import com.king.service.gdb.bean.Star;

/**
 * Created by JingYang on 2016/8/2 0002.
 * Description:
 */
public class StarProxy {
    private Star star;
    private String imagePath;

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
}
