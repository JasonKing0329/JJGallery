package com.jing.app.jjgallery.service.file;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.jing.app.jjgallery.config.ConfManager;

/**
 * Created by JingYang on 2016/7/19 0019.
 * Description:
 */
public class PreferenceService extends Service {

    private static final String TAG = "PreferenceService";

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        new Thread() {
            public void run() {
                while (true) {
                    ConfManager.saveDefaultPref(PreferenceService.this);
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        break;
                    }
                }
            }
        }.start();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();
    }
}
