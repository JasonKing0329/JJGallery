package com.jing.app.jjgallery.gdb.view.game.view;

import android.view.ViewGroup;

import com.king.service.gdb.game.bean.BattleBean;

import java.util.List;

/**
 * Created by Administrator on 2017/2/11 0011.
 */

public class BattleRoundCard extends BaseRoundCard<BattleBean> {

    public BattleRoundCard(ViewGroup container, int round, List<BattleBean> battleList
            , IBattleRoundProvider provider) {
        super(container, round, battleList, provider);
    }

    @Override
    protected BattleBean createNewItem() {
        return new BattleBean();
    }

    @Override
    protected void initNewItem(BattleBean bean) {

    }
}
