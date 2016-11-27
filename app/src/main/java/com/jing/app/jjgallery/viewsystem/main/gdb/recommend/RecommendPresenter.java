package com.jing.app.jjgallery.viewsystem.main.gdb.recommend;

import android.os.AsyncTask;

import com.jing.app.jjgallery.config.Configuration;
import com.jing.app.jjgallery.config.DBInfor;
import com.jing.app.jjgallery.model.main.file.FolderManager;
import com.jing.app.jjgallery.service.encrypt.EncrypterFactory;
import com.jing.app.jjgallery.service.encrypt.action.Encrypter;
import com.king.service.gdb.GDBProvider;
import com.king.service.gdb.bean.Record;

import java.io.File;
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

    private Record previousRecord, currentRecord, nextRecord;
    /**
     * 在loadAllRecords的时候遍历gdb/record目录，解析出所有name对应的path
     */
    private static Map<String, String> recordImageMap;

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
            Random random = new Random();
            int index = Math.abs(random.nextInt()) % recordList.size();
            currentRecord = recordList.get(index);
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
            Random random = new Random();
            int index = Math.abs(random.nextInt()) % recordList.size();
            currentRecord = recordList.get(index);
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

    public Record getCurrentRecord() {
        return currentRecord;
    }

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
