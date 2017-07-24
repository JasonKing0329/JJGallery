package com.jing.app.jjgallery.gdb.model;

import com.jing.app.jjgallery.bean.http.DownloadItem;
import com.jing.app.jjgallery.service.encrypt.EncryptUtil;
import com.jing.app.jjgallery.service.http.Command;
import com.jing.app.jjgallery.service.http.DownloadClient;
import com.jing.app.jjgallery.service.http.progress.ProgressListener;
import com.jing.app.jjgallery.util.DebugLog;
import com.jing.app.jjgallery.util.FileUtil;

import android.os.Handler;
import android.os.Message;

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
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/9/2.
 */
public class DownloadManager {

    private final int MSG_ERROR = 0;
    private final int MSG_NEXT = 1;
    private final int MSG_COMPLETE = 2;

    private class DownloadPack {
        DownloadItem item;
        ProgressListener progressListener;
        public DownloadPack(DownloadItem item, ProgressListener progressListener) {
            this.item = item;
            this.progressListener = progressListener;
        }
    }

    private int MAX_TASK;
    private Queue<DownloadPack> downloadQueue;
    private List<DownloadPack> executingdList;
    private DownloadCallback mCallback;

    private String savePath;

    public DownloadManager(DownloadCallback callback, int maxTask) {
        MAX_TASK = maxTask;
        mCallback = callback;
        downloadQueue = new LinkedList<>();
        executingdList = new ArrayList<>();
    }

    public void setSavePath(String path) {
        savePath = path;
    }

    public void downloadFile(DownloadItem item, ProgressListener progressListener) {
        DebugLog.e(item.getName());
        // 检查正在执行的任务，如果已经在执行则放弃重复执行，没有则新建下载任务
        for (DownloadPack pack:executingdList) {
            if (pack.item.getName().equals(item.getName()) && pack.item.getFlag().equals(item.getFlag())) {
                if (pack.item.getKey() != null) {
                    if (pack.item.getKey().equals(item.getKey())) {
                        DebugLog.e("return");
                        return;
                    }
                }
                else {
                    DebugLog.e("return");
                    return;
                }
            }
        }

        // 新建下载任务
        if (item.getKey() == null) {
            DebugLog.e("new pack：" + item.getName());
        }
        else {
            DebugLog.e("new pack：" + item.getKey() + "/" + item.getName());
        }
        DownloadPack pack = new DownloadPack(item, progressListener);

        // 如果正在执行的任务已经达到MAX_TASK，则进入下载队列进行排队
        if (executingdList.size() >= MAX_TASK) {
            if (pack.item.getKey() == null) {
                DebugLog.e("进入排队：" + pack.item.getName());
            }
            else {
                DebugLog.e("进入排队：" + pack.item.getKey() + "/" + pack.item.getName());
            }
            downloadQueue.offer(pack);
            return;
        }

        // 满足执行条件，开始执行新的下载任务
        startDownloadService(pack);
    }

    private boolean startDownloadService(final DownloadPack pack) {
        if (pack == null) {
            DebugLog.e("没有排队的任务了");
            // 这里只是确认没有待排队下载的任务了
            return false;
        }
        // 任务可执行，加入到正在执行列表中
        if (pack.item.getKey() == null) {
            DebugLog.e("开始执行任务：" + pack.item.getName());
        }
        else {
            DebugLog.e("开始执行任务：" + pack.item.getKey() + "/" + pack.item.getName());
        }
        executingdList.add(pack);

        final DownloadHandler handler = new DownloadHandler(pack);

        DebugLog.e("download name：" + pack.item.getName() + ", key:" + pack.item.getKey());
        // 开始网络请求下载
        new DownloadClient(pack.progressListener).getDownloadService().download(pack.item.getFlag(), pack.item.getName(), pack.item.getKey())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Subscriber<ResponseBody>() {
                    @Override
                    public void onCompleted() {
                        if (pack.item.getKey() == null) {
                            DebugLog.e("任务完成：" + pack.item.getName());
                        }
                        else {
                            DebugLog.e("任务完成：" + pack.item.getKey() + "/" + pack.item.getName());
                        }
                        handler.sendEmptyMessage(MSG_COMPLETE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        DebugLog.e(e.toString());
                        for (StackTraceElement element:e.getStackTrace()) {
                            DebugLog.e(element.toString());
                        }
                        handler.sendEmptyMessage(MSG_ERROR);
                    }

                    @Override
                    public void onNext(final ResponseBody responseBody) {
                        DebugLog.e("");
                        saveFile(pack.item, responseBody.byteStream());
                        handler.sendEmptyMessage(MSG_NEXT);
                    }
                });
        return true;
    }

    private class DownloadHandler extends Handler {

        private DownloadPack pack;

        public DownloadHandler(DownloadPack pack) {
            this.pack = pack;
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_ERROR:
                    completeDownload(pack);
                    mCallback.onDownloadError(pack.item);
                    break;
                case MSG_NEXT:
                    mCallback.onDownloadFinish(pack.item);
                    break;
                case MSG_COMPLETE:
                    completeDownload(pack);
                    break;
            }
            super.handleMessage(msg);
        }
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

        // 所有任务执行完了才是真的全部下载完了
        if (executingdList.size() == 0) {
            mCallback.onDownloadAllFinish();
        }
    }

    /**
     * 保存应用
     *
     * @param input  输入流
     */
    private File saveFile(DownloadItem item, InputStream input) {
        File file;
        // star, record支持保存多文件
        if (Command.TYPE_STAR.equals(item.getFlag())
                || Command.TYPE_RECORD.equals(item.getFlag())) {
            file = getStarRecordFile(item);
        }
        else {
            file = new File(savePath + "/" + item.getName());
        }
        DebugLog.e("save file:" + file.getPath());
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

        // 设置实际path，用于后续加密操作
        item.setPath(file.getPath());

        return file;
    }

    private File getStarRecordFile(DownloadItem item) {
        File file;
        String key = item.getKey();
        if (key == null) {
            key = item.getName().substring(0, item.getName().lastIndexOf("."));
        }
        String parent = savePath + "/" + key;
        String out = savePath + "/" + key + EncryptUtil.getFileExtra();
        File outFile = new File(out);
        File dir = new File(parent);
        // key不为null（服务端文件存在于二级目录），本地必须存在相应key目录下
        if (item.getKey() != null) {
            file = saveInFolder(item, dir, outFile);
        }
        else {
            // key为null（服务端文件存在于一级目录），out file与parent都不存在，直接存在根目录中
            if (!outFile.exists() && !dir.exists()) {
                file = new File(savePath + "/" + item.getName());
            }
            else {
                file = saveInFolder(item, dir, outFile);
            }
        }
        return file;
    }

    private File saveInFolder(DownloadItem item, File parent, File outFile) {
        File file;
        // 创建文件夹
        if (!parent.exists()) {
            parent.mkdir();
        }

        // 移动out file
        if (outFile.exists()) {
            FileUtil.moveFile(outFile.getPath(), parent.getPath() + "/" + outFile.getName());
        }

        // 待下载的文件存入到parent目录中
        file = new File(parent.getPath() + "/" + item.getName());
        // 重名文件重命名
        if (new File(parent.getPath() + "/" + item.getName().substring(0, item.getName().lastIndexOf(".")) + EncryptUtil.getFileExtra()).exists()) {
            String newName = parent.getPath() + "/" + System.currentTimeMillis() + "_" + item.getName();
            file = new File(newName);
        }
        return file;
    }

}
