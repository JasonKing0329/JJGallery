package com.jing.app.jjgallery.presenter.main;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.jing.app.jjgallery.JJApplication;
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
     * @return see PreferenceValue.VALUE_FM_VIEW_XXX
     */
    public static String getFileManagerDefaultMode(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(PreferenceKey.PREF_FM_VIEW, PreferenceValue.VALUE_FM_VIEW_LIST);
    }

    /**
     * SOrder默认打开视图
     * @return see PreferenceValue.VALUE_SORDER_VIEW_XXX
     */
    public static String getSOrderDefaultMode(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(PreferenceKey.PREF_SORDER_VIEW, PreferenceValue.VALUE_SORDER_VIEW_GRID);
    }

    /**
     * TimeLine默认打开视图
     * @return see PreferenceValue.VALUE_SORDER_VIEW_XXX
     */
    public static String getTimelineDefaultMode(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(PreferenceKey.PREF_TIMELINE_VIEW, PreferenceValue.VALUE_TIMELINE_VIEW_TIMELINE);
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
     * sorder 点击gird page item打开模式
     */
    public static String getSOrderGridItemOpenMode(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String mode = preferences.getString(PreferenceKey.PREF_SORDER_GRID_ITEM_OPEN, "" + PreferenceValue.EXPLORE_OPEN_SURF);
        return mode;
    }

    /**
     * sorder 点击gird page item打开模式
     */
    public static String getSOrderIndexItemOpenMode(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String mode = preferences.getString(PreferenceKey.PREF_SORDER_INDEX_ITEM_OPEN, "" + PreferenceValue.EXPLORE_OPEN_WALL);
        return mode;
    }

    /**
     * filemanager 点击gird page item打开模式
     */
    public static String getFileManagerIndexItemOpenMode(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String mode = preferences.getString(PreferenceKey.PREF_FM_INDEX_ITEM_OPEN, "" + PreferenceValue.EXPLORE_OPEN_WALL);
        return mode;
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
     * gdb star record界面默认排序模式
     * @return see PreferenceValue.ORDERBY_XXX
     */
    public static int getGdbStarRecordOrderMode(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String mode = preferences.getString(PreferenceKey.PREF_GDB_STAR_RECORD_ORDER, "" + PreferenceValue.GDB_SR_ORDERBY_NONE);
        return Integer.parseInt(mode);
    }

    /**
     * 设置gdb star record默认排序模式
     * @param mode see PreferenceValue.SORDER_COVER_XXX
     */
    public static void setGdbStarRecordOrderMode(Context context, int mode) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PreferenceKey.PREF_GDB_STAR_RECORD_ORDER, "" + mode);
        editor.commit();
        saveAsDefaultPreference(context);
    }

    /**
     * gdb record list界面默认排序模式
     * @return see PreferenceValue.ORDERBY_XXX
     */
    public static int getGdbRecordOrderMode(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String mode = preferences.getString(PreferenceKey.PREF_GDB_RECORD_ORDER, "" + PreferenceValue.GDB_SR_ORDERBY_NONE);
        return Integer.parseInt(mode);
    }

    /**
     * 设置gdb star record默认排序模式
     * @param mode see PreferenceValue.SORDER_COVER_XXX
     */
    public static void setGdbRecordOrderMode(Context context, int mode) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PreferenceKey.PREF_GDB_RECORD_ORDER, "" + mode);
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

    /**
     * 瀑布流竖屏列数
     */
    public static int getWaterfallColumns(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String mode = preferences.getString(PreferenceKey.PREF_WATERFALL_COL, "" + context.getResources().getInteger(R.integer.waterfall_column));
        return Integer.parseInt(mode);
    }

    /**
     * 瀑布流横屏列数
     */
    public static int getWaterfallColumnsLand(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String mode = preferences.getString(PreferenceKey.PREF_WATERFALL_COL_LAND, "" + context.getResources().getInteger(R.integer.waterfall_column_land));
        return Integer.parseInt(mode);
    }

    /**
     * 访问量页面展示的top个数
     */
    public static int getAccessRankNumbers(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String mode = preferences.getString(PreferenceKey.PREF_SORDER_CARD_TOP_NUMBER, "" + PreferenceValue.SORDER_CARD_TOP_NUMBER);
        return Integer.parseInt(mode);
    }

    /**
     * Book翻页模式
     */
    public static String getBookSwitchMode(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String mode = preferences.getString(PreferenceKey.PREF_BOOK_SWITCH, "" + PreferenceValue.VALUE_BOOK_SWITCH_LAND);
        return mode;
    }

    /**
     * http服务器站点
     */
    public static String getGdbServerBaseUrl(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String url = preferences.getString(PreferenceKey.PREF_HTTP_SERVER, "");
        return url;
    }

    /**
     * 最大并发下载数量
     */
    public static int getMaxDownloadItem(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String num = preferences.getString(PreferenceKey.PREF_MAX_DOWNLOAD, "" + PreferenceValue.HTTP_MAX_DOWNLOAD);
        return Integer.parseInt(num);
    }

    /**
     * gdb filter model
     */
    public static String getGdbFilterModel(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String json = preferences.getString(PreferenceKey.PREF_GDB_FILTER_MODEL, "");
        return json;
    }

    /**
     * gdb filter model
     */
    public static void saveGdbFilterModel(Context context, String json) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PreferenceKey.PREF_GDB_FILTER_MODEL, json);
        editor.apply();
    }

    /**
     * gdb game scenes
     */
    public static String getGdbGameScenes(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String scene = preferences.getString(PreferenceKey.PREF_GDB_GAME_SCENES, "");
        return scene;
    }

    /**
     * gdb filter model
     */
    public static void saveGdbGameScenes(Context context, String scenes) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PreferenceKey.PREF_GDB_GAME_SCENES, scenes);
        editor.apply();
    }

    /**
     * latest records number
     */
    public static int getGdbLatestRecordsNumber(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String num = preferences.getString(PreferenceKey.PREF_GDB_LATEST_NUM, "" + PreferenceValue.GDB_LATEST_NUM);
        return Integer.parseInt(num);
    }

    /**
     * latest records number
     */
    public static boolean isGdbNoImageMode(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean(PreferenceKey.PREF_GDB_NO_IMAGE, true);
    }

    /**
     * animation random mode
     */
    public static void setGdbRecmmendAnimRandom(Context context, boolean isRandom) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(PreferenceKey.PREF_GDB_REC_ANIM_RANDOM, isRandom);
        editor.commit();
    }

    /**
     * animation random mode
     */
    public static boolean isGdbRecmmendAnimRandom(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean(PreferenceKey.PREF_GDB_REC_ANIM_RANDOM, true);
    }

    /**
     * animation type
     */
    public static void setGdbRecommendAnimType(Context context, int mode) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(PreferenceKey.PREF_GDB_REC_ANIM_TYPE, mode);
        editor.commit();
    }

    /**
     * animation type
     */
    public static int getGdbRecommendAnimType(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        int mode = preferences.getInt(PreferenceKey.PREF_GDB_REC_ANIM_TYPE, 0);
        return mode;
    }

    /**
     * animation time
     */
    public static void setGdbRecommendAnimTime(Context context, int time) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(PreferenceKey.PREF_GDB_REC_ANIM_TIME, time);
        editor.commit();
    }

    /**
     * animation type
     */
    public static int getGdbRecommendAnimTime(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        int mode = preferences.getInt(PreferenceKey.PREF_GDB_REC_ANIM_TIME, 5000);
        return mode;
    }

    /**
     * scene hsv start
     */
    public static void setGdbSceneHsvStart(int time) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(JJApplication.getInstance());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(PreferenceKey.PREF_GDB_SCENE_HSV_START, time);
        editor.commit();
    }

    /**
     * scene hsv start
     */
    public static int getGdbSceneHsvStart() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(JJApplication.getInstance());
        int mode = preferences.getInt(PreferenceKey.PREF_GDB_SCENE_HSV_START, 0);
        return mode;
    }

    /**
     * scene hsv start
     */
    public static void setGdbSceneHsvAngle(int time) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(JJApplication.getInstance());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(PreferenceKey.PREF_GDB_SCENE_HSV_ANGLE, time);
        editor.commit();
    }

    /**
     * scene hsv start
     */
    public static int getGdbSceneHsvAngle() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(JJApplication.getInstance());
        int mode = preferences.getInt(PreferenceKey.PREF_GDB_SCENE_HSV_ANGLE, 360);
        return mode;
    }

    /**
     * random head path in home nav header
     */
    public static void setGdbNavHeadRandom(boolean isRandom) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(JJApplication.getInstance());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(PreferenceKey.PREF_GDB_NAV_HEAD_RANDOM, isRandom);
        editor.commit();
    }

    /**
     * random head path in home nav header
     */
    public static boolean isGdbNavHeadRandom() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(JJApplication.getInstance());
        return preferences.getBoolean(PreferenceKey.PREF_GDB_NAV_HEAD_RANDOM, true);
    }

    /**
     * animation random mode
     */
    public static void setGdbStarListNavAnimRandom(Context context, boolean isRandom) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(PreferenceKey.PREF_GDB_STAR_LIST_NAV_ANIM_RANDOM, isRandom);
        editor.commit();
    }

    /**
     * animation random mode
     */
    public static boolean isGdbStarListNavAnimRandom(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean(PreferenceKey.PREF_GDB_STAR_LIST_NAV_ANIM_RANDOM, true);
    }

    /**
     * animation type
     */
    public static void setGdbStarListNavAnimType(Context context, int mode) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(PreferenceKey.PREF_GDB_STAR_LIST_NAV_ANIM_TYPE, mode);
        editor.commit();
    }

    /**
     * animation type
     */
    public static int getGdbStarListNavAnimType(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        int mode = preferences.getInt(PreferenceKey.PREF_GDB_STAR_LIST_NAV_ANIM_TYPE, 0);
        return mode;
    }

    /**
     * animation time
     */
    public static void setGdbStarListNavAnimTime(Context context, int time) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(PreferenceKey.PREF_GDB_STAR_LIST_NAV_ANIM_TIME, time);
        editor.commit();
    }

    /**
     * animation type
     */
    public static int getGdbStarListNavAnimTime(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        int mode = preferences.getInt(PreferenceKey.PREF_GDB_STAR_LIST_NAV_ANIM_TIME, 5000);
        return mode;
    }

    /**
     * animation random mode
     */
    public static void setGdbRecordNavAnimRandom(Context context, boolean isRandom) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(PreferenceKey.PREF_GDB_RECORD_NAV_ANIM_RANDOM, isRandom);
        editor.commit();
    }

    /**
     * animation random mode
     */
    public static boolean isGdbRecordNavAnimRandom(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean(PreferenceKey.PREF_GDB_RECORD_NAV_ANIM_RANDOM, true);
    }

    /**
     * animation type
     */
    public static void setGdbRecordNavAnimType(Context context, int mode) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(PreferenceKey.PREF_GDB_RECORD_NAV_ANIM_TYPE, mode);
        editor.commit();
    }

    /**
     * animation type
     */
    public static int getGdbRecordNavAnimType(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        int mode = preferences.getInt(PreferenceKey.PREF_GDB_RECORD_NAV_ANIM_TYPE, 0);
        return mode;
    }

    /**
     * animation time
     */
    public static void setGdbRecordNavAnimTime(Context context, int time) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(PreferenceKey.PREF_GDB_RECORD_NAV_ANIM_TIME, time);
        editor.commit();
    }

    /**
     * animation type
     */
    public static int getGdbRecordNavAnimTime(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        int mode = preferences.getInt(PreferenceKey.PREF_GDB_RECORD_NAV_ANIM_TIME, 5000);
        return mode;
    }

    /**
     * animation random mode
     */
    public static void setGdbStarNavAnimRandom(Context context, boolean isRandom) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(PreferenceKey.PREF_GDB_STAR_NAV_ANIM_RANDOM, isRandom);
        editor.commit();
    }

    /**
     * animation random mode
     */
    public static boolean isGdbStarNavAnimRandom(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean(PreferenceKey.PREF_GDB_STAR_NAV_ANIM_RANDOM, true);
    }

    /**
     * animation type
     */
    public static void setGdbStarNavAnimType(Context context, int mode) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(PreferenceKey.PREF_GDB_STAR_NAV_ANIM_TYPE, mode);
        editor.commit();
    }

    /**
     * animation type
     */
    public static int getGdbStarNavAnimType(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        int mode = preferences.getInt(PreferenceKey.PREF_GDB_STAR_NAV_ANIM_TYPE, 0);
        return mode;
    }

    /**
     * animation time
     */
    public static void setGdbStarNavAnimTime(Context context, int time) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(PreferenceKey.PREF_GDB_STAR_NAV_ANIM_TIME, time);
        editor.commit();
    }

    /**
     * animation type
     */
    public static int getGdbStarNavAnimTime(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        int mode = preferences.getInt(PreferenceKey.PREF_GDB_STAR_NAV_ANIM_TIME, 5000);
        return mode;
    }

    /**
     * relate surf to record
     */
    public static void setGdbSurfRelated(boolean isRandom) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(JJApplication.getInstance());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(PreferenceKey.PREF_GDB_SURF_RELATE, isRandom);
        editor.commit();
    }

    /**
     * relate surf to record
     */
    public static boolean isGdbSurfRelated() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(JJApplication.getInstance());
        return preferences.getBoolean(PreferenceKey.PREF_GDB_SURF_RELATE, true);
    }

    /**
     * relate surf to record
     * @param isHorizontal
     */
    public static void setGdbSwipeListOrientation(boolean isHorizontal) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(JJApplication.getInstance());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(PreferenceKey.PREF_GDB_SWIPE_LIST_ORIENTATION, isHorizontal);
        editor.commit();
    }

    /**
     * relate surf to record
     */
    public static boolean isGdbSwipeListHorizontal() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(JJApplication.getInstance());
        return preferences.getBoolean(PreferenceKey.PREF_GDB_SWIPE_LIST_ORIENTATION, false);
    }

}
