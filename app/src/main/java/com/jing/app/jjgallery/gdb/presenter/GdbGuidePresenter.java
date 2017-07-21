package com.jing.app.jjgallery.gdb.presenter;

import android.os.AsyncTask;

import com.jing.app.jjgallery.config.DBInfor;
import com.jing.app.jjgallery.gdb.bean.StarProxy;
import com.jing.app.jjgallery.gdb.bean.recommend.FilterBean;
import com.jing.app.jjgallery.gdb.bean.recommend.FilterModel;
import com.jing.app.jjgallery.gdb.model.GdbImageProvider;
import com.jing.app.jjgallery.gdb.view.home.GHomeBean;
import com.jing.app.jjgallery.gdb.view.home.IHomeView;
import com.jing.app.jjgallery.gdb.view.recommend.IRecommend;
import com.jing.app.jjgallery.util.DebugLog;
import com.jing.app.jjgallery.gdb.GdbConstants;
import com.king.service.gdb.GDBProvider;
import com.king.service.gdb.bean.FavorBean;
import com.king.service.gdb.bean.GDBProperites;
import com.king.service.gdb.bean.Record;
import com.king.service.gdb.bean.RecordOneVOne;
import com.king.service.gdb.bean.Star;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

/**
 * Created by Administrator on 2016/11/27 0027.
 * the presenter of gdb guide view
 */

public class GdbGuidePresenter {

    private final int NUM_LOAD_MORE = 10;

    private IRecommend recommendView;
    private GDBProvider gdbProvider;
    private List<Record> recordList;

    private GDBProvider favorProvider;
    /**
     * 一共缓存3个推荐，previous, next and current
     */
    private Record previousRecord, currentRecord, nextRecord;
    /**
     * 过滤器
     */
    private FilterModel filterModel;

    public GdbGuidePresenter() {
        gdbProvider = new GDBProvider(DBInfor.GDB_DB_PATH);
        favorProvider = new GDBProvider(DBInfor.GDB_FAVOR_DB_PATH);
    }

    public GdbGuidePresenter(IRecommend recommendView) {
        this();
        this.recommendView = recommendView;
    }

    public void setRecommendView(IRecommend recommendView) {
        this.recommendView = recommendView;
    }

    public void initialize() {
        new LoadRecordListTask().execute();
    }

    public void recommendNext() {
        if (nextRecord == null) {
            previousRecord = currentRecord;
            currentRecord = newRecord();
            nextRecord = null;
            recommendView.onRecordRecommand(currentRecord);
        }
        else {
            recommendView.onRecordRecommand(nextRecord);
            previousRecord = currentRecord;
            currentRecord = nextRecord;
            nextRecord = null;
        }
    }

    public void recommendPrevious() {
        if (previousRecord == null) {
            nextRecord = currentRecord;
            currentRecord = newRecord();
            previousRecord = null;
            recommendView.onRecordRecommand(currentRecord);
        }
        else {
            recommendView.onRecordRecommand(previousRecord);
            nextRecord = currentRecord;
            currentRecord = previousRecord;
            previousRecord = null;
        }
    }

    /**
     * 获得新记录
     * @return
     */
    public Record newRecord() {
        if (recordList == null || recordList.size() == 0) {
            return null;
        }
        // 没有设置过滤器的情况，直接随机位置
        if (filterModel == null) {
            Random random = new Random();
            int index = Math.abs(random.nextInt()) % recordList.size();
            return recordList.get(index);
        }
        else {// 打乱当前所有记录，选出第一个符合过滤器条件的记录
            Collections.shuffle(recordList);
            boolean pass;
            for (Record record:recordList) {
                pass = true;
                // 记录是NR并且过滤器勾选了支持NR才判定为通过
                if (record.getHDLevel() == GDBProperites.RECORD_HD_NR && filterModel.isSupportNR()) {
                    pass = true;
                }
                // 普通记录，以及是NR但是过滤器没有勾选NR，需要检测其他过滤项
                else {
                    for (int i = 0; i < filterModel.getList().size(); i ++) {
                        boolean result = checkPassFilterItem(record, filterModel.getList().get(i));
                        pass = pass && result;
                    }
                }
                if (pass) {
                    return record;
                }
            }
            return null;
        }
    }

