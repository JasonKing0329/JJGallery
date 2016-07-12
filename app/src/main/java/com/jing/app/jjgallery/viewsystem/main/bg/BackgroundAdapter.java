package com.jing.app.jjgallery.viewsystem.main.bg;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jing.app.jjgallery.R;

import java.util.List;

/**
 * Created by Administrator on 2016/7/12 0012.
 */
public class BackgroundAdapter extends RecyclerView.Adapter<BackgroundAdapter.BgHolder>
    implements View.OnClickListener{

    private Context mContext;
    private List<BkBean> itemList;

    private int selectIndex;

    public BackgroundAdapter(Context context, List<BkBean> itemList) {
        mContext = context;
        this.itemList = itemList;
        selectIndex = -1;
    }
    @Override
    public BgHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_bg_selector, parent, false);
        BgHolder holder = new BgHolder(view);
        holder.container = (ViewGroup) view.findViewById(R.id.bg_selector_container);
        holder.name = (TextView) view.findViewById(R.id.bg_selector_item_name);
        return holder;
    }

    @Override
    public void onBindViewHolder(BgHolder holder, int position) {
        holder.position = position;
        holder.container.setTag(holder);
        holder.container.setOnClickListener(this);
        if (position == selectIndex) {
            holder.container.setBackgroundColor(Color.argb(0x99, 0xbb, 0xbb, 0xbb));
        }
        else {
            holder.container.setBackgroundColor(Color.TRANSPARENT);
        }
        holder.name.setText(itemList.get(position).getDetailName());
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    @Override
    public void onClick(View v) {
        BgHolder holder = (BgHolder) v.getTag();
        selectIndex = holder.position;
        notifyDataSetChanged();
    }

    public int getSelectIndex() {
        return selectIndex;
    }

    public String getSelectedKey() {
        return itemList.get(getSelectIndex()).getPreferenceKey();
    }

    public static class BgHolder extends RecyclerView.ViewHolder {

        public ViewGroup container;
        public TextView name;
        public int position;

        public BgHolder(View itemView) {
            super(itemView);
        }
    }
}
