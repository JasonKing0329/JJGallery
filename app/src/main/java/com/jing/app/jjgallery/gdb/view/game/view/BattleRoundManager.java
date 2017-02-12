package com.jing.app.jjgallery.gdb.view.game.view;

import android.content.Context;
import android.widget.LinearLayout;

import com.jing.app.jjgallery.gdb.model.game.BattleDetailData;
import com.king.service.gdb.game.bean.BattleBean;
import com.king.service.gdb.game.bean.PlayerBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/2/11 0011.
 */

public class BattleRoundManager implements IBattleRoundProvider {

    private List<BattleRoundCard> roundCardList;
    private LinearLayout llCardsContainer;

    private BattleDetailData battleDetailData;
    private Map<Integer, List<BattleBean>> roundMap;

    private OnBattleItemListener onBattleItemListener;

    private int editRound;

    public BattleRoundManager(BattleDetailData data, LinearLayout llCardsContainer
            , List<BattleBean> battleList, OnBattleItemListener onBattleItemListener) {
        this.llCardsContainer = llCardsContainer;
        this.battleDetailData = data;
        this.onBattleItemListener = onBattleItemListener;
        roundMap = new HashMap<>();
        roundCardList = new ArrayList<>();
        initRoundList(battleList);
    }

    private void initRoundList(List<BattleBean> battleList) {
        if (battleList != null) {
            for (int i = 0; i < battleList.size(); i ++) {
                int round = battleList.get(i).getRound();
                List<BattleBean> list = roundMap.get(round);
                if (list == null) {
                    list = new ArrayList<>();
                    roundMap.put(round, list);
                }
                list.add(battleList.get(i));
            }

            for (Integer round:roundMap.keySet()) {
                addBattleCard(llCardsContainer, round, roundMap.get(round));
            }
        }
    }

    private void addBattleCard(LinearLayout llCardsContainer, int round,  List<BattleBean> battleList) {
        BattleRoundCard roundCard = new BattleRoundCard(llCardsContainer, round, battleList, this);
        roundCard.setOnBattleItemListener(onBattleItemListener);
        roundCardList.add(roundCard);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.topMargin = dp2px(llCardsContainer.getContext(), 15);
        llCardsContainer.addView(roundCard.getView(), params);
    }

    private int dp2px(Context context, int value) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (value * scale + 0.5f);
    }

    public void addNewRound() {
        List<BattleBean> list = new ArrayList<>();
        int round = roundMap.keySet().size() + 1;
        roundMap.put(round, list);
        addBattleCard(llCardsContainer, round, list);
    }

    /**
     * insert player to current focus battle item
     * @param bean
     */
    public void addPlayerToFocusItem(PlayerBean bean) {
        if (editRound > 0) {
            BattleRoundCard roundCard = findBattleRoundCard(editRound);
            if (bean.getTopCoachId() == battleDetailData.getCoach().getId()) {
                roundCard.setTopPlayer(bean);
            }
            else {
                roundCard.setBottomPlayer(bean);
            }
        }
    }

    private BattleRoundCard findBattleRoundCard(int editRound) {
        for (BattleRoundCard card:roundCardList) {
            if (editRound == card.round) {
                return card;
            }
        }
        return null;
    }

    @Override
    public BattleDetailData getBattleDetailData() {
        return battleDetailData;
    }

    @Override
    public void onCardRequestEdit(int round) {
        editRound = round;
    }

    @Override
    public void onCardEditCancel(int round) {
        editRound = 0;
    }
}
