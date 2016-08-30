package com.jing.app.jjgallery.viewsystem.main.timeline;

import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.viewsystem.main.AbsHomeActivity;
import com.jing.app.jjgallery.viewsystem.sub.waterfall.WaterfallAllFileFragment;
import com.jing.app.jjgallery.viewsystem.sub.waterfall.WaterfallFragment;
import com.king.lib.colorpicker.ColorPickerSelectionData;

import java.util.List;

public class HomeWaterfallActivity extends AbsHomeActivity {

    private WaterfallFragment fragment;

    @Override
    public boolean isActionBarNeed() {
        return true;
    }

    @Override
    public int getContentView() {
        return R.layout.activity_home_waterfall;
    }

    @Override
    public void initController() {
    }

    @Override
    protected void setUpContentView() {
        setHomeViewPagerIndex(2);
        onWaterfallPage();
    }

    @Override
    protected View setUpRightMenu() {
        View view = LayoutInflater.from(this).inflate(R.layout.layout_sliding_right_timeline, null);
        new RightMenuPage(view, RightMenuPage.INDEX_WATERFALL);
        return view;
    }

    @Override
    public void initBackgroundWork() {

    }

    public void onWaterfallPage() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (fragment == null) {
            fragment = new WaterfallAllFileFragment();
            fragment.setActionbar(mActionBar);
        }

        ft.replace(R.id.waterfall_fragment_container, fragment, "WaterfallFragment");
        ft.commit();
    }

    @Override
    protected void onActionIconClick(View view) {
        fragment.onActionIconClick(view);
    }

    @Override
    protected boolean handleBack() {
        return fragment.onBackPressed();
    }

    @Override
    protected boolean needOptionWhenExit() {
        return false;
    }

    @Override
    protected void onExit() {

    }

    @Override
    protected List<ColorPickerSelectionData> getListSelectionData() {
        return null;
    }

    @Override
    public void createMenu(MenuInflater menuInflater, Menu menu) {
        super.createMenu(menuInflater, menu);
        fragment.createMenu(menuInflater, menu);
    }

    @Override
    public void onPrepareMenu(MenuInflater menuInflater, Menu menu) {
        super.onPrepareMenu(menuInflater, menu);
        fragment.onPrepareMenu(menuInflater, menu);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        boolean resSuper = super.onMenuItemClick(item);
        if (resSuper) {
            return true;
        }
        return fragment.onMenuItemClick(item);
    }
}
