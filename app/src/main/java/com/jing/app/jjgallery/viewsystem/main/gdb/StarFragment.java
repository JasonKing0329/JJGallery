package com.jing.app.jjgallery.viewsystem.main.gdb;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jing.app.jjgallery.BaseActivity;
import com.jing.app.jjgallery.BaseSlidingActivity;
import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.bean.StarProxy;
import com.jing.app.jjgallery.presenter.main.GdbPresenter;
import com.jing.app.jjgallery.presenter.main.SettingProperties;
import com.jing.app.jjgallery.viewsystem.ActivityManager;
import com.jing.app.jjgallery.viewsystem.ProgressProvider;
import com.jing.app.jjgallery.viewsystem.publicview.ActionBar;
import com.jing.app.jjgallery.viewsystem.publicview.PullZoomRecyclerView;
import com.king.service.gdb.bean.Record;

/**
 * Created by JingYang on 2016/8/1 0001.
 * Description:
 */
public class StarFragment extends Fragment implements IStarView, StarRecordsAdapter.OnRecordItemClickListener {

    private int starId;
    private GdbPresenter mPresenter;
    protected PullZoomRecyclerView mRecyclerView;
    private StarRecordsAdapter mAdapter;

    public ActionBar mActionbar;
    private int currentSortMode = -1;
    private StarProxy starProxy;

    public StarFragment(int starId) {
        this.starId = starId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mPresenter = new GdbPresenter(this);
        currentSortMode = SettingProperties.getGdbStarRecordOrderMode(getActivity());
        return inflater.inflate(R.layout.fragment_pull_zoom_header, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initActionbar();

        mRecyclerView = (PullZoomRecyclerView) view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        if (getActivity() instanceof ProgressProvider) {
            ((ProgressProvider) getActivity()).showProgressCycler();
        }
        mPresenter.loadStar(starId);
    }

    public void setActionbar(ActionBar actionbar) {
        this.mActionbar = actionbar;
    }

    private void initActionbar() {
        if (getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).requestActionbarFloating(false);
        }
        else if (getActivity() instanceof BaseSlidingActivity) {
            ((BaseSlidingActivity) getActivity()).requestActionbarFloating(false);
        }
        mActionbar.addSortIcon();
        mActionbar.addBackIcon();
        mActionbar.hide();
    }

    @Override
    public void onStarLoaded(StarProxy star) {
        starProxy = star;
        mPresenter.sortRecords(star.getStar().getRecordList(), currentSortMode);

        mAdapter = new StarRecordsAdapter(star, mRecyclerView);
        mAdapter.setItemClickListener(this);
        mRecyclerView.setAdapter(mAdapter);
        if (getActivity() instanceof ProgressProvider) {
            ((ProgressProvider) getActivity()).dismissProgressCycler();
        }
    }

    public void onIconClick(View view) {
        if (view.getId() == R.id.actionbar_sort) {
            new RecordSortPopup().showSortPopup(getActivity(), view, new RecordSortPopup.SortCallback() {
                @Override
                public void onSortModeSelected(int sortMode, boolean refresh) {
                    if (currentSortMode != sortMode) {
                        currentSortMode = sortMode;
                        SettingProperties.setGdbStarRecordOrderMode(getActivity(), currentSortMode);
                        if (refresh) {
                            refresh();
                        }
                    }
                }
            });
        }
    }

    private void refresh() {
        mPresenter.sortRecords(starProxy.getStar().getRecordList(), currentSortMode);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClickRecordItem(Record record) {
        ActivityManager.startGdbRecordActivity(getActivity(), record);
    }
}
