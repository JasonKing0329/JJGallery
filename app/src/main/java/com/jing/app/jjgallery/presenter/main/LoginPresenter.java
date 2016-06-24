package com.jing.app.jjgallery.presenter.main;

import com.jing.app.jjgallery.model.main.login.ILoginModel;
import com.jing.app.jjgallery.viewsystem.main.login.ILoginView;

/**
 * Created by Administrator on 2016/6/23 0023.
 */
public class LoginPresenter {
    private ILoginModel loginModel;
    private ILoginView loginView;

    public LoginPresenter(ILoginView iLoginView) {
        loginView = iLoginView;
    }

    public void onSign() {

    }
}
