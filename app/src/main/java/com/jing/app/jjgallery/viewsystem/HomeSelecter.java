package com.jing.app.jjgallery.viewsystem;

import android.app.Activity;
import android.content.Intent;

import com.jing.app.jjgallery.config.PreferenceKey;
import com.jing.app.jjgallery.controller.AccessController;
import com.jing.app.jjgallery.presenter.main.SettingProperties;
import com.jing.app.jjgallery.viewsystem.main.filesystem.FileManagerActivity;
import com.jing.app.jjgallery.viewsystem.main.settings.SettingsActivity;

/**
 * Created by Administrator on 2016/6/23 0023.
 */
public class HomeSelecter implements  HomeProvider {
    @Override
    public void startHomeActivity(Activity activity, Object datas) {

        AccessController.getInstance().changeAccessMode(AccessController.ACCESS_MODE_SUPERUSER);

        int startViewMode = SettingProperties.getStartViewMode(activity);
        switch (startViewMode) {
            case PreferenceKey.START_VIEW_TIMELINE:
//                activity.startActivity(new Intent().setClass(activity, TimeLineUpdateActivity.class));
                break;
            case PreferenceKey.START_VIEW_GUIDE:
//                activity.startActivity(new Intent().setClass(activity, GuideActivity.class));
                break;
            default:
                activity.startActivity(new Intent().setClass(activity, FileManagerActivity.class));
                break;
        }
    }
}
