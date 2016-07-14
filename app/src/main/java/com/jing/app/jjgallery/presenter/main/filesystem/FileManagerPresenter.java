package com.jing.app.jjgallery.presenter.main.filesystem;

import android.content.Context;
import android.os.AsyncTask;

import com.jing.app.jjgallery.BasePresenter;
import com.jing.app.jjgallery.config.PreferenceKey;
import com.jing.app.jjgallery.model.main.file.FileCallback;
import com.jing.app.jjgallery.model.main.file.FolderManager;
import com.jing.app.jjgallery.presenter.main.SettingProperties;
import com.jing.app.jjgallery.viewsystem.main.filesystem.IFileDataCallback;
import com.jing.app.jjgallery.viewsystem.main.filesystem.IFileManagerView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by JingYang on 2016/6/24 0024.
 * Description:
 */
public class FileManagerPresenter extends BasePresenter implements FileCallback {
    // view callback of FileManagerActivity
    private IFileManagerView fileManagerView;
    // data callback of FileXXXPage
    private IFileDataCallback fileDataCallback;

    private FolderManager folderManager;

    public FileManagerPresenter(IFileManagerView view) {
        fileManagerView = view;
        folderManager = new FolderManager();
    }

    public void setFileDataCallback(IFileDataCallback fileDataCallback) {
        this.fileDataCallback = fileDataCallback;
    }

    public void startFileManagerPage(Context context) {
        String mode = SettingProperties.getFileManagerDefaultMode(context);
        if (mode.equals(PreferenceKey.VALUE_FM_VIEW_THUMB)) {
            fileManagerView.onThumbPage();
        }
        else if (mode.equals(PreferenceKey.VALUE_FM_VIEW_INDEX)) {
            fileManagerView.onIndexPage();
        }
        else {
            fileManagerView.onListPage();
        }
    }

    public void switchToIndexPage() {
        fileManagerView.onIndexPage();
    }

    public void switchToThumbPage() {
        fileManagerView.onThumbPage();
    }

    public void switchToListPage() {
        fileManagerView.onListPage();
    }

    public void loadAllFolders() {
        folderManager.loadAllFolders();
    }

    @Override
    public void onLoadAllFolders(List<String> list) {
        fileDataCallback.onLoadAllFolders(list);
    }
}
