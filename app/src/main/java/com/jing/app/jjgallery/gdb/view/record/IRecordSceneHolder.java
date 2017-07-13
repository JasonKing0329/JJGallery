package com.jing.app.jjgallery.gdb.view.record;

import com.jing.app.jjgallery.gdb.presenter.record.RecordListPresenter;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/7/12 16:18
 */
public interface IRecordSceneHolder {
    RecordListPresenter getPresenter();

    void onSelectScene(String scene);
}
