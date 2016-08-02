package com.jing.app.jjgallery.viewsystem.main.gdb;

import android.support.v4.app.FragmentTransaction;

import com.jing.app.jjgallery.BaseActivity;
import com.jing.app.jjgallery.R;

public class GDBHomeActivity extends BaseActivity {

    private StarListFragment fragment;
    @Override
    protected boolean isActionBarNeed() {
        return true;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_gdb_home;
    }

    @Override
    protected void initController() {

    }

    @Override
    protected void initView() {
        initActionBar();
        onStarListPage();
    }

    private void initActionBar() {
        mActionBar.addBackIcon();
        mActionBar.addMenuIcon();
        mActionBar.addSearchIcon();
    }

    public void onStarListPage() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (fragment == null) {
            StarListFragment fragment = new StarListFragment();
            this.fragment = fragment;
        }

        ft.replace(R.id.gdb_fragment_container, fragment, "StarListFragment");
        ft.commit();
    }
    @Override
    protected void initBackgroundWork() {

    }

    @Override
    public void onTextChanged(String text, int start, int before, int count) {
        super.onTextChanged(text, start, before, count);
        fragment.onTextChanged(text, start, before, count);
    }
}
