package com.jing.app.jjgallery.gdb.view.star;

import com.jing.app.jjgallery.gdb.bean.StarProxy;
import com.king.service.gdb.bean.StarCountBean;

import java.util.List;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/7/12 9:36
 */
public interface IStarListView {
    void onLoadStarList(List<StarProxy> list);
    void onStarCountLoaded(StarCountBean bean);
}
