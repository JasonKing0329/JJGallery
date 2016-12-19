package com.jing.app.jjgallery.gdb.view.recommend;

import com.king.service.gdb.bean.Record;

import java.util.List;

/**
 * Created by Administrator on 2016/11/27 0027.
 */

public interface IRecommend {
    void onRecordsLoaded(List<Record> list);

    void onRecordRecommand(Record record);
}
