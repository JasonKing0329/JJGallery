package com.jing.app.jjgallery.gdb.view;

import android.view.MenuItem;

/**
 * Created by Administrator on 2016/9/2.
 */
public interface IGdbFragment {
    boolean onMenuItemClick(MenuItem item);
    void onServerConnected();
    void onServerUnavailable();

    /**
     * 下载文件全部已加密
     */
    void onDownloadItemEncrypted();
}
