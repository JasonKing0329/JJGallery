package com.jing.app.jjgallery.gdb.view.star;

import com.jing.app.jjgallery.bean.http.DownloadItem;
import com.jing.app.jjgallery.gdb.bean.StarProxy;
import com.king.service.gdb.bean.Star;
import com.king.service.gdb.bean.StarCountBean;

import java.util.List;

/**
 * Created by Administrator on 2016/7/30 0030.
 */
public interface IGdbStarListView {
    void onLoadStarList(List<StarProxy> list);
    void onStarCountLoaded(StarCountBean bean);
    void onRequestFail();
    void onCheckPass(boolean hasNew, List<DownloadItem> fileNames);
}
