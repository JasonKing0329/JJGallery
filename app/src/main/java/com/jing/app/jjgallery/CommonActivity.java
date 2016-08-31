package com.jing.app.jjgallery;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.jing.app.jjgallery.controller.ThemeManager;
import com.jing.app.jjgallery.util.DisplayHelper;
import com.jing.app.jjgallery.viewsystem.ProgressProvider;
import com.jing.app.jjgallery.viewsystem.main.bg.BackgroundManager;
import com.jing.app.jjgallery.viewsystem.publicview.ActionBar;
import com.jing.app.jjgallery.viewsystem.publicview.ActionBarManager;
import com.jing.app.jjgallery.viewsystem.publicview.ProgressManager;
import com.jing.app.jjgallery.viewsystem.publicview.toast.TastyToast;

/**
 * Created by Administrator on 2016/8/29.
 */
public class CommonActivity {

    private Activity activity;
    private ActivityAction activityAction;

    private ViewGroup mActionbarGroup;
    private ViewGroup mContentGroup;

    protected ActionBar mActionBar;

    private ProgressDialog progressDialog;

    private int curOrientation;

    private ProgressManager progressManager;
    private ActionBarManager actionBarManager;
    private boolean isActionbarFloating;

    public void preOnCreate(Activity activity, ActivityAction action) {
        this.activity = activity;
        activityAction = action;
        ((JJApplication) activity.getApplication()).addActivity(activity);
        if (activityAction.isFullScreen()) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            DisplayHelper.enableFullScreen();
        }
        DisplayHelper.disableScreenshot(activity);

        activity.setTheme(ThemeManager.getInstance().getDefaultTheme(activity));
    }

    public void onCreate(Bundle savedInstanceState) {
        curOrientation = activity.getResources().getConfiguration().orientation;
        activity.setContentView(R.layout.activity_base_sliding);

        mActionbarGroup = (ViewGroup) activity.findViewById(R.id.actionbar);
        mContentGroup = (ViewGroup) activity.findViewById(R.id.frame_content);
        progressManager = new ProgressManager(activity);
        BackgroundManager.getInstance().addProgressSubscriber(progressManager);

        if (activityAction.isActionBarNeed()) {
            View view = activity.getLayoutInflater().inflate(R.layout.actionbar_l, null);
            mActionbarGroup.addView(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            mActionBar = new ActionBar(activity, view);
            activityAction.setActionBar(mActionBar);
            actionBarManager = new ActionBarManager(activity, mActionBar);
            mActionBar.setActionIconListener((ActionBar.ActionIconListener) activity);
            mActionBar.setActionMenuListener((ActionBar.ActionMenuListener) activity);
            mActionBar.setActionSearchListener((ActionBar.ActionSearchListener) activity);
        }
        View content = activity.getLayoutInflater().inflate(activityAction.getContentView(), null);
        mContentGroup.addView(content, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        progressDialog = new ProgressDialog(activity);
        activityAction.initController();
        activityAction.initView();

        activityAction.initBackgroundWork();
    }

    public void showProgress(String text) {
        progressDialog.setMessage(text);
        progressDialog.show();
    }

    public boolean dismissProgress() {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
            return true;
        }
        return  false;
    }

    public void showProgressCycler() {
        if (progressManager == null) {
            progressManager = new ProgressManager(activity);
            BackgroundManager.getInstance().addProgressSubscriber(progressManager);
        }
        progressManager.showProgressCycler();
    }

    public boolean dismissProgressCycler() {
        if (progressManager.isShowing()) {
            progressManager.dismissProgressCycler();
            return true;
        }
        return false;
    }

    public void showToastLong(String text) {
        Toast.makeText(activity, text, Toast.LENGTH_LONG).show();
    }

    public void showToastShort(String text) {
        Toast.makeText(activity, text, Toast.LENGTH_SHORT).show();
    }

    public void showToastLong(String text, int type) {
        showToastLib(text, type, TastyToast.LENGTH_LONG);
    }

    public void showToastShort(String text, int type) {
        showToastLib(text, type, TastyToast.LENGTH_SHORT);
    }

    public void showToastLib(String text, int type, int time) {
        switch (type) {
            case ProgressProvider.TOAST_SUCCESS:
                TastyToast.makeText(activity, text, time, TastyToast.SUCCESS);
                break;
            case ProgressProvider.TOAST_ERROR:
                TastyToast.makeText(activity, text, time, TastyToast.ERROR);
                break;
            case ProgressProvider.TOAST_WARNING:
                TastyToast.makeText(activity, text, time, TastyToast.WARNING);
                break;
            case ProgressProvider.TOAST_INFOR:
                TastyToast.makeText(activity, text, time, TastyToast.INFO);
                break;
            case ProgressProvider.TOAST_DEFAULT:
                TastyToast.makeText(activity, text, time, TastyToast.DEFAULT);
                break;
        }
    }

    protected void onDestroy() {
        ((JJApplication) activity.getApplication()).removeActivity(activity);
        BackgroundManager.getInstance().removeProgressSubscriber(progressManager);
    }

    public void onConfigurationChanged(Configuration newConfig) {
        if (newConfig.orientation != curOrientation) {
            curOrientation = newConfig.orientation;
            if (mActionBar != null) {
                mActionBar.onConfiguration(newConfig.orientation);
            }
            activityAction.onOrentaionChanged(newConfig);
        }
    }

    /**
     * 设置actionbar浮于content之上，同时运用透明度背景色
     * @param disableParentOperation 禁止BaseActivity的dispatch touch处理，由派生类自己改写操作(例如WallActivity涉及top bottom两个bar的控制)
     */
    public void requestActionbarFloating(boolean disableParentOperation) {
        mActionBar.setBackgroundColor(activity.getResources().getColor(ThemeManager.getInstance().getWallActionbarColorId(activity)));
        RelativeLayout container = (RelativeLayout) activity.findViewById(R.id.main_container);
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

    public boolean dispatchTouchEvent(MotionEvent event) {
        if (isActionbarFloating) {
            if (actionBarManager != null) {
                actionBarManager.dispatchTouchEvent(event);
                return true;
            }
        }
        return false;
    }

    public ActionBar getActionBar() {
        return mActionBar;
    }
}
