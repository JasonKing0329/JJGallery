package com.jing.app.jjgallery.gdb.view.game.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.service.image.SImageLoader;
import com.king.service.gdb.game.bean.PlayerBean;

import java.util.List;

/**
 * Created by Administrator on 2017/2/1 0001.
 */

public class BattlePlayerAdapter extends RecyclerView.Adapter<BattlePlayerAdapter.PlayerHolder> implements View.OnClickListener
    , View.OnLongClickListener{

    private List<PlayerBean> list;
    private IPlayerImageProvider imageProvider;
    private OnPlayerItemListener onPlayerItemListener;

    public BattlePlayerAdapter(List<PlayerBean> list, IPlayerImageProvider imageProvider) {
        this.list = list;
        this.imageProvider = imageProvider;
    }

    public void setOnPlayerItemListener(OnPlayerItemListener onPlayerItemListener) {
        this.onPlayerItemListener = onPlayerItemListener;
    }

    @Override
    public PlayerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PlayerHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_gdb_game_group_player, parent, false));
    }

    @Override
    public void onBindViewHolder(PlayerHolder holder, int position) {
        holder.group.setTag(position);
        holder.group.setOnClickListener(this);
        holder.group.setOnLongClickListener(this);
        SImageLoader.getInstance().displayImage(imageProvider.getPlayerImage(list.get(position).getName()), holder.imageView);
    }

    @Override
    public int getItemCount() {
        return list == null ? 0:list.size();
    }

    @Override
    public void onClick(View v) {
        int position = (int) v.getTag();
        onPlayerItemListener.onPlayerItemClick(list.get(position));
    }

    @Override
    public boolean onLongClick(View v) {
        int position = (int) v.getTag();
        onPlayerItemListener.onPlayerItemLongClick(this, list, position);
        return true;
    }

    public static class PlayerHolder extends RecyclerView.ViewHolder {

        ViewGroup group;
        ImageView imageView;
        public PlayerHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.group_player_image);
            group = (ViewGroup) itemView.findViewById(R.id.group_player_group);
        }
    }

    public interface OnPlayerItemListener {
        void onPlayerItemClick(PlayerBean bean);
        void onPlayerItemLongClick(BattlePlayerAdapter adapter, List<PlayerBean> list, int position);
    }
}
