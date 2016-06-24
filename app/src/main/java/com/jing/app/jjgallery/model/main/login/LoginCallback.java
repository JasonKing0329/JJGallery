package com.jing.app.jjgallery.model.main.login;

/**
 * Created by Administrator on 2016/6/23 0023.
 */
public interface LoginCallback {
    public void onLoginSuccess();
    public void onLoginFailed(int type, String msg);
}
