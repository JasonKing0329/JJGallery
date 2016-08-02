package com.jing.app.jjgallery.presenter.main;

import android.os.AsyncTask;

import com.jing.app.jjgallery.bean.StarProxy;
import com.jing.app.jjgallery.config.Configuration;
import com.jing.app.jjgallery.config.DBInfor;
import com.jing.app.jjgallery.config.PreferenceValue;
import com.jing.app.jjgallery.model.main.file.FolderManager;
import com.jing.app.jjgallery.service.encrypt.EncrypterFactory;
import com.jing.app.jjgallery.service.encrypt.action.Encrypter;
import com.jing.app.jjgallery.viewsystem.main.gdb.IGdbRecordListView;
import com.jing.app.jjgallery.viewsystem.main.gdb.IGdbStarListView;
import com.jing.app.jjgallery.viewsystem.main.gdb.IStarView;
import com.king.service.gdb.GDBProvider;
import com.king.service.gdb.bean.Record;
import com.king.service.gdb.bean.RecordSingleScene;
import com.king.service.gdb.bean.Star;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Administrator on 2016/7/30 0030.
 */
public class GdbPresenter {
    private IGdbStarListView gdbStarListView;
    private IGdbRecordListView gdbRecordListView;
    private IStarView starView;
    private GDBProvider gdbProvider;

    public GdbPresenter(IGdbStarListView gdbStarListView) {
        this.gdbStarListView = gdbStarListView;
        gdbProvider = new GDBProvider(DBInfor.GDB_DB_PATH);
    }

    public GdbPresenter(IGdbRecordListView gdbRecordListView) {
        this.gdbRecordListView = gdbRecordListView;
        gdbProvider = new GDBProvider(DBInfor.GDB_DB_PATH);
    }

    public GdbPresenter(IStarView starView) {
        this.starView = starView;
        gdbProvider = new GDBProvider(DBInfor.GDB_DB_PATH);
    }

    public void loadStarList() {
        new LoadStarListTask().execute();
    }

    public void loadRecordList(int sortMode) {
        new LoadRecordListTask().execute(sortMode);
    }

    public void loadStar(int starId) {
        new LoadStarTask().execute(starId);
    }

    public void sortRecords(List<Record> recordList, int sortMode) {
        if (sortMode != PreferenceValue.GDB_SR_ORDERBY_NONE) {
            Collections.sort(recordList, new RecordComparator(sortMode));
        }
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

            gdbStarListView.onLoadStarList(resultList);

            super.onPostExecute(list);
        }

        @Override
        protected List<Star> doInBackground(Void... params) {
            return gdbProvider.getStars();
        }
    }

    private class LoadRecordListTask extends AsyncTask<Integer, Void, List<Record>> {
        @Override
        protected void onPostExecute(List<Record> list) {

            gdbRecordListView.onLoadRecordList(list);

            super.onPostExecute(list);
        }

        @Override
        protected List<Record> doInBackground(Integer... params) {
            List<Record> list = gdbProvider.getAllRecords();
            sortRecords(list, params[0]);
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
            Encrypter encrypter = EncrypterFactory.create();
            List<String> list = new FolderManager().loadPathList(Configuration.GDB_IMG_STAR);
            for (String path:list) {
                String name = encrypter.decipherOriginName(new File(path));
                String preName = name.split("\\.")[0];
                if (preName.equals(star.getName())) {
                    proxy.setImagePath(path);
                    break;
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
        public RecordComparator(int sortMode) {
            this.sortMode = sortMode;
        }

        @Override
        public int compare(Record lhs, Record rhs) {
            int result = 0;
            switch (sortMode) {
                case PreferenceValue.GDB_SR_ORDERBY_NAME:// asc
                    result = lhs.getName().toLowerCase().compareTo(rhs.getName().toLowerCase());
                    break;
                case PreferenceValue.GDB_SR_ORDERBY_DATE:// desc
                    result = (int) (rhs.getLastModifyTime() - lhs.getLastModifyTime());
                    break;
                case PreferenceValue.GDB_SR_ORDERBY_SCORE:// desc
                    result = rhs.getScore() - lhs.getScore();
                    break;
                case PreferenceValue.GDB_SR_ORDERBY_FK:// desc
                    int leftScore = 0;
                    if (lhs instanceof RecordSingleScene) {
                        leftScore = ((RecordSingleScene) lhs).getScoreFk();
                    }
                    int rightScore = 0;
                    if (rhs instanceof RecordSingleScene) {
                        rightScore = ((RecordSingleScene) rhs).getScoreFk();
                    }
                    result = rightScore - leftScore;
                    break;
                case PreferenceValue.GDB_SR_ORDERBY_CUM:// desc
                    leftScore = 0;
                    if (lhs instanceof RecordSingleScene) {
                        leftScore = ((RecordSingleScene) lhs).getScoreCum();
                    }
                    rightScore = 0;
                    if (rhs instanceof RecordSingleScene) {
                        rightScore = ((RecordSingleScene) rhs).getScoreCum();
                    }
                    result = rightScore - leftScore;
                    break;
            }
            return result;
        }
    }
}
