package com.jing.app.jjgallery.model.pub;

/**
 * Created by Administrator on 2016/9/2.
 */
public interface DownloadCallback {
    void onDownloadFinish(String key);
    void onDownloadError(String key);
    void onDownloadAllFinish();
}
