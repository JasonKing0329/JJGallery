package com.jing.app.jjgallery.viewsystem.main.filesystem;

import android.content.Context;
import android.view.View;

import com.jing.app.jjgallery.config.Configuration;
import com.jing.app.jjgallery.presenter.sub.ThumbPresenter;
import com.jing.app.jjgallery.viewsystem.sub.thumb.OnThumbItemListener;
import com.jing.app.jjgallery.viewsystem.sub.thumb.ThumbFolderAdapter;
import com.jing.app.jjgallery.viewsystem.sub.thumb.ThumbPage;

import java.io.File;
import java.util.List;

/**
 * Created by JingYang on 2016/7/8 0008.
 * Description:
 */
public class FileManagerThumbPage extends ThumbPage implements OnThumbItemListener {

    private Context mContext;
    private FileThumbFolderAdapter mFolderAdapter;

    private List<File> folderList;
    private List<File> tempFileList;

    public FileManagerThumbPage(Context context, View contentView, boolean isChooserMode) {
        super(context, contentView, isChooserMode);
        mContext = context;
    }

    @Override
    protected ThumbFolderAdapter getFolderAdapter() {
        if (mFolderAdapter == null) {
            folderList = mPresenter.loadSubFolders(Configuration.APP_DIR_IMG);
            mFolderAdapter = new FileThumbFolderAdapter(mContext, this, folderList);
        }
        return mFolderAdapter;
    }

    @Override
    public void onThumbItemClick(View view, int position) {

    }

    @Override
    public void onThumbItemLongClick(View view, int position) {

    }
}
