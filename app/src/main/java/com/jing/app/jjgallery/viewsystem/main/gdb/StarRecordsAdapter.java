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
import com.king.service.gdb.bean.Record;

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

    private int sortMode;

    public StarRecordsAdapter(StarProxy star, PullZoomRecyclerView recyclerView) {
        this.star = star;
        this.recyclerView = recyclerView;
        listData = star.getStar().getRecordList();
        nameColorNormal = recyclerView.getContext().getResources().getColor(ThemeManager.getInstance().getGdbSRTextColorId(recyclerView.getContext(), false));
        nameColorBareback = recyclerView.getContext().getResources().getColor(ThemeManager.getInstance().getGdbSRTextColorId(recyclerView.getContext(), true));
        SImageLoader.getInstance().setDefaultImgRes(R.drawable.wall_bk1);

        addViewType(Record.class, new ViewHolderFactory<RecordHolder>() {
            @Override
            public RecordHolder onCreateViewHolder(ViewGroup parent) {
                RecordHolder holder = new RecordHolder(parent);
                holder.setParameters(nameColorNormal, nameColorBareback, StarRecordsAdapter.this);
                return holder;
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

    public void setSortMode(int sortMode) {
        this.sortMode = sortMode;
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

    /**
     * 覆盖父类的方法，这个PullZoom open source是把第recyclerView第0个当成pull image，
     * 但是现在RecordHolder是跟record list界面共用的一个holder，那里是正常的用第0个index的
     * 所以覆盖父类的实现方法，当holder的类型是RecordHolder，将position - 1。父类的getItem其实就是listData.get(position - 1)
     * 但是这里要把正确的position传给RecordHolder
     *
     * PullZoomHeaderHolder还是实现父类的方法
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (holder instanceof PullZoomHeaderHolder) {
            super.onBindViewHolder(holder, position);
        }
        else {
            RecordHolder vh = (RecordHolder) holder;
            vh.setSortMode(sortMode);
            vh.bind(listData.get(position - 1), position - 1);
        }
    }

    @Override
    public void onClick(View v) {
        if (itemClickListener != null) {
            Record record = (Record) v.getTag();
            itemClickListener.onClickRecordItem(record);
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
}
