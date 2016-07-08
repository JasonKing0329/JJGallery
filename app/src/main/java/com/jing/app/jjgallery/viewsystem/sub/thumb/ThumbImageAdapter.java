package com.jing.app.jjgallery.viewsystem.sub.thumb;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.controller.PictureManagerUpdate;
import com.jing.app.jjgallery.model.sub.RecycleAdapterLoadController;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2016/7/8 0008.
 */
public class ThumbImageAdapter extends RecyclerView.Adapter<ThumbImageAdapter.ViewHolder>
        implements View.OnClickListener, View.OnLongClickListener, RecycleAdapterLoadController.ImageProvider {

    private final boolean DEBUG = true;
    private final String TAG = "ThumbImageAdapter";

    private Context mContext;

    private OnThumbImageItemListener mListener;

    private HashMap<Integer, Boolean> checkMap;
    private boolean showActionMode;
    private List<String> imageFileList;
    private int showItemLongClickAnim = -1;

    private RecycleAdapterLoadController loadController;

    public ThumbImageAdapter(Context context, OnThumbImageItemListener listener) {
        mListener = listener;
        mContext = context;
        checkMap = new HashMap<Integer, Boolean>();
        loadController = new RecycleAdapterLoadController(this);
    }

    public void setDatas(List<String> list) {
        imageFileList = list;
    }

    public void resetMap() {
        checkMap.clear();
    }

    public void selectAll() {
        for (int i = 0; i < imageFileList.size(); i ++) {
            checkMap.put(i, true);
        }
    }

    public HashMap<Integer, Boolean> getCheckMap() {
        return checkMap;
    }

    public void showActionMode(boolean show) {
        showActionMode = show;
    }

    public boolean isActionMode() {
        return showActionMode;
    }

    public void notifyShowAnimation(int position) {
        showItemLongClickAnim = position;
    }

    @Override
    public ThumbImageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.thumb_image_item, null);
        ViewHolder holder = new ViewHolder(view);
        holder.container = (ViewGroup) view.findViewById(R.id.thumb_item_container);
        holder.image = (ImageView) view.findViewById(R.id.thumb_item_image);
        holder.cBox = (CheckBox) view.findViewById(R.id.thumb_item_checkbox);
        return holder;
    }

    @Override
    public void onBindViewHolder(ThumbImageAdapter.ViewHolder holder, final int position) {

        holder.position = position;

        holder.container.setOnClickListener(this);
        holder.container.setOnLongClickListener(this);
        holder.container.setTag(holder);

        if (showActionMode) {
            holder.cBox.setVisibility(View.VISIBLE);
            Boolean status = checkMap.get(position);
            if (status == null || !status) {
                holder.cBox.setChecked(false);
            }
            else {
                holder.cBox.setChecked(true);
            }
            holder.cBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton arg0, boolean checked) {
                    if (checked) {
                        checkMap.put(position, true);
                    }
                    else {
                        checkMap.remove(position);
                    }
                }
            });
        }
        else {
            holder.cBox.setVisibility(View.GONE);
        }

        loadController.onLoad(holder.image, position, imageFileList.get(position));

        if (showItemLongClickAnim == position) {
            holder.container.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.thumb_item_longclick));
            showItemLongClickAnim = -1;
        }
    }

    @Override
    public int getItemCount() {
        return imageFileList == null ? 0:imageFileList.size();
    }

    @Override
    public void onViewRecycled(ViewHolder holder) {
        if (DEBUG) {
            Log.d(TAG, "onViewRecycled " + holder.position);
        }
        loadController.onRecycle(holder.image, holder.position);
        super.onViewRecycled(holder);
    }

    /**
     * don't direct call notifyItemRemoved, call this method to change cache reference about bitmap
     * 通知删除同时通知loadController有增删操作发生
     * 因为position的改变导致缓存的bitmap关联的position改变
     * @param index
     */
    public void notifyRemoved(int index) {
        notifyItemRemoved(index);
        loadController.notifyRemoved(index);
    }

    @Override
    public void onClick(View v) {
        if (mListener != null) {
            int position = ((ViewHolder) v.getTag()).position;
            mListener.onThumbImageItemClick(v, position);
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if (mListener != null) {
            int position = ((ViewHolder) v.getTag()).position;
            mListener.onThumbImageItemLongClick(v, position);
        }
        return true;
    }

    @Override
    public Bitmap createBitmap(String path) {
        Bitmap bitmap = PictureManagerUpdate.getInstance().createSpictureItem(
                path, mContext);
        return bitmap;
    }

    public String getImagePath(int position) {
        return imageFileList.get(position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ViewGroup container;
        public ImageView image;
        public CheckBox cBox;
        public int position;
        public ViewHolder(View view) {
            super(view);
        }

    }
}
