package com.jing.app.jjgallery.gdb.presenter.game;

import android.os.AsyncTask;

import com.jing.app.jjgallery.gdb.bean.GamePlayerBean;
import com.jing.app.jjgallery.gdb.model.game.GroupData;
import com.jing.app.jjgallery.gdb.view.game.IGroupView;
import com.king.service.gdb.bean.Star;
import com.king.service.gdb.game.bean.CoachBean;
import com.king.service.gdb.game.bean.GroupBean;
import com.king.service.gdb.game.bean.PlayerBean;
import com.king.service.gdb.game.bean.SeasonBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/2/8 11:05
 */
public class GroupPresenter extends GamePresenter {
    private IGroupView groupView;
    private Random random;

    private PlayerRandomTask playerRandomTask;

    public GroupPresenter(IGroupView groupView) {
        this.groupView = groupView;
        random = new Random();
    }

    public void loadGroupDatas(final int seasonId) {
        Observable.create(new Observable.OnSubscribe<GroupData>() {
            @Override
            public void call(Subscriber<? super GroupData> subscriber) {

                GroupData groupData = getGroupData(seasonId);
                // load player image path
                loadPlayerImageMap();

                subscriber.onNext(groupData);
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<GroupData>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(GroupData groupData) {
                        groupView.onGroupDataLoaded(groupData);
                    }
                });
    }

    private GroupData getGroupData(int seasonId) {
        GroupData groupData = new GroupData();
        // load SeasonBean
        SeasonBean season = gameProvider.getSeasonById(seasonId);
        groupData.setSeason(season);
        // load 4 CoachBean
        CoachBean coach = gameProvider.getCoachById(season.getCoachId1());
        groupData.setCoach1(coach);
        coach = gameProvider.getCoachById(season.getCoachId2());
        groupData.setCoach2(coach);
        coach = gameProvider.getCoachById(season.getCoachId3());
        groupData.setCoach3(coach);
        coach = gameProvider.getCoachById(season.getCoachId4());
        groupData.setCoach4(coach);
        // load players
        List<PlayerBean> playerListTop1 = new ArrayList<>();
        List<PlayerBean> playerListTop2 = new ArrayList<>();
        List<PlayerBean> playerListTop3 = new ArrayList<>();
        List<PlayerBean> playerListTop4 = new ArrayList<>();
        List<PlayerBean> playerListBottom1 = new ArrayList<>();
        List<PlayerBean> playerListBottom2 = new ArrayList<>();
        List<PlayerBean> playerListBottom3 = new ArrayList<>();
        List<PlayerBean> playerListBottom4 = new ArrayList<>();
        groupData.setPlayerListTop1(playerListTop1);
        groupData.setPlayerListTop2(playerListTop2);
        groupData.setPlayerListTop3(playerListTop3);
        groupData.setPlayerListTop4(playerListTop4);
        groupData.setPlayerListBottom1(playerListBottom1);
        groupData.setPlayerListBottom2(playerListBottom2);
        groupData.setPlayerListBottom3(playerListBottom3);
        groupData.setPlayerListBottom4(playerListBottom4);

        List<GroupBean> list = gameProvider.getGroupList(seasonId);
        if (list != null && list.size() > 0) {
            for (GroupBean group : list) {
                if (group.getCoachId() == groupData.getCoach1().getId()) {
                    if (group.getType() == 1) {
                        playerListTop1.add(gameProvider.getPlayerById(group.getPlayerId()));
                    } else {// 0
                        playerListBottom1.add(gameProvider.getPlayerById(group.getPlayerId()));
                    }
                } else if (group.getCoachId() == groupData.getCoach2().getId()) {
                    if (group.getType() == 1) {
                        playerListTop2.add(gameProvider.getPlayerById(group.getPlayerId()));
                    } else {// 0
                        playerListBottom2.add(gameProvider.getPlayerById(group.getPlayerId()));
                    }
                } else if (group.getCoachId() == groupData.getCoach3().getId()) {
                    if (group.getType() == 1) {
                        playerListTop3.add(gameProvider.getPlayerById(group.getPlayerId()));
                    } else {// 0
                        playerListBottom3.add(gameProvider.getPlayerById(group.getPlayerId()));
                    }
                } else if (group.getCoachId() == groupData.getCoach4().getId()) {
                    if (group.getType() == 1) {
                        playerListTop4.add(gameProvider.getPlayerById(group.getPlayerId()));
                    } else {// 0
                        playerListBottom4.add(gameProvider.getPlayerById(group.getPlayerId()));
                    }
                }
            }
        }
        return groupData;
    }

    /**
     * next player for group random
     *
     * @param playerFromMode
     * @return
     */
    public void nextPlayer(String playerFromMode) {
        playerRandomTask = new PlayerRandomTask();
        playerRandomTask.execute(playerFromMode);
    }

    public void stopRandom() {
        playerRandomTask.cancel(true);
    }

    private class PlayerRandomTask extends AsyncTask<String, GamePlayerBean, Void> {

        private boolean isRun;

        public void setRun(boolean run) {
            isRun = run;
        }

        @Override
        protected void onPreExecute() {
            isRun = true;
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... params) {
            List<Star> stars = gdbProvider.getStars(params[0]);
            List<GamePlayerBean> list = distinctStars(stars);
            if (list != null) {
                while (isRun) {
                    int index = Math.abs(random.nextInt()) % list.size();
                    publishProgress(list.get(index));
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        break;
                    }
                }
            }
            return null;
        }

        /**
         * 过滤已存在于_group表中的player
         *
         * @param list
         * @return
         */
        private List<GamePlayerBean> distinctStars(List<Star> list) {
            List<GamePlayerBean> results = new ArrayList<>();
            List<GroupBean> beans = gameProvider.getGroupBeanList();
            Map<String, Boolean[]> map = new HashMap<>();
            for (int i = 0; i < beans.size(); i++) {
                String name = beans.get(i).getPlayerName();
                Boolean[] exists = map.get(name);
                if (exists == null) {
                    exists = new Boolean[2];
                    exists[0] = false;
                    exists[1] = false;
                    map.put(name, exists);
                }
                if (beans.get(i).getType() == 0) {
                    exists[0] = true;
                } else if (beans.get(i).getType() == 1) {
                    exists[1] = true;
                }
            }

            for (int i = 0; i < list.size(); i++) {
                Star star = list.get(i);
                GamePlayerBean bean = new GamePlayerBean();
                bean.setStar(star);
                Boolean[] exists = map.get(star.getName());
                if (exists != null) {
                    // 如果star是half, 判断只有当1和0都出现过才过滤
                    if (star.getBeBottom() > 0 && star.getBeTop() > 0) {
                        if (exists != null) {
                            if (exists[0] && exists[1]) {
                                continue;
                            }
                            if (exists[0]) {
                                bean.setHasBeenBottom(true);
                            }
                            if (exists[1]) {
                                bean.setHasBeenTop(true);
                            }
                        }
                    }
                    // 如果star不是half, 只要出现过就过滤掉
                    else {
                        continue;
                    }
                }
                results.add(bean);
            }
            return results;
        }

        @Override
        protected void onProgressUpdate(GamePlayerBean... values) {
            groupView.onPlayerRecommended(values[0]);
            super.onProgressUpdate(values);
        }
    }

    /**
     * @param bean
     * @param type
     * @return player id
     */
    public int savePlayer(PlayerBean bean, int type) {
        return gameProvider.savePlayer(bean, type);
    }

    public void saveGroupBean(GroupBean bean) {
        gameProvider.insertGroupBean(bean);
    }

    public void deletePlayer(int playerId, int seasonId) {
        gameProvider.deletePlayer(playerId, seasonId);
    }

}
