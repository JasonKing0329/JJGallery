package com.jing.app.jjgallery.viewsystem.main.bg;

import android.content.Context;
import android.support.annotation.NonNull;

import com.jing.app.jjgallery.config.PreferenceKey;
import com.jing.app.jjgallery.presenter.main.SettingProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JingYang on 2016/7/12 0012.
 * Description: 背景设置器，通过设置图片路径以preference key的形式存储
 * getItemList增加需要设置的背景项
 * XXXObserver绑定相应的设置功能（notifyXXXChanged）
 * XXXSubScriber注册要实时变化的位置(onXXXChanged)（要注意add和remove的时机）
 */
public class BackgroundManager implements SlidingObserver, FMBgObserver, SOrderBgObserver
    , ProgressObserver{

    private static BackgroundManager instance;
    private List<SlidingSubscriber> slidingSubscriberList;
    private List<FMBgSubscriber> fmBgSubscriberList;
    private List<SOrderSubscriber> sorderSubscriberList;
    private List<ProgressSubscriber> progressSubscriberList;

    private BackgroundManager() {
        slidingSubscriberList = new ArrayList<>();
        fmBgSubscriberList = new ArrayList<>();
        sorderSubscriberList = new ArrayList<>();
        progressSubscriberList = new ArrayList<>();
    }

    public static BackgroundManager getInstance() {
        if (instance == null) {
            instance = new BackgroundManager();
        }
        return instance;
    }

    /**
     * sliding menu background of home
     * @param subscriber
     */
    public void addSlidingSubscriber(SlidingSubscriber subscriber) {
        slidingSubscriberList.add(subscriber);
    }

    public void removeSlidingSubscriber(SlidingSubscriber subscriber) {
        slidingSubscriberList.remove(subscriber);
    }

    /**
     * background of file manager related page
     * @param subscriber
     */
    public void addFMBgSubscriber(FMBgSubscriber subscriber) {
        fmBgSubscriberList.add(subscriber);
    }

    public void removeFMBgSubscriber(FMBgSubscriber subscriber) {
        fmBgSubscriberList.remove(subscriber);
    }

    /**
     * background of sorder related page
     * @param subscriber
     */
    public void addSOrderSubscriber(SOrderSubscriber subscriber) {
        sorderSubscriberList.add(subscriber);
    }

    public void removeSOrderSubscriber(SOrderSubscriber subscriber) {
        sorderSubscriberList.remove(subscriber);
    }

    /**
     * background of progress view
     * @param subscriber
     */
    public void addProgressSubscriber(ProgressSubscriber subscriber) {
        progressSubscriberList.add(subscriber);
    }

    public void removeProgressSubscriber(ProgressSubscriber subscriber) {
        progressSubscriberList.remove(subscriber);
    }

    /**
     * save by SharedPreference
     * @param context
     * @param key
     * @param newPath
     * @return
     */
    private boolean saveVale(Context context, String key, String newPath) {
        String path = SettingProperties.getPreference(context, key);
        if (!newPath.equals(path)) {
            SettingProperties.savePreference(context, key, newPath);
            return true;
        }
        return false;
    }

    /**
     * save background related to the key
     * @param context
     * @param key Preference key
     * @param path image path
     */
    public void setBackground(Context context, String key, String path) {
        if (saveVale(context, key, path)) {
            if (key.equals(PreferenceKey.PREF_SLIDING_BK_LEFT)) {
                notifySlidingBkChanged(TYPE_LEFT, path);
            }
            else if (key.equals(PreferenceKey.PREF_SLIDING_BK_LEFT_LAND)) {
                notifySlidingBkChanged(TYPE_LEFT_LAND, path);
            }
            else if (key.equals(PreferenceKey.PREF_SLIDING_BK_RIGHT)) {
                notifySlidingBkChanged(TYPE_RIGHT, path);
            }
            else if (key.equals(PreferenceKey.PREF_SLIDING_BK_RIGHT_LAND)) {
                notifySlidingBkChanged(TYPE_RIGHT_LAND, path);
            }
            else if (key.equals(PreferenceKey.PREF_SLIDING_CIRCLE)) {
                notifySlidingCircleChanged(path);
            }
            else if (key.equals(PreferenceKey.PREF_BG_FM_INDEX)) {
                notifyFMIndexBgChanged(path);
            }
            else if (key.equals(PreferenceKey.PREF_BG_FM_INDEX_LAND)) {
                notifyFMIndexBgLandChanged(path);
            }
            else if (key.equals(PreferenceKey.PREF_BG_SORDER_INDEX)) {
                notifySOrderIndexBgChanged(path);
            }
            else if (key.equals(PreferenceKey.PREF_BG_SORDER_INDEX_LAND)) {
                notifySOrderIndexBgLandChanged(path);
            }
            else if (key.equals(PreferenceKey.PREF_BG_PROGRESS)) {
                notifyProgressSrcChanged(path);
            }
            // gdb相关页面都是离开后随即销毁，不通知更新
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

    @Override
    public void notifyFMIndexBgChanged(String path) {
        for (FMBgSubscriber subscriber:fmBgSubscriberList) {
            subscriber.onIndexBgChanged(path);
        }
    }

    @Override
    public void notifyFMIndexBgLandChanged(String path) {
        for (FMBgSubscriber subscriber:fmBgSubscriberList) {
            subscriber.onIndexBgLandChanged(path);
        }
    }

    @Override
    public void notifySOrderIndexBgChanged(String path) {
        for (SOrderSubscriber subscriber:sorderSubscriberList) {
            subscriber.onIndexBgChanged(path);
        }
    }

    @Override
    public void notifySOrderIndexBgLandChanged(String path) {
        for (SOrderSubscriber subscriber:sorderSubscriberList) {
            subscriber.onIndexBgLandChanged(path);
        }
    }

    @Override
    public void notifyProgressSrcChanged(String path) {
        for (ProgressSubscriber subscriber:progressSubscriberList) {
            subscriber.onProgressSrcChanged(path);
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
        bean = new BkBean();
        bean.setPreferenceKey(PreferenceKey.PREF_BG_FM_INDEX);
        bean.setDetailName("Home -> File manager -> Index page");
        list.add(bean);
        bean = new BkBean();
        bean.setPreferenceKey(PreferenceKey.PREF_BG_FM_INDEX_LAND);
        bean.setDetailName("Home -> File manager -> Index page(Landscape)");
        list.add(bean);
        bean = new BkBean();
        bean.setPreferenceKey(PreferenceKey.PREF_BG_SORDER_INDEX);
        bean.setDetailName("Home -> SOrder page -> Index page");
        list.add(bean);
        bean = new BkBean();
        bean.setPreferenceKey(PreferenceKey.PREF_BG_SORDER_INDEX_LAND);
        bean.setDetailName("Home -> SOrder page -> Index page(Landscape)");
        list.add(bean);
        bean = new BkBean();
        bean.setPreferenceKey(PreferenceKey.PREF_BG_PROGRESS);
        bean.setDetailName("Progress view");
        list.add(bean);
        bean = new BkBean();
        bean.setPreferenceKey(PreferenceKey.PREF_GDB_GAME_BG);
        bean.setDetailName("GDB->guide->game bg");
        list.add(bean);
        bean = new BkBean();
        bean.setPreferenceKey(PreferenceKey.PREF_GDB_STAR_BG);
        bean.setDetailName("GDB->guide->star bg");
        list.add(bean);
        bean = new BkBean();
        bean.setPreferenceKey(PreferenceKey.PREF_GDB_RECORD_BG);
        bean.setDetailName("GDB->guide->record bg");
        list.add(bean);
        bean = new BkBean();
        bean.setPreferenceKey(PreferenceKey.PREF_GDB_NAV_HEADER_BG);
        bean.setDetailName("GDB->guide->navigation header view");
        list.add(bean);
        return list;
    }

}
