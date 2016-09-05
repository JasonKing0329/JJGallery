package com.jing.app.jjgallery.presenter.main;

import android.os.AsyncTask;

import com.jing.app.jjgallery.bean.RecordProxy;
import com.jing.app.jjgallery.bean.StarProxy;
import com.jing.app.jjgallery.bean.http.DownloadItem;
import com.jing.app.jjgallery.bean.http.GdbCheckNewFileBean;
import com.jing.app.jjgallery.bean.http.GdbRespBean;
import com.jing.app.jjgallery.config.Configuration;
import com.jing.app.jjgallery.config.DBInfor;
import com.jing.app.jjgallery.config.PreferenceValue;
import com.jing.app.jjgallery.model.main.file.FolderManager;
import com.jing.app.jjgallery.service.encrypt.EncrypterFactory;
import com.jing.app.jjgallery.service.encrypt.action.Encrypter;
import com.jing.app.jjgallery.service.http.GdbHttpClient;
import com.jing.app.jjgallery.service.http.progress.AppHttpClient;
import com.jing.app.jjgallery.viewsystem.main.gdb.IGdbFragment;
import com.jing.app.jjgallery.viewsystem.main.gdb.IGdbRecordListView;
import com.jing.app.jjgallery.viewsystem.main.gdb.IGdbStarListView;
import com.jing.app.jjgallery.viewsystem.main.gdb.IStarView;
import com.king.service.gdb.GDBProvider;
import com.king.service.gdb.bean.GDBProperites;
import com.king.service.gdb.bean.Record;
import com.king.service.gdb.bean.RecordOneVOne;
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
    private static Encrypter encrypter;

    /**
     * 在loadAllStars的时候遍历gdb/star目录，解析出所有name对应的path
     */
    private Map<String, String> starImageMap;
    /**
     * 在loadAllRecords的时候遍历gdb/record目录，解析出所有name对应的path
     */
    private static Map<String, String> recordImageMap;

    public GdbPresenter(IGdbStarListView gdbStarListView) {
        this.gdbStarListView = gdbStarListView;
        commonView = (IGdbFragment) gdbStarListView;
        init();
    }

    public GdbPresenter(IGdbRecordListView gdbRecordListView) {
        this.gdbRecordListView = gdbRecordListView;
        commonView = (IGdbFragment) gdbRecordListView;
        init();
    }

    public GdbPresenter(IStarView starView) {
        this.starView = starView;
        init();
    }

    private void init() {
        gdbProvider = new GDBProvider(DBInfor.GDB_DB_PATH);
        encrypter = EncrypterFactory.create();
        starImageMap = new HashMap<>();
        recordImageMap = new HashMap<>();
    }

    public void loadStarList() {
        new LoadStarListTask().execute();
    }

    public String getStarImage(String starName) {
        return starImageMap.get(starName);
    }

    public String getRecordImage(String starName) {
        return recordImageMap.get(starName);
    }
    /**
     *
     * @param sortMode see PreferenceValue.GDB_SR_ORDERBY_XXX
     * @param desc
     */
    public void loadRecordList(int sortMode, boolean desc) {
        new LoadRecordListTask().execute(sortMode, desc);
    }

    public void loadStar(int starId) {
        new LoadStarTask().execute(starId);
    }

    /**
     * 加载star对应的record数量
     * @param star
     */
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

    public static String getRecordPath(String recordName) {
        String result = recordImageMap.get(recordName);
        if (result == null) {
            // load image path of star
            List<String> list = new FolderManager().loadPathList(Configuration.GDB_IMG_RECORD);
            for (String path:list) {
                String name = encrypter.decipherOriginName(new File(path));
                int dot = name.lastIndexOf(".");
                String preName = name.substring(0, dot);
                if (preName.equals(recordName)) {
                    return path;
                }
            }
        }
        return result;
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
        GdbHttpClient.getInstance().getGdbService().checkNewFile("star")
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
        GdbHttpClient.getInstance().getGdbService().checkNewFile("record")
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
     * @param downloadList
     * @return
     */
    public List<DownloadItem> pickStarToDownload(List<DownloadItem> downloadList) {
        List<DownloadItem> list = new ArrayList<>();
        for (DownloadItem item:downloadList) {
            String name = item.getName().substring(0, item.getName().lastIndexOf("."));
            if (starImageMap.get(name) == null) {
                list.add(item);
            }
        }
        return list;
    }

    public List<DownloadItem> pickRecordToDownload(List<DownloadItem> downloadList) {
        List<DownloadItem> list = new ArrayList<>();
        for (DownloadItem item:downloadList) {
            String name = item.getName().substring(0, item.getName().lastIndexOf("."));
            if (recordImageMap.get(name) == null) {
                list.add(item);
            }
        }
        return list;
    }

    private class LoadStarListTask extends AsyncTask<Void, Void, List<Star>> {
        @Override
        protected void onPostExecute(List<Star> list) {

            List<Star> resultList = new ArrayList<>();
            // add headers
            // about header rules, see viewsystem/main/gdb/StarListAdapter.java
            Star star = null;
            char index = 'A';
            for (int i = 0; i < 26; i ++) {
                star = new Star();
                star.setId(-1);
                star.setName("" + index ++);
                resultList.add(star);
            }
            resultList.addAll(list);
            Collections.sort(resultList, new StarComparator());

            // load available images for stars
            List<String> pathList = new FolderManager().loadPathList(Configuration.GDB_IMG_STAR);
            for (String path:pathList) {
                String name = encrypter.decipherOriginName(new File(path));
                String preName = name.substring(0, name.lastIndexOf("."));
                starImageMap.put(preName, path);
            }

            gdbStarListView.onLoadStarList(resultList);

            super.onPostExecute(list);
        }

        @Override
        protected List<Star> doInBackground(Void... params) {
            return gdbProvider.getStars();
        }
    }

    private class LoadRecordListTask extends AsyncTask<Object, Void, List<Record>> {
        @Override
        protected void onPostExecute(List<Record> list) {

            gdbRecordListView.onLoadRecordList(list);

            super.onPostExecute(list);
        }

        @Override
        protected List<Record> doInBackground(Object... params) {
            List<Record> list = gdbProvider.getAllRecords();
            sortRecords(list, (Integer) params[0], (Boolean) params[1]);

            // load available images for records
            List<String> pathList = new FolderManager().loadPathList(Configuration.GDB_IMG_RECORD);
            for (String path:pathList) {
                String name = encrypter.decipherOriginName(new File(path));
                String preName = name.substring(0, name.lastIndexOf("."));
                recordImageMap.put(preName, path);
            }

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
            String headPath = starImageMap.get(star.getName());
            if (headPath != null) {
                proxy.setImagePath(headPath);
            }
            else {
                List<String> list = new FolderManager().loadPathList(Configuration.GDB_IMG_STAR);
                for (String path:list) {
                    String name = encrypter.decipherOriginName(new File(path));
                    String preName = name.substring(0, name.lastIndexOf("."));
                    if (preName.equals(star.getName())) {
                        proxy.setImagePath(path);
                        break;
                    }
                }
            }
            return proxy;
        }
    }

    public class StarComparator implements Comparator<Star> {

        @Override
        public int compare(Star l, Star r) {
            if (l == null || r == null) {
                return 0;
            }

            return l.getName().toLowerCase().compareTo(r.getName().toLowerCase());
        }
    }

    private class RecordComparator implements Comparator<Record> {

        private int sortMode;
        private boolean desc;
        public RecordComparator(int sortMode, boolean desc) {
            this.sortMode = sortMode;
            this.desc = desc;
        }

        @Override
        public int compare(Record lhs, Record rhs) {
            RecordOneVOne left = null;
            RecordOneVOne right = null;
            if (lhs instanceof RecordOneVOne) {
                left = (RecordOneVOne) lhs;
            }
            if (rhs instanceof RecordOneVOne) {
                right = (RecordOneVOne) rhs;
            }
            
            int result = 0;
            switch (sortMode) {
                case PreferenceValue.GDB_SR_ORDERBY_NAME:// asc
                    if (desc) {
                        result = rhs.getName().toLowerCase().compareTo(lhs.getName().toLowerCase());
                    }
                    else {
                        result = lhs.getName().toLowerCase().compareTo(rhs.getName().toLowerCase());
                    }
                    break;
                case PreferenceValue.GDB_SR_ORDERBY_DATE:
                    if (desc) {
                        long lr = rhs.getLastModifyTime() - lhs.getLastModifyTime();
                        // 直接把负数long型强制转化为int会有问题，所以还是要通过判断来返回真正的带符号的整型
                        if (lr > 0) {
                            return 1;
                        }
                        else if (lr < 0) {
                            return -1;
                        }
                        else {
                            return 0;
                        }
                    }
                    else {
                        result = (int) (lhs.getLastModifyTime() - rhs.getLastModifyTime());
                    }
                    break;
                case PreferenceValue.GDB_SR_ORDERBY_SCORE:
                    if (desc) {
                        result = rhs.getScore() - lhs.getScore();
                    }
                    else {
                        result = lhs.getScore() - rhs.getScore();
                    }
                    break;
                case PreferenceValue.GDB_SR_ORDERBY_FK:
                    if (desc) {
                        result = right.getScoreFk() - left.getScoreFk();
                    }
                    else {
                        result = left.getScoreFk() - right.getScoreFk();
                    }
                    break;
                case PreferenceValue.GDB_SR_ORDERBY_CUM:
                    if (desc) {
                        result = right.getScoreCum() - left.getScoreCum();
                    }
                    else {
                        result = left.getScoreCum() - right.getScoreCum();
                    }
                    break;
                case PreferenceValue.GDB_SR_ORDERBY_BJOB:
                    if (desc) {
                        result = right.getScoreBJob() - left.getScoreBJob();
                    }
                    else {
                        result = left.getScoreBJob() - right.getScoreBJob();
                    }
                    break;
                case PreferenceValue.GDB_SR_ORDERBY_STAR1:
                    if (desc) {
                        result = right.getScoreStar1() - left.getScoreStar1();
                    }
                    else {
                        result = left.getScoreStar1() - right.getScoreStar1();
                    }
                    break;
                case PreferenceValue.GDB_SR_ORDERBY_STAR2:
                    if (desc) {
                        result = right.getScoreStar2() - left.getScoreStar2();
                    }
                    else {
                        result = left.getScoreStar2() - right.getScoreStar2();
                    }
                    break;
                case PreferenceValue.GDB_SR_ORDERBY_STARCC1:
                    if (desc) {
                        result = right.getScoreStarC1() - left.getScoreStarC1();
                    }
                    else {
                        result = left.getScoreStarC1() - right.getScoreStarC1();
                    }
                    break;
                case PreferenceValue.GDB_SR_ORDERBY_STARCC2:
                    if (desc) {
                        result = right.getScoreStarC2() - left.getScoreStarC2();
                    }
                    else {
                        result = left.getScoreStarC2() - right.getScoreStarC2();
                    }
                    break;
                case PreferenceValue.GDB_SR_ORDERBY_BAREBACK:
                    if (desc) {
                        result = right.getScoreNoCond() - left.getScoreNoCond();
                    }
                    else {
                        result = left.getScoreNoCond() - right.getScoreNoCond();
                    }
                    break;
                case PreferenceValue.GDB_SR_ORDERBY_SCOREFEEL:
                    if (desc) {
                        result = right.getScoreFeel() - left.getScoreFeel();
                    }
                    else {
                        result = left.getScoreFeel() - right.getScoreFeel();
                    }
                    break;
                case PreferenceValue.GDB_SR_ORDERBY_STORY:
                    if (desc) {
                        result = right.getScoreStory() - left.getScoreStory();
                    }
                    else {
                        result = left.getScoreStory() - right.getScoreStory();
                    }
                    break;
                case PreferenceValue.GDB_SR_ORDERBY_FOREPLAY:
                    if (desc) {
                        result = right.getScoreForePlay() - left.getScoreForePlay();
                    }
                    else {
                        result = left.getScoreForePlay() - right.getScoreForePlay();
                    }
                    break;
                case PreferenceValue.GDB_SR_ORDERBY_RIM:
                    if (desc) {
                        result = right.getScoreRim() - left.getScoreRim();
                    }
                    else {
                        result = left.getScoreRim() - right.getScoreRim();
                    }
                    break;
                case PreferenceValue.GDB_SR_ORDERBY_RHYTHM:
                    if (desc) {
                        result = right.getScoreRhythm() - left.getScoreRhythm();
                    }
                    else {
                        result = left.getScoreRhythm() - right.getScoreRhythm();
                    }
                    break;
                case PreferenceValue.GDB_SR_ORDERBY_SCENE:
                    if (desc) {
                        result = right.getScoreScene() - left.getScoreScene();
                    }
                    else {
                        result = left.getScoreScene() - right.getScoreScene();
                    }
                    break;
                case PreferenceValue.GDB_SR_ORDERBY_CSHOW:
                    if (desc) {
                        result = right.getScoreCShow() - left.getScoreCShow();
                    }
                    else {
                        result = left.getScoreCShow() - right.getScoreCShow();
                    }
                    break;
                case PreferenceValue.GDB_SR_ORDERBY_SPECIAL:
                    if (desc) {
                        result = right.getScoreSpeicial() - left.getScoreSpeicial();
                    }
                    else {
                        result = left.getScoreSpeicial() - right.getScoreSpeicial();
                    }
                    break;
                case PreferenceValue.GDB_SR_ORDERBY_HD:
                    if (desc) {
                        result = rhs.getHDLevel() - lhs.getHDLevel();
                    }
                    else {
                        result = lhs.getHDLevel() - rhs.getHDLevel();
                    }
                    break;
                case PreferenceValue.GDB_SR_ORDERBY_FK1:
                    if (desc) {
                        result = right.getScoreFkType1() - left.getScoreFkType1();
                    }
                    else {
                        result = left.getScoreFkType1() - right.getScoreFkType1();
                    }
                    break;
                case PreferenceValue.GDB_SR_ORDERBY_FK2:
                    if (desc) {
                        result = right.getScoreFkType2() - left.getScoreFkType2();
                    }
                    else {
                        result = left.getScoreFkType2() - right.getScoreFkType2();
                    }
                    break;
                case PreferenceValue.GDB_SR_ORDERBY_FK3:
                    if (desc) {
                        result = right.getScoreFkType3() - left.getScoreFkType3();
                    }
                    else {
                        result = left.getScoreFkType3() - right.getScoreFkType3();
                    }
                    break;
                case PreferenceValue.GDB_SR_ORDERBY_FK4:
                    if (desc) {
                        result = right.getScoreFkType4() - left.getScoreFkType4();
                    }
                    else {
                        result = left.getScoreFkType4() - right.getScoreFkType4();
                    }
                    break;
                case PreferenceValue.GDB_SR_ORDERBY_FK5:
                    if (desc) {
                        result = right.getScoreFkType5() - left.getScoreFkType5();
                    }
                    else {
                        result = left.getScoreFkType5() - right.getScoreFkType5();
                    }
                    break;
                case PreferenceValue.GDB_SR_ORDERBY_FK6:
                    if (desc) {
                        result = right.getScoreFkType6() - left.getScoreFkType6();
                    }
                    else {
                        result = left.getScoreFkType6() - right.getScoreFkType6();
                    }
                    break;
            }
            return result;
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
