package com.jing.app.jjgallery.gdb.view.update;

import com.jing.app.jjgallery.bean.http.AppCheckBean;

/**
 * Created by Administrator on 2016/9/6.
 */
public interface IUpdateView {
    void onGdbDatabaseFound(AppCheckBean bean);
    void onGdbDatabaseIsLatest();
    void onServiceDisConnected();
    void onRequestError();
}
