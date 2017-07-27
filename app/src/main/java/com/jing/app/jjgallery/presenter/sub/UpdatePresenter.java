package com.jing.app.jjgallery.presenter.sub;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;

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
        Uri uri = Uri.fromFile(new File(path));
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
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
