package com.jing.app.jjgallery.gdb.presenter.game;

import com.jing.app.jjgallery.gdb.model.game.CrossData;
import com.jing.app.jjgallery.gdb.model.game.CrossDetailData;
import com.jing.app.jjgallery.gdb.view.game.IBattleDetailView;
import com.jing.app.jjgallery.gdb.view.game.battlecross.ICrossView;
import com.king.service.gdb.game.bean.BattleResultBean;
import com.king.service.gdb.game.bean.CoachBean;
import com.king.service.gdb.game.bean.CrossBean;
import com.king.service.gdb.game.bean.CrossResultBean;
import com.king.service.gdb.game.bean.PlayerBean;
import com.king.service.gdb.game.bean.SeasonBean;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/2/14 16:57
 */
public class CrossPresenter extends GamePresenter {
    private ICrossView crossView;
    private IBattleDetailView detailView;

    public CrossPresenter(ICrossView crossView) {
        this.crossView = crossView;
    }

    public void setDetailView(IBattleDetailView detailView) {
        this.detailView = detailView;
    }

    public PlayerBean getPlayer(CrossDetailData detailData, int playerId) {
        if (detailData.getPlayerListTop() != null) {
            List<PlayerBean> list = detailData.getPlayerListTop();
            for (PlayerBean bean:list) {
                if (bean.getId() == playerId) {
                    return bean;
                }
            }
        }
        if (detailData.getPlayerListBottom() != null) {
            List<PlayerBean> list = detailData.getPlayerListBottom();
            for (PlayerBean bean:list) {
                if (bean.getId() == playerId) {
                    return bean;
                }
            }
        }
        return null;
    }

    public void loadCrossBasics(int seasonId) {
        CrossData crossData = new CrossData();
        // load SeasonBean
        SeasonBean season = gameProvider.getSeasonById(seasonId);
        crossData.setSeason(season);
        // load 4 CoachBean
        CoachBean coach = gameProvider.getCoachById(season.getCoachId1());
        crossData.setCoach1(coach);
        coach = gameProvider.getCoachById(season.getCoachId2());
        crossData.setCoach2(coach);
        coach = gameProvider.getCoachById(season.getCoachId3());
        crossData.setCoach3(coach);
        coach = gameProvider.getCoachById(season.getCoachId4());
        crossData.setCoach4(coach);
        crossView.onBattleDataLoaded(crossData);
    }


    public void loadDetails(final CrossDetailData detailData) {
        Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(Subscriber<? super Object> subscriber) {

                // load players
                // coach1 is in index 0~3, coach2 is in index 4~7
                List<PlayerBean> playerListTop = new ArrayList<>();
                List<PlayerBean> playerListBottom = new ArrayList<>();
                detailData.setPlayerListTop(playerListTop);
                detailData.setPlayerListBottom(playerListBottom);
                // coach1 top, top 4 players
                List<BattleResultBean> list = gameProvider.getBattleResultList(detailData.getSeason().getId()
                        , detailData.getCoach().getId(), 4, 1);
                if (list != null && list.size() > 0) {
                    for (BattleResultBean bean : list) {
                        playerListTop.add(gameProvider.getPlayerById(bean.getPlayerId()));
                    }
                }
                // coach1 bottom, top 4 players
                list = gameProvider.getBattleResultList(detailData.getSeason().getId()
                        , detailData.getCoach().getId(), 4, 0);
                if (list != null && list.size() > 0) {
                    for (BattleResultBean bean : list) {
                        playerListBottom.add(gameProvider.getPlayerById(bean.getPlayerId()));
                    }
                }
                // coach2 top, top 4 players
                list = gameProvider.getBattleResultList(detailData.getSeason().getId()
                        , detailData.getCoach2().getId(), 4, 1);
                if (list != null && list.size() > 0) {
                    for (BattleResultBean bean : list) {
                        playerListTop.add(gameProvider.getPlayerById(bean.getPlayerId()));
                    }
                }
                // coach1 bottom, top 4 players
                list = gameProvider.getBattleResultList(detailData.getSeason().getId()
                        , detailData.getCoach2().getId(), 4, 0);
                if (list != null && list.size() > 0) {
                    for (BattleResultBean bean : list) {
                        playerListBottom.add(gameProvider.getPlayerById(bean.getPlayerId()));
                    }
                }

                // load battle beans
                List<CrossBean> crossList = gameProvider.getCrossList(detailData.getSeason().getId()
                        , detailData.getCoach().getId(), detailData.getCoach2().getId());
                detailData.setBattleList(crossList);

                // load player image path
                loadPlayerImageMap();

                subscriber.onNext(null);
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Object data) {
                        detailView.onDetailLoaded();
                    }
                });
    }

    public void saveCrossBean(CrossBean bean) {
        gameProvider.updateCrossBean(bean);
    }

    public void deleteCrossBean(CrossBean bean) {
        gameProvider.deleteCrossBean(bean.getId());
    }

    public boolean isCrossResultExist(int seasonId, int coachId1, int coachId2) {
        return gameProvider.isCrossResultExist(seasonId, coachId1, coachId2);
    }

    public void deleteCrossResults(int seasonId, int coachId1, int coachId2) {
        gameProvider.deleteCrossResults(seasonId, coachId1, coachId2);
    }

    public boolean saveCrossResultBeans(List<CrossResultBean> datas, int upNumber) {
        return gameProvider.saveCrossResultBeans(datas, upNumber);
    }
}
