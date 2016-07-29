package com.jing.app.jjgallery.viewsystem;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.bean.order.SOrder;
import com.jing.app.jjgallery.config.PreferenceValue;
import com.jing.app.jjgallery.presenter.main.SettingProperties;
import com.jing.app.jjgallery.viewsystem.main.filesystem.FileManagerActivity;
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

/**
 * Created by JingYang on 2016/7/12 0012.
 * Description:
 */
public class ActivityManager {

    private static void applyAnimation(Activity activity) {
        activity.overridePendingTransition(R.anim.activity_left_in, R.anim.activity_right_out);
    }

    public static void startFileManagerActivity(Activity from) {
        from.startActivity(new Intent().setClass(from, FileManagerActivity.class));
        applyAnimation(from);
    }

    public static void startTimeLineActivity(Activity from) {
        from.startActivity(new Intent().setClass(from, TimeLineActivity.class));
        applyAnimation(from);
    }

    public static void startWaterfallActivity(Activity from) {
        from.startActivity(new Intent().setClass(from, HomeWaterfallActivity.class));
        applyAnimation(from);
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

    public static void startSOrderActivity(Activity from) {
        from.startActivity(new Intent().setClass(from, SOrderActivity.class));
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
    public static void startSurfActivity(Activity from, SOrder order) {
        Bundle bundle = new Bundle();
        bundle.putInt("src_mode", UiController.SRC_MODE_ORDER);
        bundle.putInt("orderId", order.getId());
        Intent intent = new Intent().setClass(from, SurfActivity.class);
        intent.putExtras(bundle);
        from.startActivity(intent);
        applyAnimation(from);
    }

    public static void startRandomSurfActivity(Activity from) {
        from.startActivity(new Intent().setClass(from, RandomSurfActivity.class));
        applyAnimation(from);
    }

    public static void startWallActivity(Activity from, String path) {
        Bundle bundle = new Bundle();
        bundle.putInt(WallActivity.MODE_KEY, WallActivity.MODE_FOLDER);
        bundle.putString(WallActivity.MODE_VALUE_KEY, path);
        Intent intent = new Intent().setClass(from, WallActivity.class);
        intent.putExtras(bundle);
        from.startActivity(intent);
        applyAnimation(from);
    }
    public static void startWallActivity(Activity from, SOrder order) {
        Bundle bundle = new Bundle();
        bundle.putInt(WallActivity.MODE_KEY, WallActivity.MODE_ORDER);
        bundle.putInt(WallActivity.MODE_VALUE_KEY, order.getId());
        Intent intent = new Intent().setClass(from, WallActivity.class);
        intent.putExtras(bundle);
        from.startActivity(intent);
        applyAnimation(from);
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

    public static void startBookActivity(Activity from, SOrder order) {
        Bundle bundle = new Bundle();
        bundle.putInt(BookActivity.KEY_TYPE, BookActivity.SORDER);
        bundle.putInt(BookActivity.KEY_ORDER_ID, order.getId());
        Intent intent = new Intent().setClass(from, BookActivity.class);
        intent.putExtras(bundle);
        from.startActivity(intent);
        applyAnimation(from);
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
        if (PreferenceValue.EXPLORE_OPEN_WALL.equals(mode)) {
            startWallActivity(from, order);
        }
        else if (PreferenceValue.EXPLORE_OPEN_BOOK.equals(mode)) {
            startBookActivity(from, order);
        }
        else {
            startSurfActivity(from, order);
        }
    }

    /**
     * SOrderGridPage/SOrderIndexPage/FileManagerIndexPage
     * 其order/folder item会根据setting配置选择不同的直接打开方式
     * @param from
     * @param path
     */
    public static void startExploreActivity(Activity from, String path, String mode) {
        if (PreferenceValue.EXPLORE_OPEN_WALL.equals(mode)) {
            startWallActivity(from, path);
        }
        else if (PreferenceValue.EXPLORE_OPEN_BOOK.equals(mode)) {
            startBookActivity(from, path);
        }
        else {
            startSurfActivity(from, path);
        }
    }
}
