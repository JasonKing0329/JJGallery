package com.jing.app.jjgallery;

import com.jing.app.jjgallery.util.CrashHandler;

/**
 * Created by JingYang on 2016/8/10 0010.
 * Description:
 */
public class JJApplication extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(getApplicationContext());
    }
}
