package com.jing.app.jjgallery.gdb.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.jing.app.jjgallery.controller.ThemeManager;
import com.king.service.gdb.bean.Record;

import java.util.List;

/**
 * Created by JingYang on 2016/8/2 0002.
 * Description:
 */
public class RecordsListAdapter extends RecyclerView.Adapter<RecordHolder> implements View.OnClickListener {

    public interface OnRecordItemClickListener {
        void onClickRecordItem(Record record);
    }

    private List<Record> recordList;
    private int nameColorNormal, nameColorBareback;
    private OnRecordItemClickListener itemClickListener;

    private int sortMode;

    public RecordsListAdapter(Context context, List<Record> list) {
        this.recordList = list;
        nameColorNormal = context.getResources().getColor(ThemeManager.getInstance().getGdbSRTextColorId(context, false));
        nameColorBareback = context.getResources().getColor(ThemeManager.getInstance().getGdbSRTextColorId(context, true));
    }

    public void setRecordList(List<Record> recordList) {
        this.recordList = recordList;
    }

    public void setItemClickListener(OnRecordItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public void setSortMode(int sortMode) {
        this.sortMode = sortMode;
    }

    @Override
    public RecordHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecordHolder holder = new RecordHolder(parent);
        holder.setParameters(nameColorNormal, nameColorBareback, RecordsListAdapter.this);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecordHolder holder, int position) {
        holder.setSortMode(sortMode);
        holder.bind(recordList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return recordList == null ? 0:recordList.size();
    }

    @Override
    public void onClick(View v) {
        if (itemClickListener != null) {
            Record record = (Record) v.getTag();
            itemClickListener.onClickRecordItem(record);
        }
    }

}
