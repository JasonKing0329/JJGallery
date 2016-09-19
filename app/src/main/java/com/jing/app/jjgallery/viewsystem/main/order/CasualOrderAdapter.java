package com.jing.app.jjgallery.viewsystem.main.order;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.github.siyamed.shapeimageview.CircularImageView;
import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.bean.order.SOrder;
import com.jing.app.jjgallery.service.image.SImageLoader;
import com.jing.app.jjgallery.util.ColorUtils;
import com.jing.app.jjgallery.util.DensityUtil;
import com.jing.app.jjgallery.viewsystem.publicview.CasualBaseAdapter;
import com.jing.app.jjgallery.viewsystem.publicview.CasualLayout;
import com.jing.app.jjgallery.viewsystem.sub.key.Keyword;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Administrator on 2016/9/18.
 */
public class CasualOrderAdapter extends CasualBaseAdapter {

    private Context context;
    private List<SOrder> totalList;
    private int itemWidth;
    private int itemHeight;

    private List<SOrder> showList;

    public CasualOrderAdapter(Context context, List<SOrder> list) {
        this.context = context;
        this.totalList = list;
        itemWidth = context.getResources().getDimensionPixelSize(R.dimen.sorder_index_casual_item_size);
        itemHeight = itemWidth + DensityUtil.dip2px(context, 20);
        showList = new ArrayList<>();
        feedKeyword();
    }

    @Override
    public int getCount() {
        return showList == null ? 0: showList.size();
    }

    @Override
    public void clear() {
        showList.clear();
        notifyDataSetChanged();
    }

    @Override
    public View getView(int i) {
        SOrder order = showList.get(i);
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_order_index_casual, null);
        TextView textView = (TextView) view.findViewById(R.id.sorder_casual_name);
        textView.setText(order.getName());
        textView.setTextColor(ColorUtils.randomLightColor());
        SImageLoader.getInstance().displayImage(order.getCoverPath()
            , (CircularImageView) view.findViewById(R.id.sorder_casual_image));

        return view;
    }

    @Override
    public Keyword getKeyword(int i) {
        Keyword keyword = new Keyword();
        keyword.setDisplayName(showList.get(i).getName());
        keyword.setObject(showList.get(i));
        return keyword;
    }

    @Override
    public int getItemWidth(int i) {
        return itemWidth;
    }

    @Override
    public int getItemHeight(int i) {
        return itemHeight;
    }

    @Override
    public void feedKeyword() {

        showList.clear();
        Collections.shuffle(totalList);
        for (int i = 0; i < CasualLayout.MAX && i < totalList.size(); i ++) {
            showList.add(totalList.get(i));
        }
    }
}
