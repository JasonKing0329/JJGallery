package com.jing.app.jjgallery;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.jing.app.jjgallery.viewsystem.publicview.ActionBar;

/**
 * Created by Administrator on 2016/6/23 0023.
 */
public abstract class BaseActivity extends AppCompatActivity implements ActionBar.ActionIconListener
    , ActionBar.ActionMenuListener, ActionBar.ActionSearchListener {

    private ViewGroup mActionbarGroup;
    private ViewGroup mContentGroup;

    protected ActionBar mActionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (isFullScreen()) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        super.onCreate(savedInstanceState);

        getSupportActionBar().hide();
        setContentView(R.layout.activity_base);

        mActionbarGroup = (ViewGroup) findViewById(R.id.actionbar);
        mContentGroup = (ViewGroup) findViewById(R.id.content);

        if (isActionBarNeed()) {
            View view = getLayoutInflater().inflate(R.layout.actionbar_l, null);
            mActionbarGroup.addView(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            mActionBar = new ActionBar(this, view);
            mActionBar.setActionIconListener(this);
            mActionBar.setActionMenuListener(this);
            mActionBar.setActionSearchListener(this);
        }
        View content = getLayoutInflater().inflate(getContentView(), null);
        mContentGroup.addView(content, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        initController();
        initView();

        initBackgroundWork();
    }

    protected boolean isFullScreen() {
        return  true;
    }

    protected abstract boolean isActionBarNeed();

    protected abstract int getContentView();

    protected abstract void initController();

    protected abstract void initView();

    protected abstract void initBackgroundWork();

    @Override
    public void onBack() {

    }

    @Override
    public void onIconClick(View view) {

    }

    @Override
    public void createMenu(MenuInflater menuInflater, Menu menu) {

    }

    @Override
    public void onPrepareMenu(MenuInflater menuInflater, Menu menu) {

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }

    @Override
    public void onTextChanged(String text, int start, int before, int count){

    }
}
