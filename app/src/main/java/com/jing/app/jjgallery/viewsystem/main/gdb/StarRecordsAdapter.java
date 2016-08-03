package com.jing.app.jjgallery.viewsystem.main.gdb;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.bean.StarProxy;
import com.jing.app.jjgallery.controller.ThemeManager;
import com.jing.app.jjgallery.service.image.SImageLoader;
import com.jing.app.jjgallery.viewsystem.publicview.PullZoomRecyclerView;
import com.king.service.gdb.bean.GDBProperites;
import com.king.service.gdb.bean.Record;
import com.king.service.gdb.bean.RecordSingleScene;

import java.util.List;

/**
 * Created by Administrator on 2016/7/30 0030.
 */
public class StarRecordsAdapter extends RecyclerListAdapter implements View.OnClickListener {

    public interface OnRecordItemClickListener {
        void onClickRecordItem(Record record);
    }

    private PullZoomRecyclerView recyclerView;
    protected List<Record> listData;
    private StarProxy star;

    private int nameColorNormal, nameColorBareback;
    private OnRecordItemClickListener itemClickListener;

    public StarRecordsAdapter(StarProxy star, PullZoomRecyclerView recyclerView) {
        this.star = star;
        this.recyclerView = recyclerView;
        listData = star.getStar().getRecordList();
        ThemeManager themeManager = new ThemeManager((recyclerView.getContext()));
        nameColorNormal = recyclerView.getContext().getResources().getColor(themeManager.getGdbSRTextColorId(false));
        nameColorBareback = recyclerView.getContext().getResources().getColor(themeManager.getGdbSRTextColorId(true));
        SImageLoader.getInstance().setDefaultImgRes(R.drawable.wall_bk1);

        addViewType(Record.class, new ViewHolderFactory<RecordHolder>() {
            @Override
            public RecordHolder onCreateViewHolder(ViewGroup parent) {
                return new RecordHolder(parent);
            }
        });
        addViewType(TYPE_HEADER, new ViewHolderFactory<PullZoomHeaderHolder>() {
            @Override
            public PullZoomHeaderHolder onCreateViewHolder(ViewGroup parent) {
                return new PullZoomHeaderHolder(parent);
            }
        });

    }

    public void setItemClickListener(OnRecordItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public Object getItem(int position) {
        if (position == 0) {
            return ITEM_HEADER;
        }
        return listData.get(--position);
    }

    @Override
    public int getItemCount() {
        return listData.size() + 1;
    }

    @Override
    public void onClick(View v) {
        if (itemClickListener != null) {
            int position = (int) v.getTag();
            itemClickListener.onClickRecordItem(listData.get(position));
        }
    }

    private class PullZoomHeaderHolder extends RecyclerListAdapter.ViewHolder<Object> {
        private ImageView zoomView;
        private ViewGroup zoomHeaderContainer;
        private TextView nameView;
        private TextView numberView;

        public PullZoomHeaderHolder(ViewGroup parent) {
            this(LayoutInflater.from(recyclerView.getContext()).inflate(R.layout.adapter_gdb_star_header, parent, false));
        }

        public PullZoomHeaderHolder(View view) {
            super(view);
            zoomView = (ImageView) view.findViewById(R.id.gdb_star_header_image);
            zoomHeaderContainer = (ViewGroup) view.findViewById(R.id.gdb_star_header_container);
            nameView = (TextView) view.findViewById(R.id.gdb_star_header_name);
            numberView = (TextView) view.findViewById(R.id.gdb_star_header_number);
        }

        @Override
        public void bind(Object item, int position) {
            recyclerView.setZoomView(zoomView);
            recyclerView.setHeaderContainer(zoomHeaderContainer);
            SImageLoader.getInstance().displayImage(star.getImagePath(), zoomView);
            nameView.setText(star.getStar().getName());
            numberView.setText(String.format(recyclerView.getContext().getString(R.string.gdb_star_file_numbers), listData.size()));
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

        public RecordHolder(ViewGroup parent) {
            this(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_gdb_star_item, parent, false));
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
        }

        @Override
        public void bind(Record item, int position) {
            container.setTag(position);
            container.setOnClickListener(StarRecordsAdapter.this);
//                imageView.setImageResource(item);
            seqView.setText("" + position);
            nameView.setText(item.getName());
            scoreView.setText("" + item.getScore());
            if (item instanceof RecordSingleScene) {
                RecordSingleScene record = (RecordSingleScene) item;
                fkView.setText("fk(" + record.getScoreFk() + ")");
                cumView.setText("cum(" + record.getScoreCum() + ")");

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
        }
    }
}
