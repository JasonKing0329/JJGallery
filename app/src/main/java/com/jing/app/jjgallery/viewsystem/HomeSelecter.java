package com.jing.app.jjgallery.viewsystem;

import android.app.Activity;
import android.content.Intent;

import com.jing.app.jjgallery.viewsystem.main.settings.SettingsActivity;

/**
 * Created by Administrator on 2016/6/23 0023.
 */
public class HomeSelecter implements  HomeProvider {
    @Override
    public void startHomeActivity(Activity activity, Object datas) {
        Intent intent = new Intent();
        intent.setClass(activity, SettingsActivity.class);
        activity.startActivity(intent);
    }
}
