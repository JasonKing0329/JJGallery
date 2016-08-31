package com.jing.app.jjgallery.viewsystem;

/**
 * Created by JingYang on 2016/7/29 0029.
 * Description:
 * 显示统一样式的等待框以及toast
 */
public interface ProgressProvider {

    int TOAST_DEFAULT = 0;
    int TOAST_SUCCESS = 1;
    int TOAST_ERROR = 2;
    int TOAST_INFOR = 3;
    int TOAST_WARNING = 4;

    /**
     * 显示雷达扫描样式的等待框
     */
    void showProgressCycler();
    /**
     * 隐藏雷达扫描样式的等待框
     */
    boolean dismissProgressCycler();
    /**
     * 显示默认样式的等待框
     */
    void showProgress(String text);
    /**
     * 隐藏默认样式的等待框
     */
    boolean dismissProgress();

    /**
     * 显示默认toast, 长时间
     */
    void showToastLong(String text);
    /**
     * 显示默认toast, 短时间
     */
    void showToastShort(String text);
    /**
     * 显示默认toast, 长时间
     */
    void showToastLong(String text, int type);
    /**
     * 显示默认toast, 短时间
     */
    void showToastShort(String text, int type);
}
