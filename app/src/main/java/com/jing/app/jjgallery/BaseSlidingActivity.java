package com.jing.app.jjgallery;

import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.jeremyfeinstein.slidingmenu.lib.app.SlidingAppCompatActivity;
import com.jing.app.jjgallery.controller.ThemeManager;
import com.jing.app.jjgallery.util.DisplayHelper;
import com.jing.app.jjgallery.viewsystem.ProgressProvider;
import com.jing.app.jjgallery.viewsystem.main.bg.BackgroundManager;
import com.jing.app.jjgallery.viewsystem.publicview.ActionBar;
import com.jing.app.jjgallery.viewsystem.publicview.ActionBarManager;
import com.jing.app.jjgallery.viewsystem.publicview.ProgressManager;

/**
 * Created by JingYang on 2016/7/7 0007.
 * Description:
 * 带有sliding menu的activity
 * 用于主界面AbsHomeActivity
 * 除了继承SlidingAppCompatActivity以外，其他的封装层面和BaseActivity一致
 */
public abstract class BaseSlidingActivity extends SlidingAppCompatActivity implements ActionBar.ActionIconListener
        , ActionBar.ActionMenuListener, ActionBar.ActionSearchListener, ProgressProvider {
    private ViewGroup mActionbarGroup;
    private ViewGroup mContentGroup;

    protected ActionBar mActionBar;

    private ProgressDialog progressDialog;

    private int curOrientation;

    private ProgressManager progressManager;
    private ActionBarManager actionBarManager;
    private boolean isActionbarFloating;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        ((JJApplication) getApplication()).addActivity(this);
        if (isFullScreen()) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            DisplayHelper.enableFullScreen();
        }
        DisplayHelper.disableScreenshot(this);

        setTheme(ThemeManager.getInstance().getDefaultTheme(this));
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
            actionBarManager = new ActionBarManager(this, mActionBar);
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

    @Override
    public void showProgress(String text) {
        progressDialog.setMessage(text);
        progressDialog.show();
    }

    @Override
    public boolean dismissProgress() {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
            return true;
        }
        return  false;
    }

    @Override
    public void showProgressCycler() {
        if (progressManager == null) {
            progressManager = new ProgressManager(this);
            BackgroundManager.getInstance().addProgressSubscriber(progressManager);
        }
        progressManager.showProgressCycler();
    }

    @Override
    public boolean dismissProgressCycler() {
        if (progressManager.isShowing()) {
            progressManager.dismissProgressCycler();
            return true;
        }
        return false;
    }

    protected void showToastLong(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

    protected void showToastShort(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
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
        ((JJApplication) getApplication()).removeActivity(this);
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

    /**
     * 设置actionbar浮于content之上
     * @param disableParentOperation 禁止BaseActivity的dispatch touch处理，由派生类自己改写操作(例如WallActivity涉及top bottom两个bar的控制)
     */
    public void requestActionbarFloating(boolean disableParentOperation) {
        RelativeLayout container = (RelativeLayout) findViewById(R.id.main_container);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mContentGroup.getLayoutParams();
        params.removeRule(RelativeLayout.BELOW);
        container.removeView(mContentGroup);
        container.removeView(mActionbarGroup);
        container.addView(mContentGroup, 0);
        container.addView(mActionbarGroup, 1);
        if (!disableParentOperation) {
            isActionbarFloating = true;
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (isActionbarFloating) {
            if (actionBarManager != null) {
                actionBarManager.dispatchTouchEvent(event);
            }
        }
        return super.dispatchTouchEvent(event);
    }

}
