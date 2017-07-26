package com.jing.app.jjgallery.gdb.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.siyamed.shapeimageview.CircularImageView;
import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.gdb.bean.StarProxy;
import com.jing.app.jjgallery.gdb.presenter.star.StarListPresenter;
import com.jing.app.jjgallery.gdb.view.star.OnStarClickListener;
import com.jing.app.jjgallery.service.image.SImageLoader;
import com.jing.app.jjgallery.viewsystem.sub.dialog.DefaultDialogManager;
import com.king.service.gdb.bean.FavorBean;

import java.util.List;

/**
 * Created by Administrator on 2016/11/27 0027.
 */

public class StarListNumAdapter extends RecyclerView.Adapter<StarListNumAdapter.ViewHolder> implements View.OnClickListener {

    private OnStarClickListener onStarClickListener;
    private StarListPresenter mPresenter;

    private List<StarProxy> originList;

    public StarListNumAdapter(List<StarProxy> list) {
        this.originList = list;
    }

    public void setOnStarClickListener(OnStarClickListener listener) {
        onStarClickListener = listener;
    }

    public void setPresenter(StarListPresenter mPresenter) {
        this.mPresenter = mPresenter;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_gdb_starlist_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        StarProxy item = originList.get(position);
        holder.name.setText(item.getStar().getName() + " (" + item.getStar().getRecordNumber() + ")");
        String headPath = item.getImagePath();
        if (headPath == null) {
            holder.imageView.setVisibility(View.GONE);
        }
        else {
            holder.imageView.setVisibility(View.VISIBLE);
            SImageLoader.getInstance().displayImage(headPath, holder.imageView);
        }
        holder.name.setTag(item);
        holder.name.setOnClickListener(this);

        holder.favorView.setTag(position);
        holder.favorView.setOnClickListener(this);

        if (item.getFavor() > 0) {
            holder.favorView.setSelected(true);
            holder.favorScore.setVisibility(View.VISIBLE);
            holder.favorScore.setText(String.valueOf(item.getFavor()));
        }
        else {
            holder.favorView.setSelected(false);
            holder.favorScore.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return originList == null ? 0:originList.size();
    }

    @Override
    public void onClick(View v) {
        if (v instanceof TextView) {
            if (onStarClickListener != null) {
                StarProxy star = (StarProxy) v.getTag();
                onStarClickListener.onStarClick(star);
            }
        }
        else if (v instanceof ImageView) {
            final int position = (int) v.getTag();
            if (originList.get(position).getFavor() > 0) {
                originList.get(position).setFavor(0);
                saveFavor(originList.get(position), 0);
                notifyItemChanged(position);
            }
            else {
                new DefaultDialogManager().openInputDialog(v.getContext(), new DefaultDialogManager.OnDialogActionListener() {
                    @Override
                    public void onOk(String name) {
                        try {
                            int favor = Integer.parseInt(name);
                            originList.get(position).setFavor(favor);
                            saveFavor(originList.get(position), favor);
                            notifyItemChanged(position);
                        } catch (Exception e) {
                            originList.get(position).setFavor(0);
                        }
                    }
                });
            }
        }
    }

    private void saveFavor(StarProxy starProxy, int favor) {
        FavorBean bean = new FavorBean();
        bean.setStarId(starProxy.getStar().getId());
        bean.setStarName(starProxy.getStar().getName());
        bean.setFavor(favor);
        mPresenter.saveFavor(bean);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        CircularImageView imageView;
        ImageView favorView;
        TextView favorScore;
        
        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.gdb_star_name);
            imageView = (CircularImageView) itemView.findViewById(R.id.gdb_star_headimg);
            favorView = (ImageView) itemView.findViewById(R.id.gdb_star_favor);
            favorScore = (TextView) itemView.findViewById(R.id.gdb_star_favor_score);
        }
    }
}
