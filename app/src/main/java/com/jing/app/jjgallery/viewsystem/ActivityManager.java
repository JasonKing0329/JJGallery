package com.jing.app.jjgallery.viewsystem;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.view.View;

import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.bean.order.SOrder;
import com.jing.app.jjgallery.config.ConfManager;
import com.jing.app.jjgallery.config.PreferenceValue;
import com.jing.app.jjgallery.gdb.view.home.GdbGuideActivity;
import com.jing.app.jjgallery.gdb.view.game.battlecross.BattleActivity;
import com.jing.app.jjgallery.gdb.view.game.GroupActivity;
import com.jing.app.jjgallery.gdb.view.game.SeasonActivity;
import com.jing.app.jjgallery.gdb.view.game.battlecross.CrossActivity;
import com.jing.app.jjgallery.model.pub.ObjectCache;
import com.jing.app.jjgallery.presenter.main.SettingProperties;
import com.jing.app.jjgallery.service.image.SImageConstants;
import com.jing.app.jjgallery.viewsystem.main.filesystem.FileManagerActivity;
import com.jing.app.jjgallery.gdb.view.list.GDBListActivity;
import com.jing.app.jjgallery.gdb.view.record.RecordActivity;
import com.jing.app.jjgallery.gdb.view.star.StarActivity;
import com.jing.app.jjgallery.viewsystem.main.order.SOrderActivity;
import com.jing.app.jjgallery.viewsystem.main.settings.SettingsActivity;
import com.jing.app.jjgallery.viewsystem.main.timeline.HomeWaterfallActivity;
import com.jing.app.jjgallery.viewsystem.main.timeline.TimeLineActivity;
import com.jing.app.jjgallery.viewsystem.sub.book.BookActivity;
import com.jing.app.jjgallery.viewsystem.sub.surf.RandomSurfActivity;
import com.jing.app.jjgallery.viewsystem.sub.surf.SurfActivity;
import com.jing.app.jjgallery.viewsystem.sub.surf.UiController;
import com.jing.app.jjgallery.viewsystem.sub.wall.WallActivity;
import com.jing.app.jjgallery.viewsystem.sub.waterfall.WaterfallActivity;
import com.king.service.gdb.bean.Record;
import com.king.service.gdb.bean.Star;

import java.io.File;

/**
 * Created by JingYang on 2016/7/12 0012.
 * Description:
 */
public class ActivityManager {

    private static void applyAnimation(Activity activity) {
        activity.overridePendingTransition(R.anim.activity_left_in, R.anim.activity_right_out);
    }

    public static void startFileManagerActivity(Activity from, Bundle bundle) {
        Intent intent = new Intent().setClass(from, FileManagerActivity.class);
        if (bundle == null) {
            from.startActivity(intent);
            applyAnimation(from);
        }
        else {
            ActivityCompat.startActivity(from, intent, bundle);
        }
    }

    public static void startTimeLineActivity(Activity from, Bundle bundle) {
        Intent intent = new Intent().setClass(from, TimeLineActivity.class);
        if (bundle == null) {
            from.startActivity(intent);
            applyAnimation(from);
        }
        else {
            ActivityCompat.startActivity(from, intent, bundle);
        }
    }

    public static boolean startGDBHomeActivity(Activity from, Bundle bundle) {
        if (!new File(ConfManager.GDB_DB_PATH).exists()) {
            ((ProgressProvider) from).showToastLong(from.getString(R.string.gdb_no_conf), ProgressProvider.TOAST_WARNING);
            return false;
        }

        SImageConstants.setHideImageMode(SettingProperties.isGdbNoImageMode(from));
        Intent intent = new Intent().setClass(from, GdbGuideActivity.class);
        if (bundle == null) {
            from.startActivity(intent);
            applyAnimation(from);
        }
        else {
            ActivityCompat.startActivity(from, intent, bundle);
        }
        return true;
    }

    public static boolean startGDBStarListActivity(Activity from, Bundle bundle) {
        if (!new File(ConfManager.GDB_DB_PATH).exists()) {
            ((ProgressProvider) from).showToastLong(from.getString(R.string.gdb_no_conf), ProgressProvider.TOAST_WARNING);
            return false;
        }
        Intent intent = new Intent().setClass(from, GDBListActivity.class);
        intent.putExtra(GDBListActivity.START_MODE, GDBListActivity.STAR);
        if (bundle == null) {
            from.startActivity(intent);
            applyAnimation(from);
        }
        else {
            ActivityCompat.startActivity(from, intent, bundle);
        }
        return true;
    }

