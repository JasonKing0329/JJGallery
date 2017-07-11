package com.jing.app.jjgallery.gdb.view.record;

import com.king.service.gdb.bean.Record;

import java.util.List;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/7/11 11:30
 */
public interface IGdbSceneView {
    void onScenesLoaded(List<String> data);

    void onRecordsLoaded(List<Record> data);
}
