package com.jing.app.jjgallery.presenter.sub;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;

import com.jing.app.jjgallery.BuildConfig;
import com.jing.app.jjgallery.http.bean.response.AppCheckBean;
import com.jing.app.jjgallery.http.bean.response.GdbRespBean;
import com.jing.app.jjgallery.config.Configuration;
import com.jing.app.jjgallery.service.http.Command;
import com.jing.app.jjgallery.service.http.progress.AppHttpClient;
import com.jing.app.jjgallery.viewsystem.sub.update.IUpdateView;

import java.io.File;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/9/6.
 */
public class UpdatePresenter {

    private IUpdateView updateView;

    public UpdatePresenter(IUpdateView view) {
        updateView = view;
    }

    public static String getAppVersionName(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void checkAppUpdate(Context context) {
        final String versionName = getAppVersionName(context);
        AppHttpClient.getInstance().getAppService().isServerOnline()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GdbRespBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        updateView.onServiceDisConnected();
                    }

                    @Override
                    public void onNext(GdbRespBean gdbRespBean) {
                        if (gdbRespBean.isOnline()) {
                            requestCheckAppUpdate(versionName);
                        }
                    }
                });
    }

    private void requestCheckAppUpdate(String versionName) {
        AppHttpClient.getInstance().getAppService().checkAppUpdate(Command.TYPE_APP, versionName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<AppCheckBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        updateView.onRequestError();
                    }

                    @Override
                    public void onNext(AppCheckBean appCheckBean) {
                        if (appCheckBean.isAppUpdate()) {
                            updateView.onAppUpdateFound(appCheckBean);
                        }
                        else {
                            updateView.onAppIsLatest();
                        }
                    }
                });
    }

    /**
     * 安装应用
     */
    public void installApp(Context context, String path) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        //判断是否是AndroidN以及更高的版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".fileProvider", new File(path));
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(new File(path)), "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    public void clearAppFolder() {
        File file = new File(Configuration.APP_DIR_CONF_APP);
        File files[] = file.listFiles();
        for (File f:files) {
            f.delete();
        }
    }
}