    public static boolean startGDBRecordListActivity(Activity from, Bundle bundle) {
        if (!new File(ConfManager.GDB_DB_PATH).exists()) {
            ((ProgressProvider) from).showToastLong(from.getString(R.string.gdb_no_conf), ProgressProvider.TOAST_WARNING);
            return false;
        }
        Intent intent = new Intent().setClass(from, GDBListActivity.class);
        intent.putExtra(GDBListActivity.START_MODE, GDBListActivity.RECORD);
        if (bundle == null) {
            from.startActivity(intent);
            applyAnimation(from);
        }
        else {
            ActivityCompat.startActivity(from, intent, bundle);
        }
        return true;
    }

    public static void startWaterfallActivity(Activity from, Bundle bundle) {
        Intent intent = new Intent().setClass(from, HomeWaterfallActivity.class);
        if (bundle == null) {
            from.startActivity(intent);
            applyAnimation(from);
        }
        else {
            ActivityCompat.startActivity(from, intent, bundle);
        }
    }

    public static void startWaterfallActivity(Activity from, String filePath) {
        Intent intent = new Intent();
        intent.setClass(from, WaterfallActivity.class);
        intent.putExtra(WaterfallActivity.KEY_TYPE, WaterfallActivity.FOLDER);
        intent.putExtra(WaterfallActivity.KEY_FOLDER_PATH, filePath);
        from.startActivity(intent);
        applyAnimation(from);
    }

    public static void startWaterfallActivity(Activity from, SOrder order) {
        Intent intent = new Intent();
        intent.setClass(from, WaterfallActivity.class);
        intent.putExtra(WaterfallActivity.KEY_TYPE, WaterfallActivity.SORDER);
        intent.putExtra(WaterfallActivity.KEY_ORDER_ID, order.getId());
        from.startActivity(intent);
        applyAnimation(from);
    }
    public static void startWaterfallActivity(Activity from, SOrder order, View view) {
        if (view == null) {
            startWaterfallActivity(from, order);
            return;
        }
        Intent intent = new Intent().setClass(from, WaterfallActivity.class);
        // transition animation
        Pair participants = new Pair<>(view, from.getString(R.string.trans_waterfall));
        ActivityOptionsCompat transitionActivityOptions =
                ActivityOptionsCompat.makeSceneTransitionAnimation(
                        from, participants);
        Bundle bundle = transitionActivityOptions.toBundle();
        intent.putExtra(WaterfallActivity.KEY_TYPE, WaterfallActivity.SORDER);
        intent.putExtra(WaterfallActivity.KEY_ORDER_ID, order.getId());
        intent.putExtras(bundle);
        ActivityCompat.startActivity(from, intent, bundle);
    }

    public static void startSOrderActivity(Activity from, Bundle bundle) {
        Intent intent = new Intent().setClass(from, SOrderActivity.class);
        if (bundle == null) {
            from.startActivity(intent);
            applyAnimation(from);
        }
        else {
            ActivityCompat.startActivity(from, intent, bundle);
        }
    }

    public static void startSettingActivity(Activity from) {
        from.startActivity(new Intent().setClass(from, SettingsActivity.class));
        applyAnimation(from);
    }

    public static void startSettingActivity(Activity from, int requestCode) {
        from.startActivityForResult(new Intent().setClass(from, SettingsActivity.class), requestCode);
        applyAnimation(from);
    }

    public static void startSurfActivity(Activity from, String path) {
        Bundle bundle = new Bundle();
		bundle.putInt("src_mode", UiController.SRC_MODE_FOLDER);
		bundle.putString("path", path);
        Intent intent = new Intent().setClass(from, SurfActivity.class);
        intent.putExtras(bundle);
        from.startActivity(intent);
        applyAnimation(from);
    }
    public static void startSurfActivity(Activity from, String path, View animView) {
        if (animView == null) {
            startSurfActivity(from, path);
            return;
        }
        Intent intent = new Intent().setClass(from, SurfActivity.class);
        // transition animation
        Pair participants = new Pair<>(animView, from.getString(R.string.trans_surf_center));
        ActivityOptionsCompat transitionActivityOptions =
                ActivityOptionsCompat.makeSceneTransitionAnimation(
                        from, participants);
        Bundle bundle = transitionActivityOptions.toBundle();
        bundle.putInt("src_mode", UiController.SRC_MODE_FOLDER);
        bundle.putString("path", path);
        intent.putExtras(bundle);
        ActivityCompat.startActivity(from, intent, bundle);
    }

