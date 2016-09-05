package com.jing.app.jjgallery.presenter.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.jing.app.jjgallery.bean.http.AppCheckBean;
import com.jing.app.jjgallery.bean.http.GdbRespBean;
import com.jing.app.jjgallery.model.main.login.ILoginModel;
import com.jing.app.jjgallery.model.main.login.LoginCallback;
import com.jing.app.jjgallery.model.main.login.LoginModel;
import com.jing.app.jjgallery.service.http.progress.AppHttpClient;
import com.jing.app.jjgallery.util.DebugLog;
import com.jing.app.jjgallery.viewsystem.main.login.ILoginView;

import java.io.File;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by JingYang on 2016/6/23 0023.
 */
public class LoginPresenter implements LoginCallback {
    private ILoginModel loginModel;
    private ILoginView loginView;

    public LoginPresenter(Context context, ILoginView iLoginView) {
        loginView = iLoginView;
        loginModel = new LoginModel(context, this);
    }

    public void sign(String user, String pwd) {
        loginModel.signBasic(user, pwd);
    }

    public boolean isFingerPrintEnabled() {
        return loginModel.isFingerPrintEnable();
    }

    public void signFingerPrint() {
        loginModel.signWithFingerPrint();
    }

    @Override
    public void onLoginSuccess() {
        loginView.onSignSuccess();
    }

    @Override
    public void onLoginFailed(int type, String msg) {
        loginView.onSignFailed(type, msg);
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
                        DebugLog.e("服务器连接失败");
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
        AppHttpClient.getInstance().getAppService().checkAppUpdate("app", versionName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<AppCheckBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        DebugLog.e("更新app失败");
                    }

                    @Override
                    public void onNext(AppCheckBean appCheckBean) {
                        if (appCheckBean.isAppUpdate()) {
                            loginView.onAppUpdateFound(appCheckBean);
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
