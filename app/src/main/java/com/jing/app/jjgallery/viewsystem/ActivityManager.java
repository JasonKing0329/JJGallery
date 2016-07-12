package com.jing.app.jjgallery.viewsystem;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.viewsystem.main.settings.SettingsActivity;
import com.jing.app.jjgallery.viewsystem.sub.surf.SurfActivity;

/**
 * Created by JingYang on 2016/7/12 0012.
 * Description:
 */
public class ActivityManager {
    public static void startSettingActivity(Activity from) {
        from.startActivity(new Intent().setClass(from, SettingsActivity.class));
        applyAnimation(from);
    }

    private static void applyAnimation(Activity activity) {
        activity.overridePendingTransition(R.anim.activity_left_in, R.anim.activity_right_out);
    }

    public static void startSurfActivity(Activity from, int mode, String path) {
        Bundle bundle = new Bundle();
		bundle.putInt("src_mode", mode);
		bundle.putString("path", path);
        Intent intent = new Intent().setClass(from, SurfActivity.class);
        intent.putExtras(bundle);
        from.startActivity(intent);
        applyAnimation(from);
    }
}
