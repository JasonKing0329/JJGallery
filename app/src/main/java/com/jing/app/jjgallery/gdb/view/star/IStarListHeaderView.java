package com.jing.app.jjgallery.gdb.view.star;

import com.king.service.gdb.bean.StarCountBean;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/7/12 9:36
 */
public interface IStarListHeaderView {
    void onStarCountLoaded(StarCountBean bean);

    void onFavorListLoaded();
}
