package com.jing.app.jjgallery.presenter.main.filesystem;

import android.content.Context;

import com.jing.app.jjgallery.BasePresenter;
import com.jing.app.jjgallery.config.PreferenceKey;
import com.jing.app.jjgallery.presenter.main.SettingProperties;
import com.jing.app.jjgallery.viewsystem.main.filesystem.IFileManagerView;

/**
 * Created by JingYang on 2016/6/24 0024.
 * Description:
 */
public class FileManagerPresenter extends BasePresenter {
    private IFileManagerView fileManagerView;

    public FileManagerPresenter(IFileManagerView view) {
        fileManagerView = view;
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
}
