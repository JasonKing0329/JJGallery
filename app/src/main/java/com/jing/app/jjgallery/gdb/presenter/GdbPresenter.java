package com.jing.app.jjgallery.gdb.presenter;

import android.os.AsyncTask;

import com.jing.app.jjgallery.gdb.bean.RecordProxy;
import com.king.service.gdb.bean.FavorBean;
import com.king.service.gdb.bean.StarCountBean;
import com.jing.app.jjgallery.gdb.bean.StarProxy;
import com.jing.app.jjgallery.bean.http.DownloadItem;
import com.jing.app.jjgallery.bean.http.GdbCheckNewFileBean;
import com.jing.app.jjgallery.bean.http.GdbRespBean;
import com.jing.app.jjgallery.config.DBInfor;
import com.jing.app.jjgallery.config.PreferenceValue;
import com.jing.app.jjgallery.gdb.model.RecordComparator;
import com.jing.app.jjgallery.service.encrypt.EncryptUtil;
import com.jing.app.jjgallery.service.http.Command;
import com.jing.app.jjgallery.service.http.GdbHttpClient;
import com.jing.app.jjgallery.service.http.progress.AppHttpClient;
import com.jing.app.jjgallery.gdb.GdbConstants;
import com.jing.app.jjgallery.gdb.view.list.IGdbFragment;
import com.jing.app.jjgallery.gdb.view.record.IGdbRecordListView;
import com.jing.app.jjgallery.gdb.view.star.IGdbStarListView;
import com.jing.app.jjgallery.gdb.view.star.IStarView;
import com.king.service.gdb.GDBProvider;
import com.king.service.gdb.bean.GDBProperites;
import com.king.service.gdb.bean.Record;
import com.king.service.gdb.bean.RecordSingleScene;
import com.king.service.gdb.bean.Star;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/7/30 0030.
 */
public class GdbPresenter {

    private IGdbFragment commonView;
    private IGdbStarListView gdbStarListView;
    private IGdbRecordListView gdbRecordListView;
    private IStarView starView;
    private GDBProvider gdbProvider;
    private GDBProvider favorProvider;

    public GdbPresenter() {
        init();
    }

    private void init() {
        gdbProvider = new GDBProvider(DBInfor.GDB_DB_PATH);
        favorProvider = new GDBProvider(DBInfor.GDB_FAVOR_DB_PATH);
    }

    public void setViewCallback(IGdbStarListView gdbStarListView) {
        this.gdbStarListView = gdbStarListView;
        commonView = (IGdbFragment) gdbStarListView;
    }

    public void setViewCallback(IGdbRecordListView gdbRecordListView) {
        this.gdbRecordListView = gdbRecordListView;
        commonView = (IGdbFragment) gdbRecordListView;
    }

    public void setViewCallback(IStarView starView) {
        this.starView = starView;
    }

    public void loadStarList(String starMode) {
        int orderBy = GdbConstants.STAR_SORT_NAME;
        new LoadStarListTask().execute(orderBy, starMode);
    }

    public void loadStarListOrderByNumber(String starMode) {
        int orderBy = GdbConstants.STAR_SORT_RECORDS;
        new LoadStarListTask().execute(orderBy, starMode);
    }

