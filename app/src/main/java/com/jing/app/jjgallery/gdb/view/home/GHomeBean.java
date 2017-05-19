package com.jing.app.jjgallery.gdb.view.home;

import com.jing.app.jjgallery.gdb.bean.StarProxy;
import com.king.service.gdb.bean.Record;

import java.util.List;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/5/19 13:37
 */
public class GHomeBean {
    private Record coverRecord;
    private List<StarProxy> starList;
    private List<Record> recordList;

    public Record getCoverRecord() {
        return coverRecord;
    }

    public void setCoverRecord(Record coverRecord) {
        this.coverRecord = coverRecord;
    }

    public List<StarProxy> getStarList() {
        return starList;
    }

    public void setStarList(List<StarProxy> starList) {
        this.starList = starList;
    }

    public List<Record> getRecordList() {
        return recordList;
    }

    public void setRecordList(List<Record> recordList) {
        this.recordList = recordList;
    }
}
