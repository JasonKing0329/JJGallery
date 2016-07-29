package com.jing.app.jjgallery.viewsystem.sub.book;

/**
 * Created by JingYang on 2016/7/28 0028.
 * Description:
 */
public class FolderBookFragment extends BookFragment {

    private String folderPath;

    public void setFolderPath(String folderPath) {
        this.folderPath = folderPath;
    }

    @Override
    protected void loadDatas() {
        mPresenter.loadDatasByFolder(folderPath);
    }
}
