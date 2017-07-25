package com.jing.app.jjgallery.gdb.view.pub;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.bean.DownloadItemProxy;
import com.jing.app.jjgallery.bean.http.DownloadItem;
import com.jing.app.jjgallery.gdb.model.DownloadCallback;
import com.jing.app.jjgallery.gdb.model.DownloadManager;
import com.jing.app.jjgallery.presenter.main.SettingProperties;
import com.jing.app.jjgallery.service.http.progress.ProgressListener;
import com.jing.app.jjgallery.util.DisplayHelper;
import com.jing.app.jjgallery.viewsystem.publicview.CustomDialog;
import com.jing.app.jjgallery.viewsystem.publicview.download.DownloadAdapter;
import com.jing.app.jjgallery.viewsystem.publicview.download.DownloadExistAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * FIXME SettingActivity改为继承至support包后废弃掉
 */
public class DownloadDialog extends CustomDialog implements DownloadCallback, Handler.Callback {

    public interface OnDownloadListener {
        void onDownloadFinish(DownloadItem item);
        void onDownloadFinish(List<DownloadItem> downloadList);
    }

    private TextView emptyView;
    private RecyclerView downloadRecyclerView;
    private DownloadAdapter adapter;

    private RecyclerView existedRecyclerView;
    private DownloadExistAdapter existAdapter;

    private ViewGroup optionGroup;
    private ViewGroup downListGroup;

    private TextView continueButton;

    /**
     * 全部下载内容
     */
    private List<DownloadItemProxy> itemList;
    /**
     * 直接下载，不提示
     */
    private boolean startNoOption;
    /**
     * 不直接下载的提示内容
     */
    private String optionMessage;
    /**
     * 下载目录
     */
    private String savePath;

    /**
     * 标志重新下载（不是重新打开下载框）
     */
    private boolean newUpdateFlag;

    private DownloadManager downloadManager;
    private Handler handler;

    private OnDownloadListener onDownloadListener;
    private List<DownloadItem> downloadList;
    private List<DownloadItem> existedList;

    public DownloadDialog(Context context, OnCustomDialogActionListener actionListener) {
        super(context, actionListener);
        requestCancelAction(true);
        applyLightThemeStyle();
        setTitle(R.string.download_title);

        itemList = new ArrayList<>();
        downloadManager = new DownloadManager(this, SettingProperties.getMaxDownloadItem(context));
        handler = new Handler(this);

        HashMap<String, Object> map = new HashMap<>();
        actionListener.onLoadData(map);

        downloadList = (List<DownloadItem>) map.get("items");
        Object object = map.get("existedItems");
        if (object != null) {
            existedList = (List<DownloadItem>) object;
        }
        savePath = (String) map.get("savePath");
        if (map.get("noOption") != null) {
            startNoOption = (Boolean) map.get("noOption");
        }
        if (map.get("optionMsg") != null) {
            optionMessage = (String) map.get("optionMsg");
        }

        downloadManager.setSavePath(savePath);
        fillProxy(downloadList);
        newUpdateFlag = true;
    }

    public void setOnDownloadListener(OnDownloadListener onDownloadListener) {
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

    @Override
    protected View getCustomView() {
        View view = getLayoutInflater().inflate(R.layout.dlg_download, null);
        emptyView = (TextView) view.findViewById(R.id.download_empty);
        continueButton = (TextView) view.findViewById(R.id.download_continue);
        continueButton.setOnClickListener(this);
        optionGroup = (ViewGroup) view.findViewById(R.id.download_option_group);
        downListGroup = (ViewGroup) view.findViewById(R.id.download_list_group);

        downloadRecyclerView = (RecyclerView) view.findViewById(R.id.download_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        downloadRecyclerView.setLayoutManager(layoutManager);
        existedRecyclerView = (RecyclerView) view.findViewById(R.id.download_list_existed);

        int col = 2;
        if (DisplayHelper.isTabModel(getContext())) {
            col = 3;
        }
        GridLayoutManager gridManager = new GridLayoutManager(getContext(), col);
        existedRecyclerView.setLayoutManager(gridManager);
        return view;
    }

    @Override
    protected View getCustomToolbar() {
        return null;
    }

    @Override
    public void show() {
        super.show();

        if (newUpdateFlag) {

            // 如果有已存在的下载内容，先进行覆盖筛选
            if (existedList != null && existedList.size() > 0) {
                updateExistedList();
            }
            else {
                updateDownloadList();
            }
        }
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        if (view == continueButton) {
            List<DownloadItem> list = existAdapter.getCheckedItems();
            downloadList.addAll(list);
            addProxy(list);
            optionGroup.setVisibility(View.GONE);
            updateDownloadList();
        }
    }

    private void updateExistedList() {
        optionGroup.setVisibility(View.VISIBLE);
        existAdapter = new DownloadExistAdapter(existedList);
        existedRecyclerView.setAdapter(existAdapter);
    }

    private void updateDownloadList() {
        downListGroup.setVisibility(View.VISIBLE);
        if (itemList.size() == 0) {
            emptyView.setVisibility(View.VISIBLE);
            downloadRecyclerView.setVisibility(View.INVISIBLE);
        }
        else {
            if (startNoOption) {
                showListAndStartDownload();
            }
            else {
                new AlertDialog.Builder(getContext())
                        .setMessage(optionMessage)
                        .setPositiveButton(R.string.ok, new OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                showListAndStartDownload();
                            }
                        })
                        .setNegativeButton(R.string.cancel, new OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dismiss();
                            }
                        })
                        .show();
            }
        }
    }

    private void showListAndStartDownload() {
        // 开始下载任务后再show就要显示之前的内容
        newUpdateFlag = false;

        adapter = new DownloadAdapter(itemList);
        downloadRecyclerView.setAdapter(adapter);
        startDownload();
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
                        handler.sendMessage(message);
                    }
                }
            });
        }
    }

    public void newUpdate(List<DownloadItem> downloadItems, List<DownloadItem> existList) {
        downloadList = downloadItems;
        existedList = existList;
        fillProxy(downloadItems);
        newUpdateFlag = true;
    }

    @Override
    public void onDownloadFinish(DownloadItem item) {
        if (onDownloadListener != null) {
            onDownloadListener.onDownloadFinish(item);
        }
    }

    @Override
    public void onDownloadError(DownloadItem item) {

    }

    @Override
    public void onDownloadAllFinish() {
        if (onDownloadListener != null) {
            onDownloadListener.onDownloadFinish(downloadList);
        }
    }

    @Override
    public boolean handleMessage(Message msg) {
        Bundle bundle = msg.getData();
        int index = bundle.getInt("index");
        int progress = bundle.getInt("progress");
        itemList.get(index).setProgress(progress);
        adapter.notifyDataSetChanged();
        return false;
    }
}
