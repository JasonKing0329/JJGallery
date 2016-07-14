package com.jing.app.jjgallery.viewsystem.main.order;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.presenter.main.order.SOrderPresenter;
import com.jing.app.jjgallery.presenter.sub.ThumbPresenter;
import com.jing.app.jjgallery.res.AppResManager;
import com.jing.app.jjgallery.viewsystem.IFragment;
import com.jing.app.jjgallery.viewsystem.main.AbsHomeActivity;
import com.jing.app.jjgallery.viewsystem.main.bg.BackgroundManager;
import com.jing.app.jjgallery.viewsystem.main.bg.SOrderSubscriber;
import com.jing.app.jjgallery.viewsystem.main.filesystem.SOrderIndexPage;
import com.king.lib.colorpicker.ColorPickerSelectionData;

import java.util.List;

public class SOrderActivity extends AbsHomeActivity implements ISOrderView, SOrderSubscriber {

    private SOrderPresenter mPresenter;
    private IFragment mCurrentFragment;
    private IFragment mGridFragment, mIndexFragment, mThumbFragment;

    @Override
    protected boolean isActionBarNeed() {
        return true;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_sorder;
    }

    @Override
    protected void initController() {
        mPresenter = new SOrderPresenter(this);
        BackgroundManager.getInstance().addSOrderSubscriber(this);
    }

    @Override
    protected void initBackgroundWork() {

    }
    @Override
    protected void setUpContentView() {
        mActionBar.useMenuLeftIcon();
        mActionBar.addMenuIcon();
        mActionBar.setTitle(getString(R.string.tab_sorder));
        mPresenter.startSOrderPage(this);
    }

    @Override
    protected void setUpLeftMenu() {

    }

    @Override
    protected void setUpRightMenu() {

    }

    @Override
    public void onGridPage() {

    }

    @Override
    public void onThumbPage() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (mThumbFragment == null) {
            mThumbFragment = new SOrderThumbFragment();
            mThumbFragment.setActionbar(mActionBar);
            mThumbFragment.setPresenter(new ThumbPresenter());
        }
        else {
            mThumbFragment.getPage().initActionbar(mActionBar);
        }

        setCurrentFragment(ft, mThumbFragment, "SOrderThumbFragment");
    }

    @Override
    public void onIndexPage() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (mIndexFragment == null) {
            mIndexFragment = new SOrderIndexFragment();
            mIndexFragment.setActionbar(mActionBar);
            mIndexFragment.setPresenter(mPresenter);
        }
        else {
            mIndexFragment.getPage().initActionbar(mActionBar);
        }

        setCurrentFragment(ft, mIndexFragment, "SOrderIndexFragment");
    }

    private void setCurrentFragment(FragmentTransaction ft, IFragment fragment, String tag) {
        // no animation at first time
        if (mCurrentFragment != null) {
            if (mCurrentFragment == mGridFragment) {// current fragment is list fragment
                if (fragment == mIndexFragment) {// index view
                    applyAnimatinLeftIn(ft);
                }
                else {// thumb view
                    applyAnimatinRightIn(ft);
                }
            }
            else if (mCurrentFragment == mIndexFragment) {// current fragment is index fragment
                if (fragment == mGridFragment) {// list view
                    applyAnimatinRightIn(ft);
                }
                else {// thumb view
                    applyAnimatinLeftIn(ft);
                }
            }
            else {// current fragment is thumb fragment
                if (fragment == mGridFragment) {// list view
                    applyAnimatinLeftIn(ft);
                }
                else {// index view
                    applyAnimatinRightIn(ft);
                }
            }
        }
        ft.replace(R.id.sorder_fragment_container, (Fragment) fragment, tag);
        ft.commit();

        mCurrentFragment = fragment;
    }

    @Override
    protected void onActionIconClick(View view) {
        if (view.getId() == R.id.actionbar_menu_left) {
            showMenu();
        }
        else {
            mCurrentFragment.getPage().onIconClick(view);
        }
    }

    @Override
    protected boolean handleBack() {
        return mCurrentFragment.getPage().onBack();
    }

    @Override
    protected boolean needOptionWhenExit() {
        return false;
    }

    @Override
    protected void onExit() {
        mCurrentFragment.getPage().onExit();
    }

    @Override
    public void onTextChanged(String text, int start, int before, int count) {
        mCurrentFragment.getPage().onTextChanged(text, start, before, count);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mCurrentFragment.getPage().onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    @Override
    public void createMenu(MenuInflater menuInflater, Menu menu) {
        mCurrentFragment.getPage().createMenu(menuInflater, menu);
    }

    @Override
    public void onPrepareMenu(MenuInflater menuInflater, Menu menu) {
        mCurrentFragment.getPage().onPrepareMenu(menuInflater, menu);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        super.onMenuItemClick(item);
        return mCurrentFragment.getPage().onMenuItemClick(item);
    }

    @Override
    protected List<ColorPickerSelectionData> getListSelectionData() {
        if (mCurrentFragment.getColorPage() != null) {
            return mCurrentFragment.getColorPage().getColorPickerSelectionData();
        }
        else {
            return new AppResManager().getHomeList(this);
        }
    }

    @Override
    public void onColorChanged(String key, int newColor) {
        if (mCurrentFragment.getColorPage() != null) {
            mCurrentFragment.getColorPage().onColorChanged(key, newColor);
        }
        super.onColorChanged(key, newColor);
    }

    @Override
    public void onApplyDefaultColors() {
        if (mCurrentFragment.getColorPage() != null) {
            mCurrentFragment.getColorPage().onApplyDefaultColors();
        }
        super.onApplyDefaultColors();
    }

    @Override
    protected void applyExtendColors() {
        if (mCurrentFragment.getColorPage() != null) {
            mCurrentFragment.getColorPage().applyExtendColors();
        }
        super.applyExtendColors();
    }

    @Override
    public void onIndexBgChanged(String path) {
        if (mCurrentFragment == mIndexFragment) {
            ((SOrderIndexPage) mIndexFragment.getPage()).onIndexBgChanged(path);
        }
    }

    @Override
    public void onIndexBgLandChanged(String path) {
        if (mCurrentFragment == mIndexFragment) {
            ((SOrderIndexPage) mIndexFragment.getPage()).onIndexBgLandChanged(path);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BackgroundManager.getInstance().removeSOrderSubscriber(this);
    }

    public SOrderPresenter getPresenter() {
        return mPresenter;
    }
}
