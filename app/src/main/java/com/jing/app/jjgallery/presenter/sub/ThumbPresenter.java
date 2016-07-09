package com.jing.app.jjgallery.presenter.sub;

import com.jing.app.jjgallery.BasePresenter;
import com.jing.app.jjgallery.model.main.file.FolderManager;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by JingYang on 2016/7/8 0008.
 * Description:
 */
public class ThumbPresenter extends BasePresenter {

    public static final int SORT_NAME = 0;
    public static final int SORT_TIME = 1;
    private FolderManager folderManager;

    public ThumbPresenter() {
        folderManager = new FolderManager();
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
}
