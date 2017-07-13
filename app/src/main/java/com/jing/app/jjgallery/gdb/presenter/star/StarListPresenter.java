package com.jing.app.jjgallery.gdb.presenter.star;

import android.os.AsyncTask;

import com.jing.app.jjgallery.bean.http.DownloadItem;
import com.jing.app.jjgallery.bean.http.GdbCheckNewFileBean;
import com.jing.app.jjgallery.config.DBInfor;
import com.jing.app.jjgallery.gdb.GdbConstants;
import com.jing.app.jjgallery.gdb.bean.StarProxy;
import com.jing.app.jjgallery.gdb.presenter.ManageListPresenter;
import com.jing.app.jjgallery.gdb.view.list.IManageListView;
import com.jing.app.jjgallery.gdb.view.star.IStarListView;
import com.jing.app.jjgallery.service.encrypt.EncryptUtil;
import com.jing.app.jjgallery.service.http.Command;
import com.jing.app.jjgallery.service.http.GdbHttpClient;
import com.king.service.gdb.GDBProvider;
import com.king.service.gdb.bean.FavorBean;
import com.king.service.gdb.bean.Star;
import com.king.service.gdb.bean.StarCountBean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/7/12 9:35
 */
public class StarListPresenter extends ManageListPresenter {

    private GDBProvider gdbProvider;
    private GDBProvider favorProvider;

    private IStarListView starListView;

    public StarListPresenter(IManageListView view) {
        super(view);
        gdbProvider = new GDBProvider(DBInfor.GDB_DB_PATH);
        favorProvider = new GDBProvider(DBInfor.GDB_FAVOR_DB_PATH);
    }

    public void setStarListView(IStarListView starListView) {
        this.starListView = starListView;
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
                        view.onRequestFail();
                    }

                    @Override
                    public void onNext(GdbCheckNewFileBean bean) {
                        view.onCheckPass(bean.isStarExisted(), bean.getStarItems());
                    }
                });
    }

    /**
     * 检查已有图片的star，将其过滤掉
     *
     * @param downloadList 服务端提供的下载列表
     * @param existedList  已存在的下载内容，不能为null
     * @return 未存在的下载内容
     */
    public List<DownloadItem> pickStarToDownload(List<DownloadItem> downloadList, List<DownloadItem> existedList) {
        List<DownloadItem> list = new ArrayList<>();
        for (DownloadItem item : downloadList) {
            String name = item.getName().substring(0, item.getName().lastIndexOf("."));
            String path = EncryptUtil.getEncryptStarPath(name);
            if (path == null) {
                list.add(item);
            } else {
                item.setPath(path);
                existedList.add(item);
            }
        }
        return list;
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

    public void queryIndicatorData() {
        new QueryIndicatorTask().execute();
    }

    public void saveFavor(FavorBean bean) {
        favorProvider.saveFavor(bean);
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

            starListView.onLoadStarList(resultList);

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

    private class QueryIndicatorTask extends AsyncTask<Object, Void, StarCountBean> {
        @Override
        protected void onPostExecute(StarCountBean bean) {

            starListView.onStarCountLoaded(bean);
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

}
