package com.jing.app.jjgallery;

import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import com.jeremyfeinstein.slidingmenu.lib.app.SlidingAppCompatActivity;
import com.jing.app.jjgallery.controller.ThemeManager;
import com.jing.app.jjgallery.util.DisplayHelper;
import com.jing.app.jjgallery.viewsystem.main.bg.BackgroundManager;
import com.jing.app.jjgallery.viewsystem.publicview.ActionBar;
import com.jing.app.jjgallery.viewsystem.publicview.ProgressManager;

/**
 * Created by JingYang on 2016/7/7 0007.
 * Description:
 * 带有sliding menu的activity
 * 用于主界面AbsHomeActivity
 * 除了继承SlidingAppCompatActivity以外，其他的封装层面和BaseActivity一致
 */
public abstract class BaseSlidingActivity extends SlidingAppCompatActivity implements ActionBar.ActionIconListener
        , ActionBar.ActionMenuListener, ActionBar.ActionSearchListener {
    private ViewGroup mActionbarGroup;
    private ViewGroup mContentGroup;

    protected ActionBar mActionBar;

    private ProgressDialog progressDialog;

    private int curOrientation;

    private ProgressManager progressManager;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (isFullScreen()) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            DisplayHelper.enableFullScreen();
        }
        DisplayHelper.disableScreenshot(this);

        setTheme(new ThemeManager(this).getDefaultTheme());
        super.onCreate(savedInstanceState);

        curOrientation = getResources().getConfiguration().orientation;

        getSupportActionBar().hide();
        setContentView(R.layout.activity_base_sliding);

        mActionbarGroup = (ViewGroup) findViewById(R.id.actionbar);
        mContentGroup = (ViewGroup) findViewById(R.id.frame_content);
        progressManager = new ProgressManager(this);
        BackgroundManager.getInstance().addProgressSubscriber(progressManager);

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

        progressDialog = new ProgressDialog(this);
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

    protected void showProgress(String text) {
        progressDialog.setMessage(text);
        progressDialog.show();
    }

    protected boolean dismissProgress() {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
            return true;
        }
        return  false;
    }

    protected void showToastLong(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

    protected void showToastShort(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    public void showProgressCycler() {
        progressManager.showProgressCycler();
    }

    public void dismissProgressCycler() {
        progressManager.dismissProgressCycler();
    }

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

    public void applyAnimatinLeftIn(FragmentTransaction ft) {
        ft.setCustomAnimations(R.anim.activity_left_in, R.anim.activity_right_out);
    }

    public void applyAnimatinRightIn(FragmentTransaction ft) {
        ft.setCustomAnimations(R.anim.activity_right_in, R.anim.activity_left_out);
    }

    public void applyAnimation() {
        //不知道为啥不管用
        overridePendingTransition(R.anim.activity_left_in, R.anim.activity_right_out);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BackgroundManager.getInstance().removeProgressSubscriber(progressManager);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (newConfig.orientation != curOrientation) {
            curOrientation = newConfig.orientation;
            if (mActionBar != null) {
                mActionBar.onConfiguration(newConfig.orientation);
            }
            onOrentaionChanged(newConfig);
        }
        super.onConfigurationChanged(newConfig);
    }

    protected abstract void onOrentaionChanged(Configuration newConfig);
}
