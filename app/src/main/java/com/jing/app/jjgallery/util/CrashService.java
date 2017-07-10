package com.jing.app.jjgallery.util;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.jing.app.jjgallery.viewsystem.publicview.CrashDialogActivity;

/**
 * 描述: 程序崩溃后通过service启动activity的方式来自定义提示对话框
 * <p/>作者：景阳
 * <p/>创建时间: 2017/6/28 17:05
 */
public class CrashService extends Service {
    private static CrashService mInstance = null;

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static CrashService getInstance() {
        return mInstance;
    }

    public void sendError(String message) {
        Intent intent = new Intent(this, CrashDialogActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(CrashDialogActivity.KEY_MSG, message);
        startActivity(intent);
        stopSelf();
    }
}
