package com.jing.app.jjgallery.gdb.view.surf;

import com.king.service.gdb.bean.Record;

import java.util.List;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/7/27 11:21
 */
public interface ISurfView {
    void onRequestFail();

    void onFolderReceived(List<SurfFileBean> list);

    void onRecordRelated(int index);

    void onSortFinished();
}
