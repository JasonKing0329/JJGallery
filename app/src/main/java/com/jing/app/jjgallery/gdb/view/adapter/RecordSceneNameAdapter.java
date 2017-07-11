package com.jing.app.jjgallery.gdb.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.util.ColorUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/7/11 11:16
 */
public class RecordSceneNameAdapter extends RecyclerView.Adapter<RecordSceneNameAdapter.NameHolder> {

    private List<String> list;
    private List<Integer> colorList;

    public RecordSceneNameAdapter(List<String> list) {
        this.list = list;
        if (list != null) {
            colorList = new ArrayList<>();
            for (int i = 0; i < list.size(); i ++) {
                colorList.add(0);
            }
        }
    }

    @Override
    public NameHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NameHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_scene_name, parent, false));
    }

    @Override
    public void onBindViewHolder(NameHolder holder, int position) {
        holder.tvName.setText(list.get(position));
        // 避免每次产生新颜色
        if (colorList.get(position) == 0) {
            colorList.set(position, ColorUtils.randomWhiteTextBgColor());
        }
        holder.tvName.setBackgroundColor(colorList.get(position));
    }

    @Override
    public int getItemCount() {
        return list == null ? 0:list.size();
    }

    public static class NameHolder extends RecyclerView.ViewHolder {

        TextView tvName;

        public NameHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
        }
    }
}
