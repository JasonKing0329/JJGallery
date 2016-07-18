package com.jing.app.jjgallery.viewsystem.main.filesystem;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;

import com.jing.app.jjgallery.BasePresenter;
import com.jing.app.jjgallery.BaseSlidingActivity;
import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.config.PreferenceKey;
import com.jing.app.jjgallery.controller.AccessController;
import com.jing.app.jjgallery.presenter.main.SettingProperties;
import com.jing.app.jjgallery.presenter.main.filesystem.FileManagerPresenter;
import com.jing.app.jjgallery.service.image.SImageLoader;
import com.jing.app.jjgallery.viewsystem.ActivityManager;
import com.jing.app.jjgallery.viewsystem.IPage;
import com.jing.app.jjgallery.viewsystem.publicview.ActionBar;
import com.jing.app.jjgallery.viewsystem.sub.key.AbsKeyAdapter;
import com.jing.app.jjgallery.viewsystem.sub.key.Keyword;
import com.jing.app.jjgallery.viewsystem.sub.key.KeywordsFlow;
import com.jing.app.jjgallery.viewsystem.sub.key.OnKeywordClickListener;

import java.util.List;

/**
 * Created by JingYang on 2016/7/7 0007.
 * Description:
 */
public class FileManagerIndexPage implements IPage, IFileDataCallback, OnKeywordClickListener {

    private Context context;

    private ImageView bkView;
    private KeywordsFlow keywordsFlow;
    private AbsKeyAdapter mKeyAdapter;

    private FileManagerPresenter mPresenter;

    private List<String> folderList;

    public FileManagerIndexPage(Context context, View view) {
        this.context = context;
        bkView = (ImageView) view.findViewById(R.id.fm_index_bk);
        keywordsFlow = (KeywordsFlow) view.findViewById(R.id.view_keyword_flow);
        keywordsFlow.setBackgroundColor(Color.TRANSPARENT);
        keywordsFlow.setTextColorMode(KeywordsFlow.TEXT_COLOR_LIGHT);
        keywordsFlow.setOnKeywordClickListener(this);

        updateBackground(context.getResources().getConfiguration().orientation);
    }

    private void updateBackground(int orientation) {
        String bkPath = null;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            bkPath = SettingProperties.getPreference(context, PreferenceKey.PREF_BG_FM_INDEX_LAND);
        }
        else {
            bkPath = SettingProperties.getPreference(context, PreferenceKey.PREF_BG_FM_INDEX);
        }
        if (bkPath != null) {
            SImageLoader.getInstance().displayImage(bkPath, bkView);
        }
    }

    @Override
    public void initData() {

        ((BaseSlidingActivity) context).showProgressCycler();
        mPresenter.setFileDataCallback(this);
        mPresenter.loadAllFolders();
    }

    @Override
    public void onLoadAllFolders(List<String> folders) {
        folderList = folders;
        mKeyAdapter = new FileIndexAdapter(keywordsFlow, folders);
        mKeyAdapter.prepareKeyword();
        mKeyAdapter.feedKeyword();
        mKeyAdapter.goToShow(KeywordsFlow.ANIMATION_IN);
        ((BaseSlidingActivity) context).dismissProgressCycler();
    }

    @Override
    public void onTouchEvent(MotionEvent event) {
        if (mKeyAdapter != null) {
            mKeyAdapter.onTouchEvent(event);
        }
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
        updateBackground(newConfig.orientation);
        // keywordsFlow的item坐标是根据width/height计算的，转屏发生了变化所以必须重布局
        // 并且在布局完成后post重新feedKeyword进行重计算
        keywordsFlow.requestLayout();
        keywordsFlow.invalidate();

        // 防止第一次进入由于加载目录时间过长，转屏过程发生空指针异常
        if (folderList != null) {
            // 必须重新刷新
            keywordsFlow.post(new Runnable() {
                @Override
                public void run() {
                    keywordsFlow.rubKeywords();
                    mKeyAdapter.feedKeyword();
                    keywordsFlow.go2Show(KeywordsFlow.ANIMATION_OUT);
                }
            });
        }
    }

    @Override
    public void onKeywordClick(View view, Keyword keyword) {
        String path = (String) keyword.getObject();
        ActivityManager.startSurfActivity((Activity) context, path);
    }

    public void onIndexBgChanged(String path) {
        if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            SImageLoader.getInstance().displayImage(path, bkView);
        }
    }

    public void onIndexBgLandChanged(String path) {
        if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            SImageLoader.getInstance().displayImage(path, bkView);
        }
    }
}
