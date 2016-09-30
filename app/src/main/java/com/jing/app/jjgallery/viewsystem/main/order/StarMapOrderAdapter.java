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
import com.jing.app.jjgallery.viewsystem.publicview.starmap.StarMapBaseAdapter;
import com.jing.app.jjgallery.viewsystem.sub.key.Keyword;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Administrator on 2016/9/29.
 */
public class StarMapOrderAdapter extends StarMapBaseAdapter {

    public static final int DEFAULT_SHOW_NUMBER = 15;
    private Context context;
    private List<SOrder> totalList;
    private List<SOrder> showList;
    private int showNumber;

    public StarMapOrderAdapter(Context context, List<SOrder> list) {
        this.context = context;
        this.totalList = list;
        showNumber = DEFAULT_SHOW_NUMBER;
        showList = new ArrayList<>();
        SImageLoader.getInstance().setDefaultImgRes(R.drawable.default_user_for_circle);
    }

    public void setShowNumber(int showNumber) {
        this.showNumber = showNumber;
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
    public View createView(int i, View childAt) {
        SOrder order = showList.get(i);
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_order_index_casual, null);
        view.setTag(order);
        return view;
    }

    @Override
    public void bindViewData(int position, View view, int imageSize) {
        SOrder order = showList.get(position);
        TextView textView = (TextView) view.findViewById(R.id.sorder_casual_name);
        textView.setText(order.getName());
        textView.setTextColor(ColorUtils.randomLightColor());

        /**
         * 先设置param里的大小，否则ImageLoader会根据match_parent的大小加载过大的图片
         */
        CircularImageView imageView = (CircularImageView)view.findViewById(R.id.sorder_casual_image);
        imageView.getLayoutParams().width = imageSize;
        imageView.getLayoutParams().height = imageSize;

        SImageLoader.getInstance().displayImage(order.getCoverPath(), imageView);
    }

    @Override
    public int getTextHeight() {
        return context.getResources().getDimensionPixelOffset(R.dimen.starmap_item_text_height);
    }

    @Override
    public Keyword getKeyword(int i) {
        Keyword keyword = new Keyword();
        keyword.setDisplayName(showList.get(i).getName());
        keyword.setObject(showList.get(i));
        return keyword;
    }

    @Override
    public void feedKeyword() {

        showList.clear();
        Collections.shuffle(totalList);
        for (int i = 0; i < showNumber && i < totalList.size(); i ++) {
            showList.add(totalList.get(i));
        }
    }
}
