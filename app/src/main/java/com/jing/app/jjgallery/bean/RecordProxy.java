package com.jing.app.jjgallery.bean;

import com.king.service.gdb.bean.Record;

/**
 * Created by JingYang on 2016/8/5 0005.
 * Description:
 */
public class RecordProxy {

    // for header
    private boolean isHeader;
    private int itemNumber;
    private String headerName;

    // for Record
    private Record record;
    private int positionInHeader;

    public Record getRecord() {
        return record;
    }

    public void setRecord(Record record) {
        this.record = record;
    }

    public boolean isHeader() {
        return isHeader;
    }

    public void setHeader(boolean header) {
        isHeader = header;
    }

    public int getItemNumber() {
        return itemNumber;
    }

    public void setItemNumber(int itemNumber) {
        this.itemNumber = itemNumber;
    }

    public int getPositionInHeader() {
        return positionInHeader;
    }

    public void setPositionInHeader(int positionInHeader) {
        this.positionInHeader = positionInHeader;
    }

    public String getHeaderName() {
        return headerName;
    }

    public void setHeaderName(String headerName) {
        this.headerName = headerName;
    }
}
