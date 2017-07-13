package com.jing.app.jjgallery.gdb.presenter.star;

import android.os.AsyncTask;

import com.jing.app.jjgallery.config.DBInfor;
import com.jing.app.jjgallery.config.PreferenceValue;
import com.jing.app.jjgallery.gdb.bean.StarProxy;
import com.jing.app.jjgallery.gdb.model.RecordComparator;
import com.jing.app.jjgallery.gdb.view.star.IStarView;
import com.jing.app.jjgallery.service.encrypt.EncryptUtil;
import com.king.service.gdb.GDBProvider;
import com.king.service.gdb.bean.Record;
import com.king.service.gdb.bean.Star;

import java.util.Collections;
import java.util.List;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/7/13 10:35
 */
public class StarPresenter {

    private IStarView starView;
    private GDBProvider gdbProvider;

    public StarPresenter(IStarView starView) {
        this.starView = starView;
        gdbProvider = new GDBProvider(DBInfor.GDB_DB_PATH);
    }

    public void loadStar(int starId) {
        new LoadStarTask().execute(starId);
    }

    public void sortRecords(List<Record> recordList, int sortMode, boolean desc) {
        if (sortMode != PreferenceValue.GDB_SR_ORDERBY_NONE) {
            Collections.sort(recordList, new RecordComparator(sortMode, desc));
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

}
