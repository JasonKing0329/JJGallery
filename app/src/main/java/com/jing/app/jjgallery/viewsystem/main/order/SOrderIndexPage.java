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

import com.jing.app.jjgallery.BasePresenter;
import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.bean.order.SOrder;
import com.jing.app.jjgallery.config.PreferenceKey;
import com.jing.app.jjgallery.config.PreferenceValue;
import com.jing.app.jjgallery.presenter.main.SettingProperties;
import com.jing.app.jjgallery.presenter.main.order.SOrderPresenter;
import com.jing.app.jjgallery.service.image.SImageLoader;
import com.jing.app.jjgallery.viewsystem.ActivityManager;
import com.jing.app.jjgallery.viewsystem.IPage;
import com.jing.app.jjgallery.viewsystem.ProgressProvider;
import com.jing.app.jjgallery.viewsystem.publicview.ActionBar;
import com.jing.app.jjgallery.viewsystem.publicview.starmap.StarMapLayout;
import com.jing.app.jjgallery.viewsystem.sub.key.Keyword;
import com.jing.app.jjgallery.viewsystem.sub.key.KeywordsFlow;

import java.util.List;

/**
 * Created by JingYang on 2016/7/14 0014.
 * Description:
 */
public class SOrderIndexPage implements IPage, ISOrderDataCallback, StarMapLayout.OnItemClickListener {

    private Context context;

    private ImageView bkView;
    private StarMapLayout starMapLayout;
    private StarMapOrderAdapter mAdapter;

    private SOrderPresenter mPresenter;

    private List<SOrder> orderList;

    public SOrderIndexPage(Context context, View view) {
        this.context = context;
        bkView = (ImageView) view.findViewById(R.id.fm_index_bk);
        starMapLayout = (StarMapLayout) view.findViewById(R.id.view_keyword_flow);
        starMapLayout.setBackgroundColor(Color.TRANSPARENT);
        starMapLayout.setOnItemClickListener(this);

        updateBackground(context.getResources().getConfiguration().orientation);
    }

    private void updateBackground(int orientation) {
        String bkPath = null;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            bkPath = SettingProperties.getPreference(context, PreferenceKey.PREF_BG_SORDER_INDEX_LAND);
        }
        else {
            bkPath = SettingProperties.getPreference(context, PreferenceKey.PREF_BG_SORDER_INDEX);
        }
        if (bkPath != null) {
            SImageLoader.getInstance().displayImage(bkPath, bkView);
        }
    }

    @Override
    public void initData() {
        if (context instanceof ProgressProvider) {
            ((ProgressProvider) context).showProgressCycler();
        }
        mPresenter.setDataCallback(this);
        mPresenter.loadAllOrders(PreferenceValue.ORDERBY_NONE);
    }

    @Override
    public void onQueryAllOrders(List<SOrder> list) {
        orderList = list;
        mAdapter = new StarMapOrderAdapter(context, list);

        resetStarMapLayout();

        if (context instanceof  ProgressProvider) {
            ((ProgressProvider) context).dismissProgressCycler();
        }
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

            case R.id.actionbar_cover:
                break;
        }
    }

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
        actionBar.clearActionIcon();
        actionBar.addMenuIcon();
        actionBar.addColorIcon();
        actionBar.onConfiguration(context.getResources().getConfiguration().orientation);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        updateBackground(newConfig.orientation);
        // starMapLayout的item坐标是根据width/height计算的，转屏发生了变化所以必须重布局
        // 并且在布局完成后post重新feedKeyword进行重计算
        starMapLayout.requestLayout();
        starMapLayout.invalidate();

        // 防止第一次进入由于加载目录时间过长，转屏过程发生空指针异常
        if (orderList != null) {
            // 必须重新刷新
            starMapLayout.postDelayed(new Runnable() {
                @Override
                public void run() {
                    resetStarMapLayout();
                }
            }, 200);//这里直接post还获取不到最新的width和height，需要延迟一会
        }
    }

    private void resetStarMapLayout() {
        starMapLayout.reset();
        starMapLayout.setAdapter(mAdapter);
        int maxNumber = starMapLayout.getMaxItem();
        if (StarMapOrderAdapter.DEFAULT_SHOW_NUMBER > maxNumber) {
            mAdapter.setShowNumber(maxNumber);
        }
        mAdapter.feedKeyword();
        mAdapter.goToShow(KeywordsFlow.ANIMATION_IN);
    }

    @Override
    public void onTouchEvent(MotionEvent event) {
        if (mAdapter != null) {
            mAdapter.onTouchEvent(event);
        }
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

    @Override
    public void onItemClick(View view, Keyword keyword) {
        SOrder order = (SOrder) keyword.getObject();
        mPresenter.accessOrder(order);

        ActivityManager.startExploreActivity((Activity) context, order, SettingProperties.getSOrderIndexItemOpenMode(context), view);
    }
}
