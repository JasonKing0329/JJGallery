package com.jing.app.jjgallery.gdb.view.record;

import com.king.service.gdb.bean.Record;

import java.util.List;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/7/12 11:23
 */
public interface IRecordListView {
    void onLoadRecordList(List<Record> list);
    void onMoreRecordsLoaded(List<Record> list);
}
