package com.jing.app.jjgallery.gdb.view.game.battlecross;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.jing.app.jjgallery.BaseActivity;
import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.viewsystem.publicview.ActionBar;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/2/14 17:31
 */
public abstract class BaseBattleActivity extends BaseActivity {

    public static final String KEY_SEASON_ID = "key_season_id";

    private BaseCoachFragment ftCoach;
    private BaseDetailFragment[] ftDetails;
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
    }

    @Override
    public void initView() {
        showProgressCycler();
        ftDetails = initDetailFragments();
        initSubView(getIntent().getIntExtra(KEY_SEASON_ID, 0));
    }

    protected abstract BaseDetailFragment[] initDetailFragments();

    @Override
    public void initBackgroundWork() {

    }

    protected abstract void initSubView(int seasonId);

    protected void showCoachFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (ftCoach == null) {
            ftCoach = createCoachFragment();
            ft.add(R.id.battle_fragment, ftCoach, "BaseCoachFragment");
        }
        else {
            ft.show(ftCoach).hide(ftCurrent);
        }
        ftCurrent = ftCoach;
        ft.commit();
    }

    protected abstract BaseCoachFragment createCoachFragment();

    protected void showDetailFragment(int index) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (ftDetails[index] == null) {
            ftDetails[index] = createDetailFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("index", index);
            ftDetails[index].setArguments(bundle);
            ft.add(R.id.battle_fragment, ftDetails[index], "BaseDetailFragment" + index).hide(ftCurrent);
        }
        else {
            ft.show(ftDetails[index]).hide(ftCurrent);
        }
        ftCurrent = ftDetails[index];
        ft.commit();
    }

    protected abstract BaseDetailFragment createDetailFragment();

    @Override
    public void onBack() {
        if (ftCurrent instanceof BaseDetailFragment) {
            showCoachFragment();
        }
        else {
            finish();
        }
    }

}
