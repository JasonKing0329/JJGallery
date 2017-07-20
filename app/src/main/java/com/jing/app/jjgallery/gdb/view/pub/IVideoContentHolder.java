package com.jing.app.jjgallery.gdb.view.pub;

import com.king.service.gdb.bean.Record;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/7/20 15:43
 */
public interface IVideoContentHolder {
    String getVideoPath();
    Record getRecord();
    void setDialogWidth(int width);
}
