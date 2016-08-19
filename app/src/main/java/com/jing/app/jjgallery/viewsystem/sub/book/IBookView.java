package com.jing.app.jjgallery.viewsystem.sub.book;

import com.jing.app.jjgallery.bean.BookInforBean;
import com.jing.app.jjgallery.service.image.ImageValue;

import java.util.List;

/**
 * Created by JingYang on 2016/7/29 0029.
 * Description:
 */
public interface IBookView {
    void onDatasReady(List<List<ImageValue>> list, BookInforBean inforBean);
}
