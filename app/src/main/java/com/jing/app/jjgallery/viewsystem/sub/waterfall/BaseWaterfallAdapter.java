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
import com.jing.app.jjgallery.model.sub.WaterfallHelper;
import com.jing.app.jjgallery.service.image.ImageValue;
import com.jing.app.jjgallery.service.image.SImageLoader;

import java.util.List;

/**
 * Created by JingYang on 2016/7/26 0026.
 * Description:
 */
public abstract class BaseWaterfallAdapter extends RecyclerView.Adapter<BaseWaterfallAdapter.WHolder>
    implements View.OnClickListener, View.OnLongClickListener{

    private final String TAG = "BaseWaterfallAdapter";
    private Context mContext;
    private WaterfallHelper mHelper;
    private OnWaterfallItemListener itemListener;

    private boolean isActionMode;
    protected SparseBooleanArray mCheckMap;

    private int nColumn;
    private int nPadding;

    public BaseWaterfallAdapter(Context context, int column) {
        mContext = context;
        mHelper = new WaterfallHelper();
        nColumn = column;
        nPadding = context.getResources().getDimensionPixelSize(R.dimen.waterfall_item_padding);
        mCheckMap = new SparseBooleanArray();
    }

    public void setColumn(int column) {
        nColumn = column;
    }

    public void setOnWaterfallItemListener(OnWaterfallItemListener itemListener) {
        this.itemListener = itemListener;
    }

    public abstract String getImagePath(int position);

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
        holder.checkBox.setChecked(mCheckMap.get(position));
        SImageLoader.getInstance().displayImage(getImagePath(position), holder.image);
    }

    @Override
    public void onViewRecycled(WHolder holder) {
        Log.d(TAG, "onViewRecycled " + holder.position);
        super.onViewRecycled(holder);
    }

    @Override
    public void onClick(View v) {
        WHolder holder = (WHolder) v.getTag();
        int position = holder.position;
        if (isActionMode) {
            boolean check = !mCheckMap.get(position);
            holder.checkBox.setChecked(check);
            if (check) {
                mCheckMap.put(position, check);
            }
            else {
                mCheckMap.delete(position);
            }

            defineCheckMapSize();
        }
        else {
            if (itemListener != null) {
                itemListener.onItemClick(position);
            }
        }
    }

    @Override
    public boolean onLongClick(View v) {
        isActionMode = !isActionMode;

        WHolder holder = (WHolder) v.getTag();
        int position = holder.position;
        if (itemListener != null) {
            itemListener.onItemLongClick(position);
        }

        if (isActionMode) {
            mCheckMap.put(position, true);
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

    public void cancelActionMode() {
        isActionMode = false;
        if (mCheckMap != null) {
            mCheckMap.clear();
        }
    }

    private void defineCheckMapSize() {
        if (itemListener != null) {
            if (mCheckMap.size() == 0) {
                itemListener.onEmptyChecked();
            }
            else if (mCheckMap.size() == getItemCount()) {
                itemListener.onFullChecked();
            }
        }
    }

    public boolean isSelctionMode() {
        return isActionMode;
    }

    protected abstract List<String> getSelectedList();
    protected abstract List<Integer> getSelectedIndex();
    protected abstract void removeItem(int index);

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
            value.setPath(getImagePath(position));
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

    /**
     * 对当前recycler list进行原始排序
     */
    public abstract void onOriginSequence();

    /**
     * 对当前recycler list进行随机排序
     */
    public abstract void onRandomSequence();

}
