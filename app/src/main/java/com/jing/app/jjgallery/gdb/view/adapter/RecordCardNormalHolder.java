package com.jing.app.jjgallery.gdb.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jing.app.jjgallery.R;
import com.king.service.gdb.bean.Record;
import com.king.service.gdb.bean.Star;

/**
 * 描述: RecordCardHolder的普通RecyclerView adapter适配
 * <p/>作者：景阳
 * <p/>创建时间: 2017/8/9 13:20
 */
public class RecordCardNormalHolder extends RecyclerView.ViewHolder {

    private RecordCardHolder cardHolder;

    public RecordCardNormalHolder(View view) {
        super(LayoutInflater.from(view.getContext()).inflate(R.layout.adapter_gdb_record_card, (ViewGroup) view, false));
        cardHolder = new RecordCardHolder();
        cardHolder.init(itemView);
    }

    public void bind(Record item, int position, int endPosition, View.OnClickListener cardListener) {
        cardHolder.bindView(item, position, endPosition, cardListener);
    }

    public void setCurrentStar(Star currentStar) {
        cardHolder.setCurrentStar(currentStar);
    }
}
