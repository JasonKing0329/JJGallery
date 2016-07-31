package com.jing.app.jjgallery.viewsystem.main.gdb;

import com.king.service.gdb.bean.RecordOneVOne;

import java.util.List;

/**
 * Created by Administrator on 2016/7/31 0031.
 */
public interface IStarView {
    void onRecordsLoaded(List<RecordOneVOne> list);
}
