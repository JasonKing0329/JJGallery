package com.jing.app.jjgallery.viewsystem.sub.waterfall;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.jing.app.jjgallery.BaseActivity;
import com.jing.app.jjgallery.BaseSlidingActivity;
import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.controller.ThemeManager;
import com.jing.app.jjgallery.presenter.main.SettingProperties;
import com.jing.app.jjgallery.presenter.main.order.SOrderProvider;
import com.jing.app.jjgallery.presenter.sub.WaterfallPresenter;
import com.jing.app.jjgallery.viewsystem.publicview.ActionBar;
import com.jing.app.jjgallery.viewsystem.sub.dialog.ShowImageDialog;

import java.util.List;

/**
 * Created by JingYang on 2016/7/26 0026.
 * Description:
 */
public abstract class WaterfallFragment extends Fragment implements IWaterfall, OnWaterfallItemListener {

    private final String TAG = "WaterfallFragment";
    /**
     * 删除时的透明过程
     */
    private final int TIME_GALLERY_ANIM_REMOVE = 200;
    /**
     * 删除时的透明过程结束后后面的item向前挤压的过程
     */
    private final int TIME_GALLERY_ANIM_MOVE = 500;

    protected WaterfallPresenter mPresenter;
    private RecyclerView recyclerView;
    private StaggeredGridLayoutManager layoutManager;

    protected int nColumn;
    private int orientation;
    private ShowImageDialog imageDialog;

    private ActionBar mActionbar;
    protected SOrderProvider sOrderProvider;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        orientation = getResources().getConfiguration().orientation;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mPresenter = new WaterfallPresenter(this);
        initSOrderProvider();

        initActionbar();
        SCROLL_DISTANCE = getResources().getDimensionPixelSize(R.dimen.timeline_scroll_distance_action);
        View view = inflater.inflate(R.layout.page_waterfall, null);
        recyclerView = (RecyclerView) view.findViewById(R.id.waterfall_recyclerview);

        loadColumns();

        layoutManager = new StaggeredGridLayoutManager(nColumn, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        DefaultItemAnimator animator = new DefaultItemAnimator();
        animator.setRemoveDuration(TIME_GALLERY_ANIM_REMOVE);
        animator.setMoveDuration(TIME_GALLERY_ANIM_MOVE);
        recyclerView.setItemAnimator(animator);

        startBackgroundWork();
        return view;
    }

    protected abstract void initSOrderProvider();

    private void loadColumns() {
        if (isLandscape()) {
            nColumn = SettingProperties.getWaterfallColumnsLand(getActivity());
        }
        else {
            nColumn = SettingProperties.getWaterfallColumns(getActivity());
        }
    }

    public void setActionbar(ActionBar actionbar) {
        mActionbar = actionbar;
    }

    private void initActionbar() {
        mActionbar.clearActionIcon();
        if (getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).requestActionbarFloating();
        }
        else if (getActivity() instanceof BaseSlidingActivity) {
            ((BaseSlidingActivity) getActivity()).requestActionbarFloating();
            mActionbar.useMenuLeftIcon();
        }

        mActionbar.setBackgroundColor(getResources().getColor(new ThemeManager(getActivity()).getWallActionbarColor()));
        mActionbar.disableSelectAll();
        mActionbar.addMenuIcon();
        mActionbar.hide();
    }

    private void startBackgroundWork() {
        loadListData();
    }

    @Override
    public void onLoadDataFinished(Object data) {
        onDatasReady(data);
        getAdapter().setOnWaterfallItemListener(this);
        recyclerView.setAdapter(getAdapter());
    }

    protected abstract void loadListData();

    protected abstract void onDatasReady(Object data);

    protected abstract BaseWaterfallAdapter getAdapter();

    private boolean isLandscape() {
        return orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (newConfig.orientation != orientation) {
            orientation = newConfig.orientation;
            loadColumns();
            layoutManager.setSpanCount(nColumn);
            if (getAdapter() != null) {
                getAdapter().notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onItemClick(int position) {
        if (imageDialog == null) {
            imageDialog = new ShowImageDialog(getActivity(), null, 0);
        }
        imageDialog.setImagePath(getAdapter().getImagePath(position));
        imageDialog.fitImageView();
        imageDialog.show();
    }

    @Override
    public void onItemLongClick(int position) {
        showSelectionMode(getAdapter().isSelctionMode());
    }

    protected void showSelectionMode(boolean show) {
        if (show) {
            mActionbar.setSelectionMode();
        }
        else {
            mActionbar.cancelSelectionMode();
        }
    }

    @Override
    public void onEmptyChecked() {
        mActionbar.showSelectAllStatus(false);
    }

    @Override
    public void onFullChecked() {
        mActionbar.showSelectAllStatus(true);
    }

    public boolean onBackPressed() {
        if (getAdapter().onBackPressed()) {
            mActionbar.cancelSelectionMode();
            return true;
        }
        return false;
    }

    public void onActionIconClick(View view) {
        switch (view.getId()) {
            case R.id.actionbar_addto:
                sOrderProvider.openOrderChooserToAddItem(getSelectedList());
                break;
            case R.id.actionbar_delete:
                deleteSelectedFile();
                break;
        }
    }

    protected abstract void deleteSelectedFile();

    protected List<String> getSelectedList() {
        return getAdapter().getSelectedList();
    }


    private float touchStartY;
    private float SCROLL_DISTANCE;

    /**
     * 如果需要使用上拉隐藏actionbar，下拉显示actionbar，需要在Activity的dispatchTouchEvent调用该方法
     * ps.不能在recyclerView上注册onTouchListener，ACTION_DOWN事件会被拦截记录不了startY的值
     * @param event
     */
    public void dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchStartY = event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                float dis = event.getRawY() - touchStartY;
                if (dis < -SCROLL_DISTANCE) {
                    if (!mActionbar.isHidden()) {
                        mActionbar.hide();
                    }
                } else if (dis > SCROLL_DISTANCE) {
                    if (!mActionbar.isShowing()) {
                        mActionbar.show();
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
    }
}
