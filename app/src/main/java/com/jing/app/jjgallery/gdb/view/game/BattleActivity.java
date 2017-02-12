package com.jing.app.jjgallery.gdb.view.game;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.jing.app.jjgallery.BaseActivity;
import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.gdb.model.game.BattleData;
import com.jing.app.jjgallery.gdb.presenter.game.BattlePresenter;
import com.jing.app.jjgallery.gdb.view.IBattleView;
import com.jing.app.jjgallery.viewsystem.publicview.ActionBar;

import java.util.ArrayList;
import java.util.List;

public class BattleActivity extends BaseActivity implements IBattleView {

    public static final String KEY_SEASON_ID = "key_season_id";

    private BattlePresenter mPresenter;
    private BattleData battleData;

    private BattleCoachFragment ftCoach;
    private BattleDetailFragment[] ftDetails;
    private Fragment ftCurrent;

    @Override
    public boolean isActionBarNeed() {
        return true;
    }

    @Override
    public int getContentView() {
        return R.layout.gdb_game_battle;
    }

    @Override
    public void initController() {
        mPresenter = new BattlePresenter(this);
        ftDetails = new BattleDetailFragment[4];
    }

    @Override
    public void initView() {
        showProgressCycler();
        mPresenter.loadBattleBasics(getIntent().getIntExtra(KEY_SEASON_ID, 0));
    }

    @Override
    public void initBackgroundWork() {

    }

    private void showCoachFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (ftCoach == null) {
            ftCoach = new BattleCoachFragment();
            ft.add(R.id.battle_fragment, ftCoach, "BattleCoachFragment");
        }
        else {
            ft.show(ftCoach).hide(ftCurrent);
        }
        ftCurrent = ftCoach;
        ft.commit();
    }

    private void showDetailFragment(int index) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (ftDetails[index] == null) {
            ftDetails[index] = new BattleDetailFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("index", index);
            ftDetails[index].setArguments(bundle);
            ft.add(R.id.battle_fragment, ftDetails[index], "BattleDetailFragment" + index).hide(ftCurrent);
        }
        else {
            ft.show(ftDetails[index]).hide(ftCurrent);
        }
        ft.commit();
    }

    @Override
    public ActionBar getActionbar() {
        return mActionBar;
    }

    @Override
    public void onBattleDataLoaded(BattleData data) {
        battleData = data;
        dismissProgressCycler();
        showCoachFragment();
    }

    @Override
    public BattlePresenter getPresenter() {
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
    public void onBack() {
        if (ftCurrent instanceof BattleDetailFragment) {
            showCoachFragment();
        }
        else {
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        onBack();
    }
}
