package com.jing.app.jjgallery.presenter.sub;

import com.jing.app.jjgallery.BasePresenter;
import com.jing.app.jjgallery.model.main.file.FolderManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by JingYang on 2016/7/8 0008.
 * Description:
 */
public class ThumbPresenter extends BasePresenter {

    private FolderManager folderManager;

    public List<File> loadSubFolders(String path) {
        return  new FolderManager().loadList(path);
    }
}
