package com.jing.app.jjgallery.gdb.presenter.record;

import android.os.AsyncTask;

import com.jing.app.jjgallery.http.bean.data.DownloadItem;
import com.jing.app.jjgallery.http.bean.request.GdbCheckNewFileBean;
import com.jing.app.jjgallery.config.Configuration;
import com.jing.app.jjgallery.config.DBInfor;
import com.jing.app.jjgallery.config.PreferenceValue;
import com.jing.app.jjgallery.gdb.GdbConstants;
import com.jing.app.jjgallery.gdb.model.RecordComparator;
import com.jing.app.jjgallery.gdb.model.VideoModel;
import com.jing.app.jjgallery.gdb.presenter.ManageListPresenter;
import com.jing.app.jjgallery.gdb.view.list.IManageListView;
import com.jing.app.jjgallery.gdb.view.record.IRecordListView;
import com.jing.app.jjgallery.gdb.view.record.IRecordSceneView;
import com.jing.app.jjgallery.service.encrypt.EncryptUtil;
import com.jing.app.jjgallery.service.http.Command;
import com.jing.app.jjgallery.service.http.GdbHttpClient;
import com.king.service.gdb.GDBProvider;
import com.king.service.gdb.bean.Record;
import com.king.service.gdb.bean.SceneBean;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/7/12 11:22
 */
public class RecordListPresenter extends ManageListPresenter {

    private IRecordListView recordListView;
    private IRecordSceneView recordSceneView;
    private GDBProvider gdbProvider;

    /**
     * 如果使用检测服务器端数据功能，view不能为空
     * @param view
     */
    public RecordListPresenter(IManageListView view) {
        super(view);
        gdbProvider = new GDBProvider(DBInfor.GDB_DB_PATH);
    }

    public void setRecordListView(IRecordListView recordListView) {
        this.recordListView = recordListView;
    }

    public void setRecordSceneView(IRecordSceneView recordSceneView) {
        this.recordSceneView = recordSceneView;
    }

    public void checkNewRecordFile() {
        GdbHttpClient.getInstance().getGdbService().checkNewFile(Command.TYPE_RECORD)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GdbCheckNewFileBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        view.onRequestFail();
                    }

