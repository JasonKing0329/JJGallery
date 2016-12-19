package com.jing.app.jjgallery.gdb.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.jing.app.jjgallery.controller.ThemeManager;
import com.king.service.gdb.bean.Record;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JingYang on 2016/8/2 0002.
 * Description:
 */
public class RecordListAdapter extends RecyclerView.Adapter<RecordHolder> implements View.OnClickListener {

    public interface OnRecordItemClickListener {
        void onClickRecordItem(Record record);
    }

    private List<Record> originList;
    private List<Record> recordList;
    private int nameColorNormal, nameColorBareback;
    private OnRecordItemClickListener itemClickListener;

    private int sortMode;

    public RecordListAdapter(Context context, List<Record> list) {
        this.originList = list;
        recordList = new ArrayList<>();
        for (Record record:originList) {
            recordList.add(record);
        }
        nameColorNormal = context.getResources().getColor(ThemeManager.getInstance().getGdbSRTextColorId(context, false));
        nameColorBareback = context.getResources().getColor(ThemeManager.getInstance().getGdbSRTextColorId(context, true));
    }

    public List<Record> getRecordList() {
        return recordList;
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
        holder.setParameters(nameColorNormal, nameColorBareback, RecordListAdapter.this);
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

    public void onStarFilter(String name) {
        recordList.clear();
        if (name.trim().length() == 0) {
            for (Record record:originList) {
                recordList.add(record);
            }
        }
        else {
            for (int i = 0; i < originList.size(); i ++) {
                if (originList.get(i).getId() == -1) {
                    recordList.add(originList.get(i));
                }
                else {
                    if (originList.get(i).getName().toLowerCase().contains(name.toLowerCase())) {
                        recordList.add(originList.get(i));
                    }
                }
            }
        }
        notifyDataSetChanged();
    }
}
