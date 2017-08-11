package com.jing.app.jjgallery.gdb.view.home;

import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.siyamed.shapeimageview.RoundedImageView;
import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.gdb.model.VideoModel;
import com.jing.app.jjgallery.gdb.presenter.GdbGuidePresenter;
import com.jing.app.jjgallery.gdb.view.pub.HeaderFooterRecyclerAdapter;
import com.jing.app.jjgallery.service.image.SImageLoader;
import com.king.service.gdb.bean.Record;
import com.king.service.gdb.bean.RecordOneVOne;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/5/20 0020.
 */

public class GHomeRecordListAdapter extends HeaderFooterRecyclerAdapter<Record> {

    private OnListListener onListListener;
    private GHomeBean homeBean;
    private FragmentManager fragmentManager;
    private GHomeHeader.OnStarListener onStarListener;
    private SimpleDateFormat dateFormat;

    public GHomeRecordListAdapter(List<Record> list) {
        super(list);
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    }

    public void setOnListListener(OnListListener onListListener) {
        this.onListListener = onListListener;
    }

    @Override
    protected RecyclerView.ViewHolder onCreateHeader(ViewGroup parent) {
        return new GHomeHeader(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_home_main, parent, false)
                , fragmentManager, onStarListener);
    }

    @Override
    protected RecyclerView.ViewHolder onCreateFooter(ViewGroup parent) {
        return new MoreHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_home_record_list_more, parent, false));
    }

    @Override
    protected RecyclerView.ViewHolder onCreateItem(ViewGroup parent) {
        return new ItemHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_home_record_list, parent, false));
    }

    @Override
    protected void onBindHeaderView(RecyclerView.ViewHolder holder) {
        GHomeHeader headHolder = (GHomeHeader) holder;
        headHolder.bindView(homeBean);
    }

    @Override
    protected void onBindFooterView(RecyclerView.ViewHolder holder) {
        MoreHolder moreHolder = (MoreHolder) holder;
        moreHolder.groupMore.setOnClickListener(moreListener);
    }

    @Override
    protected void onBindItemView(RecyclerView.ViewHolder holder, int position) {
        
        Record record = list.get(position);
        
        ItemHolder itemHolder = (ItemHolder) holder;
        itemHolder.groupItem.setTag(position);
        itemHolder.groupItem.setOnClickListener(itemListener);
        SImageLoader.getInstance().displayImage(
                GdbGuidePresenter.getRecordPath(record.getName()), itemHolder.ivRecord);
        if (record instanceof RecordOneVOne) {
            RecordOneVOne orecord = (RecordOneVOne) record;
            itemHolder.tvStar1.setText(orecord.getStar1().getName());
            itemHolder.tvStar2.setText(orecord.getStar2().getName());
        }
        else {
            itemHolder.tvStar1.setText("");
            itemHolder.tvStar2.setText("");
        }

        // 第一个位置以及与上一个位置日期不同的，显示日期
        if (position == 0 || isNotSameDay(record, list.get(position - 1))) {
            itemHolder.tvDate.setVisibility(View.VISIBLE);
            itemHolder.tvDate.setText(dateFormat.format(new Date(record.getLastModifyTime())));
        }
        else {
            itemHolder.tvDate.setVisibility(View.GONE);
        }

        // deprecated item
        if (record.getDeprecated() == 1) {
            itemHolder.tvDeprecated.setVisibility(View.VISIBLE);
        }
        else {
            itemHolder.tvDeprecated.setVisibility(View.GONE);
        }

        // can be played in device
        if (VideoModel.getVideoPath(record.getName()) == null) {
            itemHolder.ivPlay.setVisibility(View.GONE);
        }
        else {
            itemHolder.ivPlay.setVisibility(View.VISIBLE);
        }
    }

    private boolean isNotSameDay(Record curRecord, Record lastRecord) {
        String curDay = dateFormat.format(new Date(curRecord.getLastModifyTime()));
        String lastDay = dateFormat.format(new Date(lastRecord.getLastModifyTime()));
        return !curDay.equals(lastDay);
    }

    private View.OnClickListener moreListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (onListListener != null) {
                onListListener.onLoadMore();
            }
        }
    };

    private View.OnClickListener itemListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (onListListener != null) {
                int position = (int) view.getTag();
                onListListener.onClickItem(view, list.get(position));
            }
        }
    };

    public void setHomeBean(GHomeBean homeBean) {
        this.homeBean = homeBean;
    }

    public void setFragmentManager(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    public void setOnStarListener(GHomeHeader.OnStarListener onStarListener) {
        this.onStarListener = onStarListener;
    }

    public static class MoreHolder extends RecyclerView.ViewHolder {

        ViewGroup groupMore;

        public MoreHolder(View itemView) {
            super(itemView);
            groupMore = (ViewGroup) itemView.findViewById(R.id.group_more);
        }
    }

    public static class ItemHolder extends RecyclerView.ViewHolder {

        ViewGroup groupItem;
        RoundedImageView ivRecord;
        TextView tvDate;
        TextView tvStar1;
        TextView tvStar2;
        TextView tvDeprecated;
        ImageView ivPlay;

        public ItemHolder(View itemView) {
            super(itemView);
            ivRecord = (RoundedImageView) itemView.findViewById(R.id.iv_record_image);
            groupItem = (ViewGroup) itemView.findViewById(R.id.group_item);
            tvDate = (TextView) itemView.findViewById(R.id.tv_record_date);
            tvStar1 = (TextView) itemView.findViewById(R.id.tv_record_star1);
            tvStar2 = (TextView) itemView.findViewById(R.id.tv_record_star2);
            tvDeprecated = (TextView) itemView.findViewById(R.id.tv_deprecated);
            ivPlay = (ImageView) itemView.findViewById(R.id.iv_play);
        }
    }

    public interface OnListListener {
        void onLoadMore();
        void onClickItem(View view, Record record);
    }
}
