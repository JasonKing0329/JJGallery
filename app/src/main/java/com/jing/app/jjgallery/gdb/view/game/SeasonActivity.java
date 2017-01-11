package com.jing.app.jjgallery.gdb.view.game;

import android.os.Bundle;

import com.jing.app.jjgallery.gdb.presenter.game.GamePresenter;
import com.king.service.gdb.game.bean.SeasonBean;

/**
 * 持有两个fragment
 * 其中SeasonListFragment一直存在，创建后切换通过show/hide
 * SeasonEditFragment每次都add，切换回SeasonListFragment后remove(edit涉及insert 和 update，所以每次都重新实例化是可行的)
 */
public class SeasonActivity extends GameActivity implements IGameManager<SeasonBean> {

    private GamePresenter gamePresenter;

    @Override
    public void initController() {
        super.initController();
        gamePresenter = new GamePresenter();
    }

    @Override
    protected void initSubView() {
        mActionBar.setTitle("Season");
    }

    @Override
    protected GameListFragment createListFragment() {
        return new SeasonListFragment();
    }

    @Override
    protected GameEditFragment createEditFragment() {
        return new SeasonEditFragment();
    }

    @Override
    public void onSaveData(SeasonBean seasonBean) {
        gamePresenter.saveSeason(seasonBean);
        showListPage();
        listFragment.getIGameList().onDataUpdated(seasonBean);
    }

    @Override
    public void updateData(SeasonBean seasonBean) {
        Bundle data = new Bundle();
        if (seasonBean == null) {
            data.putBoolean(SeasonEditFragment.KEY_INIT_WITH_DATA, false);
        }
        else {
            data.putBoolean(SeasonEditFragment.KEY_INIT_WITH_DATA, true);
            data.putInt(SeasonEditFragment.KEY_SEASON_ID, seasonBean.getId());
        }
        addSeason(data);
    }

    @Override
    public GamePresenter getPresenter() {
        return gamePresenter;
    }

}
