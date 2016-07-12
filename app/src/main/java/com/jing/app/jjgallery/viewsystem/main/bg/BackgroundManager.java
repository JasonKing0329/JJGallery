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

    public void setSlidingLeftBg(Context context, String newPath) {
        String path = SettingProperties.getPreference(context, PreferenceKey.PREF_SLIDING_BK_LEFT);
        if (!newPath.equals(path)) {
            SettingProperties.savePreference(context, PreferenceKey.PREF_SLIDING_BK_LEFT, newPath);
            notifySlidingBkChanged(TYPE_LEFT, newPath);
        }
    }

    public void setSlidingLeftLandBg(Context context, String newPath) {
        String path = SettingProperties.getPreference(context, PreferenceKey.PREF_SLIDING_BK_LEFT_LAND);
        if (!newPath.equals(path)) {
            SettingProperties.savePreference(context, PreferenceKey.PREF_SLIDING_BK_LEFT_LAND, newPath);
            notifySlidingBkChanged(TYPE_LEFT_LAND, newPath);
        }
    }

    public void setSlidingRightBg(Context context, String newPath) {
        String path = SettingProperties.getPreference(context, PreferenceKey.PREF_SLIDING_BK_RIGHT);
        if (!newPath.equals(path)) {
            SettingProperties.savePreference(context, PreferenceKey.PREF_SLIDING_BK_RIGHT, newPath);
            notifySlidingBkChanged(TYPE_RIGHT, newPath);
        }
    }

    public void setSlidingRightLandBg(Context context, String newPath) {
        String path = SettingProperties.getPreference(context, PreferenceKey.PREF_SLIDING_BK_RIGHT_LAND);
        if (!newPath.equals(path)) {
            SettingProperties.savePreference(context, PreferenceKey.PREF_SLIDING_BK_RIGHT_LAND, newPath);
            notifySlidingBkChanged(TYPE_RIGHT_LAND, newPath);
        }
    }

    public void setSlidingCircle(Context context, String newPath) {
        String path = SettingProperties.getPreference(context, PreferenceKey.PREF_SLIDING_CIRCLE);
        if (!newPath.equals(path)) {
            SettingProperties.savePreference(context, PreferenceKey.PREF_SLIDING_CIRCLE, path);
            notifySlidingCircleChanged(newPath);
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

    }
}
