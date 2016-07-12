package com.jing.app.jjgallery.presenter.main;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.config.PreferenceKey;

/**
 * Created by JingYang on 2016/6/24 0024.
 * Description:
 */
public class SettingProperties {

    public static boolean isFingerPrintEnable(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean(PreferenceKey.PREF_SAFETY_FP, false);
    }

    public static boolean isAppInited(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean(PreferenceKey.PREF_APP_INITED, false);
    }

    public static String getFileManagerDefaultMode(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(PreferenceKey.PREF_FM_VIEW, PreferenceKey.VALUE_FM_VIEW_LIST);
    }

    public static void setAppInited(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(PreferenceKey.PREF_APP_INITED, true);
        editor.commit();
    }

    public static boolean isShowFileOriginMode(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean(PreferenceKey.PREF_FM_ORIGIN, false);
    }

    public static int getStartViewMode(Context context) {
        String modes[] = context.getResources().getStringArray(R.array.setting_start_view_key);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String loadMode = preferences.getString("setting_start_view", modes[0]);
        if (loadMode.equals(modes[0])) {
            return PreferenceKey.START_VIEW_FILEMANAGER;
        }
        else if (loadMode.equals(modes[1])) {
            return PreferenceKey.START_VIEW_GUIDE;
        }
        else {
            return PreferenceKey.START_VIEW_TIMELINE;
        }
    }

    public static void savePreference(Context context, String key, String value) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getPreference(Context context, String key) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(key, null);
    }

    public static int getAutoPlayMode(Context context) {
        String[] modes = context.getResources().getStringArray(R.array.setting_auto_play_mode_key);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String mode = preferences.getString("setting_auto_play_mode", modes[0]);
        if (mode.equals(modes[0])) {
            return PreferenceKey.AUTOPLAY_MODE_SEQUENCE;
        }
        else if (mode.equals(modes[1])) {
            return PreferenceKey.AUTOPLAY_MODE_RANDOM;
        }
        else {
            return PreferenceKey.AUTOPLAY_MODE_SEQUENCE;
        }
    }

    public static int getMinNumberToPlay(Context context) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String text = preferences.getString("setting_min_items", "7");
        int min_number = Integer.parseInt(text);
        return min_number;
    }

    public static int getAnimationSpeed(Context context) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String[] speeds = context.getResources().getStringArray(R.array.setting_auto_play_time_key);
        String text = preferences.getString("setting_auto_play_speed", speeds[0]);
        int speed = context.getResources().getInteger(R.integer.autoplay_speed_fast);
        if (text.equals(speeds[1])) {//normal
            speed = context.getResources().getInteger(R.integer.autoplay_speed_normal);
        }
        else if (text.equals(speeds[2])) {//slow
            speed = context.getResources().getInteger(R.integer.autoplay_speed_slow);
        }
        return speed;
    }
}
