package com.jing.app.jjgallery.gdb.presenter.record;

import android.os.AsyncTask;
import android.text.TextUtils;

import com.jing.app.jjgallery.config.DBInfor;
import com.jing.app.jjgallery.gdb.bean.Star3W;
import com.jing.app.jjgallery.gdb.bean.StarProxy;
import com.jing.app.jjgallery.gdb.model.GdbImageProvider;
import com.jing.app.jjgallery.gdb.view.record.IRecordView;
import com.king.service.gdb.GDBProvider;
import com.king.service.gdb.bean.GDBProperites;
import com.king.service.gdb.bean.RecordThree;
import com.king.service.gdb.bean.Star;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/7/13 10:40
 */
public class RecordPresenter {

    private IRecordView recordView;
    private GDBProvider gdbProvider;

    private List<Star3W> star3wList;

    public RecordPresenter(IRecordView recordView) {
        this.recordView = recordView;
        gdbProvider = new GDBProvider(DBInfor.GDB_DB_PATH);
    }

    public void loadStar(int starId) {
        new LoadStarTask().execute(starId);
    }

    private class LoadStarTask extends AsyncTask<Integer, Void, StarProxy> {
        @Override
        protected void onPostExecute(StarProxy star) {

            recordView.onStarLoaded(star);

            super.onPostExecute(star);
        }

        @Override
        protected StarProxy doInBackground(Integer... params) {
            // load star object and star's records
            Star star = gdbProvider.getStarRecords(params[0]);
            StarProxy proxy = new StarProxy();
            proxy.setStar(star);

            // load image path of star
            String headPath = GdbImageProvider.getStarRandomPath(star.getName(), null);
            proxy.setImagePath(headPath);
            return proxy;
        }
    }

    private List<Star3W> parseStar3WList(RecordThree record) {
        List<Star3W> knownList = new ArrayList<>();
        List<Star> stars = record.getStarTopList();
        if (stars != null && stars.size() > 0) {
            String[] scores = null;
            String[] scoresC = null;
            if (!TextUtils.isEmpty(record.getScoreTop())) {
                scores = record.getScoreTop().split(",");
            }
            if (!TextUtils.isEmpty(record.getScoreTopC())) {
                scoresC = record.getScoreTopC().split(",");
            }
            for (int i = 0; i < stars.size(); i ++) {
                try {
                    Star3W star3w = new Star3W();
                    knownList.add(star3w);
                    star3w.setStar(stars.get(i));
                    star3w.setFlag(GDBProperites.STAR_3W_FLAG_TOP);
                    star3w.setScore(Integer.parseInt(scores[i]));
                    star3w.setScoreC(Integer.parseInt(scoresC[i]));
                } catch (Exception e) {
                    continue;
                }
            }
        }

        stars = record.getStarBottomList();
        if (stars != null && stars.size() > 0) {
            String[] scores = null;
            String[] scoresC = null;
            if (!TextUtils.isEmpty(record.getScoreBottom())) {
                scores = record.getScoreBottom().split(",");
            }
            if (!TextUtils.isEmpty(record.getScoreBottomC())) {
                scoresC = record.getScoreBottomC().split(",");
            }
            for (int i = 0; i < stars.size(); i ++) {
                try {
                    Star3W star3w = new Star3W();
                    knownList.add(star3w);
                    star3w.setStar(stars.get(i));
                    star3w.setFlag(GDBProperites.STAR_3W_FLAG_BOTTOM);
                    star3w.setScore(Integer.parseInt(scores[i]));
                    star3w.setScoreC(Integer.parseInt(scoresC[i]));
                } catch (Exception e) {
                    continue;
                }
            }
        }

        stars = record.getStarMixList();
        if (stars != null && stars.size() > 0) {
            String[] scores = null;
            String[] scoresC = null;
            if (!TextUtils.isEmpty(record.getScoreMix())) {
                scores = record.getScoreMix().split(",");
            }
            if (!TextUtils.isEmpty(record.getScoreMixC())) {
                scoresC = record.getScoreMixC().split(",");
            }
            for (int i = 0; i < stars.size(); i ++) {
                try {
                    Star3W star3w = new Star3W();
                    knownList.add(star3w);
                    star3w.setStar(stars.get(i));
                    star3w.setFlag(GDBProperites.STAR_3W_FLAG_MIX);
                    star3w.setScore(Integer.parseInt(scores[i]));
                    star3w.setScoreC(Integer.parseInt(scoresC[i]));
                } catch (Exception e) {
                    continue;
                }
            }
        }
        return knownList;
    }

    public List<Star3W> getStar3WList(RecordThree record) {
        if (star3wList == null)  {
            star3wList = parseStar3WList(record);
        }
        return star3wList;
    }

}
