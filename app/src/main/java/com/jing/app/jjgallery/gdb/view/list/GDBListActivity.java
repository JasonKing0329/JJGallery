package com.jing.app.jjgallery.gdb.view.list;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.bean.http.DownloadItem;
import com.jing.app.jjgallery.gdb.GBaseActivity;
import com.jing.app.jjgallery.gdb.presenter.ManageListPresenter;
import com.jing.app.jjgallery.gdb.view.pub.DownloadDialog;
import com.jing.app.jjgallery.viewsystem.ProgressProvider;
import com.jing.app.jjgallery.viewsystem.publicview.CustomDialog;
import com.jing.app.jjgallery.viewsystem.publicview.DefaultDialogManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class GDBListActivity extends GBaseActivity implements IManageListView {

    private DownloadDialog downloadDialog;

    protected ManageListPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
    }

    @Override
    public void onServerConnected() {
        onServerConnectSuccess();
    }

    protected abstract void onServerConnectSuccess();

    @Override
    public void onServerUnavailable() {
        showToastLong(getString(R.string.gdb_server_offline), ProgressProvider.TOAST_ERROR);
    }

    @Override
    public void onDownloadItemEncrypted() {
        onDownloadFinished();
    }

    protected abstract void onDownloadFinished();

    @Override
    public void onRequestFail() {
        showToastLong(getString(R.string.gdb_request_fail), ProgressProvider.TOAST_ERROR);
    }

    @Override
    public void onMoveImagesSuccess() {
        showToastLong(getString(R.string.success), ProgressProvider.TOAST_INFOR);
    }

    @Override
    public void onMoveImagesFail() {
        showToastLong(getString(R.string.failed), ProgressProvider.TOAST_INFOR);
    }

    protected void showDownloadDialog() {
        if (downloadDialog != null) {
            downloadDialog.show();
        }
    }

    @Override
    public void onCheckPass(boolean hasNew, final List<DownloadItem> downloadList) {
        if (hasNew) {
            if (downloadDialog == null) {
                downloadDialog = new DownloadDialog(this, new CustomDialog.OnCustomDialogActionListener() {
                    @Override
                    public boolean onSave(Object object) {
                        return false;
                    }

                    @Override
                    public boolean onCancel() {
                        return false;
                    }

                    @Override
                    public void onLoadData(HashMap<String, Object> data) {
                        List<DownloadItem> repeatList = new ArrayList<>();
                        data.put("items", getListToDownload(downloadList, repeatList));
                        data.put("existedItems", repeatList);
                        data.put("savePath", getSavePath());
                        data.put("optionMsg", String.format(getString(R.string.gdb_option_download), downloadList.size()));
                    }
                });
                downloadDialog.setOnDownloadListener(new DownloadDialog.OnDownloadListener() {
                    @Override
                    public void onDownloadFinish(DownloadItem item) {

                    }

                    @Override
                    public void onDownloadFinish(List<DownloadItem> downloadList) {
                        // 所有内容下载完成后，统一进行异步encypt，然后更新starImageMap和recordImageMap，完成后通知adapter更新
                        presenter.finishDownload(downloadList);
                        optionServerAction(downloadList);
                    }
                });
            }
            else {
                List<DownloadItem> repeatList = new ArrayList<>();
                List<DownloadItem> newList = getListToDownload(downloadList, repeatList);
                downloadDialog.newUpdate(newList, repeatList);
            }
            downloadDialog.show();
        }
        else {
            showToastLong(getString(R.string.gdb_no_new_images), ProgressProvider.TOAST_INFOR);
        }
    }

    protected abstract String getListType();

    protected abstract String getSavePath();

    protected abstract List<DownloadItem> getListToDownload(List<DownloadItem> downloadList, List<DownloadItem> repeatList);

    /**
     * request server move original image files
     * @param downloadList
     */
    private void optionServerAction(final List<DownloadItem> downloadList) {
        new DefaultDialogManager().showOptionDialog(this, null, getString(R.string.gdb_download_done)
                , getResources().getString(R.string.yes), null, getResources().getString(R.string.no)
                , new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == DialogInterface.BUTTON_POSITIVE) {
                            presenter.requestServeMoveImages(getListType(), downloadList);
                        }
                    }
                }, null);
    }


}
