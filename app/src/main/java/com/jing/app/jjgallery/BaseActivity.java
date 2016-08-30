package com.jing.app.jjgallery;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.jing.app.jjgallery.viewsystem.ProgressProvider;
import com.jing.app.jjgallery.viewsystem.publicview.ActionBar;

/**
 * Created by Administrator on 2016/6/23 0023.
 * full screen
 * actionbar
 * progress view
 * activity/fragment animation
 */
public abstract class BaseActivity extends AppCompatActivity implements ActivityAction, ActionBar.ActionIconListener
    , ActionBar.ActionMenuListener, ActionBar.ActionSearchListener, ProgressProvider {

    private CommonActivity commonActivity = new CommonActivity();
    protected ActionBar mActionBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        commonActivity.preOnCreate(this, this);
        super.onCreate(savedInstanceState);

        getSupportActionBar().hide();
        commonActivity.onCreate(savedInstanceState);
    }

    /**
     * 统一设置为全屏
     * @return
     */
    @Override
    public boolean isFullScreen() {
        return true;
    }

    @Override
    public void setActionBar(ActionBar actionBar) {
        mActionBar = actionBar;
    }

    @Override
    public void showProgress(String text) {
        commonActivity.showProgress(text);
    }

    @Override
    public boolean dismissProgress() {
        return  commonActivity.dismissProgress();
    }

    @Override
    public void showProgressCycler() {
        commonActivity.showProgressCycler();
    }

    @Override
    public boolean dismissProgressCycler() {
        return  commonActivity.dismissProgressCycler();
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

    public void applyFragmentAnimatin(FragmentTransaction ft) {
        ft.setCustomAnimations(R.anim.activity_left_in, R.anim.activity_right_out);
    }

    public void applyAnimation() {
        //不知道为啥不管用
        overridePendingTransition(R.anim.activity_left_in, R.anim.activity_right_out);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        commonActivity.onDestroy();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        commonActivity.onConfigurationChanged(newConfig);
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onOrentaionChanged(Configuration newConfig) {

    }

    /**
     * 设置actionbar浮于content之上，同时运用透明度背景色
     * @param disableParentOperation 禁止BaseActivity的dispatch touch处理，由派生类自己改写操作(例如WallActivity涉及top bottom两个bar的控制)
     */
    public void requestActionbarFloating(boolean disableParentOperation) {
        commonActivity.requestActionbarFloating(disableParentOperation);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        commonActivity.dispatchTouchEvent(event);
        return super.dispatchTouchEvent(event);
    }

}
