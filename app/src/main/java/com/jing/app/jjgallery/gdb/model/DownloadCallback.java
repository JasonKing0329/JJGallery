package com.jing.app.jjgallery.gdb.model;

import com.jing.app.jjgallery.http.bean.data.DownloadItem;

/**
 * Created by Administrator on 2016/9/2.
 */
public interface DownloadCallback {
    void onDownloadFinish(DownloadItem item);
    void onDownloadError(DownloadItem item);
    void onDownloadAllFinish();
}
