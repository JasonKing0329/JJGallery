package com.jing.app.jjgallery.gdb.view.home;

import com.king.service.gdb.bean.Record;

import java.util.List;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/5/19 17:30
 */
public interface IHomeView {
    void onHomeDataLoaded(GHomeBean bean);

    void onMoreRecordsLoaded(List<Record> list);
}
