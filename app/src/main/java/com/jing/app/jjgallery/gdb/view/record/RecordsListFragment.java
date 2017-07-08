package com.jing.app.jjgallery.gdb.view.record;

import android.content.DialogInterface;
import android.support.v7.widget.LinearLayoutManager;
import android.view.MenuItem;
import android.view.View;

import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.bean.http.DownloadItem;
import com.jing.app.jjgallery.config.Configuration;
import com.jing.app.jjgallery.gdb.GBaseFragment;
import com.jing.app.jjgallery.gdb.view.IFragmentHolder;
import com.jing.app.jjgallery.gdb.view.adapter.RecordsListAdapter;
import com.jing.app.jjgallery.gdb.view.list.GDBListActivity;
import com.jing.app.jjgallery.gdb.view.list.IGdbFragment;
import com.jing.app.jjgallery.gdb.view.list.IListPageParent;
import com.jing.app.jjgallery.gdb.view.pub.AutoLoadMoreRecyclerView;
import com.jing.app.jjgallery.gdb.view.pub.DownloadDialog;
import com.jing.app.jjgallery.presenter.main.SettingProperties;
import com.jing.app.jjgallery.service.http.Command;
import com.jing.app.jjgallery.viewsystem.ActivityManager;
import com.jing.app.jjgallery.viewsystem.ProgressProvider;
import com.jing.app.jjgallery.viewsystem.publicview.CustomDialog;
import com.jing.app.jjgallery.viewsystem.publicview.DefaultDialogManager;
import com.king.service.gdb.bean.Record;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/5/24 9:59
 */
