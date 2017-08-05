package com.jing.app.jjgallery.gdb.presenter.star;

import com.jing.app.jjgallery.config.DBInfor;
import com.jing.app.jjgallery.gdb.bean.StarProxy;
import com.jing.app.jjgallery.gdb.model.GdbImageProvider;
import com.jing.app.jjgallery.gdb.view.star.IStarSwipeView;
import com.king.service.gdb.GDBProvider;
import com.king.service.gdb.bean.FavorBean;
import com.king.service.gdb.bean.Star;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/8/4 11:20
 */
public class StarSwipePresenter {

    private final int LOAD_NUM = 4;

    private IStarSwipeView swipeView;
    private GDBProvider gdbProvider;
    private GDBProvider favorProvider;

    private Map<Integer, FavorBean> favorMap;

    public StarSwipePresenter(IStarSwipeView swipeView) {
        this.swipeView = swipeView;
        gdbProvider = new GDBProvider(DBInfor.GDB_DB_PATH);
        favorProvider = new GDBProvider(DBInfor.GDB_FAVOR_DB_PATH);
    }

    public void loadNewStars() {
        Observable.create(new Observable.OnSubscribe<List<StarProxy>>() {
            @Override
            public void call(Subscriber<? super List<StarProxy>> subscriber) {

                // query favor map
                if (favorMap == null) {
                    favorMap = new HashMap<>();
                    List<FavorBean> list = favorProvider.getFavors();
                    for (FavorBean bean:list) {
                        favorMap.put(bean.getStarId(), bean);
                    }
                }

                // load LOAD_NUM stars
                List<Star> starList = gdbProvider.getRandomStars(LOAD_NUM);
                List<StarProxy> slist = new ArrayList<>();

                for (Star star:starList) {

                    // load records for each star
                    // 很奇怪如果是从这里装配后，后面onStarLoaded之后执行的代码调试的时候也是对的，但SwipeFlingAdapterView就是显示不出来
                    gdbProvider.loadStarRecords(star);

                    StarProxy proxy = new StarProxy();
                    proxy.setStar(star);
                    // load image path
                    proxy.setImagePath(GdbImageProvider.getStarRandomPath(star.getName(), null));
                    // relate favor bean to star
                    FavorBean bean = favorMap.get(star.getId());
                    if (bean == null) {
                        proxy.setFavor(0);
                    }
                    else {
                        proxy.setFavor(bean.getFavor());
                        proxy.setFavorBean(bean);
                    }
                    slist.add(proxy);
                }

                subscriber.onNext(slist);
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<List<StarProxy>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<StarProxy> list) {
                        swipeView.onStarLoaded(list);
                    }
                });
    }

    public void saveFavor(FavorBean bean) {
        favorProvider.saveFavor(bean);
        gdbProvider.saveFavor(bean);
    }

}
