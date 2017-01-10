package com.jing.app.jjgallery.gdb.view.game;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.jing.app.jjgallery.BaseActivity;
import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.gdb.presenter.game.GamePresenter;
import com.king.service.gdb.game.bean.SeasonBean;

/**
 * 持有两个fragment
 * 其中SeasonListFragment一直存在，创建后切换通过show/hide
 * SeasonEditFragment每次都add，切换回SeasonListFragment后remove(edit涉及insert 和 update，所以每次都重新实例化是可行的)
 */
public class SeasonActivity extends BaseActivity implements ISeasonManager {

    private GamePresenter gamePresenter;

    private SeasonListFragment listFragment;
    private SeasonEditFragment editFragment;

    private Fragment curFragment;

    @Override
    public boolean isActionBarNeed() {
        return true;
    }

    @Override
    public int getContentView() {
        return R.layout.activity_gdb_game;
    }

    @Override
    public void initController() {
        gamePresenter = new GamePresenter();
    }

    @Override
    public void initView() {

        mActionBar.addAddIcon();
        mActionBar.addBackIcon();
        mActionBar.setTitle("Season");

        showListPage();
    }

    @Override
    public void initBackgroundWork() {
    }

    @Override
    public GamePresenter getPresenter() {
        return gamePresenter;
    }

    @Override
    public void onSaveSeasonBean(SeasonBean seasonBean) {
        gamePresenter.saveSeason(seasonBean);
        showListPage();
        listFragment.onSeasonUpdated(seasonBean);
    }

    @Override
    public void updateSeason(SeasonBean seasonBean) {
        addSeason(seasonBean);
    }

    @Override
    public void onBack() {
        if (curFragment == editFragment) {
            showListPage();
        }
        else {
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        onBack();
    }

    @Override
    public void onIconClick(View view) {
        switch (view.getId()) {
            case R.id.actionbar_add:
                addSeason(null);
                break;
        }
    }

    private void showListPage() {

        if (listFragment == null) {
            listFragment = new SeasonListFragment();
        }
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (listFragment.isAdded()) {
            ft.show(listFragment);
        }
        else {
            ft.add(R.id.season_fragment, listFragment, "SeasonListFragment");
        }

        // remove edit fragment, need recreate instance
        if (editFragment != null && editFragment.isAdded()) {
            ft.remove(editFragment);
            editFragment = null;
        }

        ft.commit();
        curFragment = listFragment;

        mActionBar.clearActionIcon();
        mActionBar.addBackIcon();
        mActionBar.addAddIcon();
        mActionBar.setTitle("Season");
    }

    private void addSeason(SeasonBean season) {
        if (editFragment == null) {
            editFragment = new SeasonEditFragment();
        }
        Bundle data = new Bundle();
        if (season == null) {
            data.putBoolean(SeasonEditFragment.KEY_INIT_WITH_DATA, false);
        }
        else {
            data.putBoolean(SeasonEditFragment.KEY_INIT_WITH_DATA, true);
            data.putInt(SeasonEditFragment.KEY_SEASON_ID, season.getId());
        }
        editFragment.setArguments(data);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.season_fragment, editFragment, "SeasonEditFragment").hide(listFragment);
        ft.commit();
        curFragment = editFragment;

        mActionBar.clearActionIcon();
        mActionBar.addBackIcon();
        mActionBar.setTitle("Edit season");
    }

}
