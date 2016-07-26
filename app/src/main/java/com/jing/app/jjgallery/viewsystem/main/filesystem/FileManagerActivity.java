package com.jing.app.jjgallery.viewsystem.main.filesystem;

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
import com.jing.app.jjgallery.presenter.main.filesystem.FileManagerPresenter;
import com.jing.app.jjgallery.presenter.sub.ThumbPresenter;
import com.jing.app.jjgallery.res.AppResManager;
import com.jing.app.jjgallery.viewsystem.main.AbsHomeActivity;
import com.jing.app.jjgallery.viewsystem.IFragment;
import com.jing.app.jjgallery.viewsystem.main.bg.BackgroundManager;
import com.jing.app.jjgallery.viewsystem.main.bg.FMBgSubscriber;
import com.king.lib.colorpicker.ColorPickerSelectionData;

import java.util.List;

/**
 * 文件管理器Activity
 * 负责list/thumb/index三个page的切换调度，下发由activity触发的回调方法以及公共监听方法
 * 只负责逻辑层面的调度
 * UI以及各自的功能交由三个Page页自己处理
 */
public class FileManagerActivity extends AbsHomeActivity implements IFileManagerView, FMBgSubscriber {

    private FileManagerPresenter mPresenter;

    private IFragment mCurrentFragment;
    private IFragment mListFragment, mIndexFragment, mThumbFragment;

    @Override
    protected boolean isActionBarNeed() {
        return true;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_file_manager;
    }

    @Override
    protected void initController() {
        mPresenter = new FileManagerPresenter(this);
        BackgroundManager.getInstance().addFMBgSubscriber(this);
    }

    @Override
    protected void setUpContentView() {
        mActionBar.useMenuLeftIcon();
        mActionBar.addMenuIcon();
        mActionBar.setTitle(getString(R.string.tab_filemanager));
        mPresenter.startFileManagerPage(this);
    }

    @Override
    protected View setUpRightMenu() {
        View view = LayoutInflater.from(this).inflate(R.layout.layout_sliding_right_fm, null);
        new RightMenuPage(view, this);
        return view;
    }

    @Override
    protected void initBackgroundWork() {

    }

    @Override
    public void onListPage() {

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (mListFragment == null) {
            mListFragment = new FileManagerListFragment();
            mListFragment.setActionbar(mActionBar);
            mListFragment.setPresenter(mPresenter);
        }
        else {
            mListFragment.getPage().initActionbar(mActionBar);
        }

        setCurrentFragment(ft, mListFragment, "FileManagerListFragment");
    }

    @Override
    public void onThumbPage() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (mThumbFragment == null) {
            mThumbFragment = new FileManagerThumbFragment();
//            mThumbFragment = new SOrderThumbFragment();
            mThumbFragment.setActionbar(mActionBar);
            mThumbFragment.setPresenter(new ThumbPresenter());
        }
        else {
            mThumbFragment.getPage().initActionbar(mActionBar);
        }

        setCurrentFragment(ft, mThumbFragment, "FileManagerThumbFragment");
    }

    @Override
    public void onIndexPage() {

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (mIndexFragment == null) {
            mIndexFragment = new FileManagerIndexFragment();
            mIndexFragment.setActionbar(mActionBar);
            mIndexFragment.setPresenter(mPresenter);
        }
        else {
            mIndexFragment.getPage().initActionbar(mActionBar);
        }

        setCurrentFragment(ft, mIndexFragment, "FileManagerIndexFragment");
    }

    private void setCurrentFragment(FragmentTransaction ft, IFragment fragment, String tag) {
        // no animation at first time
        if (mCurrentFragment != null) {
            if (mCurrentFragment == mListFragment) {// current fragment is list fragment
                if (fragment == mIndexFragment) {// index view
                    applyAnimatinLeftIn(ft);
                }
                else {// thumb view
                    applyAnimatinRightIn(ft);
                }
            }
            else if (mCurrentFragment == mIndexFragment) {// current fragment is index fragment
                if (fragment == mListFragment) {// list view
                    applyAnimatinRightIn(ft);
                }
                else {// thumb view
                    applyAnimatinLeftIn(ft);
                }
            }
            else {// current fragment is thumb fragment
                if (fragment == mListFragment) {// list view
                    applyAnimatinLeftIn(ft);
                }
                else {// index view
                    applyAnimatinRightIn(ft);
                }
            }
        }
        ft.replace(R.id.fm_fragment_container, (Fragment) fragment, tag);
        ft.commit();

        mCurrentFragment = fragment;
    }

    @Override
    public void onBack() {
        if (!handleBack()) {
            onBackPressed();
        }
    }

    @Override
    public void onActionIconClick(View view) {
        mCurrentFragment.getPage().onIconClick(view);
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

    public FileManagerPresenter getPresenter() {
        return mPresenter;
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
            ((FileManagerIndexPage) mIndexFragment.getPage()).onIndexBgChanged(path);
        }
    }

    @Override
    public void onIndexBgLandChanged(String path) {
        if (mCurrentFragment == mIndexFragment) {
            ((FileManagerIndexPage) mIndexFragment.getPage()).onIndexBgLandChanged(path);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BackgroundManager.getInstance().removeFMBgSubscriber(this);
    }
}
