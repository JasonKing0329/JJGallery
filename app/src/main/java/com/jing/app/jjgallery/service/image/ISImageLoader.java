package com.jing.app.jjgallery.service.image;

import android.widget.ImageView;

/**
 * Created by JingYang on 2016/7/18 0018.
 * Description:
 */
public interface ISImageLoader {
    void displayImage(String path, ImageView imageView);
    void setDefaultImgRes(int resId);
}