    public static void startSurfActivity(Activity from, SOrder order) {
        Bundle bundle = new Bundle();
        bundle.putInt("src_mode", UiController.SRC_MODE_ORDER);
        bundle.putInt("orderId", order.getId());
        Intent intent = new Intent().setClass(from, SurfActivity.class);
        intent.putExtras(bundle);
        from.startActivity(intent);
        applyAnimation(from);
    }
    public static void startSurfActivity(Activity from, SOrder order, View view) {
        if (view == null) {
            startSurfActivity(from, order);
            return;
        }
        Intent intent = new Intent().setClass(from, SurfActivity.class);
        // transition animation
        Pair participants = new Pair<>(view, from.getString(R.string.trans_surf_center));
        ActivityOptionsCompat transitionActivityOptions =
                ActivityOptionsCompat.makeSceneTransitionAnimation(
                        from, participants);
        Bundle bundle = transitionActivityOptions.toBundle();
        bundle.putInt("src_mode", UiController.SRC_MODE_ORDER);
        bundle.putInt("orderId", order.getId());
        intent.putExtras(bundle);
        ActivityCompat.startActivity(from, intent, bundle);
    }

    public static void startRandomSurfActivity(Activity from) {
        from.startActivity(new Intent().setClass(from, RandomSurfActivity.class));
        applyAnimation(from);
    }

