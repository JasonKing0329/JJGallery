package com.jing.app.jjgallery.gdb.presenter.record;

import android.os.AsyncTask;

import com.jing.app.jjgallery.config.DBInfor;
import com.jing.app.jjgallery.gdb.bean.StarProxy;
import com.jing.app.jjgallery.gdb.view.record.IRecordView;
import com.jing.app.jjgallery.service.encrypt.EncryptUtil;
import com.king.service.gdb.GDBProvider;
import com.king.service.gdb.bean.Star;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/7/13 10:40
 */
public class RecordPresenter {

    private IRecordView recordView;
    private GDBProvider gdbProvider;

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
            String headPath = EncryptUtil.getEncryptStarPath(star.getName());
            proxy.setImagePath(headPath);
            return proxy;
        }
    }

}
