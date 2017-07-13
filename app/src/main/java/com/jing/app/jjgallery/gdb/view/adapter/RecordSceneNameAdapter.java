package com.jing.app.jjgallery.gdb.view.adapter;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.presenter.main.SettingProperties;
import com.jing.app.jjgallery.util.ColorUtils;
import com.jing.app.jjgallery.util.FormatUtil;
import com.king.lib.saveas.ScreenUtils;
import com.king.service.gdb.bean.SceneBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/7/11 11:16
 */
public class RecordSceneNameAdapter extends RecyclerView.Adapter<RecordSceneNameAdapter.NameHolder>
    implements View.OnClickListener{

    private List<SceneBean> list;
    private List<Integer> colorList;
    private OnSceneItemClickListener onSceneItemClickListener;

    private int hsvStart;
    private int hsvAngle;

    public RecordSceneNameAdapter(List<SceneBean> list) {
        this.list = list;
        if (list != null) {
            colorList = new ArrayList<>();
            for (int i = 0; i < list.size(); i ++) {
                colorList.add(0);
            }
        }

        hsvStart = SettingProperties.getGdbSceneHsvStart();
        hsvAngle = SettingProperties.getGdbSceneHsvAngle();
    }

    public void updateBgColors(int start, int angle) {
        hsvStart = start;
        hsvAngle = angle;
        colorList = new ArrayList<>();
        for (int i = 0; i < list.size(); i ++) {
            colorList.add(0);
        }
        notifyDataSetChanged();
    }

    @Override
    public NameHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NameHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_scene_name, parent, false));
    }

    @Override
    public void onBindViewHolder(NameHolder holder, int position) {
        SceneBean bean = list.get(position);
        holder.tvName.setText(bean.getScene());
        // 避免每次产生新颜色
        if (colorList.get(position) == 0) {
            colorList.set(position, ColorUtils.randomBgColor(hsvStart, hsvAngle));
        }
        holder.groupContainer.setBackground(getBackground(holder.groupContainer, colorList.get(position)));

        holder.groupContainer.setTag(bean.getScene());
        holder.groupContainer.setOnClickListener(this);

        holder.tvNumber.setText(String.valueOf(bean.getNumber()));
        holder.tvAvg.setText("Avg(" + FormatUtil.formatFloatEnd(bean.getAverage()) + ")");
        holder.tvMax.setText("Max(" + bean.getMax() + ")");
    }

    private Drawable getBackground(View view, int color) {
        GradientDrawable normal = new GradientDrawable();
        normal.setColor(color);
        normal.setCornerRadius(ScreenUtils.dp2px(view.getContext(), 10));
        GradientDrawable press = new GradientDrawable();
        press.setColor(Color.argb(0x66, 0, 0, 0));
        press.setCornerRadius(ScreenUtils.dp2px(view.getContext(), 10));
        StateListDrawable drawable = new StateListDrawable();
        drawable.addState(new int[] { android.R.attr.state_pressed }, press);
        drawable.addState(new int[] {}, normal);
        return drawable;
    }

    @Override
    public int getItemCount() {
        return list == null ? 0:list.size();
    }

    public void setOnSceneItemClickListener(OnSceneItemClickListener onSceneItemClickListener) {
        this.onSceneItemClickListener = onSceneItemClickListener;
    }

    @Override
    public void onClick(View v) {
        if (onSceneItemClickListener != null) {
            onSceneItemClickListener.onSceneItemClick((String) v.getTag());
        }
    }

    public static class NameHolder extends RecyclerView.ViewHolder {

        TextView tvName;
        TextView tvNumber;
        TextView tvAvg;
        TextView tvMax;
        ViewGroup groupContainer;

        public NameHolder(View itemView) {
            super(itemView);
            groupContainer = (ViewGroup) itemView.findViewById(R.id.group_container);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            tvNumber = (TextView) itemView.findViewById(R.id.tv_number);
            tvAvg = (TextView) itemView.findViewById(R.id.tv_avg);
            tvMax = (TextView) itemView.findViewById(R.id.tv_max);
        }
    }

    public interface OnSceneItemClickListener {
        void onSceneItemClick(String scene);
    }
}
