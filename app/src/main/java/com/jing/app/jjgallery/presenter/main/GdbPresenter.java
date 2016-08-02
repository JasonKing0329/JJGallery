package com.jing.app.jjgallery.presenter.main;

import android.os.AsyncTask;

import com.jing.app.jjgallery.bean.StarProxy;
import com.jing.app.jjgallery.config.Configuration;
import com.jing.app.jjgallery.config.DBInfor;
import com.jing.app.jjgallery.model.main.file.FolderManager;
import com.jing.app.jjgallery.service.encrypt.EncrypterFactory;
import com.jing.app.jjgallery.service.encrypt.action.Encrypter;
import com.jing.app.jjgallery.viewsystem.main.gdb.IGdbView;
import com.jing.app.jjgallery.viewsystem.main.gdb.IStarView;
import com.jing.app.jjgallery.viewsystem.main.gdb.LetterComparator;
import com.king.service.gdb.GDBProvider;
import com.king.service.gdb.bean.Star;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Administrator on 2016/7/30 0030.
 */
public class GdbPresenter {
    private IGdbView gdbView;
    private IStarView starView;
    private GDBProvider gdbProvider;

    public GdbPresenter(IGdbView gdbView) {
        this.gdbView = gdbView;
        gdbProvider = new GDBProvider(DBInfor.GDB_DB_PATH);
    }

    public GdbPresenter(IStarView starView) {
        this.starView = starView;
        gdbProvider = new GDBProvider(DBInfor.GDB_DB_PATH);
    }

    public void loadStarList() {
        new LoadStarListTask().execute();
    }

    public void loadStar(int starId) {
        new LoadStarTask().execute(starId);
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
            Collections.sort(resultList, new LetterComparator());

            gdbView.onLoadStarList(resultList);

            super.onPostExecute(list);
        }

        @Override
        protected List<Star> doInBackground(Void... params) {
            return gdbProvider.getStars();
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
}
