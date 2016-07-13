package com.jing.app.jjgallery.viewsystem;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.jing.app.jjgallery.BaseActivity;
import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.config.PreferenceKey;
import com.jing.app.jjgallery.controller.AccessController;
import com.jing.app.jjgallery.presenter.main.SettingProperties;
import com.jing.app.jjgallery.viewsystem.main.filesystem.FileManagerActivity;
import com.jing.app.jjgallery.viewsystem.main.order.SOrderActivity;
import com.jing.app.jjgallery.viewsystem.main.settings.SettingsActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/6/23 0023.
 */
public class HomeSelecter implements  HomeProvider {

    private Context mContext;
    private List<HomeBean> list;

    public HomeSelecter(Context context) {
        mContext = context;
        list = new ArrayList<>();
        HomeBean bean = new HomeBean();
        bean.setPreferenceKey(PreferenceKey.START_VIEW_FILEMANAGER);
        bean.setName(context.getString(R.string.tab_filemanager));
        list.add(bean);
        bean = new HomeBean();
        bean.setPreferenceKey(PreferenceKey.START_VIEW_SORDER);
        bean.setName(context.getString(R.string.tab_sorder));
        list.add(bean);
        bean = new HomeBean();
        bean.setPreferenceKey(PreferenceKey.START_VIEW_TIMELINE);
        bean.setName(context.getString(R.string.setting_start_view_timeline));
        list.add(bean);
    }

    @Override
    public void startDefaultHome(Activity from, Object datas) {
        int startViewMode = SettingProperties.getStartViewMode(from);
        startHome(from, startViewMode, datas);
    }

    @Override
    public void startHome(Activity from, int key, Object datas) {
        switch (key) {
            case PreferenceKey.START_VIEW_TIMELINE:
//                activity.startActivity(new Intent().setClass(activity, TimeLineUpdateActivity.class));
                break;
            case PreferenceKey.START_VIEW_GUIDE:
//                activity.startActivity(new Intent().setClass(activity, GuideActivity.class));
                break;
            case PreferenceKey.START_VIEW_SORDER:
                from.startActivity(new Intent().setClass(from, SOrderActivity.class));
                break;
            default:
                from.startActivity(new Intent().setClass(from, FileManagerActivity.class));
                break;
        }
    }

    @Override
    public List<HomeBean> getHomeList() {
        return list;
    }
}
