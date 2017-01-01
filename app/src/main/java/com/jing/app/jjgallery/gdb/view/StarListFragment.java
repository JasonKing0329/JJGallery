package com.jing.app.jjgallery.gdb.view;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.gdb.GDBHomeActivity;
import com.jing.app.jjgallery.gdb.bean.StarProxy;
import com.jing.app.jjgallery.bean.http.DownloadItem;
import com.jing.app.jjgallery.config.Configuration;
import com.jing.app.jjgallery.gdb.GdbConstants;
import com.jing.app.jjgallery.gdb.view.adapter.StarListAdapter;
import com.jing.app.jjgallery.gdb.view.adapter.StarListNumAdapter;
import com.jing.app.jjgallery.service.image.SImageLoader;
import com.jing.app.jjgallery.viewsystem.ActivityManager;
import com.jing.app.jjgallery.viewsystem.ProgressProvider;
import com.jing.app.jjgallery.gdb.view.recommend.RecommendDialog;
import com.jing.app.jjgallery.viewsystem.publicview.ActionBar;
import com.jing.app.jjgallery.viewsystem.publicview.CustomDialog;
import com.jing.app.jjgallery.viewsystem.publicview.WaveSideBarView;
import com.king.service.gdb.bean.Star;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2016/7/30 0030.
 */
