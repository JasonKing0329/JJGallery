package com.jing.app.jjgallery.viewsystem.sub.update;

import com.jing.app.jjgallery.bean.http.AppCheckBean;

/**
 * Created by Administrator on 2016/9/6.
 */
public interface IUpdateView {
    void onAppUpdateFound(AppCheckBean bean);
    void onAppIsLatest();
    void onServiceDisConnected();
    void onRequestError();
}
