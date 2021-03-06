package com.jing.app.jjgallery.gdb.presenter;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import com.jing.app.jjgallery.http.bean.response.AppCheckBean;
import com.jing.app.jjgallery.http.bean.response.GdbRespBean;
import com.jing.app.jjgallery.config.DBInfor;
import com.jing.app.jjgallery.service.http.Command;
import com.jing.app.jjgallery.service.http.progress.AppHttpClient;
import com.jing.app.jjgallery.gdb.view.update.IUpdateView;
import com.king.service.gdb.GDBProvider;

import java.io.File;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/9/6.
 */
public class GdbUpdatePresenter {

    private IUpdateView updateView;
    private GDBProvider gdbProvider;

    public GdbUpdatePresenter(IUpdateView view) {
        updateView = view;
        gdbProvider = new GDBProvider(DBInfor.GDB_DB_PATH);
    }

    public void checkGdbDatabase() {
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
                            requestGdbDatabase();
                        }
                    }
                });
    }

    public static String getDbVersionName() {
        GDBProvider provider = new GDBProvider(DBInfor.GDB_DB_PATH);
        return getDbVersionName(provider);
    }

    public static String getDbVersionName(GDBProvider provider) {
        String versionName = provider.getVersionName();
        if (versionName == null) {
            versionName = "0";
        }
        return versionName;
    }

    private void requestGdbDatabase() {
        String versionName = getDbVersionName(gdbProvider);
        AppHttpClient.getInstance().getAppService().checkGdbDatabaseUpdate(Command.TYPE_GDB_DATABASE, versionName)
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
                        if (appCheckBean.isGdbDatabaseUpdate()) {
                            updateView.onGdbDatabaseFound(appCheckBean);
                        }
                        else {
                            updateView.onGdbDatabaseIsLatest();
                        }
                    }
                });
    }

    /**
     * 安装应用
     */
    public void installApp(Activity activity, String path) {
        Uri uri = Uri.fromFile(new File(path));
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        activity.startActivity(intent);
    }

}
