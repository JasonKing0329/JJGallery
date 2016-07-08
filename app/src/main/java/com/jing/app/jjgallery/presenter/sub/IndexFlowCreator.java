package com.jing.app.jjgallery.presenter.sub;

import com.jing.app.jjgallery.presenter.main.filesystem.FolderManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by JingYang on 2016/7/7 0007.
 * Description:
 */
public class IndexFlowCreator {
    public static List<String> createFolderIndex() {

        List<String> list = new ArrayList<String>();
        List<File> fileList = new FolderManager().collectAllFolders();;
        for (int i = 0; i < fileList.size(); i ++) {
            list.add(fileList.get(i).getPath());
        }
        return list;
    }

}
