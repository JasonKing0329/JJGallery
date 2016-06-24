package com.jing.app.jjgallery.presenter.main;

import android.content.Context;

import com.jing.app.jjgallery.model.main.login.ILoginModel;
import com.jing.app.jjgallery.model.main.login.LoginCallback;
import com.jing.app.jjgallery.model.main.login.LoginModel;
import com.jing.app.jjgallery.viewsystem.main.login.ILoginView;

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
}
