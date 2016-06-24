package com.jing.app.jjgallery.viewsystem.main.login;

/**
 * Created by Administrator on 2016/6/23 0023.
 */
public interface ILoginView {
    public void onSignSuccess();
    public void onSignFailed(int type, String msg);
}
