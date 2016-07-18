package com.jing.app.jjgallery.presenter.main.order;

/**
 * Created by JingYang on 2016/7/11 0011.
 * Description:
 */
public interface SOrderProviderCallback {
    void onMoveFinish(String folderPath);

    void onAddToOrderFinished();

    void onDeleteIndex(int index);
    void onDeleteFinished(int count);
}
