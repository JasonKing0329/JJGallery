package com.jing.app.jjgallery.gdb.view.game.battlecross;

import com.jing.app.jjgallery.gdb.model.game.BattleData;
import com.jing.app.jjgallery.gdb.presenter.game.BattlePresenter;
import com.jing.app.jjgallery.gdb.presenter.game.GamePresenter;
import com.jing.app.jjgallery.viewsystem.publicview.ActionBar;

public class BattleActivity extends BaseBattleActivity implements IBattleView {

    private BattlePresenter mPresenter;
    private BattleData battleData;

    @Override
    protected BaseDetailFragment[] initDetailFragments() {
        return new BattleDetailFragment[4];
    }

    @Override
    public void initSubView(int seasonId) {
        mPresenter = new BattlePresenter(this);
        mPresenter.loadBattleBasics(seasonId);
    }

    @Override
    protected BaseCoachFragment createCoachFragment() {
        return new BattleCoachFragment();
    }

    @Override
    protected BaseDetailFragment createDetailFragment() {
        return new BattleDetailFragment();
    }

    @Override
    public void onBattleDataLoaded(BattleData data) {
        battleData = data;
        dismissProgressCycler();
        showCoachFragment();
    }

    @Override
    public ActionBar getActionbar() {
        return mActionBar;
    }

    @Override
    public BattlePresenter getPresenter() {
        return mPresenter;
    }

    @Override
    public GamePresenter getGamePresenter() {
        return mPresenter;
    }

    @Override
    public BattleData getBattleData() {
        return battleData;
    }

    @Override
    public void showCoachBattle(int index) {
        showDetailFragment(index);
    }

    @Override
    public void onBackPressed() {
        onBack();
    }

}
