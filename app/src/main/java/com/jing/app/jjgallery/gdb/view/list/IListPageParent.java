package com.jing.app.jjgallery.gdb.view.list;

import com.jing.app.jjgallery.gdb.presenter.GdbPresenter;
import com.jing.app.jjgallery.gdb.view.IFragmentHolder;
import com.jing.app.jjgallery.viewsystem.publicview.ActionBar;

/**
 * Created by Administrator on 2017/1/1 0001.
 */

public interface IListPageParent extends IFragmentHolder {
    GdbPresenter getPresenter();
    ActionBar getActionbar();
}
