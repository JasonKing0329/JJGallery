package com.jing.app.jjgallery;

import android.app.Activity;

import com.jing.app.jjgallery.util.CrashHandler;

import java.util.Stack;

/**
 * Created by JingYang on 2016/8/10 0010.
 * Description:
 */
public class JJApplication extends android.app.Application {

    private static Stack<Activity> activities = new Stack<>();
    @Override
    public void onCreate() {
        super.onCreate();
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(getApplicationContext());
    }

    public void addActivity(Activity activity) {
        activities.push(activity);
    }

    public void removeActivity() {
        activities.pop();
    }

    public static void closeAll() {
        while (activities.size() > 0) {
            Activity activity = activities.pop();
            if (activity != null) {
                activity.finish();
            }
        }
    }
}
