package com.jing.app.jjgallery.gdb.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jing.app.jjgallery.R;
import com.king.service.gdb.bean.Record;

import static android.R.attr.name;

/**
 * Created by Administrator on 2016/8/6 0006.
 */
public class RecordHolder extends RecyclerListAdapter.ViewHolder<Record> {

    private RecordViewHolder viewHolder;
    private int sortMode;

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
        viewHolder.bind(item, position, sortMode);
    }

    /**
     * 在图片底部显示size, last modify time
     * @param lastModifyTime
     * @param size
     */
    public void bindExtra(long lastModifyTime, long size) {
        viewHolder.bindExtra(lastModifyTime, size);
    }

    /**
     * show simple file mode
     * @param name
     * @param lastModifyTime
     * @param size
     */
    public void bind(String name, long lastModifyTime, long size) {
        viewHolder.bind(name, lastModifyTime, size);
    }

    public void setSortMode(int sortMode) {
        this.sortMode = sortMode;
    }

    public void hideIndexView() {
        viewHolder.hideIndexView();
    }
}
