package com.jing.app.jjgallery.viewsystem;

/**
 * Created by JingYang on 2016/7/29 0029.
 * Description:
 */
public interface ProgressProvider {
    void showProgressCycler();
    boolean dismissProgressCycler();
    void showProgress(String text);
    boolean dismissProgress();
}
