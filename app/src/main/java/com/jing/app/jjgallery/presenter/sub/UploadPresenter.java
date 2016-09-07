package com.jing.app.jjgallery.presenter.sub;

import com.jing.app.jjgallery.config.ConfManager;
import com.jing.app.jjgallery.config.Configuration;
import com.jing.app.jjgallery.service.http.UploadClient;
import com.jing.app.jjgallery.util.DebugLog;
import com.jing.app.jjgallery.viewsystem.sub.upload.IUploadView;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/9/7.
 */
public class UploadPresenter {

    private IUploadView uploadView;

    public UploadPresenter(IUploadView uploadView) {
        this.uploadView = uploadView;
    }

    public void uploadAppData(){

        //组装partMap对象
        Map<String, RequestBody> partMap = new HashMap<>();

        // gdb database
        File file = new File(ConfManager.GDB_DB_PATH);
        RequestBody fileBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        partMap.put("file\"; filename=\""+file.getName(), fileBody);

        // app database
        file = new File(ConfManager.DB_PATH);
        fileBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        partMap.put("file\"; filename=\""+file.getName(), fileBody);

        // preference
        file = new File(Configuration.EXTEND_RES_COLOR);
        fileBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        partMap.put("file\"; filename=\""+file.getName(), fileBody);

        // preference
        file = new File(Configuration.APP_DIR_CONF_PREF_DEF);
        File files[] = file.listFiles();
        if (files.length > 0) {
            file = files[0];
        }
        fileBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        partMap.put("file\"; filename=\""+file.getName(), fileBody);

        //使用RxJava方式调度任务并监听
        new UploadClient().getUploadService().upload(partMap)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<ResponseBody>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        DebugLog.e("upload" + e.getMessage());
                        uploadView.onUploadFail();
                    }

                    @Override
                    public void onNext(ResponseBody body) {
                        DebugLog.e("upload");
                        uploadView.onUploadSuccess();
                    }
                });
    }
}
