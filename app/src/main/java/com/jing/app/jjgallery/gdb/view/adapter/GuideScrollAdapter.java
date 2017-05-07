package com.jing.app.jjgallery.gdb.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.gdb.presenter.GdbGuidePresenter;
import com.jing.app.jjgallery.gdb.view.pub.AutoScrollAdapter;
import com.jing.app.jjgallery.gdb.view.pub.AutoScrollView;
import com.jing.app.jjgallery.service.image.SImageLoader;
import com.jing.app.jjgallery.util.FormatUtil;
import com.king.service.gdb.bean.Record;

import java.util.List;

/**
 * Created by 景阳 on 2016/12/27.
 * guide latest records
 */

public class GuideScrollAdapter extends AutoScrollAdapter<GuideScrollAdapter.ItemHolder> implements View.OnClickListener {

    public interface OnScrollItemClickListener {
        void onScrollItemClick(View view, Record record);
    }

    private List<Record> mList;
    private GdbGuidePresenter presenter;

    private int itemWidth;

    private OnScrollItemClickListener listener;

    private boolean isTablet;

    public GuideScrollAdapter(List<Record> mList, int itemWidth, boolean isTablet) {
        this.mList = mList;
        this.itemWidth = itemWidth;
        this.isTablet = isTablet;
    }

    public void setOnScrollItemClickListener(OnScrollItemClickListener listener) {
        this.listener = listener;
    }

    public void setPresenter(GdbGuidePresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public int getCount() {
        return mList == null ? 0:mList.size();
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent) {
        return new ItemHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_guide_latest_item, parent, false));
    }

    @Override
    public void onBindView(int position, ItemHolder holder) {
        Record record = mList.get(position);
        holder.name.setText(record.getDirectory() + "/" + record.getName());
        if (isTablet) {
            holder.name.setVisibility(View.VISIBLE);
        }
        else {
            holder.name.setVisibility(View.GONE);
        }
        holder.score.setText(String.valueOf(record.getScore()));
        holder.group.setTag(position);
        holder.group.setOnClickListener(this);
        holder.date.setText(FormatUtil.formatDate(record.getLastModifyTime()));

        SImageLoader.getInstance().setDefaultImgRes(R.drawable.gdb_record_default);
        SImageLoader.getInstance().displayImage(presenter.getRecordPath(record.getName()), holder.image);
    }

    @Override
    public int getItemWidth() {
        return itemWidth;
    }

    @Override
    public void onClick(View v) {
        if (listener != null) {
            int position = (Integer) v.getTag();
            listener.onScrollItemClick(v, mList.get(position));
        }
    }

    public static class ItemHolder extends AutoScrollView.ViewHolder {

        ImageView image;
        TextView date;
        TextView name;
        TextView score;
        ViewGroup group;

        public ItemHolder(View view) {
            super(view);
            date = (TextView) view.findViewById(R.id.gdb_guide_item_date);
            group = (ViewGroup) view.findViewById(R.id.gdb_guide_item_group);
            image = (ImageView) view.findViewById(R.id.gdb_guide_item_image);
            name = (TextView) view.findViewById(R.id.gdb_guide_item_name);
            score = (TextView) view.findViewById(R.id.gdb_guide_item_score);
        }
    }
}
