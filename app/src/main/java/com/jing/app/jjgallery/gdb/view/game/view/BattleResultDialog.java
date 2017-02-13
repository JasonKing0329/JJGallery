package com.jing.app.jjgallery.gdb.view.game.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.gdb.model.game.BattleDetailData;
import com.jing.app.jjgallery.viewsystem.publicview.CustomDialog;
import com.king.service.gdb.game.bean.BattleBean;
import com.king.service.gdb.game.bean.BattleResultBean;
import com.king.service.gdb.game.bean.PlayerBean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2017/2/12 0012.
 */

public class BattleResultDialog extends CustomDialog {

    private ListView lvTop;
    private ListView lvBottom;
    private List<BattleBean> battleList;

    private List<RoundResultBean> topResultList;
    private List<RoundResultBean> bottomResultList;
    private BattleDetailData battleDetailData;

    private BattleResultAdapter topAdapter;
    private BattleResultAdapter bottomAdapter;

    private OnBattleResultListener onBattleResultListener;

    public BattleResultDialog(Context context, OnCustomDialogActionListener actionListener) {
        super(context, actionListener);
        requestCancelAction(true);
        requestSaveAction(true, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createResultDatas();
            }
        });

        topResultList = new ArrayList<>();
        bottomResultList = new ArrayList<>();
        HashMap<String, Object> map = new HashMap<>();
        actionListener.onLoadData(map);
        battleList = (List<BattleBean>) map.get("battles");
        battleDetailData = (BattleDetailData) map.get("battleDetailBean");
        createResultBeanList();

        topAdapter = new BattleResultAdapter(topResultList);
        bottomAdapter = new BattleResultAdapter(bottomResultList);
        lvTop.setAdapter(topAdapter);
        lvBottom.setAdapter(bottomAdapter);
    }

    public void setOnBattleResultListener(OnBattleResultListener onBattleResultListener) {
        this.onBattleResultListener = onBattleResultListener;
    }

    /**
     * 组装battle_result表数据
     */
    private void createResultBeanList() {
        if (battleList != null) {
            for (PlayerBean bean:battleDetailData.getPlayerListTop()) {
                RoundResultBean rb = getPlayerResult(bean.getId(), 1);
                rb.name = bean.getName();
                rb.bean = new BattleResultBean();
                rb.bean.setSeasonId(battleDetailData.getSeason().getId());
                rb.bean.setCoachId(battleDetailData.getCoach().getId());
                rb.bean.setPlayerId(bean.getId());
                rb.bean.setScore(Integer.parseInt(rb.scoreTotal));
                rb.bean.setType(1);
                topResultList.add(rb);
            }
            for (PlayerBean bean:battleDetailData.getPlayerListBottom()) {
                RoundResultBean rb = getPlayerResult(bean.getId(), 0);
                rb.name = bean.getName();
                rb.bean = new BattleResultBean();
                rb.bean.setSeasonId(battleDetailData.getSeason().getId());
                rb.bean.setCoachId(battleDetailData.getCoach().getId());
                rb.bean.setPlayerId(bean.getId());
                rb.bean.setScore(Integer.parseInt(rb.scoreTotal));
                rb.bean.setType(0);
                bottomResultList.add(rb);
            }

            // 按照总分进行降序排序
            Collections.sort(topResultList, new Comparator<RoundResultBean>() {
                @Override
                public int compare(RoundResultBean lhs, RoundResultBean rhs) {
                    return Integer.parseInt(rhs.scoreTotal) - Integer.parseInt(lhs.scoreTotal);
                }
            });
            // 按照总分进行降序排序
            Collections.sort(bottomResultList, new Comparator<RoundResultBean>() {
                @Override
                public int compare(RoundResultBean lhs, RoundResultBean rhs) {
                    return Integer.parseInt(rhs.scoreTotal) - Integer.parseInt(lhs.scoreTotal);
                }
            });

            // set rank
            for (int i = 0; i < topResultList.size(); i ++) {
                topResultList.get(i).bean.setRank(i + 1);
            }
            for (int i = 0; i < bottomResultList.size(); i ++) {
                bottomResultList.get(i).bean.setRank(i + 1);
            }
        }
    }

    private RoundResultBean getPlayerResult(int playerId, int type) {
        RoundResultBean rb = new RoundResultBean();
        List<BattleBean> bl = new ArrayList<>();
        // load player's battle
        for (BattleBean bean:battleList) {
            if (type == 1) {
                if (playerId == bean.getTopPlayerId()) {
                    bl.add(bean);
                }
            }
            else {
                if (playerId == bean.getBottomPlayerId()) {
                    bl.add(bean);
                }
            }
        }
        // 按照轮次进行升序排序
        Collections.sort(bl, new Comparator<BattleBean>() {
            @Override
            public int compare(BattleBean lhs, BattleBean rhs) {
                return lhs.getRound() - rhs.getRound();
            }
        });

        // count total score and create round score
        int sum = 0;
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < bl.size(); i ++) {
            if (i == 0) {
                buffer.append(bl.get(i).getScore());
            }
            else {
                buffer.append("  ").append(bl.get(i).getScore());
            }
            sum += Integer.parseInt(bl.get(i).getScore());
        }
        rb.scoreRound = buffer.toString();
        rb.scoreTotal = String.valueOf(sum);
        return rb;
    }

    private void createResultDatas() {
        List<BattleResultBean> list = new ArrayList<>();
        for (RoundResultBean bean:topResultList) {
            list.add(bean.bean);
        }
        for (RoundResultBean bean:bottomResultList) {
            list.add(bean.bean);
        }
        onBattleResultListener.onCreateBattleResultDatas(list);
    }

    @Override
    protected View getCustomView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.gdb_game_dlg_battle_result, null);
        lvTop = (ListView) view.findViewById(R.id.battle_result_top_list);
        lvBottom = (ListView) view.findViewById(R.id.battle_result_bottom_list);
        return view;
    }

    @Override
    protected View getCustomToolbar() {
        return null;
    }

    private class BattleResultAdapter extends BaseAdapter {

        private List<RoundResultBean> list;

        private BattleResultAdapter(List<RoundResultBean> list) {
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ResultHolder holder;
            if (convertView == null) {
                holder = new ResultHolder();
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_battle_result_item, parent, false);
                holder.tvName = (TextView) convertView.findViewById(R.id.battle_result_name);
                holder.tvScoreTotal = (TextView) convertView.findViewById(R.id.battle_result_score_total);
                holder.tvScoreRound = (TextView) convertView.findViewById(R.id.battle_result_score_round);
                convertView.setTag(holder);
            }
            else {
                holder = (ResultHolder) convertView.getTag();
            }
            holder.tvName.setText(list.get(position).name);
            holder.tvScoreTotal.setText(list.get(position).scoreTotal);
            holder.tvScoreRound.setText(list.get(position).scoreRound);
            return convertView;
        }
    }

    private class ResultHolder {
        TextView tvName;
        TextView tvScoreTotal;
        TextView tvScoreRound;
    }

    private class RoundResultBean {
        String name;
        String scoreTotal;
        String scoreRound;
        BattleResultBean bean;
    }

    public interface OnBattleResultListener{
        void onCreateBattleResultDatas(List<BattleResultBean> datas);
    }
}
