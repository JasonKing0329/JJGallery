package com.jing.app.jjgallery.gdb.model.game;

import com.king.service.gdb.game.bean.CoachBean;
import com.king.service.gdb.game.bean.CrossBean;

import java.util.List;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/2/8 15:02
 */
public class CrossDetailData extends BaseBattleDetailData<CrossBean> {
    private CoachBean coach2;

    public CoachBean getCoach2() {
        return coach2;
    }

    public void setCoach2(CoachBean coach2) {
        this.coach2 = coach2;
    }
}
