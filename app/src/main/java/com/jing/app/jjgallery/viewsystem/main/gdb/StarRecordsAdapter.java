package com.jing.app.jjgallery.viewsystem.main.gdb;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.bean.StarProxy;
import com.jing.app.jjgallery.service.image.SImageLoader;
import com.jing.app.jjgallery.viewsystem.publicview.PullZoomRecyclerView;
import com.king.service.gdb.bean.Record;

import java.util.List;

/**
 * Created by Administrator on 2016/7/30 0030.
 */
public class StarRecordsAdapter extends RecyclerListAdapter {

    private PullZoomRecyclerView recyclerView;
    protected List<Record> listData;
    private StarProxy star;

    public StarRecordsAdapter(StarProxy star, PullZoomRecyclerView recyclerView) {
        this.star = star;
        this.recyclerView = recyclerView;
        listData = star.getStar().getRecordList();
        addViewType(Record.class, new ViewHolderFactory<PullZoomItemHolder>() {
            @Override
            public PullZoomItemHolder onCreateViewHolder(ViewGroup parent) {
                return new PullZoomItemHolder(parent);
            }
        });
        addViewType(TYPE_HEADER, new ViewHolderFactory<PullZoomHeaderHolder>() {
            @Override
            public PullZoomHeaderHolder onCreateViewHolder(ViewGroup parent) {
                return new PullZoomHeaderHolder(parent);
            }
        });
        SImageLoader.getInstance().setDefaultImgRes(R.drawable.wall_bk1);
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

    private class PullZoomItemHolder extends RecyclerListAdapter.ViewHolder<Record> {
        private ImageView imageView;
        private TextView nameView;
        private TextView scoreView;

        public PullZoomItemHolder(ViewGroup parent) {
            this(LayoutInflater.from(recyclerView.getContext()).inflate(R.layout.adapter_gdb_star_item, parent, false));
        }

        public PullZoomItemHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.record_thumb);
            nameView = (TextView) view.findViewById(R.id.record_name);
            scoreView = (TextView) view.findViewById(R.id.record_score);
        }

        @Override
        public void bind(Record item, int position) {
//                imageView.setImageResource(item);
            nameView.setText(item.getName());
            scoreView.setText("" + item.getScore());
        }
    }
}
