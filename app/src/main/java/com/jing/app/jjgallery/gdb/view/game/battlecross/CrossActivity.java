package com.jing.app.jjgallery.gdb.view.game.battlecross;

import com.jing.app.jjgallery.gdb.model.game.CrossData;
import com.jing.app.jjgallery.gdb.presenter.game.CrossPresenter;
import com.jing.app.jjgallery.gdb.presenter.game.GamePresenter;
import com.jing.app.jjgallery.viewsystem.publicview.ActionBar;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/2/15 11:06
 */
public class CrossActivity extends BaseBattleActivity implements ICrossView {

    private CrossPresenter mPresenter;
    private CrossData crossData;

    @Override
    protected BaseDetailFragment[] initDetailFragments() {
        return new BaseDetailFragment[2];
    }

    @Override
    protected void initSubView(int seasonId) {
        mPresenter = new CrossPresenter(this);
        mPresenter.loadCrossBasics(seasonId);
    }

    @Override
    protected BaseCoachFragment createCoachFragment() {
        return new CrossCoachFragment();
    }

    @Override
    protected BaseDetailFragment createDetailFragment() {
        return new CrossDetailFragment();
    }

    @Override
    public void showCoachCross(int index) {
        showDetailFragment(index);
    }

    @Override
    public CrossPresenter getPresenter() {
        return mPresenter;
    }

    @Override
    public GamePresenter getGamePresenter() {
        return mPresenter;
    }

    @Override
    public ActionBar getActionbar() {
        return mActionBar;
    }

    @Override
    public CrossData getBattleData() {
        return crossData;
    }

    @Override
    public void onBattleDataLoaded(CrossData data) {
        this.crossData = data;
        showCoachFragment();
    }

    @Override
    public void onBackPressed() {
        onBack();
    }

}
