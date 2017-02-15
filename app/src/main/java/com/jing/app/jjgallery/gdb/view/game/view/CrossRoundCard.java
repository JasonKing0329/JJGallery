package com.jing.app.jjgallery.gdb.view.game.view;

import android.view.ViewGroup;

import com.jing.app.jjgallery.gdb.model.game.CrossDetailData;
import com.king.service.gdb.game.bean.CrossBean;

import java.util.List;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/2/15 11:41
 */
public class CrossRoundCard extends BaseRoundCard<CrossBean> {

    public CrossRoundCard(ViewGroup container, int round, List<CrossBean> battleList, IBattleRoundProvider provider) {
        super(container, round, battleList, provider);
    }

    @Override
    protected CrossBean createNewItem() {
        return new CrossBean();
    }

    @Override
    protected void initNewItem(CrossBean bean) {
//        bean.setRematchFlag();
        bean.setCoach2Id(((CrossDetailData)roundProvider.getDetailData()).getCoach2().getId());
    }
}
