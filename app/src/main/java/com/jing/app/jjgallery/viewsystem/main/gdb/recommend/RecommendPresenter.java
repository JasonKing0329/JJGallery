package com.jing.app.jjgallery.viewsystem.main.gdb.recommend;

import android.os.AsyncTask;

import com.jing.app.jjgallery.config.Configuration;
import com.jing.app.jjgallery.config.DBInfor;
import com.jing.app.jjgallery.model.main.file.FolderManager;
import com.jing.app.jjgallery.service.encrypt.EncrypterFactory;
import com.jing.app.jjgallery.service.encrypt.action.Encrypter;
import com.jing.app.jjgallery.util.DebugLog;
import com.jing.app.jjgallery.viewsystem.main.gdb.GdbConstants;
import com.king.service.gdb.GDBProvider;
import com.king.service.gdb.bean.Record;
import com.king.service.gdb.bean.RecordOneVOne;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by Administrator on 2016/11/27 0027.
 */

public class RecommendPresenter {

    private IRecommend recommendView;
    private GDBProvider gdbProvider;
    private static Encrypter encrypter;
    private List<Record> recordList;

    /**
     * 一共缓存3个推荐，previous, next and current
     */
    private Record previousRecord, currentRecord, nextRecord;
    /**
     * 在loadAllRecords的时候遍历gdb/record目录，解析出所有name对应的path
     */
    private static Map<String, String> recordImageMap;
    /**
     * 过滤器
     */
    private FilterModel filterModel;

    public RecommendPresenter(IRecommend recommendView) {
        this.recommendView = recommendView;
        gdbProvider = new GDBProvider(DBInfor.GDB_DB_PATH);
        encrypter = EncrypterFactory.create();
        recordImageMap = new HashMap<>();
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
    private Record newRecord() {
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
                for (int i = 0; i < filterModel.getList().size(); i ++) {
                    boolean result = checkPassFilterItem(record, filterModel.getList().get(i));
                    pass = pass && result;
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

            recommendView.onRecordsLoaded(list);

            super.onPostExecute(list);
        }

        @Override
        protected List<Record> doInBackground(Object... params) {
            recordList = gdbProvider.getAllRecords();

            // load available images for records
            List<String> pathList = new FolderManager().loadPathList(Configuration.GDB_IMG_RECORD);
            for (String path:pathList) {
                String name = encrypter.decipherOriginName(new File(path));
                String preName = name.substring(0, name.lastIndexOf("."));
                recordImageMap.put(preName, path);
            }

            return recordList;
        }
    }

    public String getRecordPath(String recordName) {
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

}