public class RecordsListFragment extends GBaseFragment implements IGdbRecordListView, RecordsListAdapter.OnRecordItemClickListener
        , IGdbFragment {

    private final int DEFAULT_LOAD_MORE = 20;

    @BindView(R.id.rv_records)
    AutoLoadMoreRecyclerView rvRecords;

    private DownloadDialog downloadDialog;

    private IListPageParent iListPageParent;
    private RecordsListAdapter mAdapter;

    private int currentSortMode = -1;
    private boolean currentSortDesc = true;
    private boolean showDeprecated = true;

    private List<Record> recordList;
    private String keywords;

    @Override
    protected void bindFragmentHolder(IFragmentHolder holder) {
        iListPageParent = (IListPageParent) holder;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.page_gdb_recordlist;
    }

    @Override
    protected void initView(View view) {
        ButterKnife.bind(this, view);
        rvRecords.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvRecords.setEnableLoadMore(true);
        rvRecords.setOnLoadMoreListener(loadMoreListener);

        currentSortMode = SettingProperties.getGdbRecordOrderMode(getActivity());
        initActionbar();
        // 加载records
        loadNewRecords();

        // 检查服务端新增图片
        iListPageParent.getPresenter().checkServerStatus();
    }

    private AutoLoadMoreRecyclerView.OnLoadMoreListener loadMoreListener = new AutoLoadMoreRecyclerView.OnLoadMoreListener() {
        @Override
        public void onLoadMore() {
            loadMoreRecords();
        }
    };

    private void initActionbar() {
        iListPageParent.getActionbar().clearActionIcon();
        iListPageParent.getActionbar().addSortIcon();
        iListPageParent.getActionbar().addSearchIcon();
        iListPageParent.getActionbar().addShowIcon();
        iListPageParent.getActionbar().addMenuIcon();
        iListPageParent.getActionbar().addHomeIcon();
    }

    /**
     * actionbar 输入字符
     * @param text
     * @param start
     * @param before
     * @param count
     */
    public void onTextChanged(String text, int start, int before, int count) {
        if (mAdapter != null) {
            this.keywords = text;
            loadNewRecords();
        }
    }

    /**
     * 修改排序类型、关键词变化，重新加载list
     */
    private void loadNewRecords() {
        // 重新加载records
        iListPageParent.getPresenter().loadRecordList(currentSortMode, currentSortDesc, showDeprecated, 0, DEFAULT_LOAD_MORE, keywords);
    }

    /**
     * loadNewRecords 回调
     * @param list
     */
    @Override
    public void onLoadRecordList(List<Record> list) {
        this.recordList = list;
        if (mAdapter == null) {
            mAdapter = new RecordsListAdapter(getActivity(), list);
            mAdapter.setSortMode(currentSortMode);
            mAdapter.setItemClickListener(this);
            rvRecords.setAdapter(mAdapter);
        }
        else {
            mAdapter.setRecordList(list);
            mAdapter.setSortMode(currentSortMode);
            mAdapter.notifyDataSetChanged();
        }
        // 回到顶端
        rvRecords.scrollToPosition(0);
    }

    /**
     * 不改变排序模式、不改变关键词，紧在滑动到底部后自动加载更多
     */
    private void loadMoreRecords() {
        // 加到当前size后
        iListPageParent.getPresenter().loadMoreRecords(currentSortMode, currentSortDesc, recordList.size(), DEFAULT_LOAD_MORE, keywords);
    }

    /**
     * loadMoreRecords 回调
     * @param list
     */
    @Override
    public void onMoreRecordsLoaded(List<Record> list) {
        int originSize = mAdapter.getItemCount();
        recordList.addAll(list);
        mAdapter.notifyItemRangeInserted(originSize - 1, list.size());
    }

    /**
     * actionbar 点击icon
     * @param view
     */
    public void onIconClick(View view) {
        switch (view.getId()) {
            case R.id.actionbar_sort:
                new SortDialog(getActivity(), new CustomDialog.OnCustomDialogActionListener() {
                    @Override
                    public boolean onSave(Object object) {
                        Map<String, Object> map = (Map<String, Object>) object;
                        int sortMode = (int) map.get("sortMode");
                        boolean desc = (Boolean) map.get("desc");
                        boolean isIncludeDeprecated = (Boolean) map.get("include_deprecated");
                        if (currentSortMode != sortMode || currentSortDesc != desc || showDeprecated != isIncludeDeprecated) {
                            currentSortMode = sortMode;
                            currentSortDesc = desc;
                            showDeprecated = isIncludeDeprecated;
                            SettingProperties.setGdbRecordOrderMode(getActivity(), currentSortMode);
                            loadNewRecords();
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
                ((GDBListActivity) getContext()).onRecordSceneListPage();
                break;
        }
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

    @Override
    public void onServerConnected() {
        if (isVisible()) {
            iListPageParent.getPresenter().checkNewRecordFile();
        }
    }

    @Override
    public void onServerUnavailable() {
        if (isVisible()) {
            ((ProgressProvider) getActivity()).showToastLong(getString(R.string.gdb_server_offline), ProgressProvider.TOAST_ERROR);
        }
    }

    @Override
    public void onRequestFail() {
        if (isVisible()) {
            ((ProgressProvider) getActivity()).showToastLong(getString(R.string.gdb_request_fail), ProgressProvider.TOAST_ERROR);
        }
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
                        optionServerAction(downloadList);
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

    /**
     * request server move original image files
     * @param downloadList
     */
    private void optionServerAction(final List<DownloadItem> downloadList) {
        new DefaultDialogManager().showOptionDialog(getActivity(), null, getString(R.string.gdb_download_done)
                , getResources().getString(R.string.yes), null, getResources().getString(R.string.no)
                , new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == DialogInterface.BUTTON_POSITIVE) {
                            iListPageParent.getPresenter().requestServeMoveImages(Command.TYPE_RECORD, downloadList);
                        }
                    }
                }, null);
    }

    @Override
    public void onMoveImagesSuccess() {
        ((ProgressProvider) getActivity()).showToastLong(getString(R.string.success), ProgressProvider.TOAST_INFOR);
    }

    @Override
    public void onMoveImagesFail() {
        ((ProgressProvider) getActivity()).showToastLong(getString(R.string.failed), ProgressProvider.TOAST_INFOR);
    }

    @Override
    public void onDownloadItemEncrypted() {
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClickRecordItem(Record record) {
        ActivityManager.startGdbRecordActivity(getActivity(), record);
    }
}
