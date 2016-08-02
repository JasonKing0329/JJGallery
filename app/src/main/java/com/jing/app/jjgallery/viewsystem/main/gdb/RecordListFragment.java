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
import com.jing.app.jjgallery.config.PreferenceValue;
import com.jing.app.jjgallery.presenter.main.GdbPresenter;
import com.jing.app.jjgallery.presenter.main.SettingProperties;
import com.jing.app.jjgallery.viewsystem.publicview.ActionBar;
import com.king.service.gdb.bean.Record;

import java.util.List;

/**
 * Created by JingYang on 2016/8/2 0002.
 * Description:
 */
public class RecordListFragment extends Fragment implements IGdbRecordListView, RecordListAdapter.OnRecordItemClickListener {

    private RecyclerView mRecyclerView;
    private GdbPresenter gdbPresenter;
    public ActionBar mActionbar;

    private RecordListAdapter mAdapter;
    private int currentSortMode = -1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        gdbPresenter = new GdbPresenter(this);
        currentSortMode = SettingProperties.getGdbRecordOrderMode(getActivity());
        initActionbar();

        View view = inflater.inflate(R.layout.page_gdb_recordlist, null);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.gdb_srecord_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        gdbPresenter.loadRecordList(currentSortMode);
        return view;
    }

    @Override
    public void onLoadRecordList(List<Record> list) {
        mAdapter = new RecordListAdapter(getActivity(), list);
        mAdapter.setItemClickListener(this);
        mRecyclerView.setAdapter(mAdapter);
    }

    public void setActionbar(ActionBar actionbar) {
        this.mActionbar = actionbar;
    }

    private void initActionbar() {
        mActionbar.addSortIcon();
        mActionbar.addBackIcon();
        mActionbar.hide();
    }

    public void onIconClick(View view) {
        if (view.getId() == R.id.actionbar_sort) {
            showSortPopup(view);
        }
    }

    private void showSortPopup(View v) {
        PopupMenu menu = new PopupMenu(getActivity(), v);
        menu.getMenuInflater().inflate(R.menu.sort_gdb_star_page, menu.getMenu());
        menu.show();
        menu.setOnMenuItemClickListener(sortListener);
    }

    PopupMenu.OnMenuItemClickListener sortListener = new PopupMenu.OnMenuItemClickListener() {

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.menu_by_date:
                    if (currentSortMode != PreferenceValue.GDB_SR_ORDERBY_DATE) {
                        currentSortMode = PreferenceValue.GDB_SR_ORDERBY_DATE;
                        SettingProperties.setGdbRecordOrderMode(getActivity(), currentSortMode);
                        refresh();
                    }
                    break;
                case R.id.menu_by_name:
                    if (currentSortMode != PreferenceValue.GDB_SR_ORDERBY_NAME) {
                        currentSortMode = PreferenceValue.GDB_SR_ORDERBY_NAME;
                        SettingProperties.setGdbRecordOrderMode(getActivity(), currentSortMode);
                        refresh();
                    }
                    break;
                case R.id.menu_by_score:
                    if (currentSortMode != PreferenceValue.GDB_SR_ORDERBY_SCORE) {
                        currentSortMode = PreferenceValue.GDB_SR_ORDERBY_SCORE;
                        SettingProperties.setGdbRecordOrderMode(getActivity(), currentSortMode);
                        refresh();
                    }
                    break;
                case R.id.menu_by_fk:
                    if (currentSortMode != PreferenceValue.GDB_SR_ORDERBY_FK) {
                        currentSortMode = PreferenceValue.GDB_SR_ORDERBY_FK;
                        SettingProperties.setGdbRecordOrderMode(getActivity(), currentSortMode);
                        refresh();
                    }
                    break;
                case R.id.menu_by_cum:
                    if (currentSortMode != PreferenceValue.GDB_SR_ORDERBY_CUM) {
                        currentSortMode = PreferenceValue.GDB_SR_ORDERBY_CUM;
                        SettingProperties.setGdbRecordOrderMode(getActivity(), currentSortMode);
                        refresh();
                    }
                    break;

                case R.id.menu_by_none:
                default:
                    if (currentSortMode != PreferenceValue.GDB_SR_ORDERBY_NONE) {
                        currentSortMode = PreferenceValue.GDB_SR_ORDERBY_NONE;
                        SettingProperties.setGdbRecordOrderMode(getActivity(), currentSortMode);
                        // no action
                    }
                    break;
            }
            return true;
        }
    };

    private void refresh() {
        gdbPresenter.sortRecords(mAdapter.getRecordList(), currentSortMode);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClickRecordItem(Record record) {

    }

    public void onTextChanged(String text, int start, int before, int count) {
        if (mAdapter != null) {
            mAdapter.onStarFilter(text);
        }
    }
}
