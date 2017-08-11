package com.jing.app.jjgallery.gdb.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.king.service.gdb.bean.Record;
import com.king.service.gdb.bean.Star;

import java.util.List;

/**
 * 描述: record list presented by card item view
 * codes are implemented by RecyclerView.Adapter
 * <p/>作者：景阳
 * <p/>创建时间: 2017/8/7 11:44
 */
public class RecordCardAdapter extends RecyclerView.Adapter<RecordCardNormalHolder> {

    private List<Record> list;
    private Star currentStar;
    private OnCardActionListener onCardActionListener;

    public void setCurrentStar(Star currentStar) {
        this.currentStar = currentStar;
    }

    public void setOnCardActionListener(OnCardActionListener onCardActionListener) {
        this.onCardActionListener = onCardActionListener;
    }

    public void setRecordList(List<Record> recordList) {
        this.list = recordList;
    }

    @Override
    public RecordCardNormalHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RecordCardNormalHolder(parent);
    }

    @Override
    public void onBindViewHolder(RecordCardNormalHolder holder, int position) {
        Record record = list.get(position);
        holder.setCurrentStar(currentStar);
        holder.bind(record, position, list.size() - 1, cardListener);
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    private View.OnClickListener cardListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (onCardActionListener != null) {
                onCardActionListener.onClickCardItem(v, (Record) v.getTag());
            }
        }
    };

    public interface OnCardActionListener {
        void onClickCardItem(View v, Record record);
    }

}