    /**
     * 检查是否通过过滤项
     * @param record
     * @param filterBean
     * @return
     */
    private boolean checkPassFilterItem(Record record, FilterBean filterBean) {
        int min = filterBean.getMin();
        int max = filterBean.getMax();
        // 只设置了min，没有设置max
        if (filterBean.getMax() == 0) {
            max = Integer.MAX_VALUE;
        }

        // 如果没勾上，直接返回符合条件
        if (!filterBean.isEnable()
                // 也没有设置条件
                || min == 0 && max == Integer.MAX_VALUE
                // min 大于了 max是不合理情况，也视为符合条件
                || min > max) {
            return true;
        }

        int targetScore = getTargetScore(record, filterBean.getKeyword());
        DebugLog.e("targetScore = " + targetScore);
        return targetScore >= min && targetScore <= max;
    }

    /**
     * 根据过滤器的keyword判断score对应的record参数
     *
     * @param record
     * @param keyword
     * @return
     */
    private int getTargetScore(Record record, String keyword) {
        if (keyword.equals(GdbConstants.FILTER_KEY_SCORE)) {
            return record.getScore();
        }
        else if (keyword.equals(GdbConstants.FILTER_KEY_SCORE_FEEL)) {
            return record.getScoreFeel();
        }
        else if (keyword.equals(GdbConstants.FILTER_KEY_SCORE_STORY)) {
            return record.getScoreStory();
        }
        else if (keyword.equals(GdbConstants.FILTER_KEY_SCORE_DEPRECATED)) {
            return record.getDeprecated();
        }
        else if (record instanceof RecordOneVOne) {
            RecordOneVOne oRecord = (RecordOneVOne) record;
            if (keyword.equals(GdbConstants.FILTER_KEY_SCORE_CUM)) {
                return oRecord.getScoreCum();
            }
            else if (keyword.equals(GdbConstants.FILTER_KEY_SCORE_FK)) {
                return oRecord.getScoreFk();
            }
            else if (keyword.equals(GdbConstants.FILTER_KEY_SCORE_STAR1)) {
                return oRecord.getScoreStar1();
            }
            else if (keyword.equals(GdbConstants.FILTER_KEY_SCORE_STAR2)) {
                return oRecord.getScoreStar2();
            }
            else if (keyword.equals(GdbConstants.FILTER_KEY_SCORE_STAR)) {
                return oRecord.getScoreStar();
            }
            else if (keyword.equals(GdbConstants.FILTER_KEY_SCORE_STARCC1)) {
                return oRecord.getScoreStar1();
            }
            else if (keyword.equals(GdbConstants.FILTER_KEY_SCORE_STARCC2)) {
                return oRecord.getScoreStar2();
            }
            else if (keyword.equals(GdbConstants.FILTER_KEY_SCORE_BJOB)) {
                return oRecord.getScoreBJob();
            }
            else if (keyword.equals(GdbConstants.FILTER_KEY_SCORE_BAREBACK)) {
                return oRecord.getScoreNoCond();
            }
            else if (keyword.equals(GdbConstants.FILTER_KEY_SCORE_RHYTHM)) {
                return oRecord.getScoreRhythm();
            }
            else if (keyword.equals(GdbConstants.FILTER_KEY_SCORE_RIM)) {
                return oRecord.getScoreRim();
            }
            else if (keyword.equals(GdbConstants.FILTER_KEY_SCORE_SCECE)) {
                return oRecord.getScoreScene();
            }
            else if (keyword.equals(GdbConstants.FILTER_KEY_SCORE_CSHOW)) {
                return oRecord.getScoreCShow();
            }
            else if (keyword.equals(GdbConstants.FILTER_KEY_SCORE_SPECIAL)) {
                return oRecord.getScoreSpeicial();
            }
            else if (keyword.equals(GdbConstants.FILTER_KEY_SCORE_FOREPLAY)) {
                return oRecord.getScoreForePlay();
            }
            else if (keyword.equals(GdbConstants.FILTER_KEY_SCORE_FK_SIT_FACE)) {
                return oRecord.getScoreFkType1();
            }
            else if (keyword.equals(GdbConstants.FILTER_KEY_SCORE_FK_SIT_BACK)) {
                return oRecord.getScoreFkType2();
            }
            else if (keyword.equals(GdbConstants.FILTER_KEY_SCORE_FK_STAND_FACE)) {
                return oRecord.getScoreFkType3();
            }
            else if (keyword.equals(GdbConstants.FILTER_KEY_SCORE_FK_STAND_BACK)) {
                return oRecord.getScoreFkType4();
            }
            else if (keyword.equals(GdbConstants.FILTER_KEY_SCORE_FK_SIDE)) {
                return oRecord.getScoreFkType5();
            }
            else if (keyword.equals(GdbConstants.FILTER_KEY_SCORE_FK_SPECIAL)) {
                return oRecord.getScoreFkType6();
            }
        }
        return 0;
    }

