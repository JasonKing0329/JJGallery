package com.jing.app.jjgallery.viewsystem.main.order;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.util.ColorUtils;

/**
 * Created by JingYang on 2016/8/16 0016.
 * Description:
 */
public class CardExpandAdapter extends BaseAdapter {

    private String[] items;
    private Context mContext;

    public CardExpandAdapter(Context context) {
        mContext = context;
        items = new String[] {
                context.getString(R.string.menu_fullscreen),
                context.getString(R.string.openby_wall),
                context.getString(R.string.menu_book_view),
                context.getString(R.string.sorder_preview)
        };
    }
    @Override
    public int getCount() {
        return items.length;
    }

    @Override
    public Object getItem(int position) {
        return items[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_card_expand_item, parent, false);
            holder.tv = (TextView) convertView.findViewById(R.id.card_expand_item_text);
            holder.container = convertView.findViewById(R.id.card_expand_item_container);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv.setText(items[position]);
        holder.container.setBackgroundColor(ColorUtils.randomWhiteTextBgColor());
        return convertView;
    }

    private class ViewHolder {
        View container;
        TextView tv;
    }
}
