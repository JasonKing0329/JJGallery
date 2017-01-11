package com.jing.app.jjgallery.gdb.view.game;

import com.jing.app.jjgallery.BaseActivity;
import com.jing.app.jjgallery.R;

public class CoachActivity extends BaseActivity {

    @Override
    public boolean isActionBarNeed() {
        return true;
    }

    @Override
    public int getContentView() {
        return R.layout.activity_gdb_coach;
    }

    @Override
    public void initController() {

    }

    @Override
    public void initView() {
        mActionBar.addBackIcon();
        mActionBar.setTitle("Coach");
    }

    @Override
    public void initBackgroundWork() {

    }
}
