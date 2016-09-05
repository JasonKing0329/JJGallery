package com.jing.app.jjgallery.viewsystem.main.login;

import com.jing.app.jjgallery.bean.http.AppCheckBean;

/**
 * Created by Administrator on 2016/6/23 0023.
 */
public interface ILoginView {
    void onAppUpdateFound(AppCheckBean bean);
    void onSignSuccess();
    void onSignFailed(int type, String msg);
}
