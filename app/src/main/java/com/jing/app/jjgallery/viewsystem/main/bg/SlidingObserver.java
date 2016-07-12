package com.jing.app.jjgallery.viewsystem.main.bg;

/**
 * Created by JingYang on 2016/7/12 0012.
 * Description:
 */
public interface SlidingObserver {
    int TYPE_LEFT = 0;
    int TYPE_LEFT_LAND = 1;
    int TYPE_RIGHT = 2;
    int TYPE_RIGHT_LAND = 3;
    void notifySlidingBkChanged(int type, String path);
    void notifySlidingCircleChanged(String path);

}
