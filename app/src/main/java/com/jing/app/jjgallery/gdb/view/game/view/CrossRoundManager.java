package com.jing.app.jjgallery.gdb.view.game.view;

import android.widget.LinearLayout;

import com.jing.app.jjgallery.gdb.model.game.BaseBattleDetailData;
import com.king.service.gdb.game.bean.CrossBean;

import java.util.List;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/2/15 11:41
 */
public class CrossRoundManager extends BaseRoundManager<CrossBean> {

    public CrossRoundManager(BaseBattleDetailData<CrossBean> data, LinearLayout llCardsContainer
            , List<CrossBean> battleList, OnBattleItemListener<CrossBean> onBattleItemListener) {
        super(data, llCardsContainer, battleList, onBattleItemListener);
    }

    @Override
    protected BaseRoundCard<CrossBean> createRoundCard(LinearLayout llCardsContainer, int round, List<CrossBean> battleList) {
        return new CrossRoundCard(llCardsContainer, round, battleList, this);
    }
}
