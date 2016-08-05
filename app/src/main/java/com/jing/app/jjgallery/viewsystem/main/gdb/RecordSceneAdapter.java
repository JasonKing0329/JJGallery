package com.jing.app.jjgallery.viewsystem.main.gdb;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.bean.RecordProxy;
import com.jing.app.jjgallery.controller.ThemeManager;
import com.king.service.gdb.bean.GDBProperites;
import com.king.service.gdb.bean.Record;
import com.king.service.gdb.bean.RecordOneVOne;
import com.king.service.gdb.bean.RecordSingleScene;
import com.king.service.gdb.bean.Star;

import java.util.List;
import java.util.Random;

import cc.solart.turbo.BaseTurboAdapter;
import cc.solart.turbo.BaseViewHolder;

/**
 * Created by Jing Yang on 2016/7/30 0030.
 * @author Jing Yang
 * this open source use Item object to present Head object
 * so for Star object, make following rules:
 * if id is -1, then it stands for header, and the name stands for header text
 */
public class RecordSceneAdapter extends BaseTurboAdapter<RecordProxy, BaseViewHolder> implements View.OnClickListener {

    private List<RecordProxy> originList;
    private int nameColorNormal, nameColorBareback;
    private Random random;

    public interface OnRecordClickListener {
        void onRecordClick(Star star);
    }

    private int colors[] = new int[] {
            R.color.actionbar_bk_blue, R.color.actionbar_bk_green
            , R.color.actionbar_bk_orange, R.color.actionbar_bk_purple
            , R.color.actionbar_bk_deepblue, R.color.actionbar_bk_lightgreen
    };

    private OnRecordClickListener onRecordClickListener;

    public RecordSceneAdapter(Context context) {
        super(context);
    }

    public RecordSceneAdapter(Context context, List<RecordProxy> data) {
        super(context, data);
        originList = data;
        nameColorNormal = context.getResources().getColor(ThemeManager.getInstance().getGdbSRTextColorId(context, false));
        nameColorBareback = context.getResources().getColor(ThemeManager.getInstance().getGdbSRTextColorId(context, true));
        random = new Random();
    }

    public void setOnStarClickListener(OnRecordClickListener listener) {
        onRecordClickListener = listener;
    }

    private boolean isHeader(int position) {
        return getItem(position).isHeader();
    }

    @Override
    protected int getDefItemViewType(int position) {
        /**
         * normal item must be 0
         * open source design this way
         */
        if (isHeader(position)) {
            return 1;
        }
        return 0;
    }

    @Override
    protected BaseViewHolder onCreateDefViewHolder(ViewGroup parent, int viewType) {
        /**
         * normal item must be 0
         * open source design this way
         */
        if (viewType == 0) {
            return new RecordHolder(parent);
        }
        else {
            return new IndexHeaderHolder(inflateItemView(R.layout.adapter_gdb_starlist_header, parent));
        }
    }

    @Override
    protected void convert(BaseViewHolder holder, RecordProxy item) {
        if (holder instanceof IndexHeaderHolder) {
            ((IndexHeaderHolder) holder).header.setText(item.getHeaderName() + "(" + item.getItemNumber() + ")");
            if (holder instanceof IndexHeaderHolder) {
                int index = item.getHeaderName().charAt(0) % colors.length;
                ((IndexHeaderHolder) holder).container.setBackgroundColor(mContext.getResources().getColor(colors[index]));
            }
        }
        else if (holder instanceof RecordHolder) {
            ((RecordHolder) holder).bind(item);
        }
    }

    public int getLetterPosition(String letter){
        for (int i = 0 ; i < getData().size(); i++){
            if(isHeader(i) && getData().get(i).getRecord().getName().equals(letter)){
                return i;
            }
        }
        return -1;
    }

    public class IndexHeaderHolder extends BaseViewHolder {

        View container;
        TextView header;
        public IndexHeaderHolder(View view) {
            super(view);
            header = findViewById(R.id.gdb_star_head);
            container = findViewById(R.id.gdb_star_head_container);
        }
    }

    public class RecordHolder extends BaseViewHolder {
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

        public void bind(RecordProxy item) {
            container.setTag(item);
            container.setOnClickListener(RecordSceneAdapter.this);
//                imageView.setImageResource(item);
            seqView.setText("" + (item.getPositionInHeader() + 1));
            nameView.setText(item.getRecord().getName());
            scoreView.setText("" + item.getRecord().getScore());
            if (item.getRecord() instanceof RecordSingleScene) {
                RecordSingleScene record = (RecordSingleScene) item.getRecord();
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
            if (item.getRecord() instanceof RecordOneVOne) {
                RecordOneVOne record = (RecordOneVOne) item.getRecord();
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

    @Override
    public void onClick(View v) {
        if (onRecordClickListener != null) {
            Star star = (Star) v.getTag();
            onRecordClickListener.onRecordClick(star);
        }
    }

    public void onRecordFilter(String name) {
        mData.clear();
        if (name.trim().length() == 0) {
            for (RecordProxy record:originList) {
                mData.add(record);
            }
        }
        else {
            for (int i = 0; i < originList.size(); i ++) {
                if (originList.get(i).isHeader()) {
                    mData.add(originList.get(i));
                }
                else {
                    if (originList.get(i).getRecord().getName().toLowerCase().contains(name.toLowerCase())) {
                        mData.add(originList.get(i));
                    }
                }
            }
        }
        notifyDataSetChanged();
    }

}
