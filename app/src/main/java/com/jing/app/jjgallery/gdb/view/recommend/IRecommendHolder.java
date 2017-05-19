package com.jing.app.jjgallery.gdb.view.recommend;

import com.jing.app.jjgallery.gdb.presenter.GdbGuidePresenter;
import com.jing.app.jjgallery.gdb.view.IFragmentHolder;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/5/19 13:59
 */
public interface IRecommendHolder extends IFragmentHolder {
    GdbGuidePresenter getPresenter();
    void onRecommendRecordsLoaded();
}
