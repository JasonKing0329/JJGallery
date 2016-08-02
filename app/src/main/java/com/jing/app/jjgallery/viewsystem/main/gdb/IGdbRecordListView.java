package com.jing.app.jjgallery.viewsystem.main.gdb;

import com.king.service.gdb.bean.Record;

import java.util.List;

/**
 * Created by Administrator on 2016/7/30 0030.
 */
public interface IGdbRecordListView {
    void onLoadRecordList(List<Record> list);
}
