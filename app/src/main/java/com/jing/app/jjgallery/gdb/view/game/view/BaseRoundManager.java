package com.jing.app.jjgallery.gdb.view.game.view;

import android.content.Context;
import android.widget.LinearLayout;

import com.jing.app.jjgallery.gdb.model.game.BaseBattleDetailData;
import com.king.service.gdb.game.bean.BattleBean;
import com.king.service.gdb.game.bean.PlayerBean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/2/14 18:24
 */
public abstract class BaseRoundManager<T extends BattleBean> implements IBattleRoundProvider{

    private List<BaseRoundCard<T>> roundCardList;
    private LinearLayout llCardsContainer;

    protected BaseBattleDetailData<T> detailData;
    private Map<Integer, List<T>> roundMap;

    private OnBattleItemListener onBattleItemListener;

    private int editRound;

    public BaseRoundManager(BaseBattleDetailData<T> data, LinearLayout llCardsContainer
            , List<T> battleList, OnBattleItemListener<T> onBattleItemListener) {
        this.llCardsContainer = llCardsContainer;
        this.detailData = data;
        this.onBattleItemListener = onBattleItemListener;
        roundMap = new HashMap<>();
        roundCardList = new ArrayList<>();
        initRoundList(battleList);
    }

    private void initRoundList(List<T> battleList) {
        if (battleList != null) {
            for (int i = 0; i < battleList.size(); i ++) {
                int round = battleList.get(i).getRound();
                List<T> list = roundMap.get(round);
                if (list == null) {
                    list = new ArrayList<>();
                    roundMap.put(round, list);
                }
                list.add(battleList.get(i));
            }

            // 按照round进行升序排序
            List<Integer> roundList = new ArrayList<>();
            for (Integer round:roundMap.keySet()) {
                roundList.add(round);
            }
            Collections.sort(roundList, new Comparator<Integer>() {
                @Override
                public int compare(Integer lhs, Integer rhs) {
                    return lhs - rhs;
                }
            });

            for (Integer round:roundList) {
                addBattleCard(llCardsContainer, round, roundMap.get(round));
            }
        }
    }

    private void addBattleCard(LinearLayout llCardsContainer, int round,  List<T> battleList) {
        BaseRoundCard<T> roundCard = createRoundCard(llCardsContainer, round, battleList);
        roundCard.setOnBattleItemListener(onBattleItemListener);
        roundCardList.add(roundCard);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.topMargin = dp2px(llCardsContainer.getContext(), 15);
        llCardsContainer.addView(roundCard.getView(), params);
    }

    protected abstract BaseRoundCard<T> createRoundCard(LinearLayout llCardsContainer, int round, List<T> battleList);

    private int dp2px(Context context, int value) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (value * scale + 0.5f);
    }

    public void addNewRound() {
        List<T> list = new ArrayList<>();
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
            BaseRoundCard roundCard = findBattleRoundCard(editRound);
            if (isTopPlayer(bean)) {
                roundCard.setTopPlayer(bean);
            }
            else {
                roundCard.setBottomPlayer(bean);
            }
        }
    }

    protected abstract boolean isTopPlayer(PlayerBean bean);

    private BaseRoundCard findBattleRoundCard(int editRound) {
        for (BaseRoundCard card:roundCardList) {
            if (editRound == card.round) {
                return card;
            }
        }
        return null;
    }

    public BaseBattleDetailData<T> getDetailData() {
        return detailData;
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
