package com.jing.app.jjgallery.viewsystem.main.bg;

/**
 * Created by JingYang on 2016/7/12 0012.
 * Description:
 */
public interface SlidingSubscriber {
    void onSlidingLeftBgChanged(String path);
    void onSlidingLeftLandBgChanged(String path);
    void onSlidingRightBgChanged(String path);
    void onSlidingRightLandBgChanged(String path);
    void onSlidingCircleChanged(String path);
}
