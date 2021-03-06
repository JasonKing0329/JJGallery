package com.jing.app.jjgallery;

import android.app.Activity;
import android.content.Intent;

import com.jing.app.jjgallery.util.CrashHandler;
import com.jing.app.jjgallery.util.CrashService;
import com.jing.app.jjgallery.util.DebugLog;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Created by JingYang on 2016/8/10 0010.
 * Description:
 */
public class JJApplication extends android.app.Application {

    private static List<Activity> activities = new ArrayList<>();

    private static JJApplication instance;

    public static JJApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(getApplicationContext());

        startService(new Intent().setClass(this, CrashService.class));
    }

    public void addActivity(Activity activity) {
        DebugLog.e(activity.getClass().getName());
        activities.add(activity);
    }

    public void removeActivity(Activity activity) {
        String name = activity.getClass().getName();
        DebugLog.e(name);
        for (int i = 0; i < activities.size(); i ++) {
            if (name.equals(activities.get(i).getClass().getName())) {
                activities.remove(i);
                break;
            }
        }
    }

    public static void closeAll() {
        for (int i = 0; i < activities.size(); i ++) {
            activities.get(i).finish();
            DebugLog.e("finish " + activities.get(i).getClass().getName());
        }
    }
}
