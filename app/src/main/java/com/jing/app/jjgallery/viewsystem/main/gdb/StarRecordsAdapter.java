package com.jing.app.jjgallery.viewsystem.main.gdb;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.service.image.SImageLoader;
import com.jing.app.jjgallery.viewsystem.publicview.PullZoomRecyclerView;
import com.king.service.gdb.bean.RecordOneVOne;

import java.util.List;

/**
 * Created by Administrator on 2016/7/30 0030.
 */
public class StarRecordsAdapter extends RecyclerListAdapter {

    private List<RecordOneVOne> mList;
    private String mImagePath;
    private PullZoomRecyclerView mRecyclerView;

    public StarRecordsAdapter(PullZoomRecyclerView recyclerView, List<RecordOneVOne> list, String imagePath) {
        mRecyclerView = recyclerView;
        mList = list;
        mImagePath = imagePath;
        SImageLoader.getInstance().setDefaultImgRes(R.drawable.theme_dark);
        addViewType(Integer.class, new ViewHolderFactory<ViewHolder>() {
            @Override
            public ViewHolder onCreateViewHolder(ViewGroup parent) {
                return new ItemHolder(parent);
            }
        });
        addViewType(TYPE_HEADER, new ViewHolderFactory<ViewHolder>() {
            @Override
            public ViewHolder onCreateViewHolder(ViewGroup parent) {
                return new HeaderHolder(parent);
            }
        });
    }

    @Override
    public Object getItem(int position) {
        if (position == 0) {
            return ITEM_HEADER;
        }
        return mList.get(--position);
    }

    @Override
    public int getItemCount() {
        return mList.size() + 1;
    }

    private class ItemHolder extends RecyclerListAdapter.ViewHolder<RecordOneVOne> {

        public ItemHolder(ViewGroup parent) {
            this(LayoutInflater.from(mRecyclerView.getContext()).inflate(R.layout.adapter_gdb_star_item, parent, false));
        }

        public ItemHolder(View view) {
            super(view);
//            imageView = (ImageView) view.findViewById(R.id.imageView);
//            textView = (TextView) view.findViewById(R.id.text_view);
        }

        @Override
        public void bind(RecordOneVOne item, int position) {

        }
    }

    private class HeaderHolder extends RecyclerListAdapter.ViewHolder<Object> {

        private ImageView zoomView;
        private ViewGroup zoomHeaderContainer;

        public HeaderHolder(ViewGroup parent) {
            this(LayoutInflater.from(mRecyclerView.getContext()).inflate(R.layout.adapter_gdb_star_header, parent, false));
        }

        public HeaderHolder(View view) {
            super(view);
            zoomView = (ImageView) view.findViewById(R.id.zoom_image_view);
            zoomHeaderContainer = (ViewGroup) view.findViewById(R.id.zoom_header_container);
        }

        @Override
        public void bind(Object item, int position) {
            SImageLoader.getInstance().displayImage(mImagePath, zoomView);
            mRecyclerView.setZoomView(zoomView);
            mRecyclerView.setHeaderContainer(zoomHeaderContainer);
        }
    }
}
