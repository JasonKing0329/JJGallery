package com.jing.app.jjgallery.viewsystem.main.filesystem;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.jing.app.jjgallery.BaseActivity;
import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.presenter.main.FileManagerPresenter;
import com.jing.app.jjgallery.viewsystem.publicview.ActionBar;

public class FileManagerActivity extends BaseActivity implements IFileManagerView, ActionBar.ActionMenuListener
    , ActionBar.ActionIconListener, ActionBar.ActionSearchListener{

    private ViewGroup mContainer;

    private FileManagerPresenter mPresenter;

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
        mContainer = (ViewGroup) findViewById(R.id.fm_fragment_container);
        mPresenter.startFileManagerPage(this);
    }

    @Override
    protected void initBackgroundWork() {

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
    public void onTextChanged(String text, int start, int before, int count) {

    }

    @Override
    public void onListPage() {

    }

    @Override
    public void onThumbPage() {

    }

    @Override
    public void onIndexPage() {

    }
}
