package com.jing.app.jjgallery.viewsystem.main.filesystem;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.presenter.sub.ThumbPresenter;
import com.jing.app.jjgallery.viewsystem.sub.thumb.OnThumbFolderItemListener;
import com.jing.app.jjgallery.viewsystem.sub.thumb.ThumbFolderAdapter;

import java.io.File;
import java.util.List;

/**
 * Created by JingYang on 2016/7/8 0008.
 * Description:
 */
public class FileThumbFolderAdapter extends ThumbFolderAdapter {

    private List<File> folderList;

    private ThumbPresenter mPresenter;

    public FileThumbFolderAdapter(Context context, OnThumbFolderItemListener listener) {
        super(context, listener);
    }

    @Override
    protected void bindDataToView(int position, ImageView imageView, TextView textView) {
        textView.setText(folderList.get(position).getName());
        if (mPresenter.hasChildFolder(folderList.get(position).getPath())) {
            imageView.setImageResource(R.drawable.ic_folder_parent);
        }
        else {
            imageView.setImageResource(R.drawable.ic_folder_sub);
        }
    }

    @Override
    public int getItemCount() {
        return folderList == null ? 0:folderList.size();
    }

    public void setDatas(List<File> folderList) {
        this.folderList = folderList;
    }

    public void setPresenter(ThumbPresenter mPresenter) {
        this.mPresenter = mPresenter;
    }
}
