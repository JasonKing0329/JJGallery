package com.jing.app.jjgallery.viewsystem.main.bg;

import android.content.Context;

import com.jing.app.jjgallery.config.PreferenceKey;
import com.jing.app.jjgallery.presenter.main.SettingProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JingYang on 2016/7/12 0012.
 * Description:
 */
public class BackgroundManager implements SlidingObserver {

    private static BackgroundManager instance;
    private List<SlidingSubscriber> slidingSubscriberList;

    private BackgroundManager() {
        slidingSubscriberList = new ArrayList<>();
    }

    public static BackgroundManager getInstance() {
        if (instance == null) {
            instance = new BackgroundManager();
        }
        return instance;
    }

    public void addSlidingSubscriber(SlidingSubscriber subscriber) {
        slidingSubscriberList.add(subscriber);
    }

    public void removeSlidingSubscriber(SlidingSubscriber subscriber) {
        slidingSubscriberList.remove(subscriber);
    }

    private void setSlidingLeftBg(Context context, String newPath) {
        String path = SettingProperties.getPreference(context, PreferenceKey.PREF_SLIDING_BK_LEFT);
        if (!newPath.equals(path)) {
            SettingProperties.savePreference(context, PreferenceKey.PREF_SLIDING_BK_LEFT, newPath);
            notifySlidingBkChanged(TYPE_LEFT, newPath);
        }
    }

    private void setSlidingLeftLandBg(Context context, String newPath) {
        String path = SettingProperties.getPreference(context, PreferenceKey.PREF_SLIDING_BK_LEFT_LAND);
        if (!newPath.equals(path)) {
            SettingProperties.savePreference(context, PreferenceKey.PREF_SLIDING_BK_LEFT_LAND, newPath);
            notifySlidingBkChanged(TYPE_LEFT_LAND, newPath);
        }
    }

    private void setSlidingRightBg(Context context, String newPath) {
        String path = SettingProperties.getPreference(context, PreferenceKey.PREF_SLIDING_BK_RIGHT);
        if (!newPath.equals(path)) {
            SettingProperties.savePreference(context, PreferenceKey.PREF_SLIDING_BK_RIGHT, newPath);
            notifySlidingBkChanged(TYPE_RIGHT, newPath);
        }
    }

    private void setSlidingRightLandBg(Context context, String newPath) {
        String path = SettingProperties.getPreference(context, PreferenceKey.PREF_SLIDING_BK_RIGHT_LAND);
        if (!newPath.equals(path)) {
            SettingProperties.savePreference(context, PreferenceKey.PREF_SLIDING_BK_RIGHT_LAND, newPath);
            notifySlidingBkChanged(TYPE_RIGHT_LAND, newPath);
        }
    }

    private void setSlidingCircle(Context context, String newPath) {
        String path = SettingProperties.getPreference(context, PreferenceKey.PREF_SLIDING_CIRCLE);
        if (!newPath.equals(path)) {
            SettingProperties.savePreference(context, PreferenceKey.PREF_SLIDING_CIRCLE, newPath);
            notifySlidingCircleChanged(newPath);
        }
    }

    public void setBackground(Context context, String key, String path) {
        if (key.equals(PreferenceKey.PREF_SLIDING_BK_LEFT)) {
            setSlidingLeftBg(context, path);
        }
        else if (key.equals(PreferenceKey.PREF_SLIDING_BK_LEFT_LAND)) {
            setSlidingLeftLandBg(context, path);
        }
        else if (key.equals(PreferenceKey.PREF_SLIDING_BK_RIGHT)) {
            setSlidingRightBg(context, path);
        }
        else if (key.equals(PreferenceKey.PREF_SLIDING_BK_RIGHT_LAND)) {
            setSlidingRightLandBg(context, path);
        }
        else if (key.equals(PreferenceKey.PREF_SLIDING_CIRCLE)) {
            setSlidingCircle(context, path);
        }
    }
    
    @Override
    public void notifySlidingBkChanged(int type, String path) {
        for (SlidingSubscriber subscriber:slidingSubscriberList) {
            switch (type) {
                case TYPE_LEFT:
                    subscriber.onSlidingLeftBgChanged(path);
                    break;
                case TYPE_LEFT_LAND:
                    subscriber.onSlidingLeftLandBgChanged(path);
                    break;
                case TYPE_RIGHT:
                    subscriber.onSlidingRightBgChanged(path);
                    break;
                case TYPE_RIGHT_LAND:
                    subscriber.onSlidingRightLandBgChanged(path);
                    break;
            }
        }
    }

    @Override
    public void notifySlidingCircleChanged(String path) {
        for (SlidingSubscriber subscriber:slidingSubscriberList) {
            subscriber.onSlidingCircleChanged(path);
        }
    }

    public List<BkBean> getItemList() {
        List<BkBean> list = new ArrayList<>();
        BkBean bean = new BkBean();
        bean.setPreferenceKey(PreferenceKey.PREF_SLIDING_BK_LEFT);
        bean.setDetailName("Home -> Sliding menu -> Left -> Background");
        list.add(bean);
        bean = new BkBean();
        bean.setPreferenceKey(PreferenceKey.PREF_SLIDING_BK_LEFT_LAND);
        bean.setDetailName("Home -> Sliding menu -> Left(Landscape) -> Background");
        list.add(bean);
        bean = new BkBean();
        bean.setPreferenceKey(PreferenceKey.PREF_SLIDING_BK_RIGHT);
        bean.setDetailName("Home -> Sliding menu -> Right -> Background");
        list.add(bean);
        bean = new BkBean();
        bean.setPreferenceKey(PreferenceKey.PREF_SLIDING_BK_RIGHT_LAND);
        bean.setDetailName("Home -> Sliding menu -> Right(Landscape) -> Background");
        list.add(bean);
        bean = new BkBean();
        bean.setPreferenceKey(PreferenceKey.PREF_SLIDING_CIRCLE);
        bean.setDetailName("Home -> Sliding menu -> Left -> Circle");
        list.add(bean);
        return list;
    }


}
