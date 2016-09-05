package com.jing.app.jjgallery.viewsystem.main.gdb;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.bean.http.DownloadItem;
import com.jing.app.jjgallery.config.Configuration;
import com.jing.app.jjgallery.presenter.main.GdbPresenter;
import com.jing.app.jjgallery.presenter.main.SettingProperties;
import com.jing.app.jjgallery.viewsystem.ActivityManager;
import com.jing.app.jjgallery.viewsystem.ProgressProvider;
import com.jing.app.jjgallery.viewsystem.publicview.ActionBar;
import com.jing.app.jjgallery.viewsystem.publicview.CustomDialog;
import com.jing.app.jjgallery.viewsystem.publicview.DownloadDialog;
import com.king.service.gdb.bean.Record;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by JingYang on 2016/8/2 0002.
 * Description:
 */
public class RecordListFragment extends Fragment implements IGdbRecordListView, RecordListAdapter.OnRecordItemClickListener
    , IGdbFragment{

    private RecyclerView mRecyclerView;
    private GdbPresenter gdbPresenter;
    public ActionBar mActionbar;

    private RecordListAdapter mAdapter;
    private int currentSortMode = -1;
    private boolean currentSortDesc = true;

    private DownloadDialog downloadDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        gdbPresenter = new GdbPresenter(this);
        currentSortMode = SettingProperties.getGdbRecordOrderMode(getActivity());
        initActionbar();

        View view = inflater.inflate(R.layout.page_gdb_recordlist, null);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.gdb_srecord_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        gdbPresenter.loadRecordList(currentSortMode, currentSortDesc);
        return view;
    }

    @Override
    public void onLoadRecordList(List<Record> list) {
        mAdapter = new RecordListAdapter(getActivity(), list);
        mAdapter.setItemClickListener(this);
        mAdapter.setSortMode(currentSortMode);
        mRecyclerView.setAdapter(mAdapter);

        gdbPresenter.checkServerStatus();
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
                new SortDialog(getActivity(), new CustomDialog.OnCustomDialogActionListener() {
                    @Override
                    public boolean onSave(Object object) {
                        Map<String, Object> map = (Map<String, Object>) object;
                        int sortMode = (int) map.get("sortMode");
                        boolean desc = (Boolean) map.get("desc");
                        if (currentSortMode != sortMode || currentSortDesc != desc) {
                            currentSortMode = sortMode;
                            currentSortDesc = desc;
                            SettingProperties.setGdbRecordOrderMode(getActivity(), currentSortMode);
                            refresh();
                        }
                        return false;
                    }

                    @Override
                    public boolean onCancel() {
                        return false;
                    }

                    @Override
                    public void onLoadData(HashMap<String, Object> data) {
                        data.put("sortMode", currentSortMode);
                        data.put("desc", currentSortDesc);
                    }
                }).show();
                break;
            case R.id.actionbar_show:
                ((GDBHomeActivity) getContext()).onRecordSceneListPage();
                break;
        }
    }

    private void refresh() {
        gdbPresenter.sortRecords(mAdapter.getRecordList(), currentSortMode, currentSortDesc);
        mAdapter.setSortMode(currentSortMode);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClickRecordItem(Record record) {
        ActivityManager.startGdbRecordActivity(getActivity(), record);
    }

    public void onTextChanged(String text, int start, int before, int count) {
        if (mAdapter != null) {
            mAdapter.onStarFilter(text);
        }
    }

    @Override
    public void onServerConnected() {
//        ((ProgressProvider) getActivity()).showToastShort(getString(R.string.gdb_server_online), ProgressProvider.TOAST_SUCCESS);
        gdbPresenter.checkNewRecordFile();
    }

    @Override
    public void onServerUnavailable() {
        ((ProgressProvider) getActivity()).showToastLong(getString(R.string.gdb_server_offline), ProgressProvider.TOAST_ERROR);
    }

    @Override
    public void onRequestFail() {
        ((ProgressProvider) getActivity()).showToastLong(getString(R.string.gdb_request_fail), ProgressProvider.TOAST_ERROR);
    }

    @Override
    public void onCheckPass(boolean hasNew, final List<DownloadItem> downloadList) {
        if (hasNew) {
            if (downloadDialog == null) {
                downloadDialog = new DownloadDialog(getActivity(), new CustomDialog.OnCustomDialogActionListener() {
                    @Override
                    public boolean onSave(Object object) {
                        return false;
                    }

                    @Override
                    public boolean onCancel() {
                        return false;
                    }

                    @Override
                    public void onLoadData(HashMap<String, Object> data) {
                        data.put("items", gdbPresenter.pickRecordToDownload(downloadList));
                        data.put("savePath", Configuration.GDB_IMG_RECORD);
                        data.put("optionMsg", String.format(getContext().getString(R.string.gdb_option_download), downloadList.size()));
                    }
                });
            }
            else {
                downloadDialog.newUpdate(gdbPresenter.pickRecordToDownload(downloadList));
            }
            downloadDialog.show();
        }
        else {
            ((ProgressProvider) getActivity()).showToastLong(getString(R.string.gdb_no_new_images), ProgressProvider.TOAST_INFOR);
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_gdb_check_server:
                gdbPresenter.checkNewRecordFile();
                break;
            case R.id.menu_gdb_download:
                if (downloadDialog != null) {
                    downloadDialog.show();
                }
                break;
        }
        return false;
    }
}
