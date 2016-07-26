package com.jing.app.jjgallery.viewsystem.main.timeline;

import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.viewsystem.main.AbsHomeActivity;
import com.jing.app.jjgallery.viewsystem.sub.waterfall.WaterfallFragment;
import com.king.lib.colorpicker.ColorPickerSelectionData;

import java.util.List;

public class HomeWaterfallActivity extends AbsHomeActivity {

    private WaterfallFragment fragment;

    @Override
    protected boolean isActionBarNeed() {
        return false;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_waterfall;
    }

    @Override
    protected void initController() {
    }

    @Override
    protected void setUpContentView() {
        setHomeViewPagerIndex(1);
        onWaterfallPage();
    }

    @Override
    protected View setUpRightMenu() {
        return null;
    }

    @Override
    protected void initBackgroundWork() {

    }

    public void onWaterfallPage() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (fragment == null) {
            fragment = new WaterfallFragment();
            fragment.setActionbar(mActionBar);
        }

        ft.replace(R.id.waterfall_fragment_container, fragment, "WaterfallFragment");
        ft.commit();
    }

    @Override
    protected void onActionIconClick(View view) {

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

}