    public static void startWallActivity(Activity from, String path) {
        Intent intent = new Intent().setClass(from, WallActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(WallActivity.MODE_KEY, WallActivity.MODE_FOLDER);
        bundle.putString(WallActivity.MODE_VALUE_KEY, path);
        intent.putExtras(bundle);
        from.startActivity(intent);
        applyAnimation(from);
    }
    public static void startWallActivity(Activity from, String path, View animView) {
        if (animView == null) {
            startWallActivity(from, path);
            return;
        }
        Intent intent = new Intent().setClass(from, WallActivity.class);
        // transition animation
        Pair participants = new Pair<>(animView, from.getString(R.string.trans_wall));
        ActivityOptionsCompat transitionActivityOptions =
                ActivityOptionsCompat.makeSceneTransitionAnimation(
                        from, participants);
        Bundle bundle = transitionActivityOptions.toBundle();
        bundle.putInt(WallActivity.MODE_KEY, WallActivity.MODE_FOLDER);
        bundle.putString(WallActivity.MODE_VALUE_KEY, path);
        intent.putExtras(bundle);
        ActivityCompat.startActivity(from, intent, bundle);
    }

    public static void startWallActivity(Activity from, SOrder order) {
        Intent intent = new Intent().setClass(from, WallActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(WallActivity.MODE_KEY, WallActivity.MODE_ORDER);
        bundle.putInt(WallActivity.MODE_VALUE_KEY, order.getId());
        intent.putExtras(bundle);
        from.startActivity(intent);
        applyAnimation(from);
    }
    public static void startWallActivity(Activity from, SOrder order, View animView) {
        if (animView == null) {
            startWallActivity(from, order);
            return;
        }
        Intent intent = new Intent().setClass(from, WallActivity.class);
        // transition animation
        Pair participants = new Pair<>(animView, from.getString(R.string.trans_wall));
        ActivityOptionsCompat transitionActivityOptions =
                ActivityOptionsCompat.makeSceneTransitionAnimation(
                        from, participants);
        Bundle bundle = transitionActivityOptions.toBundle();
        bundle.putInt(WallActivity.MODE_KEY, WallActivity.MODE_ORDER);
        bundle.putInt(WallActivity.MODE_VALUE_KEY, order.getId());
        intent.putExtras(bundle);
        from.startActivity(intent, bundle);
    }

    public static void startBookActivity(Activity from, String path) {
        Bundle bundle = new Bundle();
        bundle.putInt(BookActivity.KEY_TYPE, BookActivity.FOLDER);
        bundle.putString(BookActivity.KEY_FOLDER_PATH, path);
        Intent intent = new Intent().setClass(from, BookActivity.class);
        intent.putExtras(bundle);
        from.startActivity(intent);
        applyAnimation(from);
    }
    public static void startBookActivity(Activity from, String path, View animView) {
        if (animView == null) {
            startBookActivity(from, path);
            return;
        }
        Intent intent = new Intent().setClass(from, BookActivity.class);
        // transition animation
        Pair participants = new Pair<>(animView, from.getString(R.string.trans_book));
        ActivityOptionsCompat transitionActivityOptions =
                ActivityOptionsCompat.makeSceneTransitionAnimation(
                        from, participants);
        Bundle bundle = transitionActivityOptions.toBundle();
        bundle.putInt(BookActivity.KEY_TYPE, BookActivity.FOLDER);
        bundle.putString(BookActivity.KEY_FOLDER_PATH, path);
        intent.putExtras(bundle);
        ActivityCompat.startActivity(from, intent, bundle);
    }

    public static void startBookActivity(Activity from, SOrder order) {
        Bundle bundle = new Bundle();
        bundle.putInt(BookActivity.KEY_TYPE, BookActivity.SORDER);
        bundle.putInt(BookActivity.KEY_ORDER_ID, order.getId());
        Intent intent = new Intent().setClass(from, BookActivity.class);
        intent.putExtras(bundle);
        from.startActivity(intent);
        applyAnimation(from);
    }
    public static void startBookActivity(Activity from, SOrder order, View animView) {
        if (animView == null) {
            startBookActivity(from, order);
            return;
        }
        Intent intent = new Intent().setClass(from, BookActivity.class);
        // transition animation
        Pair participants = new Pair<>(animView, from.getString(R.string.trans_book));
        ActivityOptionsCompat transitionActivityOptions =
                ActivityOptionsCompat.makeSceneTransitionAnimation(
                        from, participants);
        Bundle bundle = transitionActivityOptions.toBundle();
        bundle.putInt(BookActivity.KEY_TYPE, BookActivity.SORDER);
        bundle.putInt(BookActivity.KEY_ORDER_ID, order.getId());
        intent.putExtras(bundle);
        ActivityCompat.startActivity(from, intent, bundle);
    }

    public static void onSettingResult(Context context) {
        SettingProperties.saveAsDefaultPreference(context);
    }

    public static void reload(Activity from) {
        Intent intent = from.getIntent();
        from.overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

        from.finish();
        from.overridePendingTransition(0, 0);
        from.startActivity(intent);
    }

    /**
     * SOrderGridPage/SOrderIndexPage/FileManagerIndexPage
     * 其order/folder item会根据setting配置选择不同的直接打开方式
     * @param from
     * @param order
     */
    public static void startExploreActivity(Activity from, SOrder order, String mode) {
        startExploreActivity(from, order, mode, null);
    }
    public static void startExploreActivity(Activity from, SOrder order, String mode, View view) {
        if (PreferenceValue.EXPLORE_OPEN_WALL.equals(mode)) {
            startWallActivity(from, order, view);
        }
        else if (PreferenceValue.EXPLORE_OPEN_BOOK.equals(mode)) {
            startBookActivity(from, order, view);
        }
        else {
            startSurfActivity(from, order, view);
        }
    }

    /**
     * SOrderGridPage/SOrderIndexPage/FileManagerIndexPage
     * 其order/folder item会根据setting配置选择不同的直接打开方式
     * @param from
     * @param path
     */
    public static void startExploreActivity(Activity from, String path, String mode) {
        startExploreActivity(from, path, mode, null);
    }
    public static void startExploreActivity(Activity from, String path, String mode, View view) {
        if (PreferenceValue.EXPLORE_OPEN_WALL.equals(mode)) {
            startWallActivity(from, path, view);
        }
        else if (PreferenceValue.EXPLORE_OPEN_BOOK.equals(mode)) {
            startBookActivity(from, path, view);
        }
        else {
            startSurfActivity(from, path, view);
        }
    }

    public static void startStarActivity(Activity from, Star star) {
        Bundle bundle = new Bundle();
        bundle.putInt(StarActivity.KEY_STAR_ID, star.getId());
        Intent intent = new Intent().setClass(from, StarActivity.class);
        intent.putExtras(bundle);
        from.startActivity(intent);
        applyAnimation(from);
    }

    public static void startGdbRecordActivity(Context from, Record record) {
        ObjectCache.putData(record);
        from.startActivity(new Intent().setClass(from, RecordActivity.class));
        if (from instanceof Activity) {
            applyAnimation((Activity) from);
        }
    }

    public static boolean startGDBGameActivity(Activity from, Bundle bundle) {
        if (!new File(ConfManager.GDB_GAME_DB_PATH).exists()) {
            ((ProgressProvider) from).showToastLong(from.getString(R.string.gdb_no_conf), ProgressProvider.TOAST_WARNING);
            return false;
        }
        Intent intent = new Intent().setClass(from, SeasonActivity.class);
        if (bundle == null) {
            from.startActivity(intent);
            applyAnimation(from);
        }
        else {
            ActivityCompat.startActivity(from, intent, bundle);
        }
        return true;
    }

    public static void startGdbGameGroupActivity(Activity from, int seasonId) {
        Intent intent = new Intent().setClass(from, GroupActivity.class);
        intent.putExtra(GroupActivity.KEY_SEASON_ID, seasonId);
        from.startActivity(intent);
        applyAnimation(from);
    }

    public static void startGdbGameBattleActivity(Activity from, int seasonId) {
        Intent intent = new Intent().setClass(from, BattleActivity.class);
        intent.putExtra(BattleActivity.KEY_SEASON_ID, seasonId);
        from.startActivity(intent);
        applyAnimation(from);
    }

    public static void startGdbGameCrossActivity(Activity from, int seasonId) {
        Intent intent = new Intent().setClass(from, CrossActivity.class);
        intent.putExtra(CrossActivity.KEY_SEASON_ID, seasonId);
        from.startActivity(intent);
        applyAnimation(from);
    }

}
