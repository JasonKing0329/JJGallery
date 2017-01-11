package com.jing.app.jjgallery.gdb.view.game;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.jing.app.jjgallery.BaseActivity;
import com.jing.app.jjgallery.R;

/**
 * Created by 景阳 on 2017/1/11.
 */

public abstract class GameActivity extends BaseActivity {

    protected GameListFragment listFragment;
    protected GameEditFragment editFragment;

    protected Fragment curFragment;

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

    }

    @Override
    public void initView() {
        mActionBar.addAddIcon();
        mActionBar.addBackIcon();

        showListPage();
        initSubView();
    }

    protected abstract void initSubView();

    @Override
    public void initBackgroundWork() {

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

    protected void showListPage() {

        if (listFragment == null) {
            listFragment = createListFragment();
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

    protected abstract GameListFragment createListFragment();

    protected void addSeason(Bundle data) {
        if (editFragment == null) {
            editFragment = createEditFragment();
        }
        if (data != null) {
            editFragment.setArguments(data);
        }
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.season_fragment, editFragment, "SeasonEditFragment").hide(listFragment);
        ft.commit();
        curFragment = editFragment;

        mActionBar.clearActionIcon();
        mActionBar.addBackIcon();
        mActionBar.setTitle("Edit season");
    }

    protected abstract GameEditFragment createEditFragment();
}
