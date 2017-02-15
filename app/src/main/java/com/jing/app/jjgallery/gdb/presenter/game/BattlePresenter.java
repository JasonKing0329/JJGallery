package com.jing.app.jjgallery.gdb.presenter.game;

import com.jing.app.jjgallery.gdb.model.game.BattleData;
import com.jing.app.jjgallery.gdb.model.game.BattleDetailData;
import com.jing.app.jjgallery.gdb.view.game.battlecross.IBattleView;
import com.jing.app.jjgallery.gdb.view.game.IBattleDetailView;
import com.king.service.gdb.game.bean.BattleBean;
import com.king.service.gdb.game.bean.BattleResultBean;
import com.king.service.gdb.game.bean.CoachBean;
import com.king.service.gdb.game.bean.GroupBean;
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
 * <p/>创建时间: 2017/2/8 11:14
 */
public class BattlePresenter extends GamePresenter {

    private IBattleView battleView;
    private IBattleDetailView detailView;

    public BattlePresenter(IBattleView battleView) {
        this.battleView = battleView;
    }

    public void setDetailView(IBattleDetailView detailView) {
        this.detailView = detailView;
    }

    public void loadBattleBasics(final int seasonId) {
        Observable.create(new Observable.OnSubscribe<BattleData>() {
            @Override
            public void call(Subscriber<? super BattleData> subscriber) {

                BattleData battleData = getBattleData(seasonId);
                // load player image path
                loadPlayerImageMap();

                subscriber.onNext(battleData);
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BattleData>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(BattleData data) {
                        battleView.onBattleDataLoaded(data);
                    }
                });
    }

    private BattleData getBattleData(int seasonId) {
        BattleData battleData = new BattleData();
        // load SeasonBean
        SeasonBean season = gameProvider.getSeasonById(seasonId);
        battleData.setSeason(season);
        // load 4 CoachBean
        CoachBean coach = gameProvider.getCoachById(season.getCoachId1());
        battleData.setCoach1(coach);
        coach = gameProvider.getCoachById(season.getCoachId2());
        battleData.setCoach2(coach);
        coach = gameProvider.getCoachById(season.getCoachId3());
        battleData.setCoach3(coach);
        coach = gameProvider.getCoachById(season.getCoachId4());
        battleData.setCoach4(coach);
        return battleData;
    }

    public void loadDeatails(final BattleDetailData detailData) {
        Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(Subscriber<? super Object> subscriber) {

                // load players
                List<PlayerBean> playerListTop = new ArrayList<>();
                List<PlayerBean> playerListBottom = new ArrayList<>();
                detailData.setPlayerListTop(playerListTop);
                detailData.setPlayerListBottom(playerListBottom);
                List<GroupBean> list = gameProvider.getGroupList(detailData.getSeason().getId(), detailData.getCoach().getId());
                if (list != null && list.size() > 0) {
                    for (GroupBean group : list) {
                        if (group.getType() == 1) {
                            playerListTop.add(gameProvider.getPlayerById(group.getPlayerId()));
                        } else {// 0
                            playerListBottom.add(gameProvider.getPlayerById(group.getPlayerId()));
                        }
                    }
                }

                // load battle beans
                List<BattleBean> battleList = gameProvider.getBattleList(detailData.getSeason().getId(), detailData.getCoach().getId());
                detailData.setBattleList(battleList);

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

    public PlayerBean getPlayer(BattleDetailData detailData, int playerId) {
        if (detailData.getPlayerListTop() != null) {
            for (PlayerBean bean:detailData.getPlayerListTop()) {
                if (bean.getId() == playerId) {
                    return bean;
                }
            }
        }
        if (detailData.getPlayerListBottom() != null) {
            for (PlayerBean bean:detailData.getPlayerListBottom()) {
                if (bean.getId() == playerId) {
                    return bean;
                }
            }
        }
        return null;
    }

    public void saveBattleBean(BattleBean bean) {
        gameProvider.updateBattleBean(bean);
    }

    public void deleteBattleBean(BattleBean bean) {
        gameProvider.deleteBattleBean(bean.getId());
    }

    public boolean saveBattleResultBeans(List<BattleResultBean> datas, int promoteNum) {
        return gameProvider.saveBattleResultBeans(datas, promoteNum);
    }

    public boolean isBattleResultExist(int seasonId, int coachId) {
        return gameProvider.isBattleResultExist(seasonId, coachId);
    }

    public void deleteBattleResults(int seasonId, int coachId) {
        gameProvider.deleteBattleResults(seasonId, coachId);
    }
}
