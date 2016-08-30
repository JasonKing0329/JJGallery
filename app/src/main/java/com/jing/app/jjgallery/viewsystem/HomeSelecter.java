package com.jing.app.jjgallery.viewsystem;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.config.PreferenceValue;
import com.jing.app.jjgallery.controller.AccessController;
import com.jing.app.jjgallery.presenter.main.SettingProperties;
import com.jing.app.jjgallery.viewsystem.main.gdb.GDBHomeActivity;
import com.jing.app.jjgallery.viewsystem.main.order.SOrderActivity;
import com.jing.app.jjgallery.viewsystem.main.timeline.HomeWaterfallActivity;
import com.jing.app.jjgallery.viewsystem.main.timeline.TimeLineActivity;

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
        bean.setPreferenceKey(PreferenceValue.START_VIEW_FILEMANAGER);
        bean.setName(context.getString(R.string.tab_filemanager));
        list.add(bean);
        bean = new HomeBean();
        bean.setPreferenceKey(PreferenceValue.START_VIEW_SORDER);
        bean.setName(context.getString(R.string.tab_sorder));
        list.add(bean);
        bean = new HomeBean();
        bean.setPreferenceKey(PreferenceValue.START_VIEW_TIMELINE);
        bean.setName(context.getString(R.string.setting_start_view_timeline));
        list.add(bean);
        bean = new HomeBean();
        bean.setPreferenceKey(PreferenceValue.START_GDB);
        bean.setName(context.getString(R.string.setting_start_view_gdb));
        list.add(bean);
    }

    @Override
    public void startDefaultHome(Activity from, Object datas) {
        startDefaultHome(from, datas, null);
    }

    @Override
    public void startDefaultHome(Activity from, Object datas, Bundle bundle) {
        AccessController.getInstance().changeAccessMode(AccessController.ACCESS_MODE_SUPERUSER);
        int startViewMode = SettingProperties.getStartViewMode(from);
        startHome(from, startViewMode, datas, bundle);
    }

    @Override
    public boolean startHome(Activity from, int key, Object datas) {
        return startHome(from, key, datas, null);
    }

    @Override
    public boolean startHome(Activity from, int key, Object datas, Bundle bundle) {
        boolean result = true;
        switch (key) {
            case PreferenceValue.START_VIEW_TIMELINE:
                if (from instanceof TimeLineActivity || from instanceof HomeWaterfallActivity) {//禁止重复打开当前页面
                    result = false;
                    break;
                }
                String mode = SettingProperties.getTimelineDefaultMode(from);
                if (PreferenceValue.VALUE_TIMELINE_VIEW_WATERFALL.equals(mode)) {
                    ActivityManager.startWaterfallActivity(from, bundle);
                }
                else if (PreferenceValue.VALUE_TIMELINE_VIEW_TIMELINE.equals(mode)){
                    ActivityManager.startTimeLineActivity(from, bundle);
                }
                break;
            case PreferenceValue.START_VIEW_GUIDE:
//                activity.startActivity(new Intent().setClass(activity, GuideActivity.class));
                break;
            case PreferenceValue.START_VIEW_SORDER:
                if (from instanceof SOrderActivity) {//禁止重复打开当前页面
                    result = false;
                    break;
                }
                ActivityManager.startSOrderActivity(from, bundle);
                break;
            case PreferenceValue.START_GDB:
                if (from instanceof GDBHomeActivity) {//禁止重复打开当前页面
                    result = false;
                    break;
                }
                result = ActivityManager.startGDBHomeActivity(from, bundle);
                break;
            default:
                ActivityManager.startFileManagerActivity(from, bundle);
                break;
        }
        return result;
    }

    @Override
    public List<HomeBean> getHomeList() {
        return list;
    }
}
