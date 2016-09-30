package com.jing.app.jjgallery.viewsystem.publicview.starmap;

/**
 * Created by Administrator on 2016/9/22.
 */
public interface StarMapObserver {

    @Deprecated
    void onDataSetChanged();

    void goToShow(int animMode);
}
