package com.jing.app.jjgallery.gdb.view.surf;

import com.jing.app.jjgallery.http.bean.response.FolderResponse;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/7/27 11:21
 */
public interface ISurfView {
    void onRequestFail();

    void onFolderReceived(FolderResponse bean);
}
