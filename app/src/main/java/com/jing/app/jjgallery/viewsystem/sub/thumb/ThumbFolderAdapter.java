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

    private Context mContext;

    private OnThumbItemListener mListener;

    public ThumbFolderAdapter(Context context, OnThumbItemListener listener) {
        mListener = listener;
        mContext = context;
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

        bindDataToView(position, holder.image, holder.text);
    }

    protected abstract void bindDataToView(int position, ImageView image, TextView text);

    @Override
    public void onClick(View v) {

    }

    @Override
    public boolean onLongClick(View v) {
        return false;
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
