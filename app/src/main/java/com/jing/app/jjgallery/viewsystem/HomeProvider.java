package com.jing.app.jjgallery.viewsystem;

import android.app.Activity;
import android.os.Bundle;

import java.util.List;

/**
 * Created by Administrator on 2016/6/23 0023.
 */
public interface HomeProvider {

    void startDefaultHome(Activity from, Object datas);
    void startDefaultHome(Activity from, Object datas, Bundle bundle);
    boolean startHome(Activity from, int key, Object datas);
    boolean startHome(Activity from, int key, Object datas, Bundle bundle);
    List<HomeBean> getHomeList();
}
