package com.jing.app.jjgallery.gdb.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.siyamed.shapeimageview.CircularImageView;
import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.gdb.bean.StarProxy;
import com.jing.app.jjgallery.gdb.presenter.GdbPresenter;
import com.jing.app.jjgallery.gdb.view.star.OnStarClickListener;
import com.jing.app.jjgallery.service.image.SImageLoader;
import com.king.service.gdb.bean.Star;

import java.util.List;

/**
 * Created by Administrator on 2016/11/27 0027.
 */

public class StarListNumAdapter extends RecyclerView.Adapter<StarListNumAdapter.ViewHolder> implements View.OnClickListener {

    private OnStarClickListener onStarClickListener;
    private GdbPresenter mPresenter;

    private List<Star> originList;

    public StarListNumAdapter(List<Star> list) {
        this.originList = list;
    }

    public void setOnStarClickListener(OnStarClickListener listener) {
        onStarClickListener = listener;
    }

    public void setPresenter(GdbPresenter mPresenter) {
        this.mPresenter = mPresenter;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_gdb_starlist_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        
        Star item = originList.get(position);
        holder.name.setText(item.getName() + " (" + item.getRecordNumber() + ")");
        StarProxy proxy = new StarProxy();
        proxy.setStar(item);
        String headPath = mPresenter.getStarImage(item.getName());
        if (headPath == null) {
            holder.imageView.setVisibility(View.GONE);
        }
        else {
            holder.imageView.setVisibility(View.VISIBLE);
            SImageLoader.getInstance().displayImage(headPath, holder.imageView);
        }
        proxy.setImagePath(headPath);
        holder.name.setTag(proxy);
        holder.name.setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        return originList == null ? 0:originList.size();
    }

    @Override
    public void onClick(View v) {
        if (onStarClickListener != null) {
            StarProxy star = (StarProxy) v.getTag();
            onStarClickListener.onStarClick(star);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        CircularImageView imageView;
        
        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.gdb_star_name);
            imageView = (CircularImageView) itemView.findViewById(R.id.gdb_star_headimg);
        }
    }
}
