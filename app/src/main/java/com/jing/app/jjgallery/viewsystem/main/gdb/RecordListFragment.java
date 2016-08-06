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
        mActionbar.clearActionIcon();
        mActionbar.addSortIcon();
        mActionbar.addSearchIcon();
        mActionbar.addShowIcon();
        mActionbar.addMenuIcon();
        mActionbar.addHomeIcon();
    }

    public void onIconClick(View view) {
        switch (view.getId()) {
            case R.id.actionbar_sort:
                showSortPopup(view);
                break;
            case R.id.actionbar_show:
                ((GDBHomeActivity) getContext()).onRecordSceneListPage();
                break;
        }
    }

    private void showSortPopup(View v) {
        PopupMenu menu = new PopupMenu(getActivity(), v);
        menu.getMenuInflater().inflate(R.menu.sort_gdb_record_list, menu.getMenu());
        menu.show();
        menu.setOnMenuItemClickListener(sortListener);
    }

    PopupMenu.OnMenuItemClickListener sortListener = new PopupMenu.OnMenuItemClickListener() {

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            int sortMode;
            boolean refresh = true;
            switch (item.getItemId()) {
                case R.id.menu_by_date:
                    sortMode = PreferenceValue.GDB_SR_ORDERBY_DATE;
                    break;
                case R.id.menu_by_name:
                    sortMode = PreferenceValue.GDB_SR_ORDERBY_NAME;
                    break;
                case R.id.menu_by_score:
                    sortMode = PreferenceValue.GDB_SR_ORDERBY_SCORE;
                    break;
                case R.id.menu_by_fk:
                    sortMode = PreferenceValue.GDB_SR_ORDERBY_FK;
                    break;
                case R.id.menu_by_cum:
                    sortMode = PreferenceValue.GDB_SR_ORDERBY_CUM;
                    break;
                case R.id.menu_by_bjob:
                    sortMode = PreferenceValue.GDB_SR_ORDERBY_BJOB;
                    break;
                case R.id.menu_by_star1:
                    sortMode = PreferenceValue.GDB_SR_ORDERBY_STAR1;
                    break;
                case R.id.menu_by_star2:
                    sortMode = PreferenceValue.GDB_SR_ORDERBY_STAR2;
                    break;
                case R.id.menu_by_starcc1:
                    sortMode = PreferenceValue.GDB_SR_ORDERBY_STARCC1;
                    break;
                case R.id.menu_by_starcc2:
                    sortMode = PreferenceValue.GDB_SR_ORDERBY_STARCC2;
                    break;

                case R.id.menu_by_none:
                default:
                    sortMode = PreferenceValue.GDB_SR_ORDERBY_NONE;
                    refresh = false;
                    break;
            }

            if (currentSortMode != sortMode) {
                currentSortMode = sortMode;
                SettingProperties.setGdbRecordOrderMode(getActivity(), currentSortMode);
            }
            if (refresh) {
                refresh();
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
