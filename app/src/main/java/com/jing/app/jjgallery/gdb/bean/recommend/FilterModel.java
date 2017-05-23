package com.jing.app.jjgallery.gdb.bean.recommend;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2016/12/3 0003.
 */

public class FilterModel {

    @SerializedName("list")
    private List<FilterBean> list;

    private boolean isSupportNR;

    public List<FilterBean> getList() {
        return list;
    }

    public void setList(List<FilterBean> list) {
        this.list = list;
    }

    public boolean isSupportNR() {
        return isSupportNR;
    }

    public void setSupportNR(boolean supportNR) {
        isSupportNR = supportNR;
    }
}
