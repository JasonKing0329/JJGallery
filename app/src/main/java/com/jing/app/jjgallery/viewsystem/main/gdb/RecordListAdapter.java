package com.jing.app.jjgallery.viewsystem.main.gdb;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.controller.ThemeManager;
import com.king.service.gdb.bean.GDBProperites;
import com.king.service.gdb.bean.Record;
import com.king.service.gdb.bean.RecordOneVOne;
import com.king.service.gdb.bean.RecordSingleScene;
import com.king.service.gdb.bean.Star;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JingYang on 2016/8/2 0002.
 * Description:
 */
public class RecordListAdapter extends RecyclerView.Adapter<RecordListAdapter.RecordHolder> implements View.OnClickListener {

    public interface OnRecordItemClickListener {
        void onClickRecordItem(Record record);
    }

    private List<Record> originList;
    private List<Record> recordList;
    private int nameColorNormal, nameColorBareback;
    private OnRecordItemClickListener itemClickListener;

    public RecordListAdapter(Context context, List<Record> list) {
        this.originList = list;
        recordList = new ArrayList<>();
        for (Record record:originList) {
            recordList.add(record);
        }
        ThemeManager themeManager = new ThemeManager(context);
        nameColorNormal = context.getResources().getColor(themeManager.getGdbSRTextColorId(false));
        nameColorBareback = context.getResources().getColor(themeManager.getGdbSRTextColorId(true));
    }

    public List<Record> getRecordList() {
        return recordList;
    }

    public void setItemClickListener(OnRecordItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public RecordHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RecordHolder(parent);
    }

    @Override
    public void onBindViewHolder(RecordHolder holder, int position) {
        holder.bind(recordList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return recordList == null ? 0:recordList.size();
    }

    @Override
    public void onClick(View v) {
        if (itemClickListener != null) {
            int position = (int) v.getTag();
            itemClickListener.onClickRecordItem(recordList.get(position));
        }
    }

    public class RecordHolder extends RecyclerListAdapter.ViewHolder<Record> {
        private View container;
        private ImageView imageView;
        private TextView seqView;
        private TextView nameView;
        private TextView scoreView;
        private TextView fkView;
        private TextView cumView;
        private TextView bjobView;
        private TextView star1View;
        private TextView star2View;

        public RecordHolder(ViewGroup parent) {
            this(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_gdb_record_item, parent, false));
        }

        public RecordHolder(View view) {
            super(view);
            container = view.findViewById(R.id.record_container);
            imageView = (ImageView) view.findViewById(R.id.record_thumb);
            seqView = (TextView) view.findViewById(R.id.record_seq);
            nameView = (TextView) view.findViewById(R.id.record_name);
            scoreView = (TextView) view.findViewById(R.id.record_score);
            fkView = (TextView) view.findViewById(R.id.record_score_fk);
            cumView = (TextView) view.findViewById(R.id.record_score_cum);
            bjobView = (TextView) view.findViewById(R.id.record_score_bjob);
            star1View = (TextView) view.findViewById(R.id.record_star1);
            star2View = (TextView) view.findViewById(R.id.record_star2);
        }

        @Override
        public void bind(Record item, int position) {
            container.setTag(position);
            container.setOnClickListener(RecordListAdapter.this);
//                imageView.setImageResource(item);
            seqView.setText("" + (position + 1));
            nameView.setText(item.getName());
            scoreView.setText("" + item.getScore());
            if (item instanceof RecordSingleScene) {
                RecordSingleScene record = (RecordSingleScene) item;
                fkView.setText("fk(" + record.getScoreFk() + ")");
                cumView.setText("cum(" + record.getScoreCum() + ")");
                bjobView.setText("bjob(" + record.getScoreBJob() + ")");

                if (record.getScoreNoCond() == GDBProperites.BAREBACK) {
                    nameView.setTextColor(nameColorBareback);
                }
                else {
                    nameView.setTextColor(nameColorNormal);
                }
            }
            else {
                nameView.setTextColor(nameColorNormal);
            }
            if (item instanceof RecordOneVOne) {
                RecordOneVOne record = (RecordOneVOne) item;
                Star star1 = record.getStar1();
                if (star1 == null) {
                    star1View.setText(GDBProperites.STAR_UNKNOWN);
                }
                else {
                    star1View.setText(star1.getName() + "(" + record.getScoreStar1() + "/" + record.getScoreStarC1() + ")");
                }
                Star star2 = record.getStar2();
                if (star2 == null) {
                    star2View.setText(GDBProperites.STAR_UNKNOWN);
                }
                else {
                    star2View.setText(star2.getName() + "(" + record.getScoreStar2() + "/" + record.getScoreStarC2() + ")");
                }
            }
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