package com.jing.app.jjgallery.viewsystem.main.filesystem;

import android.content.Context;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.config.Configuration;
import com.jing.app.jjgallery.viewsystem.publicview.ActionBar;
import com.jing.app.jjgallery.viewsystem.sub.thumb.OnThumbFolderItemListener;
import com.jing.app.jjgallery.viewsystem.sub.thumb.ThumbFolderAdapter;
import com.jing.app.jjgallery.viewsystem.sub.thumb.ThumbPage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by JingYang on 2016/7/8 0008.
 * Description:
 */
public class FileManagerThumbFolderPage extends ThumbPage {

    private FileThumbFolderAdapter mFolderAdapter;

    private List<File> folderList;
    private List<File> tempFileList;

    public FileManagerThumbFolderPage(Context context, View contentView, boolean isChooserMode) {
        super(context, contentView, isChooserMode);
    }

    @Override
    public void initData() {
        loadRootFolder();
    }

    @Override
    public void initActionbar(ActionBar actionBar) {
        super.initActionbar(actionBar);
        actionBar.addThumbIcon();
        actionBar.addAddIcon();
        actionBar.onConfiguration(getContext().getResources().getConfiguration().orientation);
    }

    private void loadRootFolder() {
        folderList = mPresenter.loadSubFolders(Configuration.APP_DIR_IMG);
        if (folderList != null) {
            tempFileList = new ArrayList<>();
            for (File file:folderList) {
                tempFileList.add(file);
            }
        }

        mFolderAdapter.setDatas(tempFileList);
        mFolderAdapter.notifyDataSetChanged();
    }

    @Override
    protected ThumbFolderAdapter getFolderAdapter() {
        if (mFolderAdapter == null) {
            mFolderAdapter = new FileThumbFolderAdapter(getContext(), this);
        }
        return mFolderAdapter;
    }

    @Override
    public void onIconClick(View view) {
        super.onIconClick(view);
        switch (view.getId()) {

            case R.id.actionbar_add:
//                openCreateFolderDialog();
                break;
//			case R.id.actionbar_sort:
//				showSortPopup(v);
//				break;
            case R.id.actionbar_thumb:
                showViewModePopup(view);
                break;
        }
    }

    protected void showViewModePopup(View v) {
        PopupMenu menu = new PopupMenu(getContext(), v);
        menu.getMenuInflater().inflate(R.menu.filemanager_view_mode, menu.getMenu());
        menu.getMenu().findItem(R.id.menu_thumb_view).setVisible(false);
        menu.show();
        menu.setOnMenuItemClickListener(viewModeListener);
    }

    PopupMenu.OnMenuItemClickListener viewModeListener = new PopupMenu.OnMenuItemClickListener() {

        @Override
        public boolean onMenuItemClick(MenuItem item) {

            switch (item.getItemId()) {
                case R.id.menu_list_view:
                    if (getContext() instanceof FileManagerActivity) {
                        FileManagerActivity instance = (FileManagerActivity) getContext();
                        instance.getPresenter().switchToListPage();
                    }
                    break;
                case R.id.menu_index_view:
                    if (getContext() instanceof FileManagerActivity) {
                        FileManagerActivity instance = (FileManagerActivity) getContext();
                        instance.getPresenter().switchToIndexPage();
                    }
                    break;
            }
            return true;
        }

    };

    @Override
    public void onThumbFolderItemClick(View view, int position) {
        String path = tempFileList.get(position).getPath();
        focusFolderItem(view);
        showFolderImage(path);
    }

    @Override
    public void onThumbFolderItemLongClick(View view, int position) {

    }

}
