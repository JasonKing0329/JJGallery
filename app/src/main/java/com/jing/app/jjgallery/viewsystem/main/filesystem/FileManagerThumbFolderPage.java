package com.jing.app.jjgallery.viewsystem.main.filesystem;

import android.content.Context;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.config.Configuration;
import com.jing.app.jjgallery.presenter.sub.ThumbPresenter;
import com.jing.app.jjgallery.viewsystem.publicview.ActionBar;
import com.jing.app.jjgallery.viewsystem.sub.thumb.OnThumbFolderItemListener;
import com.jing.app.jjgallery.viewsystem.sub.thumb.ThumbFolderAdapter;
import com.jing.app.jjgallery.viewsystem.sub.thumb.ThumbPage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Created by JingYang on 2016/7/8 0008.
 * Description:
 */
public class FileManagerThumbFolderPage extends ThumbPage {

    private FileThumbFolderAdapter mFolderAdapter;

    private List<File> folderList;
    private List<File> tempFileList;
    private File currentFolder;
    private Stack<Integer> focusStack;

    private int sortMode;

    public FileManagerThumbFolderPage(Context context, View contentView, boolean isChooserMode) {
        super(context, contentView, isChooserMode);
        sortMode = ThumbPresenter.SORT_NAME;
        focusStack = new Stack<>();
    }

    @Override
    public void initData() {
        mFolderAdapter.setPresenter(mPresenter);
        loadRootFolder();
    }

    @Override
    public void initActionbar(ActionBar actionBar) {
        super.initActionbar(actionBar);
        actionBar.addThumbIcon();
        actionBar.addAddIcon();
        actionBar.onConfiguration(getContext().getResources().getConfiguration().orientation);
    }

    @Override
    protected void onUpperClicked() {
        if (currentFolder != null) {
            currentFolder = currentFolder.getParentFile();

            // 设置上级目录选中状态
            mFolderAdapter.setFocusPosition(focusStack.pop());

            loadCurrentFolder();

            // 滚动到上级目录选中位置
            scrollFolderToPosition(mFolderAdapter.getFocusPosition());
        }
    }

    private void loadRootFolder() {
        folderList = mPresenter.loadSubFolders(Configuration.APP_DIR_IMG, sortMode);
        tempFileList = new ArrayList<>();
        fillTempList();

        mFolderAdapter.setDatas(tempFileList);
        mFolderAdapter.notifyDataSetChanged();
    }

    private void fillTempList() {
        if (folderList != null) {
            tempFileList.clear();
            for (File file:folderList) {
                tempFileList.add(file);
            }
        }
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

        if (mPresenter.hasChildFolder(path)) {// 点击包含子目录的父目录
            currentFolder = tempFileList.get(position);
            // 记录当前选中位置
            focusStack.push(position);
            // 进入下级目录，清空focus状态，并滚动到列表顶部
            mFolderAdapter.clearFocus();

            loadCurrentFolder();

            // 滚动到列表顶部
            scrollFolderToPosition(0);
        }
        else {// 点击不包含子目录的目录
            // 设置选中状态
            focusFolderItem(view, position);

            // 显示当前目录内容
            showFolderImage(path);
        }
    }

    private void loadCurrentFolder() {
        folderList = mPresenter.loadSubFolders(currentFolder.getPath(), sortMode);
        fillTempList();
        mFolderAdapter.notifyDataSetChanged();

        // 设置“返回上级”显示状态
        if (currentFolder.getPath().equals(Configuration.APP_DIR_IMG)) {
            currentFolder = null;
            showUpperView(false);
        }
        else {
            showUpperView(true);
        }
    }

    @Override
    public void onThumbFolderItemLongClick(View view, int position) {

    }

}
