package com.jing.app.jjgallery.gdb.view.record;

import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.gdb.GBaseFragment;
import com.jing.app.jjgallery.gdb.view.IFragmentHolder;
import com.jing.app.jjgallery.gdb.view.adapter.RecordsListAdapter;
import com.jing.app.jjgallery.gdb.view.pub.AutoLoadMoreRecyclerView;
import com.jing.app.jjgallery.presenter.main.SettingProperties;
import com.jing.app.jjgallery.util.DebugLog;
import com.jing.app.jjgallery.viewsystem.ActivityManager;
import com.jing.app.jjgallery.viewsystem.publicview.CustomDialog;
import com.king.service.gdb.bean.Record;

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
public class RecordsListFragment extends GBaseFragment implements IRecordListView, RecordsListAdapter.OnRecordItemClickListener {

    private final int DEFAULT_LOAD_MORE = 20;

    @BindView(R.id.rv_records)
    AutoLoadMoreRecyclerView rvRecords;

    private IRecordListHolder holder;
    private RecordsListAdapter mAdapter;

    private int currentSortMode = -1;
    private boolean currentSortDesc = true;
    private boolean showDeprecated = true;
    private boolean showCanBePlayed;

    private List<Record> recordList;
    /**
     * search input keywords
     */
    private String keywords;
    /**
     * scene keywords
     */
    private String keyScene;

    @Override
    protected void bindFragmentHolder(IFragmentHolder holder) {
        this.holder = (IRecordListHolder) holder;
        this.holder.getPresenter().setRecordListView(this);
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
        // 加载records
        loadNewRecords();
    }

    private AutoLoadMoreRecyclerView.OnLoadMoreListener loadMoreListener = new AutoLoadMoreRecyclerView.OnLoadMoreListener() {
        @Override
        public void onLoadMore() {
            loadMoreRecords();
        }
    };

    /**
     * actionbar 输入字符
     */
    public void filterRecord(String text) {
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
        holder.getPresenter().loadRecordList(currentSortMode, currentSortDesc, showDeprecated, showCanBePlayed
                , 0, DEFAULT_LOAD_MORE, keywords, keyScene);
    }

    /**
     * loadNewRecords 回调
     * @param list
     */
    @Override
    public void onLoadRecordList(List<Record> list) {
        this.recordList = list;
        // activity已结束
        if (getActivity() == null || getActivity().isDestroyed()) {
            DebugLog.e("activity finished");
            return;
        }
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
     * 不改变排序模式、不改变关键词，仅在滑动到底部后自动加载更多
     */
    private void loadMoreRecords() {
        // 加到当前size后
        holder.getPresenter().loadMoreRecords(currentSortMode, currentSortDesc, showDeprecated, showCanBePlayed
                , recordList.size(), DEFAULT_LOAD_MORE, keywords, keyScene);
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

    public void changeSortType() {
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
    }

    public void refreshList() {
        mAdapter.notifyDataSetChanged();
    }

    public void showCanPlayList(boolean canPlay) {
        if (canPlay != showCanBePlayed) {
            showCanBePlayed = canPlay;
            loadNewRecords();
        }
    }

    @Override
    public void onClickRecordItem(Record record) {
        ActivityManager.startGdbRecordActivity(getActivity(), record);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        loadNewRecords();
    }

    public void setScene(String scene) {
        this.keyScene = scene;
    }

}
