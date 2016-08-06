package com.jing.app.jjgallery.viewsystem.main.gdb;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jing.app.jjgallery.R;
import com.king.service.gdb.bean.Record;

/**
 * Created by Administrator on 2016/8/6 0006.
 */
public class RecordHolder extends RecyclerListAdapter.ViewHolder<Record> {

    private RecordViewHolder viewHolder;
    public RecordHolder(ViewGroup parent) {
        this(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_gdb_record_item, parent, false));
    }

    private RecordHolder(View view) {
        super(view);
        viewHolder = new RecordViewHolder();
        viewHolder.initView(view);
    }

    public void setParameters(int nameColorNormal, int nameColorBareback, View.OnClickListener onClickListener) {
        viewHolder.setParameters(nameColorNormal, nameColorBareback, onClickListener);
    }

    @Override
    public void bind(Record item, int position) {
        viewHolder.bind(item, position);
    }
}
