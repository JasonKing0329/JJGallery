package com.jing.app.jjgallery.model.main.file;

/**
 * Created by JingYang on 2016/7/11 0011.
 * Description:
 */
public interface OnOrderItemMoveTrigger {
    void onTrigger(String src, String target, boolean allFinish);
    void onNotSupport(String src, boolean allFinish);
}
