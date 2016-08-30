package com.jing.app.jjgallery.viewsystem.sub.waterfall;

import android.support.v4.app.FragmentTransaction;
import android.transition.Slide;
import android.transition.TransitionSet;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import com.jing.app.jjgallery.BaseActivity;
import com.jing.app.jjgallery.R;

public class WaterfallActivity extends BaseActivity {

    public static final String KEY_TYPE = "waterfall_type";
    public static final int FOLDER = 0;
    public static final int SORDER = 1;

    public static final String KEY_FOLDER_PATH = "waterfall_folder_path";
    public static final String KEY_ORDER_ID = "waterfall_order_id";

    /**
     * 转场动画时间
     */
    private final int TIME_TRANS_ANIM_DURITION = 500;

    private WaterfallFragment fragment;

    @Override
    public boolean isActionBarNeed() {
        return true;
    }

    @Override
    public int getContentView() {
        return R.layout.activity_waterfall;
    }

    @Override
    public void initController() {

    }

    @Override
    public void initView() {
        onWaterfallPage(getIntent().getIntExtra(KEY_TYPE, FOLDER));

        // 加入转场动画，从上个界面注册的transitionName = trans_wall的view放大而出
        findViewById(R.id.waterfall_parent).setTransitionName(getString(R.string.trans_waterfall));
        TransitionSet set = new TransitionSet();
        Slide slide = new Slide(Gravity.BOTTOM);
        slide.addTarget(R.id.waterfall_parent);
        slide.setDuration(TIME_TRANS_ANIM_DURITION);
        getWindow().setEnterTransition(set);
    }

    @Override
    public void initBackgroundWork() {

    }

    /**
     *
     * @param type see FOLDER or SORDER
     */
    public void onWaterfallPage(int type) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (fragment == null) {
            if (type == FOLDER) {
                WaterfallFolderFragment fragment = new WaterfallFolderFragment();
                fragment.setFolderPath(getIntent().getStringExtra(KEY_FOLDER_PATH));
                this.fragment = fragment;
            }
            else if (type == SORDER) {
                WaterfallOrderFragment fragment = new WaterfallOrderFragment();
                fragment.setOrderId(getIntent().getIntExtra(KEY_ORDER_ID, -1));
                this.fragment = fragment;
            }
            fragment.setActionbar(mActionBar);
        }

        ft.replace(R.id.waterfall_fragment_container, fragment, "WaterfallFragment");
        ft.commit();
    }

    @Override
    public void onIconClick(View view) {
        fragment.onActionIconClick(view);
    }

    @Override
    public void onBack() {
        if (fragment.onBackPressed()) {
            return;
        }
        finish();
    }

    @Override
    public void onBackPressed() {
        if (fragment.onBackPressed()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void onPrepareMenu(MenuInflater menuInflater, Menu menu) {
        super.onPrepareMenu(menuInflater, menu);
        fragment.onPrepareMenu(menuInflater, menu);
    }

    @Override
    public void createMenu(MenuInflater menuInflater, Menu menu) {
        super.createMenu(menuInflater, menu);
        fragment.createMenu(menuInflater, menu);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        super.onMenuItemClick(item);
        return fragment.onMenuItemClick(item);
    }
}
