package com.jing.app.jjgallery.viewsystem.main.order;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;
import android.widget.TextView;

import com.jing.app.jjgallery.bean.order.SOrder;
import com.jing.app.jjgallery.service.image.PictureManagerUpdate;
import com.jing.app.jjgallery.viewsystem.sub.thumb.OnThumbFolderItemListener;
import com.jing.app.jjgallery.viewsystem.sub.thumb.ThumbFolderAdapter;

import java.util.List;

/**
 * Created by JingYang on 2016/7/11 0011.
 * Description:
 */
public class SOrderThumbFolderAdapter extends ThumbFolderAdapter {

    private List<SOrder> orderList;

    public SOrderThumbFolderAdapter(Context context, OnThumbFolderItemListener listener) {
        super(context, listener);
    }

    @Override
    protected void bindDataToView(int position, ImageView image, TextView text) {

        SOrder order = orderList.get(position);
        text.setText(order.getName() + "(" + order.getItemNumber() + ")");
        Bitmap bitmap = PictureManagerUpdate.getInstance().createOrderCircleCover(order.getCoverPath(), mContext);
        if (bitmap != null) {
            image.setImageBitmap(bitmap);
        }
    }

    @Override
    public int getItemCount() {
        return orderList == null ? 0:orderList.size();
    }

    public void setDatas(List<SOrder> orderList) {
        this.orderList = orderList;
    }
}