public class StarListFragment extends Fragment implements IGdbStarListView, OnStarClickListener
    , IGdbFragment {

    private int mSortMode;
    private RecyclerView mRecyclerView;
    private WaveSideBarView mSideBarView;

    private StarListAdapter mNameAdapter;

    private StarListNumAdapter mNumberAdapter;
    private IHomeShare iHomeShare;

    private DownloadDialog downloadDialog;
    private RecommendDialog recommendDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        
        iHomeShare = (IHomeShare) getActivity();
        
        initActionbar();

        View view = inflater.inflate(R.layout.page_gdb_starlist, null);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.gdb_star_recycler_view);
        mSideBarView = (WaveSideBarView) view.findViewById(R.id.gdb_star_side_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        final PinnedHeaderDecoration decoration = new PinnedHeaderDecoration();
        decoration.registerTypePinnedHeader(1, new PinnedHeaderDecoration.PinnedHeaderCreator() {
            @Override
            public boolean create(RecyclerView parent, int adapterPosition) {
                return true;
            }
        });
        mRecyclerView.addItemDecoration(decoration);

        mSideBarView.setOnTouchLetterChangeListener(new WaveSideBarView.OnTouchLetterChangeListener() {
            @Override
            public void onLetterChange(String letter) {
                int pos = mNameAdapter.getLetterPosition(letter);

                if (pos != -1) {
                    mRecyclerView.scrollToPosition(pos);
                }
            }
        });

        ((ProgressProvider) getActivity()).showProgressCycler();

        iHomeShare.getPresenter().loadStarList();
        return view;
    }

    private void initActionbar() {
        iHomeShare.getActionbar().clearActionIcon();
        iHomeShare.getActionbar().addMenuIcon();
        iHomeShare.getActionbar().addSearchIcon();
        iHomeShare.getActionbar().addHomeIcon();
        iHomeShare.getActionbar().addSortIcon();
    }

    public void onIconClick(View view) {
        switch (view.getId()) {
            case R.id.actionbar_sort:
                if (mSortMode == GdbConstants.STAR_SORT_NAME) {
                    sortByRecordNumbers();
                }
                else {
                    sortByName();
                }
                break;
        }
    }

    private void sortByName() {
        mSortMode = GdbConstants.STAR_SORT_NAME;
        iHomeShare.getPresenter().loadStarList();
    }

    private void sortByRecordNumbers() {
        mSortMode = GdbConstants.STAR_SORT_RECORDS;
        iHomeShare.getPresenter().loadStarListOrderByNumber();
    }

    @Override
    public void onLoadStarList(List<Star> list) {
        if (mSortMode == GdbConstants.STAR_SORT_RECORDS) {
            mNumberAdapter = new StarListNumAdapter(list);
            mNumberAdapter.setPresenter(iHomeShare.getPresenter());
            mNumberAdapter.setOnStarClickListener(this);
            mRecyclerView.setAdapter(mNumberAdapter);
        }
        else {
            mNameAdapter = new StarListAdapter(getActivity(), list);
            mNameAdapter.setPresenter(iHomeShare.getPresenter());
            mNameAdapter.setOnStarClickListener(this);
            mRecyclerView.setAdapter(mNameAdapter);
        }
        ((ProgressProvider) getActivity()).dismissProgressCycler();

        iHomeShare.getPresenter().checkNewStarFile();
    }

    @Override
    public void onStarClick(StarProxy star) {
        // 由于当前界面加载的star图片都是50*50的小图，但是lru包里的ImageLoader会在缓存中保存图片实例
        // 进入Star page后，加载的图片key没有变，就会从缓存读取，最后就只能显示很模糊的小图
        // 因此，在这里要删除掉该图的缓存，迫使其重新加载
        SImageLoader.getInstance().removeCache(star.getImagePath());
        ActivityManager.startStarActivity(getActivity(), star.getStar());
    }

    public void onTextChanged(String text, int start, int before, int count) {
        if (mNameAdapter != null) {
            mNameAdapter.onStarFilter(text);
        }
    }

    public void reInit() {
        // post刷新mSideBarView，根据调试发现重写初始化后WaveSideBarView会重新执行onMeasure(width,height=0)->onDraw->onMeasure(width,height=正确值)
        // 缺少重新onDraw的过程，因此通过delay执行mSideBarView.invalidate()可以激活onDraw事件，根据正确的值重新绘制
        // 用mSideBarView.post/postDelayed总是不准确
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mSideBarView.invalidate();
            }
        }, 100);
    }

    @Override
    public void onServerConnected() {
//        ((ProgressProvider) getActivity()).showToastShort(getString(R.string.gdb_server_online), ProgressProvider.TOAST_SUCCESS);
        if (isVisible()) {
            iHomeShare.getPresenter().checkNewStarFile();
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
                        data.put("items", iHomeShare.getPresenter().pickStarToDownload(downloadList, repeatList));
                        data.put("existedItems", repeatList);
                        data.put("savePath", Configuration.GDB_IMG_STAR);
                        data.put("optionMsg", String.format(getContext().getString(R.string.gdb_option_download), downloadList.size()));
                    }
                });
                downloadDialog.setOnDownloadListener(new DownloadDialog.OnDownloadListener() {
                    @Override
                    public void onDownloadFinish(DownloadItem item) {

                    }

                    @Override
                    public void onDownloadFinish(List<DownloadItem> downloadList) {
                        // 所有内容下载完成后，统一进行异步encypt，然后更新starImageMap和recordImageMap，完成后通知adapter更新
                        iHomeShare.getPresenter().finishDownload(downloadList);
                    }
                });
            }
            else {
                List<DownloadItem> repeatList = new ArrayList<>();
                List<DownloadItem> newList = iHomeShare.getPresenter().pickStarToDownload(downloadList, repeatList);
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
        mNameAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_gdb_check_server:
                iHomeShare.getPresenter().checkNewStarFile();
                break;
            case R.id.menu_gdb_recommend:
                if (recommendDialog == null) {
                    recommendDialog = new RecommendDialog(getActivity());
                }
                recommendDialog.show();
                break;
            case R.id.menu_gdb_download:
                if (downloadDialog != null) {
                    downloadDialog.show();
                }
                break;
        }
        return false;
    }

    public void reloadStarList() {
        iHomeShare.getPresenter().loadStarList();
    }

    public void checkServerStatus() {
        // 只要检测完更新，无论成功或失败都要接着开始检测图片更新
        iHomeShare.getPresenter().checkServerStatus();
    }
}
