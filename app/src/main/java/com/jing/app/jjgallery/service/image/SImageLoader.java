package com.jing.app.jjgallery.service.image;

import android.content.Context;

/**
 * Created by JingYang on 2016/7/18 0018.
 * Description:
 */
public class SImageLoader {

    private static com.jing.app.jjgallery.service.image.lru.ImageLoader instance;
    private static com.jing.app.jjgallery.service.image.lru2.ImageLoader instance2;

    /**
     * lru.ImageLoader在处理快速滑动过程中，
     * item都会直接显示实际图片，但是稍卡顿，回翻之前的item会闪回position不正确的图片
     * @return
     */
    public static ISImageLoader getInstance() {
        if (instance == null) {
            instance = com.jing.app.jjgallery.service.image.lru.ImageLoader.getInstance();
        }
        return instance;
    }

    /**
     * lru2.ImageLoader在处理快速滑动过程中，
     * 更顺畅，但是顺翻或者回翻都会大量显示default loading图片
     * @param context
     * @return
     */
    public static ISImageLoader getInstance(Context context) {
        if (instance2 == null) {
            instance2 = new com.jing.app.jjgallery.service.image.lru2.ImageLoader(context);
        }
        return instance2;
    }
}
