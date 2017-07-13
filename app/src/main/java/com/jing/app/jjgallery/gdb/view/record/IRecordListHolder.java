package com.jing.app.jjgallery.gdb.view.record;

import com.jing.app.jjgallery.gdb.presenter.record.RecordListPresenter;
import com.jing.app.jjgallery.gdb.view.IFragmentHolder;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/7/12 10:39
 */
public interface IRecordListHolder extends IFragmentHolder {
    RecordListPresenter getPresenter();
}
