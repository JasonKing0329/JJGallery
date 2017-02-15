package com.jing.app.jjgallery.gdb.view.game.view;

import android.content.Context;

import com.jing.app.jjgallery.gdb.model.game.BattleDetailData;
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

public class BattleResultDialog extends BaseResultDialog {

    private List<BattleBean> battleList;

    private List<RoundResultBean> topResultList;
    private List<RoundResultBean> bottomResultList;
    private BattleDetailData battleDetailData;

    public BattleResultDialog(Context context, OnCustomDialogActionListener actionListener) {
        super(context, actionListener);
    }

    @Override
    protected void loadAdapterDatas(HashMap<String, Object> map) {
        topResultList = new ArrayList<>();
        bottomResultList = new ArrayList<>();
        battleList = (List<BattleBean>) map.get("battles");
        battleDetailData = (BattleDetailData) map.get("battleDetailBean");
        createResultBeanList();
        topAdapter = new ResultAdapter<>(topResultList);
        bottomAdapter = new ResultAdapter<>(bottomResultList);
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

    @Override
    protected void createResultDatas() {
        List<BattleResultBean> list = new ArrayList<>();
        for (RoundResultBean bean:topResultList) {
            list.add(bean.bean);
        }
        for (RoundResultBean bean:bottomResultList) {
            list.add(bean.bean);
        }
        onBattleResultListener.onCreateBattleResultDatas(list);
    }

    private class RoundResultBean extends AbsRoundResultBean {
        BattleResultBean bean;
    }
}
