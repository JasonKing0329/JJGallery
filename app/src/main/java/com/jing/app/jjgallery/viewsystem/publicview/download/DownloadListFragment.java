package com.jing.app.jjgallery.viewsystem.publicview.download;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.bean.DownloadItemProxy;
import com.jing.app.jjgallery.http.bean.data.DownloadItem;
import com.jing.app.jjgallery.gdb.model.DownloadCallback;
import com.jing.app.jjgallery.gdb.model.DownloadManager;
import com.jing.app.jjgallery.gdb.view.IFragmentHolder;
import com.jing.app.jjgallery.presenter.main.SettingProperties;
import com.jing.app.jjgallery.service.http.progress.ProgressListener;
import com.jing.app.jjgallery.util.DebugLog;
import com.jing.app.jjgallery.viewsystem.sub.dialog.DraggableDialogFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/7/25 11:49
 */
public class DownloadListFragment extends DraggableDialogFragment.ContentFragment {

    @BindView(R.id.tv_empty)
    TextView tvEmpty;
    @BindView(R.id.rv_list)
    RecyclerView rvList;

    private DownloadManager downloadManager;

    private DownloadDialogFragment.OnDownloadListener onDownloadListener;

    private IDownloadContentHolder contentHolder;

    /**
     * 全部下载内容
     */
    private List<DownloadItemProxy> itemList;

    private DownloadAdapter listAdapter;

    @Override
    protected int getLayoutRes() {
        return R.layout.dlg_ft_download_list;
    }

    @Override
    protected void initView(View view) {
        ButterKnife.bind(this, view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvList.setLayoutManager(layoutManager);

        itemList = new ArrayList<>();
        downloadManager = new DownloadManager(downloadCallback, SettingProperties.getMaxDownloadItem(getActivity()));

        downloadManager.setSavePath(contentHolder.getSavePath());
        fillProxy(contentHolder.getDownloadList());

        listAdapter = new DownloadAdapter(itemList);
        rvList.setAdapter(listAdapter);
        startDownload();

        if (itemList.size() == 0) {
            tvEmpty.setVisibility(View.VISIBLE);
            rvList.setVisibility(View.GONE);
        }
    }

    @Override
    protected void bindChildFragmentHolder(IFragmentHolder holder) {
        contentHolder = (IDownloadContentHolder) holder;
    }

    public void setOnDownloadListener(DownloadDialogFragment.OnDownloadListener onDownloadListener) {
        this.onDownloadListener = onDownloadListener;
    }

    private void fillProxy(List<DownloadItem> list) {
        itemList.clear();
        addProxy(list);
    }

    private void addProxy(List<DownloadItem> list) {
        for (DownloadItem item:list) {
            DownloadItemProxy proxy = new DownloadItemProxy();
            proxy.setItem(item);
            proxy.setProgress(0);
            itemList.add(proxy);
        }
    }

    private void startDownload() {
        for (int i = 0; i < itemList.size(); i ++) {
            final int index = i;
            downloadManager.downloadFile(itemList.get(i).getItem(), new ProgressListener() {
                private int lastProgress;
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    int progress = (int)(100 * 1f * bytesRead / contentLength);
//                    DebugLog.e("progress:" + progress);

                    if (progress - lastProgress > 8 || done) {// 避免更新太过频繁
                        lastProgress = progress;
                        Bundle bundle = new Bundle();
                        bundle.putInt("index", index);
                        bundle.putInt("progress", progress);
                        Message message = new Message();
                        message.setData(bundle);
                        uiHandler.sendMessage(message);
                    }
                }
            });
        }
    }

    private Handler uiHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            int index = bundle.getInt("index");
            int progress = bundle.getInt("progress");
            itemList.get(index).setProgress(progress);
            listAdapter.notifyDataSetChanged();
        }
    };

    private DownloadCallback downloadCallback = new DownloadCallback() {
        @Override
        public void onDownloadFinish(DownloadItem item) {
            if (onDownloadListener != null) {
                onDownloadListener.onDownloadFinish(item);
            }
        }

        @Override
        public void onDownloadError(DownloadItem item) {
            DebugLog.e(item.getName());
        }

        @Override
        public void onDownloadAllFinish() {
            if (onDownloadListener != null) {
                onDownloadListener.onDownloadFinish(contentHolder.getDownloadList());
            }
        }
    };
}
