package com.jing.app.jjgallery.viewsystem.main.order;

import android.content.Context;
import android.content.res.Configuration;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.presenter.main.order.SOrderPresenter;
import com.jing.app.jjgallery.presenter.sub.ThumbPresenter;
import com.jing.app.jjgallery.res.AppResManager;
import com.jing.app.jjgallery.util.DebugLog;
import com.jing.app.jjgallery.viewsystem.IFragment;
import com.jing.app.jjgallery.viewsystem.main.AbsHomeActivity;
import com.jing.app.jjgallery.viewsystem.main.bg.BackgroundManager;
import com.jing.app.jjgallery.viewsystem.main.bg.SOrderSubscriber;
import com.king.lib.colorpicker.ColorPickerSelectionData;

import java.util.List;

public class SOrderActivity extends AbsHomeActivity implements ISOrderView, SOrderSubscriber {

    private SOrderPresenter mPresenter;
    private IFragment mCurrentFragment;
    private IFragment mGridFragment, mIndexFragment, mThumbFragment, mAccessFragment;

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
        setHomeViewPagerIndex(1);

        mActionBar.useMenuLeftIcon();
        mActionBar.addMenuIcon();
        mActionBar.setTitle(getString(R.string.tab_sorder));
        mPresenter.startSOrderPage(this);
    }

    @Override
    protected View setUpRightMenu() {
        View view = LayoutInflater.from(this).inflate(R.layout.layout_sliding_right_sorder, null);
        new RightMenuPage(view, this);
        return view;
    }

    @Override
    public void onGridPage() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (mGridFragment == null) {
            mGridFragment = new SOrderGridFragment();
            mGridFragment.setActionbar(mActionBar);
            mGridFragment.setPresenter(mPresenter);
        }
        else {
            mGridFragment.getPage().initActionbar(mActionBar);
        }

        Fragment fragment = getToDeleteFragment();
        setCurrentFragment(ft, mGridFragment, "SOrderGridFragment");
        removeFragment(fragment);
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

        Fragment fragment = getToDeleteFragment();
        setCurrentFragment(ft, mThumbFragment, "SOrderThumbFragment");
        removeFragment(fragment);
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

        Fragment fragment = getToDeleteFragment();
        setCurrentFragment(ft, mIndexFragment, "SOrderIndexFragment");
        removeFragment(fragment);
    }

    @Override
    public void onAccessCountPage() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (mAccessFragment == null) {
            mAccessFragment = new SOrderCardFragment();
            mAccessFragment.setActionbar(mActionBar);
            mAccessFragment.setPresenter(mPresenter);
        }
        else {
            mAccessFragment.getPage().initActionbar(mActionBar);
        }

        Fragment fragment = getToDeleteFragment();
        setCurrentFragment(ft, mAccessFragment, "SOrderCardFragment");
        removeFragment(fragment);
    }

    // sorder的几个fragment内存开销过大，不保存fragment实例
    private void removeFragment(Fragment fragment) {
        DebugLog.e("");
        if (fragment == null) {
            return;
        }
        getSupportFragmentManager().beginTransaction().remove(fragment).commit();
        if (fragment == mGridFragment) {
            mGridFragment = null;
        }
        else if (fragment == mThumbFragment) {
            mThumbFragment = null;
        }
        else if (fragment == mIndexFragment) {
            mIndexFragment = null;
        }
        else if (fragment == mAccessFragment) {
            mAccessFragment = null;
        }
    }

    private Fragment getToDeleteFragment() {
        if (mCurrentFragment == mGridFragment) {
            return (Fragment) mGridFragment;
        }
        else if (mCurrentFragment == mAccessFragment) {
            return (Fragment) mAccessFragment;
        }
        else if (mCurrentFragment == mThumbFragment) {
            return (Fragment) mThumbFragment;
        }
        else {
            return (Fragment) mIndexFragment;
        }
    }

    @Override
    public void removeAccessCountPage() {
        mAccessFragment = null;
        mCurrentFragment = null;
    }

    @Override
    public Context getContext() {
        return this;
    }

    private void setCurrentFragment(FragmentTransaction ft, IFragment fragment, String tag) {
        // no animation at first time
        if (mCurrentFragment != null) {
            if (mCurrentFragment == mGridFragment) {// current fragment is list fragment
                if (fragment == mAccessFragment) {
                    applyAnimatinLeftIn(ft);
                }
                else {
                    applyAnimatinRightIn(ft);
                }
            }
            else if (mCurrentFragment == mIndexFragment) {// current fragment is index fragment
                if (fragment == mAccessFragment) {
                    applyAnimatinRightIn(ft);
                }
                else {
                    applyAnimatinLeftIn(ft);
                }
            }
            else if (mCurrentFragment == mAccessFragment) {// current fragment is access count fragment
                if (fragment == mGridFragment) {// grid view
                    applyAnimatinRightIn(ft);
                }
                else {
                    applyAnimatinLeftIn(ft);
                }
            }
            else {// current fragment is thumb fragment
                if (fragment == mGridFragment) {
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
        mCurrentFragment.getPage().onIconClick(view);
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
        finish();
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
    protected void onOrentaionChanged(Configuration newConfig) {
        super.onOrentaionChanged(newConfig);
        mCurrentFragment.getPage().onConfigurationChanged(newConfig);
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