                    @Override
                    public void onNext(GdbCheckNewFileBean bean) {
                        view.onCheckPass(bean.isRecordExisted(), bean.getRecordItems());
                    }
                });
    }

    /**
     * 从服务端提供的下载列表中选出已存在的和不存在的
     * @param downloadList 服务端提供的下载列表
     * @param existedList 已存在的下载内容，不能为null
     * @return 未存在的下载内容
     */
    public List<DownloadItem> pickRecordToDownload(List<DownloadItem> downloadList, List<DownloadItem> existedList) {
        List<DownloadItem> list = new ArrayList<>();
        for (DownloadItem item:downloadList) {
            // name 格式为 XXX.png
            String name = item.getName().substring(0, item.getName().lastIndexOf("."));

            String path;
            // 服务端文件处于一级目录
            if (item.getKey() == null) {
                // 检查本地一级目录是否存在
                path = Configuration.GDB_IMG_RECORD + "/" + name + EncryptUtil.getFileExtra();
                if (!new File(path).exists()) {
                    // 检查本地二级目录是否存在
                    path = Configuration.GDB_IMG_RECORD + "/" + name + "/" + name + EncryptUtil.getFileExtra();
                }
            }
            // 服务端文件处于二级目录
            else {
                // 只检查本地二级目录是否存在
                path = Configuration.GDB_IMG_RECORD + "/" + item.getKey() + "/" + name + EncryptUtil.getFileExtra();
            }

            // 检查本地一级目录是否存在
            if (new File(path).exists()) {
                item.setPath(path);
                existedList.add(item);
            } else {
                list.add(item);
            }
        }
        return list;
    }

    /**
     *
     * @param sortMode see PreferenceValue.GDB_SR_ORDERBY_XXX
     * @param desc
     */
    public void loadRecordList(int sortMode, boolean desc) {
        new LoadRecordListTask().execute(sortMode, desc);
    }

    /**
     *
     * @param sortMode see PreferenceValue.GDB_SR_ORDERBY_XXX
     * @param desc
     * @param showDeprecated deprecated attribute in Record
     * @param showCanBePlayed there is video in specific disk path
     * @param limitFrom (Limit from, num)
     * @param limitNum
     * @param like name like %like%
     * @param whereScene scene=whereScene
     */
    public void loadRecordList(int sortMode, boolean desc, boolean showDeprecated, boolean showCanBePlayed, int limitFrom, int limitNum, String like, String whereScene) {
        new LoadRecordListTask().execute(sortMode, desc, showDeprecated, showCanBePlayed, limitFrom, limitNum, like, whereScene);
    }
    /**
     *
     * @param sortMode see PreferenceValue.GDB_SR_ORDERBY_XXX
     * @param desc
     * @param showDeprecated deprecated attribute in Record
     * @param showCanBePlayed there is video in specific disk path
     * @param limitFrom (Limit from, num)
     * @param limitNum
     * @param like name like %like%
     * @param whereScene scene=whereScene
     */
    public void loadMoreRecords(int sortMode, boolean desc, boolean showDeprecated, boolean showCanBePlayed, int limitFrom, int limitNum, String like, String whereScene) {
        new LoadMoreRecordsTask().execute(sortMode, desc, showDeprecated, showCanBePlayed, limitFrom, limitNum, like, whereScene);
    }

    /**
     * 重新加载records
     */
    private class LoadRecordListTask extends AsyncTask<Object, Void, List<Record>> {
        @Override
        protected void onPostExecute(List<Record> list) {

            recordListView.onLoadRecordList(list);

            super.onPostExecute(list);
        }

        @Override
        protected List<Record> doInBackground(Object... params) {
            // load limited records
            if (params.length > 2) {
                int sortMode = (Integer) params[0];
                boolean desc = (Boolean) params[1];
                boolean includeDeprecated = (Boolean) params[2];
                boolean showCanBePlayed = (Boolean) params[3];
                int from = (Integer) params[4];
                int number = (Integer) params[5];
                String like = (String) params[6];
                String scene = (String) params[7];
                // 磁盘上文件不多，加载全部供挑选
                if (showCanBePlayed) {
                    from = -1;
                    number = -1;
                }
                List<Record> list = gdbProvider.getRecords(RecordComparator.getSortColumn(sortMode), desc, includeDeprecated, from, number, like, scene);

                if (showCanBePlayed) {
                    list = pickCanBePlayedRecord(list);
                }
                return list;
            }
            // load all records
            else {
                List<Record> list = gdbProvider.getAllRecords();
                sortRecords(list, (Integer) params[0], (Boolean) params[1]);
                return list;
            }
        }
    }

    private List<Record> pickCanBePlayedRecord(List<Record> list) {
        List<Record> rList = new ArrayList<>();
        for (Record record:list) {
            if (VideoModel.getVideoPath(record.getName()) != null) {
                rList.add(record);
            }
        }
        return rList;
    }

    /**
     * 加载更多records
     */
    private class LoadMoreRecordsTask extends AsyncTask<Object, Void, List<Record>> {
        @Override
        protected void onPostExecute(List<Record> list) {

            recordListView.onMoreRecordsLoaded(list);

            super.onPostExecute(list);
        }

        @Override
        protected List<Record> doInBackground(Object... params) {
            int sortMode = (Integer) params[0];
            boolean desc = (Boolean) params[1];
            boolean includeDeprecated = (Boolean) params[2];
            boolean showCanBePlayed = (Boolean) params[3];
            int from = (Integer) params[4];
            int number = (Integer) params[5];
            String like = (String) params[6];
            String scene = (String) params[7];
            List<Record> list = gdbProvider.getRecords(RecordComparator.getSortColumn(sortMode), desc, includeDeprecated, from, number, like, scene);

            if (showCanBePlayed) {
                list = pickCanBePlayedRecord(list);
            }
            return list;
        }
    }

    /**
     * sort records
     * @param recordList
     * @param sortMode
     * @param desc
     */
    public void sortRecords(List<Record> recordList, int sortMode, boolean desc) {
        if (sortMode != PreferenceValue.GDB_SR_ORDERBY_NONE) {
            Collections.sort(recordList, new RecordComparator(sortMode, desc));
        }
    }

    /**
     * load scenes
     */
    public void loadRecordScenes() {
        Observable.create(new Observable.OnSubscribe<List<SceneBean>>() {
            @Override
            public void call(Subscriber<? super List<SceneBean>> subscriber) {

                List<SceneBean> scenes = gdbProvider.getSceneList();
                subscriber.onNext(scenes);
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<SceneBean>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<SceneBean> data) {
                        recordSceneView.onScenesLoaded(data);
                    }
                });
    }

    /**
     *
     * @param sceneList
     * @param curSortType see GdbConstants.SCENE_SORT_XXX
     */
    public void sortScenes(List<SceneBean> sceneList, int curSortType) {
        switch (curSortType) {
            case GdbConstants.SCENE_SORT_NAME:
                Collections.sort(sceneList, new SceneNameComparator());
                break;
            case GdbConstants.SCENE_SORT_NUMBER:
                Collections.sort(sceneList, new SceneNumberComparator());
                break;
            case GdbConstants.SCENE_SORT_AVG:
                Collections.sort(sceneList, new SceneAverageComparator());
                break;
            case GdbConstants.SCENE_SORT_MAX:
                Collections.sort(sceneList, new SceneMaxComparator());
                break;
        }
    }

    public class SceneNameComparator implements Comparator<SceneBean> {

        @Override
        public int compare(SceneBean l, SceneBean r) {
            if (l == null || r == null) {
                return 0;
            }

            return l.getScene().toLowerCase().compareTo(r.getScene().toLowerCase());
        }
    }

    public class SceneAverageComparator implements Comparator<SceneBean> {

        @Override
        public int compare(SceneBean l, SceneBean r) {
            if (l == null || r == null) {
                return 0;
            }

            if (r.getAverage() - l.getAverage() > 0) {
                return 1;
            }
            else if (r.getAverage() - l.getAverage() < 0) {
                return -1;
            }
            else {
                return 0;
            }
        }
    }

    public class SceneNumberComparator implements Comparator<SceneBean> {

        @Override
        public int compare(SceneBean l, SceneBean r) {
            if (l == null || r == null) {
                return 0;
            }

            if (r.getNumber() - l.getNumber() > 0) {
                return 1;
            }
            else if (r.getNumber() - l.getNumber() < 0) {
                return -1;
            }
            else {
                return 0;
            }
        }
    }

    public class SceneMaxComparator implements Comparator<SceneBean> {

        @Override
        public int compare(SceneBean l, SceneBean r) {
            if (l == null || r == null) {
                return 0;
            }

            if (r.getMax() - l.getMax() > 0) {
                return 1;
            }
            else if (r.getMax() - l.getMax() < 0) {
                return -1;
            }
            else {
                return 0;
            }
        }
    }

}
