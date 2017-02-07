package com.jing.app.jjgallery.gdb.view.game;

import com.jing.app.jjgallery.BaseActivity;
import com.jing.app.jjgallery.R;

public class BattleActivity extends BaseActivity {

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
        mActionBar.addGroupAddIcon();
        mActionBar.addMenuIcon();
    }

    @Override
    public void initBackgroundWork() {

    }
}
