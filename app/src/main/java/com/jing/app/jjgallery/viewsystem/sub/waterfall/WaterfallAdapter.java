package com.jing.app.jjgallery.viewsystem.sub.waterfall;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.bean.filesystem.FileBean;
import com.jing.app.jjgallery.model.sub.WaterfallHelper;
import com.jing.app.jjgallery.service.image.ImageValue;
import com.jing.app.jjgallery.service.image.SImageLoader;

import java.util.List;

/**
 * Created by JingYang on 2016/7/26 0026.
 * Description:
 */
public class WaterfallAdapter extends RecyclerView.Adapter<WaterfallAdapter.WHolder>
    implements View.OnClickListener, View.OnLongClickListener{

    private final String TAG = "WaterfallAdapter";
    private Context mContext;
    private List<FileBean> list;
    private WaterfallHelper mHelper;
    private OnWaterfallItemListener itemListener;

    private boolean isActionMode;
    private SparseBooleanArray checkMap;

    private int nColumn;
    private int nPadding;

    public WaterfallAdapter(Context context, List<FileBean> list, int column) {
        mContext = context;
        this.list = list;
        mHelper = new WaterfallHelper();
        nColumn = column;
        nPadding = context.getResources().getDimensionPixelSize(R.dimen.waterfall_item_padding);
        checkMap = new SparseBooleanArray();
    }

    public void setColumn(int column) {
        nColumn = column;
    }

    public void setOnWaterfallItemListener(OnWaterfallItemListener itemListener) {
        this.itemListener = itemListener;
    }

    public String getImagePath(int position) {
        return list.get(position).getPath();
    }

    @Override
    public WHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder");
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_waterfall_item, parent, false);
        WHolder holder = new WHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(WHolder holder, int position) {
        holder.calculateImageSize(position);
        holder.container.setTag(holder);
        holder.container.setOnClickListener(this);
        holder.container.setOnLongClickListener(this);
        holder.checkBox.setVisibility(isActionMode ? View.VISIBLE:View.INVISIBLE);
        holder.checkBox.setChecked(checkMap.get(position));
        SImageLoader.getInstance().displayImage(list.get(position).getPath(), holder.image);
//        holder.position = position;
//        holder.image.setImageResource(R.drawable.icon_loading);
    }

    @Override
    public void onViewRecycled(WHolder holder) {
        Log.d(TAG, "onViewRecycled " + holder.position);
        super.onViewRecycled(holder);
    }

    @Override
    public int getItemCount() {
        return list == null ? 0:list.size();
    }

    @Override
    public void onClick(View v) {
        WHolder holder = (WHolder) v.getTag();
        int position = holder.position;
        if (isActionMode) {
            boolean check = !checkMap.get(position);
            holder.checkBox.setChecked(check);
            checkMap.put(position, check);
//            notifyDataSetChanged();
        }
        else {
            if (itemListener != null) {
                itemListener.onItemClick(position);
            }
        }
    }

    @Override
    public boolean onLongClick(View v) {
        WHolder holder = (WHolder) v.getTag();
        int position = holder.position;
        if (itemListener != null) {
            itemListener.onItemLongClick(position);
        }

        isActionMode = !isActionMode;
        if (isActionMode) {
            checkMap.put(position, true);
        }
        else {
            cancelActionMode();
        }
        notifyDataSetChanged();
        return false;
    }

    public boolean onBackPressed() {
        if (isActionMode) {
            isActionMode = false;
            cancelActionMode();
            notifyDataSetChanged();
            return true;
        }
        return false;
    }

    private void cancelActionMode() {
        if (checkMap != null) {
            checkMap.clear();
        }
    }

    public class WHolder extends RecyclerView.ViewHolder {

        ImageView image;
        View container;
        CheckBox checkBox;
        int position;
        public WHolder(View itemView) {
            super(itemView);
            container = itemView.findViewById(R.id.waterfall_item_container);
            image = (ImageView) itemView.findViewById(R.id.waterfall_item_image);
            checkBox = (CheckBox) itemView.findViewById(R.id.waterfall_item_check);
        }

        public void calculateImageSize(int position) {
            this.position = position;

            ImageValue value = new ImageValue();
            value.setPath(list.get(position).getPath());
            mHelper.calculateImageSize(value);
            mHelper.reseizeImage(value, nColumn, nPadding, mContext);

            ViewGroup.LayoutParams params = image.getLayoutParams();
            params.width = value.getWidth();
            params.height =  value.getHeight();
            ViewGroup.MarginLayoutParams mp = (ViewGroup.MarginLayoutParams) params;
            mp.topMargin = nPadding;
            mp.leftMargin = nPadding;
            mp.rightMargin = nPadding;
            mp.bottomMargin = nPadding;
            image.setLayoutParams(params);
        }
    }
}
