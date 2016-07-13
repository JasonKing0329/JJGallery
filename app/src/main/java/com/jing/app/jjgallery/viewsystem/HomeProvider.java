package com.jing.app.jjgallery.viewsystem;

import android.app.Activity;

import java.util.List;

/**
 * Created by Administrator on 2016/6/23 0023.
 */
public interface HomeProvider {

    void startDefaultHome(Activity from, Object datas);
    void startHome(Activity from, int key, Object datas);
    List<HomeBean> getHomeList();
}
