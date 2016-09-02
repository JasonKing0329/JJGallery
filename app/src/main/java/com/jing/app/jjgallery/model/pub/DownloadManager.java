package com.jing.app.jjgallery.model.pub;

import android.os.Environment;

import com.jing.app.jjgallery.config.Configuration;
import com.jing.app.jjgallery.service.http.DownloadClient;
import com.jing.app.jjgallery.service.http.progress.ProgressListener;
import com.jing.app.jjgallery.util.DebugLog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import okhttp3.ResponseBody;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/9/2.
 */
public class DownloadManager {

    private class DownloadPack {
        String key;
        String flag;
        ProgressListener progressListener;
        public DownloadPack(String key, String flag, ProgressListener progressListener) {
            this.key = key;
            this.flag = flag;
            this.progressListener = progressListener;
        }
    }

    private final String FILE_EXTRA = ".png";
    private static final int MAX_TASK = 2;
    private Queue<DownloadPack> downloadQueue;
    private List<DownloadPack> executingdList;
    private DownloadCallback mCallback;

    private String savePath;

    public DownloadManager(DownloadCallback callback) {
        mCallback = callback;
        downloadQueue = new LinkedList<>();
        executingdList = new ArrayList<>();
    }

    public void setSavePath(String path) {
        savePath = path;
    }

    public void downloadFile(String key, String flag, ProgressListener progressListener) {
        DebugLog.e(key);
        // 检查正在执行的任务，如果已经在执行则放弃重复执行，没有则新建下载任务
        for (DownloadPack pack:executingdList) {
            if (pack.key.equals(key) && pack.flag.equals(flag)) {
                return;
            }
        }

        // 新建下载任务
        DebugLog.e("new pack:" + key);
        DownloadPack pack = new DownloadPack(key, flag, progressListener);

        // 如果正在执行的任务已经达到MAX_TASK，则进入下载队列进行排队
        if (executingdList.size() >= MAX_TASK) {
            DebugLog.e("进入排队:" + key);
            downloadQueue.offer(pack);
            return;
        }

        // 满足执行条件，开始执行新的下载任务
        startDownloadService(pack);
    }

    private boolean startDownloadService(final DownloadPack pack) {
        if (pack == null) {
            DebugLog.e("没有排队的任务了");
            mCallback.onDownloadAllFinish();
            return false;
        }
        // 任务可执行，加入到正在执行列表中
        DebugLog.e("开始执行任务：" + pack.key);
        executingdList.add(pack);

        // 开始网络请求下载
        new DownloadClient(pack.progressListener).getDownloadService().download(pack.key, pack.flag)
            .subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Subscriber<ResponseBody>() {
                @Override
                public void onCompleted() {
                    DebugLog.e("任务完成：" + pack.key);
                    completeDownload(pack);
                }

                @Override
                public void onError(Throwable e) {
                    DebugLog.e(e.toString());
                    for (StackTraceElement element:e.getStackTrace()) {
                        DebugLog.e(element.toString());
                    }
                    completeDownload(pack);
                    mCallback.onDownloadError(pack.key);
                }

                @Override
                public void onNext(final ResponseBody responseBody) {
                    saveFile(pack.key, responseBody.byteStream());
                    mCallback.onDownloadFinish(pack.key);
                }
            });
        return true;
    }

    private void completeDownload(DownloadPack pack) {
        // 完成后从正在执行列表中删除当前任务
        for (DownloadPack execPack:executingdList) {
            if (pack == execPack) {
                executingdList.remove(execPack);
                break;
            }
        }
        // 从排队队列中选取排在最前面的任务进行执行
        if (startDownloadService(downloadQueue.peek())) {
            downloadQueue.poll();
        }
    }

    /**
     * 保存应用
     *
     * @param input  输入流
     */
    private File saveFile(String filename, InputStream input) {
        if (!filename.endsWith(FILE_EXTRA)) {
            filename = filename + FILE_EXTRA;
        }
        File file = new File(savePath + "/" + filename);
        FileOutputStream fileOutputStream;
        try {
            fileOutputStream = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int ch;
            while ((ch = input.read(buf)) != -1) {
                fileOutputStream.write(buf, 0, ch);
            }
            fileOutputStream.flush();
            fileOutputStream.close();
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

}
