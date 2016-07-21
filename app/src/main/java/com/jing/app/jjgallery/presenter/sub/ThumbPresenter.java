package com.jing.app.jjgallery.presenter.sub;

import com.jing.app.jjgallery.BasePresenter;
import com.jing.app.jjgallery.bean.order.SOrder;
import com.jing.app.jjgallery.config.PreferenceValue;
import com.jing.app.jjgallery.model.main.file.FolderManager;
import com.jing.app.jjgallery.model.main.order.SOrderCallback;
import com.jing.app.jjgallery.model.main.order.SOrderManager;
import com.jing.app.jjgallery.viewsystem.main.order.ISOrderDataCallback;

import java.io.File;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by JingYang on 2016/7/8 0008.
 * Description:
 */
public class ThumbPresenter extends BasePresenter implements SOrderCallback {

    public static final int SORT_NAME = 0;
    public static final int SORT_TIME = 1;

    private FolderManager folderManager;
    private SOrderManager sorderManager;

    private ISOrderDataCallback sorderDataCallback;

    public ThumbPresenter() {
        folderManager = new FolderManager();
        sorderManager = new SOrderManager(this);
    }

    public void setSOrderDataCallback(ISOrderDataCallback callback) {
        this.sorderDataCallback = callback;
    }

    /**
     *
     * @param path parent folder
     * @param sortMode SORT_NAME or SORT_TIME
     * @return
     */
    public List<File> loadSubFolders(String path, int sortMode) {
        List<File> list = folderManager.loadList(path);
        if (list != null) {
            if (sortMode == SORT_TIME) {
                Collections.sort(list, new TimeComparator());
            }
            else {
                Collections.sort(list, new NameComparator());
            }
        }
        return list;
    }

    public List<String> loadFolderItems(String path) {
        return  folderManager.loadPathList(path);
    }

    public boolean hasChildFolder(String path) {
        return folderManager.hasFolderChild(new File(path));
    }

    // 异步操作
    public void loadAllOrders() {
        sorderManager.loadAllOrders(PreferenceValue.ORDERBY_NAME);
    }

    @Override
    public void onQueryAllOrders(List<SOrder> list, int orderBy) {
        if (list != null) {
            if (orderBy == PreferenceValue.ORDERBY_NAME) {
                Collections.sort(list, new SOrderComparator());
            }
            else if (orderBy == PreferenceValue.ORDERBY_DATE) {
                Collections.reverse(list);
            }
        }
        sorderDataCallback.onQueryAllOrders(list);
    }

    // 同步操作
    public void loadOrderItems(SOrder sOrder) {
        sorderManager.loadOrderItems(sOrder);
    }

    private class NameComparator implements Comparator<File> {

        @Override
        public int compare(File lhs, File rhs) {
            return lhs.getName().toLowerCase().compareTo(rhs.getName().toLowerCase());
        }
    }

    private class TimeComparator implements Comparator<File> {

        @Override
        public int compare(File lhs, File rhs) {
            return (int) (lhs.lastModified() - rhs.lastModified());
        }
    }

    private class SOrderComparator implements Comparator<SOrder> {

        @Override
        public int compare(SOrder lhs, SOrder rhs) {
            return lhs.getName().toLowerCase().compareTo(rhs.getName().toLowerCase());
        }
    }

}
