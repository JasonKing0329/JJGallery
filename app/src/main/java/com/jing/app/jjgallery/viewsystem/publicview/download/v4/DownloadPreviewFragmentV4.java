package com.jing.app.jjgallery.viewsystem.publicview.download.v4;

import android.content.DialogInterface;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.http.bean.data.DownloadItem;
import com.jing.app.jjgallery.gdb.view.IFragmentHolder;
import com.jing.app.jjgallery.util.DisplayHelper;
import com.jing.app.jjgallery.viewsystem.sub.dialog.AlertDialogFragmentV4;
import com.jing.app.jjgallery.viewsystem.publicview.download.DownloadExistAdapter;
import com.jing.app.jjgallery.viewsystem.publicview.download.IDownloadContentHolder;
import com.jing.app.jjgallery.viewsystem.sub.dialog.DraggableDialogFragmentV4;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/7/25 11:48
 */
public class DownloadPreviewFragmentV4 extends DraggableDialogFragmentV4.ContentFragmentV4 {

    @BindView(R.id.rv_existed)
    RecyclerView rvExisted;

    @BindView(R.id.cb_select_all)
    CheckBox cbSelectAll;

    private IDownloadContentHolder contentHolder;

    private DownloadExistAdapter existAdapter;

    @Override
    protected int getLayoutRes() {
        return R.layout.dlg_ft_download_exist;
    }

    @Override
    protected void initView(View view) {
        ButterKnife.bind(this, view);
        int col = 2;
        if (DisplayHelper.isTabModel(getActivity())) {
            col = 3;
        }
        GridLayoutManager gridManager = new GridLayoutManager(getActivity(), col);
        rvExisted.setLayoutManager(gridManager);

        if (contentHolder.getExistedList() != null && contentHolder.getExistedList().size() > 0) {
            rvExisted.setVisibility(View.VISIBLE);
            existAdapter = new DownloadExistAdapter(contentHolder.getExistedList());
            rvExisted.setAdapter(existAdapter);

            cbSelectAll.setVisibility(View.VISIBLE);
            cbSelectAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        existAdapter.selectAll();
                    } else {
                        existAdapter.unSelectAll();
                    }
                    existAdapter.notifyDataSetChanged();
                }
            });

        } else {
            showReadyDialog();
            cbSelectAll.setVisibility(View.GONE);
        }
    }

    private void showReadyDialog() {

        int items = contentHolder.getDownloadList().size();
        if (existAdapter != null) {
            items += existAdapter.getCheckedItems().size();
        }
        String message = String.format(getString(R.string.gdb_option_download), items);
        AlertDialogFragmentV4 dialog = new AlertDialogFragmentV4();
        dialog.setMessage(message);
        dialog.setPositiveText(getString(R.string.ok));
        dialog.setPositiveListener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (existAdapter != null) {
                    List<DownloadItem> list = existAdapter.getCheckedItems();
                    if (list.size() > 0) {
                        contentHolder.addDownloadItems(list);
                    }
                }
                contentHolder.startDownload();
            }
        });
        dialog.setNegativeText(getString(R.string.cancel));
        dialog.setNegativeListener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 重新选择
                if (contentHolder.getExistedList() != null && contentHolder.getExistedList().size() > 0) {
                }
                // 没有重复的item，直接关闭对话框
                else {
                    contentHolder.dismissDialog();
                }
            }
        });
        dialog.show(getChildFragmentManager(), "AlertDialogFragmentV4");
    }

    @Override
    protected void bindChildFragmentHolder(IFragmentHolder holder) {
        contentHolder = (IDownloadContentHolder) holder;
    }

    @OnClick(R.id.tv_continue)
    public void onViewClicked() {
        showReadyDialog();
    }
}
