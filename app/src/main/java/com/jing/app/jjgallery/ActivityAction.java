package com.jing.app.jjgallery;

import android.content.res.Configuration;

import com.jing.app.jjgallery.viewsystem.publicview.ActionBar;

/**
 * Created by Administrator on 2016/8/29.
 */
public interface ActivityAction {

    /**
     * 是否全屏
     * @return
     */
    boolean isFullScreen();

    /**
     * 设置ActionBar（历史设计缺陷，mActionbar作为protected参数由子类复用）
     * @param actionBar
     */
    void setActionBar(ActionBar actionBar);

    /**
     * 是否需要action bar
     * @return
     */
    boolean isActionBarNeed();

    /**
     * 主布局
     * @return
     */
    int getContentView();

    /**
     * 初始化controller/presenter
     */
    void initController();

    /**
     * 初始化视图
     */
    void initView();

    /**
     * 开始后台工作
     */
    void initBackgroundWork();

    /**
     * 屏幕转向发生改变
     * @param newConfig
     */
    void onOrentaionChanged(Configuration newConfig);

}
