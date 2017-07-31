package com.jing.app.jjgallery.gdb.view.surf;

import com.jing.app.jjgallery.gdb.presenter.surf.SurfPresenter;
import com.jing.app.jjgallery.gdb.view.IFragmentHolder;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/7/31 13:46
 */
public interface ISurfHolder extends IFragmentHolder {

    SurfPresenter getPresenter();

    void startProgress();

    void endProgress();
}
