package com.jing.app.jjgallery.gdb.presenter.surf;

import com.jing.app.jjgallery.config.DBInfor;
import com.jing.app.jjgallery.config.PreferenceValue;
import com.jing.app.jjgallery.gdb.model.RecordComparator;
import com.jing.app.jjgallery.gdb.view.surf.ISurfView;
import com.jing.app.jjgallery.gdb.view.surf.SurfFileBean;
import com.jing.app.jjgallery.http.bean.data.FileBean;
import com.jing.app.jjgallery.http.bean.request.FolderRequest;
import com.jing.app.jjgallery.http.bean.response.FolderResponse;
import com.jing.app.jjgallery.service.http.GdbHttpClient;
import com.king.service.gdb.GDBProvider;
import com.king.service.gdb.bean.Record;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/7/27 11:10
 */
public class SurfPresenter {

    private ISurfView surfView;
    private GDBProvider gdbProvider;

    public SurfPresenter(ISurfView surfView) {
        this.surfView = surfView;
        gdbProvider = new GDBProvider(DBInfor.GDB_DB_PATH);
    }

    public void surf(String type, String folder, final boolean relateToDatabase) {
        FolderRequest request = new FolderRequest();
        request.setType(type);
        request.setFolder(folder);
        GdbHttpClient.getInstance().getGdbService().requestSurf(request)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<FolderResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        surfView.onRequestFail();
                    }

                    @Override
                    public void onNext(FolderResponse bean) {
                        List<SurfFileBean> list = new ArrayList<>();
                        for (FileBean fb:bean.getFileList()) {
                            SurfFileBean sfb = new SurfFileBean();
                            sfb.setName(fb.getName());
                            sfb.setPath(fb.getPath());
                            sfb.setFolder(fb.isFolder());
                            sfb.setExtra(fb.getExtra());
                            sfb.setLastModifyTime(fb.getLastModifyTime());
                            sfb.setSize(fb.getSize());
                            sfb.setParentBean(fb.getParentBean());
                            list.add(sfb);
                        }
                        surfView.onFolderReceived(list);
                        if (relateToDatabase) {
                            relateToDatabase(list);
                        }
                    }
                });
    }

    public void relateToDatabase(final List<SurfFileBean> list) {
        Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                for (int i = 0; i < list.size(); i ++) {
                    SurfFileBean bean = list.get(i);
                    if (!bean.isFolder()) {
                        Record record = gdbProvider.getRecordByName(bean.getName());
                        bean.setRecord(record);
                        subscriber.onNext(i);
                    }
                }
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Integer index) {
                        surfView.onRecordRelated(index);
                    }
                });
    }

    public void sortFileList(List<SurfFileBean> surfFileList, int sortMode, boolean desc) {
        Collections.sort(surfFileList, new SurfComparator(sortMode, desc));
        surfView.onSortFinished();
    }

    private class SurfComparator implements Comparator<SurfFileBean> {

        private int sortMode;
        private boolean desc;

        private SurfComparator(int sortMode, boolean desc) {
            this.sortMode = sortMode;
            this.desc = desc;
        }

        @Override
        public int compare(SurfFileBean o1, SurfFileBean o2) {
            int result;
            // 按名称排序时，文件夹排在最前面
            if (sortMode == PreferenceValue.GDB_SR_ORDERBY_NAME) {
                // 同为文件/文件夹，按名称
                if (o1.isFolder() == o2.isFolder()) {
                    if (desc) {
                        result = o2.getName().toLowerCase().compareTo(o1.getName().toLowerCase());
                    }
                    else {
                        result = o1.getName().toLowerCase().compareTo(o2.getName().toLowerCase());
                    }
                }
                // 一个为文件，一个为文件夹，文件夹永远在前
                else {
                    result = o1.isFolder() ? -1:1;
                }
            }
            else if (sortMode == PreferenceValue.GDB_SR_ORDERBY_TIME) {
                if (o1.getLastModifyTime() - o2.getLastModifyTime() < 0) {
                    result = desc ? 1:-1;
                }
                else if (o1.getLastModifyTime() - o2.getLastModifyTime() > 0) {
                    result = desc ? -1:1;
                }
                else {
                    result = 0;
                }
            }
            else if (sortMode == PreferenceValue.GDB_SR_ORDERBY_SIZE) {
                if (o1.getSize() - o2.getSize() < 0) {
                    result = desc ? 1:-1;
                }
                else if (o1.getSize() - o2.getSize() > 0) {
                    result = desc ? -1:1;
                }
                else {
                    result = 0;
                }
            }
            // 按照record记录排序
            else {
                Record lr = o1.getRecord();
                Record rr = o2.getRecord();
                // 都有record记录，按mode和desc排在最前面
                if (lr != null && rr != null) {
                    RecordComparator.setSortMode(sortMode);
                    RecordComparator.setDesc(desc);
                    result = RecordComparator.compareRecord(lr, rr);
                }
                // 没有record记录的排在后面
                else if (lr == null && rr != null) {
                    result = 1;
                }
                else if (lr != null && rr == null) {
                    result = -1;
                }
                else {
                    result = 0;
                }
            }
            return result;
        }
    }
}
