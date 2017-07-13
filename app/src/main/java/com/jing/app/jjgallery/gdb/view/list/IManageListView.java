package com.jing.app.jjgallery.gdb.view.list;

import android.view.MenuItem;

import com.jing.app.jjgallery.bean.http.DownloadItem;

import java.util.List;

/**
 * Created by Administrator on 2016/9/2.
 */
public interface IManageListView {
    void onServerConnected();
    void onServerUnavailable();
    /**
     * 下载文件全部已加密
     */
    void onDownloadItemEncrypted();

    void onCheckPass(boolean hasNew, List<DownloadItem> fileNames);
    void onRequestFail();
    void onMoveImagesSuccess();
    void onMoveImagesFail();
}
