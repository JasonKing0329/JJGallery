package com.jing.app.jjgallery.presenter.sub;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import com.jing.app.jjgallery.bean.http.AppCheckBean;
import com.jing.app.jjgallery.bean.http.GdbRespBean;
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

    public void checkAppUpdate(final String versionName) {
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
    public void installApp(Activity activity, String path) {
        Uri uri = Uri.fromFile(new File(path));
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        activity.startActivity(intent);
    }

}
