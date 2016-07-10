package com.jing.app.jjgallery.viewsystem.main.filesystem;

import android.content.Context;
import android.content.res.Configuration;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.PopupMenu;

import com.jing.app.jjgallery.BasePresenter;
import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.controller.AccessController;
import com.jing.app.jjgallery.presenter.main.filesystem.FileManagerPresenter;
import com.jing.app.jjgallery.model.sub.IndexFlowCreator;
import com.jing.app.jjgallery.viewsystem.IPage;
import com.jing.app.jjgallery.viewsystem.publicview.ActionBar;
import com.jing.app.jjgallery.viewsystem.sub.key.AbsKeyAdapter;
import com.jing.app.jjgallery.viewsystem.sub.key.KeywordsFlow;

/**
 * Created by JingYang on 2016/7/7 0007.
 * Description:
 */
public class FileManagerIndexPage implements IPage, View.OnClickListener {

    private Context context;
    private View view;

    private KeywordsFlow keywordsFlow;
    private AbsKeyAdapter mKeyAdapter;

    private FileManagerPresenter mPresenter;

    public FileManagerIndexPage(Context context, View view) {
        this.context = context;
        this.view = view;
        keywordsFlow = (KeywordsFlow) view.findViewById(R.id.view_keyword_flow);
    }

    private void startKeywordsFlow() {
        keywordsFlow.setOnItemClickListener(this);
        mKeyAdapter = new FileIndexAdapter(keywordsFlow, IndexFlowCreator.createFolderIndex());
        mKeyAdapter.prepareKeyword();
        mKeyAdapter.feedKeyword();
        mKeyAdapter.goToShow(KeywordsFlow.ANIMATION_IN);
    }

    @Override
    public void onTouchEvent(MotionEvent event) {
        mKeyAdapter.onTouchEvent(event);
    }

    @Override
    public void initData() {
        startKeywordsFlow();
    }

    @Override
    public void setPresenter(BasePresenter presenter) {
        mPresenter = (FileManagerPresenter) presenter;
    }

    @Override
    public boolean onBack() {
        return false;
    }

    @Override
    public void onExit() {

    }

    @Override
    public void onIconClick(View view) {
        switch (view.getId()) {

            case R.id.actionbar_thumb:
                showViewModePopup(view);
                break;
        }
    }

    protected void showViewModePopup(View v) {
        PopupMenu menu = new PopupMenu(context, v);
        menu.getMenuInflater().inflate(R.menu.filemanager_view_mode, menu.getMenu());
        menu.getMenu().findItem(R.id.menu_index_view).setVisible(false);
        menu.show();
        menu.setOnMenuItemClickListener(viewModeListener);
    }

    PopupMenu.OnMenuItemClickListener viewModeListener = new PopupMenu.OnMenuItemClickListener() {

        @Override
        public boolean onMenuItemClick(MenuItem item) {

            switch (item.getItemId()) {
                case R.id.menu_list_view:
                    mPresenter.switchToListPage();
                    break;
                case R.id.menu_thumb_view:
                    mPresenter.switchToThumbPage();
                    break;
            }
            return true;
        }

    };
    @Override
    public void createMenu(MenuInflater menuInflater, Menu menu) {
        loadMenu(menuInflater, menu);
    }

    @Override
    public void onPrepareMenu(MenuInflater menuInflater, Menu menu) {
        loadMenu(menuInflater, menu);
    }

    public void loadMenu(MenuInflater menuInflater, Menu menu) {
        menu.clear();
        menuInflater.inflate(R.menu.home_file_manager, menu);
        menu.setGroupVisible(R.id.group_file, false);
        menu.setGroupVisible(R.id.group_home_public, true);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }

    @Override
    public void onTextChanged(String text, int start, int before, int count) {

    }

    @Override
    public void initActionbar(ActionBar actionBar) {
        if (AccessController.getInstance().getAccessMode() != AccessController.ACCESS_MODE_FILEMANAGER) {
            actionBar.clearActionIcon();
            actionBar.addThumbIcon();
            actionBar.addMenuIcon();
            actionBar.addColorIcon();
            actionBar.onConfiguration(context.getResources().getConfiguration().orientation);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {

    }

    @Override
    public void onClick(View v) {

    }
}
