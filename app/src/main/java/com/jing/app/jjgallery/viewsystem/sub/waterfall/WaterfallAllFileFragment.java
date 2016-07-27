package com.jing.app.jjgallery.viewsystem.sub.waterfall;

/**
 * Created by JingYang on 2016/7/27 0027.
 * Description:
 */
public class WaterfallAllFileFragment extends WaterfallFolderFragment {

    @Override
    protected void loadListData() {
        mPresenter.loadAllFileDatas();
    }
}
