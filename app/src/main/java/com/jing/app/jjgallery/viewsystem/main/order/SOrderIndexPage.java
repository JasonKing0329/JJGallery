package com.jing.app.jjgallery.viewsystem.main.order;

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
import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.bean.order.SOrder;
import com.jing.app.jjgallery.config.PreferenceKey;
import com.jing.app.jjgallery.controller.AccessController;
import com.jing.app.jjgallery.presenter.main.SettingProperties;
import com.jing.app.jjgallery.presenter.main.order.SOrderPresenter;
import com.jing.app.jjgallery.service.image.lru.ImageLoader;
import com.jing.app.jjgallery.viewsystem.ActivityManager;
import com.jing.app.jjgallery.viewsystem.IPage;
import com.jing.app.jjgallery.viewsystem.publicview.ActionBar;
import com.jing.app.jjgallery.viewsystem.sub.key.AbsKeyAdapter;
import com.jing.app.jjgallery.viewsystem.sub.key.Keyword;
import com.jing.app.jjgallery.viewsystem.sub.key.KeywordsFlow;
import com.jing.app.jjgallery.viewsystem.sub.key.OnKeywordClickListener;

import java.util.List;

/**
 * Created by JingYang on 2016/7/14 0014.
 * Description:
 */
public class SOrderIndexPage implements IPage, ISOrderDataCallback, OnKeywordClickListener {

    private Context context;

    private ImageView bkView;
    private KeywordsFlow keywordsFlow;
    private AbsKeyAdapter mKeyAdapter;

    private SOrderPresenter mPresenter;

    public SOrderIndexPage(Context context, View view) {
        this.context = context;
        bkView = (ImageView) view.findViewById(R.id.fm_index_bk);
        keywordsFlow = (KeywordsFlow) view.findViewById(R.id.view_keyword_flow);
        keywordsFlow.setBackgroundColor(Color.TRANSPARENT);
        keywordsFlow.setTextColorMode(KeywordsFlow.TEXT_COLOR_LIGHT);
        keywordsFlow.setOnKeywordClickListener(this);

        String bkPath = SettingProperties.getPreference(context, PreferenceKey.PREF_BG_SORDER_INDEX);
        if (bkPath != null) {
            ImageLoader.getInstance().loadImage(bkPath, bkView);
        }
    }

    @Override
    public void initData() {
        mPresenter.setDataCallback(this);
        mPresenter.loadAllOrders();
    }

    @Override
    public void onQueryAllOrders(List<SOrder> list) {
        mKeyAdapter = new SOrderIndexAdapter(keywordsFlow, list);
        mKeyAdapter.prepareKeyword();
        mKeyAdapter.feedKeyword();
        mKeyAdapter.goToShow(KeywordsFlow.ANIMATION_IN);
    }

    @Override
    public void setPresenter(BasePresenter presenter) {
        mPresenter = (SOrderPresenter) presenter;
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
                case R.id.menu_grid_view:
                    mPresenter.switchToGridPage();
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
        menuInflater.inflate(R.menu.home_sorder, menu);
        menu.setGroupVisible(R.id.group_sorder, false);
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
    public void onTouchEvent(MotionEvent event) {
        if (mKeyAdapter != null) {
            mKeyAdapter.onTouchEvent(event);
        }
    }

    @Override
    public void onKeywordClick(View view, Keyword keyword) {
        SOrder order = (SOrder) keyword.getObject();
        ActivityManager.startSurfActivity((Activity) context, order);
    }

    public void onIndexBgChanged(String path) {
        if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            ImageLoader.getInstance().loadImage(path, bkView);
        }
    }
    public void onIndexBgLandChanged(String path) {
        if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            ImageLoader.getInstance().loadImage(path, bkView);
        }
    }
}
