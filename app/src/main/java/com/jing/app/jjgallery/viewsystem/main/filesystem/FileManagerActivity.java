package com.jing.app.jjgallery.viewsystem.main.filesystem;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.jing.app.jjgallery.BaseActivity;
import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.presenter.main.filesystem.FileManagerPresenter;
import com.jing.app.jjgallery.viewsystem.publicview.ActionBar;

public class FileManagerActivity extends BaseActivity implements IFileManagerView {

    private FileManagerPresenter mPresenter;

    private IFragment mCurrentFragment;
    private IFilePage mCurrentPage;
    private IFragment mListFragment;
    private IFilePage mListPage;

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
    }

    @Override
    protected void initView() {
        mActionBar.addMenuIcon();
        mActionBar.setTitle(getString(R.string.tab_filemanager));
        mPresenter.startFileManagerPage(this);
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
        }
        ft.replace(R.id.fm_fragment_container, (Fragment) mListFragment, "FileManagerListFragment");
        ft.commit();

        mCurrentFragment = mListFragment;
    }

    @Override
    public void onThumbPage() {

    }

    @Override
    public void onIndexPage() {

    }

    @Override
    public void onBackPressed() {
        if (mCurrentFragment.getFilePage().onBack()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void onBack() {
        onBackPressed();
    }

    @Override
    public void onIconClick(View view) {
        mCurrentFragment.getFilePage().onIconClick(view);
    }

    @Override
    public void createMenu(MenuInflater menuInflater, Menu menu) {
        mCurrentFragment.getFilePage().createMenu(menuInflater, menu);
    }

    @Override
    public void onPrepareMenu(MenuInflater menuInflater, Menu menu) {
        mCurrentFragment.getFilePage().onPrepareMenu(menuInflater, menu);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return mCurrentFragment.getFilePage().onMenuItemClick(item);
    }

    @Override
    public void onTextChanged(String text, int start, int before, int count) {
        mCurrentFragment.getFilePage().onTextChanged(text, start, before, count);
    }

}
