package com.jing.app.jjgallery.viewsystem.sub.waterfall;

import android.support.v4.app.FragmentTransaction;
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

    private WaterfallFragment fragment;

    @Override
    protected boolean isActionBarNeed() {
        return true;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_waterfall;
    }

    @Override
    protected void initController() {

    }

    @Override
    protected void initView() {
        onWaterfallPage(getIntent().getIntExtra(KEY_TYPE, FOLDER));
    }

    @Override
    protected void initBackgroundWork() {

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
