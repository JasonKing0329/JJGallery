package com.jing.app.jjgallery.gdb.view.game;

import android.content.Intent;
import android.os.Bundle;

import com.jing.app.jjgallery.gdb.presenter.game.CoachSeasonManager;
import com.jing.app.jjgallery.gdb.presenter.game.GamePresenter;
import com.king.service.gdb.game.bean.CoachBean;

/**
 * 用于其他界面调用选择coach
 */
public class CoachActivity extends GameActivity implements ICoachManager {

    public static final String RESP_COACH_ID = "resp_coach_id";

    private GamePresenter gamePresenter;
    private CoachSeasonManager coachSeasonManager;

    @Override
    protected void initSubController() {
        gamePresenter = new GamePresenter();
        coachSeasonManager = new CoachSeasonManager();

        coachSeasonManager.setSeasonList(gamePresenter.getSeasonList());
    }

    @Override
    protected void initSubView() {
        mActionBar.setTitle("Coach");
    }

    @Override
    protected GameListFragment createListFragment() {
        return new CoachListFragment();
    }

    @Override
    protected GameEditFragment createEditFragment() {
        return new CoachEditFragment();
    }

    @Override
    public CoachSeasonManager getCoachSeasonManager() {
        return coachSeasonManager;
    }

    @Override
    public void onCoachSelect(CoachBean coachBean) {
        Intent intent = getIntent();
        intent.putExtra(RESP_COACH_ID, coachBean.getId());
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onSaveData(CoachBean data) {
        gamePresenter.saveCoach(data);
        showListPage();
        listFragment.getIGameList().onDataUpdated(data);
    }

    @Override
    public void updateData(CoachBean coachBean) {
        Bundle data = new Bundle();
        if (coachBean == null) {
            data.putBoolean(SeasonEditFragment.KEY_INIT_WITH_DATA, false);
        }
        else {
            data.putBoolean(SeasonEditFragment.KEY_INIT_WITH_DATA, true);
            data.putInt(SeasonEditFragment.KEY_ID, coachBean.getId());
        }
        addNewData(data);
    }

    @Override
    public void deleteData(CoachBean data) {

    }

    @Override
    public GamePresenter getPresenter() {
        return gamePresenter;
    }
}
