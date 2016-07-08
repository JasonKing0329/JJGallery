package com.jing.app.jjgallery.viewsystem.main.filesystem;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.jing.app.jjgallery.viewsystem.sub.thumb.OnThumbItemListener;
import com.jing.app.jjgallery.viewsystem.sub.thumb.ThumbFolderAdapter;

import java.io.File;
import java.util.List;

/**
 * Created by JingYang on 2016/7/8 0008.
 * Description:
 */
public class FileThumbFolderAdapter extends ThumbFolderAdapter {

    private List<File> folderList;

    public FileThumbFolderAdapter(Context context, OnThumbItemListener listener, List<File> folderList) {
        super(context, listener);
        this.folderList = folderList;
    }

    @Override
    protected void bindDataToView(int position, ImageView imageView, TextView textView) {
        textView.setText(folderList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return folderList == null ? 0:folderList.size();
    }
}
