package com.jing.app.jjgallery.viewsystem.publicview.download;

import com.jing.app.jjgallery.bean.http.DownloadItem;
import com.jing.app.jjgallery.viewsystem.publicview.DraggableDialogFragment;

import java.util.List;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/7/25 11:50
 */
public interface IDownloadContentHolder extends DraggableDialogFragment.IDialogContentHolder {

    List<DownloadItem> getDownloadList();

    List<DownloadItem> getExistedList();

    String getSavePath();

    void dismissDialog();

    void startDownload();

    void addDownloadItems(List<DownloadItem> checkedItems);
}
