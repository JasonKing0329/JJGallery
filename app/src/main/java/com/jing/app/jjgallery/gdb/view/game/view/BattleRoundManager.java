package com.jing.app.jjgallery.gdb.view.game.view;

import android.widget.LinearLayout;

import com.jing.app.jjgallery.gdb.model.game.BattleDetailData;
import com.king.service.gdb.game.bean.BattleBean;

import java.util.List;

/**
 * Created by Administrator on 2017/2/11 0011.
 */

public class BattleRoundManager extends BaseRoundManager<BattleBean>{
    public BattleRoundManager(BattleDetailData data, LinearLayout llCardsContainer, List<BattleBean> battleList, OnBattleItemListener onBattleItemListener) {
        super(data, llCardsContainer, battleList, onBattleItemListener);
    }

    @Override
    protected BaseRoundCard<BattleBean> createRoundCard(LinearLayout llCardsContainer, int round, List<BattleBean> battleList) {
        return new BattleRoundCard(llCardsContainer, round, battleList, this);
    }
}
