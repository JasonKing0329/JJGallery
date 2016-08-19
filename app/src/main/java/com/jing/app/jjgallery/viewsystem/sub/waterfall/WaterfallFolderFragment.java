package com.jing.app.jjgallery.viewsystem.sub.waterfall;

import com.jing.app.jjgallery.bean.filesystem.FileBean;
import com.jing.app.jjgallery.presenter.main.order.SOrderProvider;
import com.jing.app.jjgallery.presenter.main.order.SOrderProviderCallback;

import java.util.List;

/**
 * Created by JingYang on 2016/7/27 0027.
 * Description:
 */
public class WaterfallFolderFragment extends WaterfallFragment {

    private BaseWaterfallAdapter mAdapter;
    private String folder;

    public void setFolderPath(String path) {
        folder = path;
    }

    @Override
    protected void initSOrderProvider() {
        sOrderProvider = new SOrderProvider(getActivity(), new SOrderProviderCallback() {
            @Override
            public void onMoveFinish(String folderPath) {

            }

            @Override
            public void onAddToOrderFinished() {

            }

            @Override
            public void onDeleteIndex(int index) {
                //show animation
                mAdapter.removeItem(index);
            }

            @Override
            public void onDeleteFinished(int count) {
                //restore normal status
                mAdapter.cancelActionMode();
                mAdapter.notifyDataSetChanged();
                showSelectionMode(false);
            }
        });
    }

    @Override
    protected void loadListData() {
        mPresenter.loadFolderItems(folder);
    }

    @Override
    protected void onDatasReady(Object data) {
        List<FileBean> list = (List<FileBean>) data;
        mAdapter = new FolderWaterfallAdapter(getActivity(), list, nColumn);
    }

    @Override
    protected BaseWaterfallAdapter getAdapter() {
        return mAdapter;
    }

    @Override
    protected void deleteSelectedFile() {
        sOrderProvider.deleteItemFromFolder(getSelectedList());
    }

    @Override
    protected void onOriginSequence() {
        mAdapter.onOriginSequence();
    }

    @Override
    protected void onRandomSequence() {
        mAdapter.onRandomSequence();
    }
}
