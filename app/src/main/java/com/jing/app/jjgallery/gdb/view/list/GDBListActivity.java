package com.jing.app.jjgallery.gdb.view.list;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.bean.http.DownloadItem;
import com.jing.app.jjgallery.gdb.GBaseActivity;
import com.jing.app.jjgallery.gdb.bean.DownloadDialogBean;
import com.jing.app.jjgallery.gdb.presenter.ManageListPresenter;
import com.jing.app.jjgallery.viewsystem.sub.dialog.AlertDialogFragmentV4;
import com.jing.app.jjgallery.viewsystem.publicview.download.v4.DownloadDialogFragmentV4;
import com.jing.app.jjgallery.viewsystem.ProgressProvider;

import java.util.ArrayList;
import java.util.List;

public abstract class GDBListActivity extends GBaseActivity implements IManageListView {

    protected ManageListPresenter presenter;

    private DownloadDialogFragmentV4 downloadDialogFragment;

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
//        if (downloadDialogFragment != null) {
//            downloadDialogFragment.show(getSupportFragmentManager(), "DownloadDialogFragmentV4");
//        }
    }

    @Override
    public void onCheckPass(boolean hasNew, final List<DownloadItem> downloadList) {
        if (hasNew) {
            downloadDialogFragment = new DownloadDialogFragmentV4();
            DownloadDialogBean bean = new DownloadDialogBean();
            List<DownloadItem> repeatList = new ArrayList<>();
            bean.setDownloadList(getListToDownload(downloadList, repeatList));
            bean.setExistedList(repeatList);
            bean.setSavePath(getSavePath());
            bean.setShowPreview(true);
            downloadDialogFragment.setDialogBean(bean);
            downloadDialogFragment.setOnDownloadListener(new DownloadDialogFragmentV4.OnDownloadListener() {
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
            downloadDialogFragment.show(getSupportFragmentManager(), "DownloadDialogFragmentV4");
        } else {
            showToastLong(getString(R.string.gdb_no_new_images), ProgressProvider.TOAST_INFOR);
        }
    }

    protected abstract String getListType();

    protected abstract String getSavePath();

    protected abstract List<DownloadItem> getListToDownload(List<DownloadItem> downloadList, List<DownloadItem> repeatList);

    /**
     * request server move original image files
     *
     * @param downloadList
     */
    private void optionServerAction(final List<DownloadItem> downloadList) {
        AlertDialogFragmentV4 dialog = new AlertDialogFragmentV4();
        dialog.setMessage(getString(R.string.gdb_download_done));
        dialog.setPositiveText(getString(R.string.yes));
        dialog.setPositiveListener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                presenter.requestServeMoveImages(getListType(), downloadList);
            }
        });
        dialog.setNegativeText(getString(R.string.no));
        dialog.show(getSupportFragmentManager(), "AlertDialogFragmentV4");
    }


}
