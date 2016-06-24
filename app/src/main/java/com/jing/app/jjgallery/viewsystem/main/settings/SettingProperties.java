package com.jing.app.jjgallery.viewsystem.main.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by JingYang on 2016/6/24 0024.
 * Description:
 */
public class SettingProperties {

    private static final String PREF_APP_INITED = "pref_app_inited";

    public static boolean isFingerPrintEnable(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean("pref_safety_fingerprint", false);
    }

    public static boolean isAppInited(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean(PREF_APP_INITED, false);
    }

    public static void setAppInited(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(PREF_APP_INITED, true);
        editor.commit();
    }
}
