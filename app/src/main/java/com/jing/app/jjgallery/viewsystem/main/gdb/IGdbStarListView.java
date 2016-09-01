package com.jing.app.jjgallery.viewsystem.main.gdb;

import com.king.service.gdb.bean.Star;

import java.util.List;

/**
 * Created by Administrator on 2016/7/30 0030.
 */
public interface IGdbStarListView {
    void onLoadStarList(List<Star> list);
    void onServerConnected();
    void onServerUnavailable();
    void onRequestFail();
    void onCheckPass(boolean hasNew, List<String> fileNames);
}