    public Record getCurrentRecord() {
        return currentRecord;
    }

    /**
     * 注入过滤器
     * 注入时清空当前缓存的previous, next
     * @param filterModel
     */
    public void setFilterModel(FilterModel filterModel) {
        this.filterModel = filterModel;
        previousRecord = null;
        nextRecord = null;
        currentRecord = null;
    }

    /**
     * 加载全部记录
     */
    private class LoadRecordListTask extends AsyncTask<Object, Void, List<Record>> {
        @Override
        protected void onPostExecute(List<Record> list) {

            if (recommendView != null) {
                recommendView.onRecordsLoaded(list);
            }

            super.onPostExecute(list);
        }

        @Override
        protected List<Record> doInBackground(Object... params) {
            recordList = gdbProvider.getAllRecords();
            return recordList;
        }
    }

    /**
     * get image path on disk by the original name of record
     * @param recordName
     * @return
     */
    public static String getRecordPath(String recordName) {
        String result = GdbImageProvider.getRecordRandomPath(recordName, null);
        return result;
    }

    public List<Record> getLatestRecord(int number) {
        return gdbProvider.getLatestRecords(0, number);
    }

    /**
     * 获取home主页数据
     */
    public void loadHomeData(IHomeView homeView) {
        new LoadHomeDataTask(homeView).execute();
    }

    /**
     * home 主页列表加载更多数据
     * @param from 数据库结果集limit from, number
     * @param homeView
     */
    public void loadMore(int from, IHomeView homeView) {
        LoadMoreTask task = new LoadMoreTask(homeView);
        // 采用任务队列保证单位时间内只执行一次loadMore
        addToTask(task, from);
    }

    /**
     * 丢弃快速到达的相同task，保证一次只load限定的数量
     * @param task
     * @param from
     */
    private synchronized void addToTask(LoadMoreTask task, int from) {
        // 队列为空方可执行
        if (executeQueue.size() == 0) {
            // 入队
            executeQueue.offer(task);
            // 执行结束后出队
            task.execute(from);
        }
    }

    private Queue<LoadMoreTask> executeQueue = new LinkedList<>();

    /**
     * 加载全部记录
     */
    private class LoadHomeDataTask extends AsyncTask<Object, Void, GHomeBean> {

        private final IHomeView homeView;

        public LoadHomeDataTask(IHomeView homeView) {
            this.homeView = homeView;
        }

        @Override
        protected void onPostExecute(GHomeBean data) {

            if (homeView != null) {
                homeView.onHomeDataLoaded(data);
            }

            super.onPostExecute(data);
        }

        @Override
        protected GHomeBean doInBackground(Object... params) {
            GHomeBean homeBean = new GHomeBean();
            homeBean.setRecordList(getLatestRecord(NUM_LOAD_MORE));

//            List<Record> randomList = gdbProvider.getRandomRecords(1);
//            homeBean.setCoverRecord(randomList.get(0));

            List<StarProxy> starList = new ArrayList<>();

            // 获取全部favor，打乱顺序后由主页挑出前N个使用
//            List<FavorBean> favorList = favorProvider.getTopFavors(3);
            List<FavorBean> favorList = favorProvider.getFavors();
            Collections.shuffle(favorList);
            for (int i = 0; i < favorList.size(); i ++) {
                StarProxy proxy = new StarProxy();
                Star star = gdbProvider.queryStarById(favorList.get(i).getStarId());
                proxy.setStar(star);
                proxy.setImagePath(GdbImageProvider.getStarRandomPath(star.getName(), null));
                proxy.setFavor(favorList.get(i).getFavor());
                starList.add(proxy);
            }
            homeBean.setStarList(starList);
            return homeBean;
        }
    }

    /**
     * 加载全部记录
     */
    private class LoadMoreTask extends AsyncTask<Integer, Void, List<Record>> {

        private final IHomeView homeView;

        public LoadMoreTask(IHomeView homeView) {
            this.homeView = homeView;
        }

        @Override
        protected void onPostExecute(List<Record> list) {

            if (homeView != null) {
                homeView.onMoreRecordsLoaded(list);
            }

            // 任务执行完成后清空任务队列
            executeQueue.poll();
            super.onPostExecute(list);
        }

        @Override
        protected List<Record> doInBackground(Integer... params) {
            List<Record> list = gdbProvider.getLatestRecords(params[0], NUM_LOAD_MORE);
            return list;
        }
    }

}
