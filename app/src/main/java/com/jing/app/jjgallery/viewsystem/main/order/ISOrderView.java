package com.jing.app.jjgallery.viewsystem.main.order;

import android.content.Context;

/**
 * Created by JingYang on 2016/7/13 0013.
 * Description:
 */
public interface ISOrderView {
    void onGridPage();
    void onThumbPage();
    void onIndexPage();

    Context getContext();
}
