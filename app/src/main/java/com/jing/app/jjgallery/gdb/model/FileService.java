package com.jing.app.jjgallery.gdb.model;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.jing.app.jjgallery.config.DBInfor;
import com.jing.app.jjgallery.service.encrypt.EncrypterFactory;
import com.jing.app.jjgallery.service.encrypt.action.Encrypter;
import com.jing.app.jjgallery.util.DebugLog;
import com.king.service.gdb.GDBProvider;
import com.king.service.gdb.bean.Record;
import com.king.service.gdb.bean.Star;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 根据gdb数据库内容
 * 1. 调整本地数据库相应内容
 * 2. 删除无用文件
 */
public class FileService extends Service {

    private FileModel fileModel;
    private GDBProvider gdbProvider;
    private Encrypter encrypter;

    public FileService() {
        fileModel = new FileModel();
        gdbProvider = new GDBProvider(DBInfor.GDB_DB_PATH);
        encrypter = EncrypterFactory.create();
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        updateFileSystem();
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 更新文件系统
     */
    private void updateFileSystem() {
        Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(Subscriber<? super Object> subscriber) {
                DebugLog.e("updateFileSystem start");

                updateStars();
                updateRecords();
                subscriber.onCompleted();
            }
        })
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Observer<Object>() {
            @Override
            public void onCompleted() {
                DebugLog.e("updateFileSystem");
            }

            @Override
            public void onError(Throwable e) {
                DebugLog.e("updateFileSystem");
            }

            @Override
            public void onNext(Object o) {
                DebugLog.e("updateFileSystem");
            }
        });
    }

    private synchronized void updateStars() {
        List<String> pathList = fileModel.getStarFileList();
        List<Star> starList = gdbProvider.getStars(null);

        // 解析原文件名，创建映射集合
        Map<String, String> starImageMap = new HashMap<>();
        for (String path:pathList) {
            String name = encrypter.decipherOriginName(new File(path));
            String preName = name.substring(0, name.lastIndexOf("."));
            starImageMap.put(preName, path);
        }

        // 检测有效文件，有效则从map里删除，剩下的全部就为无引用文件
        for (Star star:starList) {
            if (starImageMap.get(star.getName()) != null) {
                starImageMap.remove(star.getName());
            }
        }

        // 删除无效文件
        Iterator<String> it = starImageMap.values().iterator();
        while (it.hasNext()) {
            String path = it.next();
            deleteUnavailable(new File(path));
        }
    }

    private synchronized void updateRecords() {
        List<String> pathList = fileModel.getRecordFileList();
        List<Record> recordList = gdbProvider.getAllRecords();

        // 解析原文件名，创建映射集合
        Map<String, String> starImageMap = new HashMap<>();
        for (String path:pathList) {
            String name = encrypter.decipherOriginName(new File(path));
            String preName = name.substring(0, name.lastIndexOf("."));
            starImageMap.put(preName, path);
        }

        // 检测有效文件，有效则从map里删除，剩下的全部就为无引用文件
        for (Record record:recordList) {
            if (starImageMap.get(record.getName()) != null) {
                starImageMap.remove(record.getName());
            }
        }

        // 删除无效文件
        Iterator<String> it = starImageMap.values().iterator();
        while (it.hasNext()) {
            String path = it.next();
            deleteUnavailable(new File(path));
        }
    }

    private void deleteUnavailable(File file) {

        DebugLog.e(file.getPath()
                + ", originName:" + encrypter.decipherOriginName(file));
        // 操作不可逆，测试时先注销掉仅打印待删除的文件
        encrypter.deleteFile(file);

    }

}
