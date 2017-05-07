package com.jing.app.jjgallery.gdb.view.record;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.bean.http.DownloadItem;
import com.jing.app.jjgallery.config.Configuration;
import com.jing.app.jjgallery.config.PreferenceValue;
import com.jing.app.jjgallery.gdb.view.list.GDBListActivity;
import com.jing.app.jjgallery.gdb.view.pub.DownloadDialog;
import com.jing.app.jjgallery.gdb.view.list.IGdbFragment;
import com.jing.app.jjgallery.gdb.view.list.IListPageParent;
import com.jing.app.jjgallery.gdb.view.adapter.RecordSceneExpandAdapter;
import com.jing.app.jjgallery.presenter.main.SettingProperties;
import com.jing.app.jjgallery.viewsystem.ActivityManager;
import com.jing.app.jjgallery.viewsystem.ProgressProvider;
import com.jing.app.jjgallery.viewsystem.publicview.CustomDialog;
import com.king.service.gdb.bean.Record;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by JingYang on 2016/8/5 0005.
 * Description:
 */
public class RecordSceneListFragment extends Fragment implements IGdbRecordListView, RecordSceneExpandAdapter.OnRecordClickListener
    , IGdbFragment {
    private ExpandableListView expandableListView;
    private IListPageParent iListPageParent;

    private RecordSceneExpandAdapter mAdapter;
    private int currentSortMode = -1;
    private boolean currentSortDesc = true;

    private DownloadDialog downloadDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        
        iListPageParent = (IListPageParent) getActivity();

        initActionbar();

        currentSortMode = SettingProperties.getGdbRecordOrderMode(getActivity());
        View view = inflater.inflate(R.layout.page_gdb_recordscenelist, null);
        expandableListView = (ExpandableListView) view.findViewById(R.id.gdb_record_expand_view);

        iListPageParent.getPresenter().loadRecordList(PreferenceValue.GDB_SR_ORDERBY_NONE, currentSortDesc);
        return view;
    }

    private void initActionbar() {
        iListPageParent.getActionbar().clearActionIcon();
        iListPageParent.getActionbar().addMenuIcon();
        iListPageParent.getActionbar().addSearchIcon();
        iListPageParent.getActionbar().addSortIcon();
        iListPageParent.getActionbar().addHideIcon();
        iListPageParent.getActionbar().addHomeIcon();
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
            case R.id.actionbar_hide:
                ((GDBListActivity) getContext()).onRecordListPage();
                break;
        }
    }

    private void refresh() {
        iListPageParent.getPresenter().sortSceneRecords(mAdapter.getRecordMap(), currentSortMode, currentSortDesc);
        mAdapter.setSortMode(currentSortMode);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoadRecordList(List<Record> list) {
        Map<String, List<Record>> resultList = iListPageParent.getPresenter().collectRecordsMapByScene(list, currentSortMode, currentSortDesc);
        iListPageParent.getPresenter().sortSceneRecords(resultList, currentSortMode, currentSortDesc);
        mAdapter = new RecordSceneExpandAdapter(getActivity(), resultList);
        mAdapter.setOnRecordClickListener(this);
        mAdapter.setSortMode(currentSortMode);
        expandableListView.setAdapter(mAdapter);
    }

    public void onTextChanged(String text, int start, int before, int count) {
        if (mAdapter != null) {
            mAdapter.onRecordFilter(text);
        }
    }

    @Override
    public void onRecordClick(Record record) {
        ActivityManager.startGdbRecordActivity(getActivity(), record);
    }

    @Override
    public void onServerConnected() {
//        ((ProgressProvider) getActivity()).showToastShort(getString(R.string.gdb_server_online), ProgressProvider.TOAST_SUCCESS);
        iListPageParent.getPresenter().checkNewRecordFile();
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
                        List<DownloadItem> repeatList = new ArrayList<>();
                        data.put("items", iListPageParent.getPresenter().pickRecordToDownload(downloadList, repeatList));
                        data.put("existedItems", repeatList);
                        data.put("savePath", Configuration.GDB_IMG_RECORD);
                        data.put("optionMsg", String.format(getContext().getString(R.string.gdb_option_download), downloadList.size()));
                    }
                });
                downloadDialog.setOnDownloadListener(new DownloadDialog.OnDownloadListener() {
                    @Override
                    public void onDownloadFinish(DownloadItem item) {

                    }

                    @Override
                    public void onDownloadFinish(List<DownloadItem> downloadList) {
                        iListPageParent.getPresenter().finishDownload(downloadList);
                    }
                });
            }
            else {
                List<DownloadItem> repeatList = new ArrayList<>();
                List<DownloadItem> newList = iListPageParent.getPresenter().pickRecordToDownload(downloadList, repeatList);
                downloadDialog.newUpdate(newList, repeatList);
            }
            downloadDialog.show();
        }
        else {
            ((ProgressProvider) getActivity()).showToastLong(getString(R.string.gdb_no_new_images), ProgressProvider.TOAST_INFOR);
        }
    }

    @Override
    public void onDownloadItemEncrypted() {
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_gdb_check_server:
                iListPageParent.getPresenter().checkNewRecordFile();
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
