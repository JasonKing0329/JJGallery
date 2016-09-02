package com.jing.app.jjgallery.viewsystem.publicview;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.bean.DownloadItemProxy;
import com.jing.app.jjgallery.bean.http.DownloadItem;
import com.jing.app.jjgallery.model.pub.DownloadCallback;
import com.jing.app.jjgallery.model.pub.DownloadManager;
import com.jing.app.jjgallery.service.http.progress.ProgressListener;
import com.jing.app.jjgallery.util.DebugLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2016/9/2.
 */
public class DownloadDialog extends CustomDialog implements DownloadCallback, Handler.Callback {

    private TextView emptyView;
    private RecyclerView downloadRecyclerView;
    private DownloadAdapter adapter;

    private List<DownloadItemProxy> itemList;
    private boolean newUpdateFlag;

    private DownloadManager downloadManager;
    private Handler handler;

    public DownloadDialog(Context context, OnCustomDialogActionListener actionListener) {
        super(context, actionListener);
        requestCancelAction(true);
        applyLightThemeStyle();
        setTitle(R.string.download_title);

        itemList = new ArrayList<>();
        downloadManager = new DownloadManager(this);
        handler = new Handler(this);

        HashMap<String, Object> map = new HashMap<>();
        actionListener.onLoadData(map);
        List<DownloadItem> list = (List<DownloadItem>) map.get("items");
        fillProxy(list);
        newUpdateFlag = true;
    }

    private void fillProxy(List<DownloadItem> list) {
        itemList.clear();
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
        downloadRecyclerView = (RecyclerView) view.findViewById(R.id.download_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        downloadRecyclerView.setLayoutManager(layoutManager);
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
            updateDownloadList();
        }
    }

    private void updateDownloadList() {
        if (itemList.size() == 0) {
            emptyView.setVisibility(View.VISIBLE);
            downloadRecyclerView.setVisibility(View.INVISIBLE);
        }
        else {
            String msg = String.format(getContext().getString(R.string.gdb_option_download), itemList.size());

            new AlertDialog.Builder(getContext())
                    .setMessage(msg)
                    .setPositiveButton(R.string.ok, new OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            // 开始下载任务后再show就要显示之前的内容
                            newUpdateFlag = false;

                            adapter = new DownloadAdapter(getContext(), itemList);
                            downloadRecyclerView.setAdapter(adapter);
                            startDownload();
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

    private void startDownload() {
        for (int i = 0; i < itemList.size(); i ++) {
            downloadManager.downloadFile(itemList.get(i).getItem().getKey(), itemList.get(i).getItem().getFlag(), new ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    int progress = (int)(100 * 1f * bytesRead / contentLength);
                    DebugLog.e("progress:" + progress);
                    handler.sendEmptyMessage(progress);
                }
            });
        }
    }

    public void newUpdate(List<DownloadItem> downloadItems) {
        fillProxy(downloadItems);
        newUpdateFlag = true;
    }

    @Override
    public void onDownloadFinish(String key) {

    }

    @Override
    public void onDownloadError(String key) {

    }

    @Override
    public void onDownloadAllFinish() {

    }

    @Override
    public boolean handleMessage(Message msg) {

        return false;
    }
}
