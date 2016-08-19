package com.jing.app.jjgallery.service.image;

import android.widget.ImageView;

/**
 * Created by JingYang on 2016/7/18 0018.
 * Description:
 */
public interface ISImageLoader {

    /**
     * 将path指定的图片显示到imageView上
     * @param path
     * @param imageView
     */
    void displayImage(String path, ImageView imageView);

    /**
     * 当指定加载的path不存在或有误时，显示的默认图片资源
     * @param resId
     */
    void setDefaultImgRes(int resId);

    /**
     * 强制从cache中删除指定缓存图片
     * @param path
     */
    void removeCache(String path);
}
