package com.jing.app.jjgallery.presenter.main;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.config.ConfManager;
import com.jing.app.jjgallery.config.PreferenceKey;
import com.jing.app.jjgallery.config.PreferenceValue;

/**
 * Created by JingYang on 2016/6/24 0024.
 * Description:
 */
public class SettingProperties {

    /**
     * 保存默认配置到扩展目录作为备份
     */
    public static void saveAsDefaultPreference(Context context) {
        ConfManager.saveDefaultPref(context);
    }

    /**
     * shaprePreference文件版本(com.jing.app.jjgallery_preferences.xml)
     */
    public static String getPrefVersion(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(PreferenceKey.PREF_VERSION, null);
    }

    /**
     * shaprePreference文件版本(com.jing.app.jjgallery_preferences.xml)
     * @param version version name
     */
    public static void setPrefVersion(Context context, String version) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PreferenceKey.PREF_VERSION, version);
        editor.commit();
    }

    /**
     * 指纹验证
     */
    public static boolean isFingerPrintEnable(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean(PreferenceKey.PREF_SAFETY_FP, false);
    }

    /**
     * 程序是否第一次打开（第一次登录成功前均属于第一次）
     */
    public static boolean isAppInited(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean(PreferenceKey.PREF_APP_INITED, false);
    }

    /**
     * 设置程序第一次登陆成功
     */
    public static void setAppInited(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(PreferenceKey.PREF_APP_INITED, true);
        editor.commit();
        saveAsDefaultPreference(context);
    }

    /**
     * 文件管理器默认打开视图
     */
    public static String getFileManagerDefaultMode(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(PreferenceKey.PREF_FM_VIEW, PreferenceValue.VALUE_FM_VIEW_LIST);
    }

    /**
     * SOrder默认打开视图
     */
    public static String getSOrderDefaultMode(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(PreferenceKey.PREF_SORDER_VIEW, PreferenceValue.VALUE_SORDER_VIEW_GRID);
    }

    /**
     * 随便看看数量
     */
    public static int getCasualLookNumber(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String text = preferences.getString(PreferenceKey.PREF_CASUAL_NUMBER, "" + context.getResources().getInteger(R.integer.setting_casual_number));
        return Integer.parseInt(text);
    }

    /**
     * 文件管理器--显示原文件名
     */
    public static boolean isShowFileOriginMode(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean(PreferenceKey.PREF_FM_ORIGIN, false);
    }

    /**
     * 主界面
     */
    public static int getStartViewMode(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String home = preferences.getString(PreferenceKey.PREF_HOME_VIEW, null);
        if (home != null) {
            try {
                return Integer.parseInt(home);
            } catch (Exception e) {
                e.printStackTrace();
                return  0;
            }
        }
        return 0;
    }

    /**
     * 保存key value
     * @param key preference key
     * @param value preference value
     */
    public static void savePreference(Context context, String key, String value) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.commit();
        saveAsDefaultPreference(context);
    }

    /**
     * get value by key
     * @param key preference key
     * @return preference value
     */
    public static String getPreference(Context context, String key) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(key, null);
    }

    /**
     * Surf自动播放模式
     * @return see PreferenceValue.AUTOPLAY_MODE_XXX
     */
    public static int getAutoPlayMode(Context context) {
        String[] modes = context.getResources().getStringArray(R.array.setting_auto_play_mode_key);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String mode = preferences.getString(PreferenceKey.PREF_SURF_PLAY_MODE, modes[0]);
        if (mode.equals(modes[0])) {
            return PreferenceValue.AUTOPLAY_MODE_SEQUENCE;
        }
        else if (mode.equals(modes[1])) {
            return PreferenceValue.AUTOPLAY_MODE_RANDOM;
        }
        else {
            return PreferenceValue.AUTOPLAY_MODE_SEQUENCE;
        }
    }

    /**
     * Surf自动播放速度
     * @return speed
     */
    public static int getAnimationSpeed(Context context) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String[] speeds = context.getResources().getStringArray(R.array.setting_auto_play_time_key);
        String text = preferences.getString(PreferenceKey.PREF_SURF_PLAY_SPEED, speeds[0]);
        int speed = context.getResources().getInteger(R.integer.autoplay_speed_fast);
        if (text.equals(speeds[1])) {//normal
            speed = context.getResources().getInteger(R.integer.autoplay_speed_normal);
        }
        else if (text.equals(speeds[2])) {//slow
            speed = context.getResources().getInteger(R.integer.autoplay_speed_slow);
        }
        return speed;
    }

    public static int getMinNumberToPlay(Context context) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String text = preferences.getString("setting_min_items", "7");
        return Integer.parseInt(text);
    }

    /**
     * sorder 封面模式
     */
    public static int getSOrderCoverMode(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String mode = preferences.getString(PreferenceKey.PREF_SORDER_COVER_MODE, "" + PreferenceValue.SORDER_COVER_SINGLE);
        return Integer.parseInt(mode);
    }

    /**
     * 设置sorder 封面模式
     * @param mode see PreferenceValue.SORDER_COVER_XXX
     */
    public static void setSOrderCoverMode(Context context, int mode) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PreferenceKey.PREF_SORDER_COVER_MODE, "" + mode);
        editor.commit();
        saveAsDefaultPreference(context);
    }

    /**
     * sorder grid界面，封面是cascade模式，显示的图片张数
     */
    public static int getCascadeCoverNumber(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String mode = preferences.getString(PreferenceKey.PREF_SORDER_CASCADE_NUM, "" + PreferenceValue.SORDER_CASECADE_NUM_DEFAULT);
        return Integer.parseInt(mode);
    }

    /**
     * sorder grid界面默认排序模式
     * @return see PreferenceValue.ORDERBY_XXX
     */
    public static int getOrderMode(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String mode = preferences.getString(PreferenceKey.PREF_SORDER_GRID_ORDER, "" + PreferenceValue.ORDERBY_NAME);
        return Integer.parseInt(mode);
    }

    /**
     * 设置sorder grid界面默认排序模式
     * @param mode see PreferenceValue.SORDER_COVER_XXX
     */
    public static void setOrderMode(Context context, int mode) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PreferenceKey.PREF_SORDER_GRID_ORDER, "" + mode);
        editor.commit();
        saveAsDefaultPreference(context);
    }

    /**
     * 启用翻页模式
     */
    public static boolean isPageModeEnable(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean(PreferenceKey.PREF_SORDER_PAGE_MODE, true);
    }

    /**
     * 翻页模式中每页数量
     */
    public static int getSOrderPageNumber(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String mode = preferences.getString(PreferenceKey.PREF_SORDER_PAGE_NUM, "" + PreferenceValue.SORDER_PAGE_NUM_DEFAULT);
        return Integer.parseInt(mode);
    }
}
