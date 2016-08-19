package com.jing.app.jjgallery.viewsystem.sub.waterfall;

import android.content.Context;

import com.jing.app.jjgallery.bean.filesystem.FileBean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by JingYang on 2016/7/27 0027.
 * Description:
 */
public class FolderWaterfallAdapter extends BaseWaterfallAdapter {

    private List<FileBean> originList;
    private List<FileBean> list;

    public FolderWaterfallAdapter(Context context, List<FileBean> list, int column) {
        super(context, column);
        this.list = list;
    }

    @Override
    public String getImagePath(int position) {
        return list.get(position).getPath();
    }

    @Override
    public int getItemCount() {
        return list == null ? 0:list.size();
    }

    @Override
    protected List<String> getSelectedList() {
        List<String> list = null;
        if (mCheckMap.size() > 0) {
            list = new ArrayList<>();
            for (int i = 0; i < mCheckMap.size(); i ++) {
                list.add(this.list.get(mCheckMap.keyAt(i)).getPath());
            }
        }
        return list;
    }

    @Override
    protected List<Integer> getSelectedIndex() {
        List<Integer> list = null;

        if (mCheckMap.size() > 0) {
            list = new ArrayList<>();
            for (int i = 0; i < mCheckMap.size(); i ++) {
                list.add(mCheckMap.keyAt(i));
            }
            Collections.sort(list);
        }
        return list;
    }

    @Override
    protected void removeItem(int index) {
        list.remove(index);
        notifyItemRemoved(index);
    }

    @Override
    public void onOriginSequence() {
        if (originList != null) {
            if (list != null) {
                // 恢复原始序列
                list.clear();
                for (FileBean bean:originList) {
                    list.add(bean);
                }
                notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onRandomSequence() {
        if (originList == null) {
            originList = new ArrayList<>();
            if (list != null) {
                // 保存原始序列
                for (FileBean bean:list) {
                    originList.add(bean);
                }
                // 打乱现有序列
                Collections.shuffle(list);

                notifyDataSetChanged();
            }
        }
        else {
            // 打乱现有序列
            Collections.shuffle(list);
            notifyDataSetChanged();
        }
    }
}
