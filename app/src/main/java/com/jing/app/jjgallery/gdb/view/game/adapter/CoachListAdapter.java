package com.jing.app.jjgallery.gdb.view.game.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.gdb.presenter.game.CoachSeasonManager;
import com.jing.app.jjgallery.gdb.view.game.GameValueMap;
import com.jing.app.jjgallery.gdb.view.game.custom.CoachSeasonView;
import com.jing.app.jjgallery.service.image.SImageLoader;
import com.king.service.gdb.game.bean.CoachBean;
import com.king.service.gdb.game.bean.SeasonBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/1/7 0007.
 */

public class CoachListAdapter extends RecyclerView.Adapter<CoachListAdapter.CoachListHolder> implements View.OnClickListener
    , View.OnLongClickListener{

    private List<CoachBean> coachList;
    private CoachSeasonView.OnCoachSeasonListener mListener;
    private CoachSeasonManager coachSeasonManager;
    private OnCoachSelectListener onCoachSelectListener;


    public CoachListAdapter(List<CoachBean> coachList, CoachSeasonManager manager) {
        this.coachList = coachList;
        coachSeasonManager = manager;
    }

    public void setOnCoachSeasonListener(CoachSeasonView.OnCoachSeasonListener listener) {
        this.mListener = listener;
    }

    public void setOnCoachSelectListener(OnCoachSelectListener listener) {
        this.onCoachSelectListener = listener;
    }

    public void setCoachList(List<CoachBean> coachList) {
        this.coachList = coachList;
    }

    @Override
    public CoachListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CoachListHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_gdb_coach_item, parent, false)
                        , mListener);
    }

    @Override
    public void onBindViewHolder(CoachListHolder holder, int position) {
        holder.group.setTag(position);
        holder.group.setOnClickListener(this);
        holder.group.setOnLongClickListener(this);
        holder.nameView.setText(coachList.get(position).getName());
        holder.typeView.setText(GameValueMap.fromCoachType(coachList.get(position).getType()));
        holder.coachSeasonView.setDatas(coachList.get(position), coachSeasonManager.getCoachSeasonList(coachList.get(position)));
        SImageLoader.getInstance().displayImage(coachList.get(position).getImagePath(), holder.headView);
    }

    @Override
    public int getItemCount() {
        return coachList == null ? 0:coachList.size();
    }

    @Override
    public void onClick(View v) {
        if (onCoachSelectListener != null) {
            int position = (Integer) v.getTag();
            onCoachSelectListener.onCoachSelect(coachList.get(position));
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if (onCoachSelectListener != null) {
            int position = (Integer) v.getTag();
            onCoachSelectListener.onCoachLongSelect(coachList.get(position));
        }
        return true;
    }

    public static class CoachListHolder extends RecyclerView.ViewHolder {

        private TextView nameView, typeView;
        private ImageView headView;
        private ViewGroup group;
        private CoachSeasonView coachSeasonView;

        public CoachListHolder(View itemView, CoachSeasonView.OnCoachSeasonListener listener) {
            super(itemView);
            nameView = (TextView) itemView.findViewById(R.id.coach_name);
            typeView = (TextView) itemView.findViewById(R.id.coach_type);
            headView = (ImageView) itemView.findViewById(R.id.coach_head);
            group = (ViewGroup) itemView.findViewById(R.id.coach_group);
            coachSeasonView = (CoachSeasonView) itemView.findViewById(R.id.coach_seasonview);
            coachSeasonView.setOnCoachSeasonListener(listener);
        }
    }

    public interface OnCoachSelectListener {
        void onCoachSelect(CoachBean coachBean);

        void onCoachLongSelect(CoachBean coachBean);
    }
}