    public void loadStarListOrderByFavor(String starMode) {
        int orderBy = GdbConstants.STAR_SORT_FAVOR;
        new LoadStarListTask().execute(orderBy, starMode);
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
     * @param limitFrom (Limit from, num)
     * @param limitNum
     * @param like name like %like%
     */
    public void loadRecordList(int sortMode, boolean desc, int limitFrom, int limitNum, String like) {
        new LoadRecordListTask().execute(sortMode, desc, limitFrom, limitNum, like);
    }
    /**
     *
     * @param sortMode see PreferenceValue.GDB_SR_ORDERBY_XXX
     * @param desc
     * @param limitFrom (Limit from, num)
     * @param limitNum
     * @param like name like %like%
     */
    public void loadMoreRecords(int sortMode, boolean desc, int limitFrom, int limitNum, String like) {
        new LoadMoreRecordsTask().execute(sortMode, desc, limitFrom, limitNum, like);
    }

    public void queryIndicatorData() {
        new QueryIndicatorTask().execute();
    }

    public void loadStar(int starId) {
        new LoadStarTask().execute(starId);
    }

    /**
     * 加载star对应的record数量
     * @param star
     */
    @Deprecated
    public void loadStarRecordNumber(Star star) {
        gdbProvider.loadStarRecordNumber(star);
    }

    public void sortRecords(List<Record> recordList, int sortMode, boolean desc) {
        if (sortMode != PreferenceValue.GDB_SR_ORDERBY_NONE) {
            Collections.sort(recordList, new RecordComparator(sortMode, desc));
        }
    }

    public void sortSceneRecords(List<RecordProxy> recordList, int sortMode, boolean desc) {
        if (sortMode != PreferenceValue.GDB_SR_ORDERBY_NONE) {
            List<Record> list = null;
            int index = 0;
            int headIndex = 0;
            for (RecordProxy proxy:recordList) {
                if (proxy.isHeader()) {
                    if (list != null) {
                        Collections.sort(list, new RecordComparator(sortMode, desc));
                        for (int i = 0; i < list.size(); i ++) {
                            recordList.get(headIndex + 1 + i).setRecord(list.get(i));
                        }
                    }
                    headIndex = index;
                    list = new ArrayList<>();
                }
                else {
                    list.add(proxy.getRecord());
                }
                index ++;
            }
        }
    }

    public void sortSceneRecords(Map<String, List<Record>> map, int sortMode, boolean desc) {
        if (sortMode != PreferenceValue.GDB_SR_ORDERBY_NONE) {
            for (List<Record> list:map.values()) {
                Collections.sort(list, new RecordComparator(sortMode, desc));
            }
        }
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
                        commonView.onServerUnavailable();
                    }

                    @Override
                    public void onNext(GdbRespBean gdbRespBean) {
                        if (gdbRespBean.isOnline()) {
                            commonView.onServerConnected();
                        }
                        else {
                            commonView.onServerUnavailable();
                        }
                    }
                });
    }

    public void checkNewStarFile() {
        GdbHttpClient.getInstance().getGdbService().checkNewFile(Command.TYPE_STAR)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GdbCheckNewFileBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        gdbStarListView.onRequestFail();
                    }

                    @Override
                    public void onNext(GdbCheckNewFileBean bean) {
                        gdbStarListView.onCheckPass(bean.isStarExisted(), bean.getStarItems());
                    }
                });
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
                        gdbRecordListView.onRequestFail();
                    }

                    @Override
                    public void onNext(GdbCheckNewFileBean bean) {
                        gdbRecordListView.onCheckPass(bean.isRecordExisted(), bean.getRecordItems());
                    }
                });
    }

    /**
     * 检查已有图片的star，将其过滤掉
     * @param downloadList 服务端提供的下载列表
     * @param existedList 已存在的下载内容，不能为null
     * @return 未存在的下载内容
     */
    public List<DownloadItem> pickStarToDownload(List<DownloadItem> downloadList, List<DownloadItem> existedList) {
        List<DownloadItem> list = new ArrayList<>();
        for (DownloadItem item:downloadList) {
            String name = item.getName().substring(0, item.getName().lastIndexOf("."));
            String path = EncryptUtil.getEncryptStarPath(name);
            if (path == null) {
                list.add(item);
            }
            else {
                item.setPath(path);
                existedList.add(item);
            }
        }
        return list;
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
            String name = item.getName().substring(0, item.getName().lastIndexOf("."));
            String path = EncryptUtil.getEncryptRecordPath(name);
            if (path == null) {
                list.add(item);
            }
            else {
                existedList.add(item);
            }
        }
        return list;
    }

    public void saveFavor(FavorBean bean) {
        favorProvider.saveFavor(bean);
    }

    /**
     * 将下载文件进行全部加密，回调在onDownloadItemEncrypted
     * @param downloadList
     */
    public void finishDownload(List<DownloadItem> downloadList) {

        new FinishDownloadTask().execute(downloadList);
    }

    private class LoadStarListTask extends AsyncTask<Object, Void, List<StarProxy>> {

        private int orderBy;

        @Override
        protected void onPostExecute(List<StarProxy> list) {

            List<StarProxy> resultList = new ArrayList<>();
            if (orderBy == GdbConstants.STAR_SORT_RECORDS) {// order by records number
                resultList.addAll(list);
                Collections.sort(resultList, new StarRecordsNumberComparator());
            }
            else if (orderBy == GdbConstants.STAR_SORT_FAVOR) {
                for (StarProxy proxy:list) {
                    if (proxy.getFavor() > 0) {
                        resultList.add(proxy);
                    }
                }
                Collections.sort(resultList, new StarFavorComparator());
            }
            else {// order by name
                // add headers
                // about header rules, see viewsystem/main/gdb/StarListAdapter.java
                StarProxy star = null;
                char index = 'A';
                for (int i = 0; i < 26; i ++) {
                    star = new StarProxy();
                    Star s = new Star();
                    star.setStar(s);
                    s.setId(-1);
                    s.setName("" + index ++);
                    resultList.add(star);
                }
                resultList.addAll(list);
                Collections.sort(resultList, new StarNameComparator());
            }

            gdbStarListView.onLoadStarList(resultList);

            super.onPostExecute(list);
        }

        @Override
        protected List<StarProxy> doInBackground(Object... params) {
            orderBy = (int) params[0];
            String starMode = (String) params[1];

            List<FavorBean> favorList = favorProvider.getFavors();
            Map<Integer, FavorBean> favorMap = new HashMap<>();
            for (FavorBean bean:favorList) {
                favorMap.put(bean.getStarId(), bean);
            }

            List<Star> list = gdbProvider.getStars(starMode);
            List<StarProxy> proxyList = new ArrayList<>();
            for (Star star:list) {
                StarProxy proxy = new StarProxy();
                proxy.setStar(star);
                proxy.setImagePath(EncryptUtil.getEncryptStarPath(star.getName()));
                FavorBean favor = favorMap.get(star.getId());
                proxy.setFavor(favor == null ? 0:favor.getFavor());
                proxyList.add(proxy);
            }
            return proxyList;
        }
    }

    /**
     * 重新加载records
     */
    private class LoadRecordListTask extends AsyncTask<Object, Void, List<Record>> {
        @Override
        protected void onPostExecute(List<Record> list) {

            gdbRecordListView.onLoadRecordList(list);

            super.onPostExecute(list);
        }

        @Override
        protected List<Record> doInBackground(Object... params) {
            // load limited records
            if (params.length > 2) {
                int sortMode = (Integer) params[0];
                boolean desc = (Boolean) params[1];
                int from = (Integer) params[2];
                int number = (Integer) params[3];
                String like = (String) params[4];
                List<Record> list = gdbProvider.getRecords(RecordComparator.getSortColumn(sortMode), desc, from, number, like);
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

    /**
     * 加载更多records
     */
    private class LoadMoreRecordsTask extends AsyncTask<Object, Void, List<Record>> {
        @Override
        protected void onPostExecute(List<Record> list) {

            gdbRecordListView.onMoreRecordsLoaded(list);

            super.onPostExecute(list);
        }

        @Override
        protected List<Record> doInBackground(Object... params) {
            int sortMode = (Integer) params[0];
            boolean desc = (Boolean) params[1];
            int from = (Integer) params[2];
            int number = (Integer) params[3];
            String like = (String) params[4];
            List<Record> list = gdbProvider.getRecords(RecordComparator.getSortColumn(sortMode), desc, from, number, like);
            return list;
        }
    }

    private class LoadStarTask extends AsyncTask<Integer, Void, StarProxy> {
        @Override
        protected void onPostExecute(StarProxy star) {

            starView.onStarLoaded(star);

            super.onPostExecute(star);
        }

        @Override
        protected StarProxy doInBackground(Integer... params) {
            // load star object and star's records
            Star star = gdbProvider.getStarRecords(params[0]);
            StarProxy proxy = new StarProxy();
            proxy.setStar(star);

            // load image path of star
            String headPath = EncryptUtil.getEncryptStarPath(star.getName());
            proxy.setImagePath(headPath);
            return proxy;
        }
    }

    private class FinishDownloadTask extends AsyncTask<List<DownloadItem>, Void, Void> {
        @Override
        protected void onPostExecute(Void param) {

            commonView.onDownloadItemEncrypted();
            super.onPostExecute(param);
        }

        @Override
        protected Void doInBackground(List<DownloadItem>... params) {
            for (DownloadItem item:params[0]) {
                File file = new File(item.getPath());
                // 加密文件
                EncryptUtil.encryptFile(file);
            }
            return null;
        }
    }

    private class QueryIndicatorTask extends AsyncTask<Object, Void, StarCountBean> {
        @Override
        protected void onPostExecute(StarCountBean bean) {

            gdbStarListView.onStarCountLoaded(bean);
            super.onPostExecute(bean);
        }

        @Override
        protected StarCountBean doInBackground(Object... params) {

            StarCountBean bean = gdbProvider.queryStarCount();
            return bean;
        }
    }

    /**
     * order by name
     */
    public class StarNameComparator implements Comparator<StarProxy> {

        @Override
        public int compare(StarProxy l, StarProxy r) {
            if (l == null || r == null) {
                return 0;
            }

            return l.getStar().getName().toLowerCase().compareTo(r.getStar().getName().toLowerCase());
        }
    }

    /**
     * order by records number
     */
    public class StarRecordsNumberComparator implements Comparator<StarProxy> {

        @Override
        public int compare(StarProxy l, StarProxy r) {
            if (l == null || r == null) {
                return 0;
            }

            // order by record number desc
            int result = r.getStar().getRecordNumber() - l.getStar().getRecordNumber();
            // if same, then compare name and order by name asc
            if (result == 0) {
                result = l.getStar().getName().toLowerCase().compareTo(r.getStar().getName().toLowerCase());
            }
            return result;
        }
    }

    /**
     * order by favor score
     */
    public class StarFavorComparator implements Comparator<StarProxy> {

        @Override
        public int compare(StarProxy l, StarProxy r) {

            return r.getFavor() - l.getFavor();
        }
    }

    public List<RecordProxy> collectRecordsByScene(List<Record> list, int sortMode, boolean desc) {
        List<RecordProxy> resultList = new ArrayList<>();
        Map<String, List<Record>> map = new HashMap<>();
        List<Record> subList;

        // 按scene归类list
        for (Record record:list) {
            if (record instanceof RecordSingleScene) {
                String scene = ((RecordSingleScene) record).getSceneName();
                if (scene == null) {
                    scene = GDBProperites.RECORD_UNKNOWN;
                }
                subList = map.get(scene);
                if (subList == null) {
                    subList = new ArrayList<>();
                    map.put(scene, subList);
                }
                subList.add(record);
            }
        }

        List<String> scenes = new ArrayList<>();
        // 将scene按名称升序排序
        Iterator<String> it = map.keySet().iterator();
        while (it.hasNext()) {
            scenes.add(it.next());
        }
        Collections.sort(scenes, new NameComparator());

        RecordComparator comparator = new RecordComparator(sortMode, desc);
        // 将list转化为包含header模型的proxy模型
        for (int i = 0; i < scenes.size(); i ++) {
            RecordProxy proxy = new RecordProxy();
            proxy.setHeaderName(scenes.get(i));
            proxy.setHeader(true);

            subList = map.get(scenes.get(i));
            Collections.sort(subList, comparator);
            proxy.setItemNumber(subList.size());
            resultList.add(proxy);

            for (int j = 0; j < subList.size(); j ++) {
                proxy = new RecordProxy();
                proxy.setPositionInHeader(j);
                proxy.setRecord(subList.get(j));
                resultList.add(proxy);
            }
        }
        return resultList;
    }

    public Map<String, List<Record>> collectRecordsMapByScene(List<Record> list, int sortMode, boolean desc) {
        Map<String, List<Record>> map = new HashMap<>();
        List<Record> subList;

        // 按scene归类list
        for (Record record:list) {
            if (record instanceof RecordSingleScene) {
                String scene = ((RecordSingleScene) record).getSceneName();
                if (scene == null) {
                    scene = GDBProperites.RECORD_UNKNOWN;
                }
                subList = map.get(scene);
                if (subList == null) {
                    subList = new ArrayList<>();
                    map.put(scene, subList);
                }
                subList.add(record);
            }
        }

        return map;
    }

    public class NameComparator implements Comparator<String> {

        @Override
        public int compare(String l, String r) {
            if (l == null || r == null) {
                return 0;
            }

            return l.toLowerCase().compareTo(r.toLowerCase());
        }
    }

}
