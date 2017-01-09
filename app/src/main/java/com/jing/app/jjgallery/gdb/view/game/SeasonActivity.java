package com.jing.app.jjgallery.gdb.view.game;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.jing.app.jjgallery.BaseActivity;
import com.jing.app.jjgallery.R;

public class SeasonActivity extends BaseActivity  {

    private RecyclerView recyclerView;

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

        recyclerView = (RecyclerView) findViewById(R.id.season_list);
    }

    @Override
    public void initBackgroundWork() {

    }

    @Override
    public void onBack() {
        finish();
    }

    @Override
    public void onIconClick(View view) {
        switch (view.getId()) {
            case R.id.actionbar_add:
                break;
        }
    }
}
