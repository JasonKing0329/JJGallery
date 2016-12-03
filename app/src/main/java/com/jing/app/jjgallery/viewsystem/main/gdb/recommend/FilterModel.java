package com.jing.app.jjgallery.viewsystem.main.gdb.recommend;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2016/12/3 0003.
 */

public class FilterModel {

    @SerializedName("list")
    private List<FilterBean> list;

    public List<FilterBean> getList() {
        return list;
    }

    public void setList(List<FilterBean> list) {
        this.list = list;
    }
}
