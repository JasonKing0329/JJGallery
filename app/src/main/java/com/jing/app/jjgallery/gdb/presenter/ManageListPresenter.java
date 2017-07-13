package com.jing.app.jjgallery.gdb.presenter;

import android.os.AsyncTask;

import com.jing.app.jjgallery.bean.http.DownloadItem;
import com.jing.app.jjgallery.bean.http.GdbMoveResponse;
import com.jing.app.jjgallery.bean.http.GdbRequestMoveBean;
import com.jing.app.jjgallery.bean.http.GdbRespBean;
import com.jing.app.jjgallery.config.PreferenceValue;
import com.jing.app.jjgallery.gdb.model.RecordComparator;
import com.jing.app.jjgallery.gdb.view.list.IManageListView;
import com.jing.app.jjgallery.service.encrypt.EncryptUtil;
import com.jing.app.jjgallery.service.http.Command;
import com.jing.app.jjgallery.service.http.GdbHttpClient;
import com.jing.app.jjgallery.service.http.progress.AppHttpClient;
import com.king.service.gdb.bean.Record;

import java.io.File;
import java.util.Collections;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 描述: 封装一级star list和record list都有的检测更新的通用操作
 * <p/>作者：景阳
 * <p/>创建时间: 2017/7/12 10:47
 */
public class ManageListPresenter {

    protected IManageListView view;

    public ManageListPresenter(IManageListView view) {
        this.view = view;
    }

    public void checkServerStatus() {
        AppHttpClient.getInstance().getAppService().isServerOnline()
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GdbRespBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.onServerUnavailable();
                    }

                    @Override
                    public void onNext(GdbRespBean gdbRespBean) {
                        if (gdbRespBean.isOnline()) {
                            view.onServerConnected();
                        }
                        else {
                            view.onServerUnavailable();
                        }
                    }
                });
    }

    /**
     * 将下载文件进行全部加密，回调在onDownloadItemEncrypted
     *
     * @param downloadList
     */
    public void finishDownload(List<DownloadItem> downloadList) {

        new FinishDownloadTask().execute(downloadList);
    }

    private class FinishDownloadTask extends AsyncTask<List<DownloadItem>, Void, Void> {
        @Override
        protected void onPostExecute(Void param) {

            view.onDownloadItemEncrypted();
            super.onPostExecute(param);
        }

        @Override
        protected Void doInBackground(List<DownloadItem>... params) {
            for (DownloadItem item : params[0]) {
                File file = new File(item.getPath());
                // 加密文件
                EncryptUtil.encryptFile(file);
            }
            return null;
        }
    }

    /**
     * 通知服务器移动下载源文件
     *
     * @param type
     * @param downloadItems
     */
    public void requestServeMoveImages(final String type, List<DownloadItem> downloadItems) {
        GdbRequestMoveBean bean = new GdbRequestMoveBean();
        bean.setType(type);
        bean.setDownloadList(downloadItems);

        GdbHttpClient.getInstance().getGdbService().requestMoveImages(bean)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GdbMoveResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (type.equals(Command.TYPE_RECORD)) {
                            view.onMoveImagesFail();
                        } else if (type.equals(Command.TYPE_STAR)) {
                            view.onMoveImagesFail();
                        }
                    }

                    @Override
                    public void onNext(GdbMoveResponse bean) {
                        if (bean.isSuccess()) {
                            if (type.equals(Command.TYPE_RECORD)) {
                                view.onMoveImagesSuccess();
                            } else if (type.equals(Command.TYPE_STAR)) {
                                view.onMoveImagesSuccess();
                            }
                        } else {
                            if (type.equals(Command.TYPE_RECORD)) {
                                view.onMoveImagesFail();
                            } else if (type.equals(Command.TYPE_STAR)) {
                                view.onMoveImagesFail();
                            }
                        }
                    }
                });
    }

}
