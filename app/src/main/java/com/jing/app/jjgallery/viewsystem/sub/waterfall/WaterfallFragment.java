package com.jing.app.jjgallery.viewsystem.sub.waterfall;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.bean.filesystem.FileBean;
import com.jing.app.jjgallery.presenter.main.SettingProperties;
import com.jing.app.jjgallery.presenter.sub.WaterfallPresenter;
import com.jing.app.jjgallery.viewsystem.publicview.ActionBar;
import com.jing.app.jjgallery.viewsystem.sub.dialog.ShowImageDialog;

import java.util.List;

/**
 * Created by JingYang on 2016/7/26 0026.
 * Description:
 */
public class WaterfallFragment extends Fragment implements IWaterfall, OnWaterfallItemListener {

    private WaterfallPresenter mPresenter;
    private RecyclerView recyclerView;
    private WaterfallAdapter mAdapter;
    private StaggeredGridLayoutManager layoutManager;

    private int nColumn;
    private int orientation;
    private ShowImageDialog imageDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        orientation = getResources().getConfiguration().orientation;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mPresenter = new WaterfallPresenter(this);

        View view = inflater.inflate(R.layout.page_waterfall, null);
        recyclerView = (RecyclerView) view.findViewById(R.id.waterfall_recyclerview);

        loadColumns();

        layoutManager = new StaggeredGridLayoutManager(nColumn, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        startBackgroundWork();
        return view;
    }

    private void loadColumns() {
        if (isLandscape()) {
            nColumn = SettingProperties.getWaterfallColumnsLand(getActivity());
        }
        else {
            nColumn = SettingProperties.getWaterfallColumns(getActivity());
        }
    }

    public void setActionbar(ActionBar actionbar) {

    }

    private void startBackgroundWork() {
        mPresenter.loadDatas();
    }

    private boolean isLandscape() {
        return orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    @Override
    public void onLoadDatas(List<FileBean> list) {
        mAdapter = new WaterfallAdapter(getActivity(), list, nColumn);
        mAdapter.setOnWaterfallItemListener(this);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (newConfig.orientation != orientation) {
            orientation = newConfig.orientation;
            loadColumns();
            layoutManager.setSpanCount(nColumn);
            if (mAdapter != null) {
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onItemClick(int position) {
        if (imageDialog == null) {
            imageDialog = new ShowImageDialog(getActivity(), null, 0);
        }
        imageDialog.setImagePath(mAdapter.getImagePath(position));
        imageDialog.fitImageView();
        imageDialog.show();
    }

    @Override
    public void onItemLongClick(int position) {

    }

    public boolean onBackPressed() {
        return mAdapter.onBackPressed();
    }
}
