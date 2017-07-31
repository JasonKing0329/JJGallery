package com.jing.app.jjgallery.gdb.view.surf;

import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.jing.app.jjgallery.BaseFragmentV4;
import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.gdb.view.IFragmentHolder;
import com.jing.app.jjgallery.gdb.view.pub.AutoLoadMoreRecyclerView;
import com.jing.app.jjgallery.http.HttpConstants;
import com.jing.app.jjgallery.http.bean.data.FileBean;
import com.jing.app.jjgallery.presenter.main.SettingProperties;
import com.jing.app.jjgallery.util.DebugLog;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 描述: 只负责list的展示与发起加载的时机，响应外部的排序、管理以及发起操作
 * <p/>作者：景阳
 * <p/>创建时间: 2017/7/31 13:27
 */
public class SurfFragment extends BaseFragmentV4 {

    @BindView(R.id.rv_files)
    AutoLoadMoreRecyclerView rvFiles;

    private ISurfHolder holder;

    private FileBean folderBean;
    private SurfAdapter surfAdapter;
    private List<SurfFileBean> surfFileList;
    private SurfAdapter.OnSurfItemActionListener itemListener;

    @Override
    protected void bindFragmentHolder(IFragmentHolder holder) {
        this.holder = (ISurfHolder) holder;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_gdb_surf_list;
    }

    @Override
    protected void initView(View view) {
        ButterKnife.bind(this, view);

        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rvFiles.setLayoutManager(manager);

        rvFiles.setEnableLoadMore(true);
        rvFiles.setOnLoadMoreListener(new AutoLoadMoreRecyclerView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {

            }
        });

        loadFolder();
    }

    /**
     * go to the top of list
     */
    public void goTop() {
        rvFiles.scrollToPosition(0);
    }

    /**
     * set current folder of current fragment
     * @param fileBean
     */
    public void setFolder(FileBean fileBean) {
        this.folderBean = fileBean;
    }

    /**
     * load file list of current folder
     * callback at onFolderReceived
     */
    public void loadFolder() {
        holder.startProgress();
        DebugLog.e(folderBean.getPath());
        if (HttpConstants.FOLDER_TYPE_CONTENT.equals(folderBean.getPath())) {
            holder.getPresenter().surf(HttpConstants.FOLDER_TYPE_CONTENT, null, SettingProperties.isGdbSurfRelated());
        }
        else {
            holder.getPresenter().surf(HttpConstants.FOLDER_TYPE_FOLDER, folderBean.getPath(), SettingProperties.isGdbSurfRelated());
        }

        holder.onFolderLoaded(folderBean);
    }

    /**
     * load folder finished
     * the callback of loadFolder
     * @param list
     */
    public void onFolderReceived(List<SurfFileBean> list) {
        surfFileList = list;
        updateSurfList(list);
        holder.endProgress();
    }

    /**
     * update current file list
     * @param list
     */
    private void updateSurfList(List<SurfFileBean> list) {
        if (surfAdapter == null) {
            surfAdapter = new SurfAdapter(list);
            surfAdapter.setOnSurfItemActionListener(itemListener);
            rvFiles.setAdapter(surfAdapter);
        } else {
            surfAdapter.setList(list);
            surfAdapter.notifyDataSetChanged();
        }
    }

    /**
     * set listener for list item
     * @param listener
     */
    public void setOnSurfItemActionListener(SurfAdapter.OnSurfItemActionListener listener) {
        this.itemListener = listener;
    }

    /**
     * current file list
     * @return
     */
    public List<SurfFileBean> getSurfFileList() {
        return surfFileList;
    }

    /**
     * notify item changed in index
     * @param index
     */
    public void notifyItemChanged(int index) {
        surfAdapter.notifyItemChanged(index);
    }

    /**
     * surfFileList data changed, but the surfFileList reference is not changed
     */
    public void notifyDataSetChanged() {
        surfAdapter.notifyDataSetChanged();
    }
}
