package com.jing.app.jjgallery.viewsystem.main.gdb;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.bean.RecordProxy;
import com.jing.app.jjgallery.config.PreferenceValue;
import com.jing.app.jjgallery.presenter.main.GdbPresenter;
import com.jing.app.jjgallery.presenter.main.SettingProperties;
import com.jing.app.jjgallery.viewsystem.publicview.ActionBar;
import com.king.service.gdb.bean.Record;
import com.king.service.gdb.bean.Star;

import java.util.List;

/**
 * Created by JingYang on 2016/8/5 0005.
 * Description:
 */
public class RecordSceneListFragment extends Fragment implements IGdbRecordListView, RecordSceneAdapter.OnRecordClickListener {
    private RecyclerView mRecyclerView;
    private GdbPresenter gdbPresenter;
    private ActionBar mActionbar;

    private RecordSceneAdapter mAdapter;
    private int currentSortMode = -1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        gdbPresenter = new GdbPresenter(this);
        initActionbar();

        currentSortMode = SettingProperties.getGdbRecordOrderMode(getActivity());
        View view = inflater.inflate(R.layout.page_gdb_starlist, null);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.gdb_star_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        view.findViewById(R.id.gdb_star_side_view).setVisibility(View.GONE);

        final PinnedHeaderDecoration decoration = new PinnedHeaderDecoration();
        decoration.registerTypePinnedHeader(1, new PinnedHeaderDecoration.PinnedHeaderCreator() {
            @Override
            public boolean create(RecyclerView parent, int adapterPosition) {
                return true;
            }
        });
        mRecyclerView.addItemDecoration(decoration);

        gdbPresenter.loadRecordList(PreferenceValue.GDB_SR_ORDERBY_NONE);
        return view;
    }

    public void setActionbar(ActionBar actionbar) {
        this.mActionbar = actionbar;
    }

    private void initActionbar() {
        mActionbar.clearActionIcon();
        mActionbar.addMenuIcon();
        mActionbar.addSearchIcon();
        mActionbar.addSortIcon();
        mActionbar.addHideIcon();
        mActionbar.addHomeIcon();
    }

    public void onIconClick(View view) {
        switch (view.getId()) {
            case R.id.actionbar_sort:
                new RecordSortPopup().showSortPopup(getActivity(), view, new RecordSortPopup.SortCallback() {
                    @Override
                    public void onSortModeSelected(int sortMode, boolean refresh) {
                        if (currentSortMode != sortMode) {
                            currentSortMode = sortMode;
                            SettingProperties.setGdbRecordOrderMode(getActivity(), currentSortMode);
                        }
                        if (refresh) {
                            refresh();
                        }
                    }
                });
                break;
            case R.id.actionbar_hide:
                ((GDBHomeActivity) getContext()).onRecordListPage();
                break;
        }
    }

    private void refresh() {
        gdbPresenter.sortSceneRecords(mAdapter.getRecordList(), currentSortMode);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoadRecordList(List<Record> list) {
        List<RecordProxy> resultList = gdbPresenter.collectRecordsByScene(list, currentSortMode);
        mAdapter = new RecordSceneAdapter(getActivity(), resultList);
        mRecyclerView.setAdapter(mAdapter);
    }

    public void onTextChanged(String text, int start, int before, int count) {
        if (mAdapter != null) {
            mAdapter.onRecordFilter(text);
        }
    }

    @Override
    public void onRecordClick(Record star) {

    }
}
