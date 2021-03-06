package com.jing.app.jjgallery.viewsystem.sub.thumb;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jing.app.jjgallery.R;

/**
 * Created by JingYang on 2016/7/8 0008.
 * Description:
 */
public abstract class ThumbFolderAdapter extends RecyclerView.Adapter<ThumbFolderAdapter.ViewHolder>
    implements View.OnClickListener, View.OnLongClickListener{

    protected Context mContext;

    private OnThumbFolderItemListener mListener;

    protected int mFocusPosition;

    public ThumbFolderAdapter(Context context, OnThumbFolderItemListener listener) {
        mListener = listener;
        mContext = context;
        mFocusPosition = -1;
    }

    public int getFocusPosition() {
        return mFocusPosition;
    }

    public void setFocusPosition(int position) {
        mFocusPosition = position;
    }

    public void clearFocus() {
        mFocusPosition = -1;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup group, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(
                R.layout.thumbfolder_folder_item, group, false);
        ViewHolder holder = new ViewHolder(view);
        holder.container = (ViewGroup) view.findViewById(R.id.thumb_folder_item_group);
        holder.text = (TextView) view.findViewById(R.id.thumb_folder_item_name);
        holder.image = (ImageView) view.findViewById(R.id.thumb_folder_item_image);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.position = position;
        holder.container.setOnClickListener(this);
        holder.container.setOnLongClickListener(this);
        holder.container.setTag(holder);

        if (mFocusPosition == position) {
            holder.container.setBackgroundResource(R.drawable.gallery_border_choose);
        }
        else {
            holder.container.setBackground(null);
        }

        bindDataToView(position, holder.image, holder.text, holder.container);
    }

    protected abstract void bindDataToView(int position, ImageView image, TextView text, ViewGroup container);

    @Override
    public void onClick(View v) {
        if (mListener != null) {
            int position = ((ViewHolder) v.getTag()).position;
            mListener.onThumbFolderItemClick(v, position);
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if (mListener != null) {
            int position = ((ViewHolder) v.getTag()).position;
            mListener.onThumbFolderItemLongClick(v, position);
        }
        return true;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ViewGroup container;
        public ImageView image;
        public TextView text;
        public int position;
        public ViewHolder(View view) {
            super(view);
        }

    }
}
