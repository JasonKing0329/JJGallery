package com.jing.app.jjgallery.gdb.presenter.game;

import com.jing.app.jjgallery.config.DBInfor;
import com.king.service.gdb.game.GameProvider;
import com.king.service.gdb.game.bean.CoachBean;
import com.king.service.gdb.game.bean.SeasonBean;

import java.util.List;

/**
 * Created by 景阳 on 2017/1/10.
 */

public class GamePresenter {

    private GameProvider gameProvider;

    public GamePresenter() {
        gameProvider = new GameProvider(DBInfor.GDB_GAME_DB_PATH);
    }

    public List<SeasonBean> getSeasonList() {
        return gameProvider.getSeasonList();
    }

    public SeasonBean getSeasonById(int id) {
        return gameProvider.getSeasonById(id);
    }

    public boolean saveSeason(SeasonBean seasonBean) {
        return gameProvider.updateSeason(seasonBean);
    }

    public List<CoachBean> getCoachList() {
        return gameProvider.getCoachList();
    }

    public CoachBean getCoachById(int id) {
        return gameProvider.getCoachById(id);
    }

    public void saveCoach(CoachBean coachBean) {
        gameProvider.updateCoach(coachBean);
    }
}
