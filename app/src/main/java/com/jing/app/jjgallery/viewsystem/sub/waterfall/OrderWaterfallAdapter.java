package com.jing.app.jjgallery.viewsystem.sub.waterfall;

import android.content.Context;
import android.util.SparseBooleanArray;

import com.jing.app.jjgallery.bean.filesystem.FileBean;
import com.jing.app.jjgallery.bean.order.SOrder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by JingYang on 2016/7/27 0027.
 * Description:
 */
public class OrderWaterfallAdapter extends BaseWaterfallAdapter {

    private SOrder sOrder;
    private List<String> originList;

    public OrderWaterfallAdapter(Context context, SOrder sOrder, int column) {
        super(context, column);
        this.sOrder = sOrder;
    }

    @Override
    public String getImagePath(int position) {
        return sOrder.getImgPathList().get(position);
    }

    @Override
    public int getItemCount() {
        return sOrder.getImgPathList() == null ? 0:sOrder.getImgPathList().size();
    }

    @Override
    protected List<String> getSelectedList() {
        List<String> list = null;
        if (mCheckMap.size() > 0) {
            list = new ArrayList<>();
            for (int i = 0; i < mCheckMap.size(); i ++) {
                list.add(sOrder.getImgPathList().get(mCheckMap.keyAt(i)));
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
        sOrder.getImgPathList().remove(index);
        notifyItemRemoved(index);
    }

    @Override
    public void onOriginSequence() {
        if (originList != null) {
            if (sOrder.getImgPathList() != null) {
                // 恢复原始序列
                sOrder.getImgPathList().clear();
                for (String path:originList) {
                    sOrder.getImgPathList().add(path);
                }
                notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onRandomSequence() {
        if (originList == null) {
            originList = new ArrayList<>();
            if (sOrder.getImgPathList() != null) {
                // 保存原始序列
                for (String path:sOrder.getImgPathList()) {
                    originList.add(path);
                }
                // 打乱现有序列
                Collections.shuffle(sOrder.getImgPathList());

                notifyDataSetChanged();
            }
        }
        else {
            // 打乱现有序列
            Collections.shuffle(sOrder.getImgPathList());
            notifyDataSetChanged();
        }
    }
}
