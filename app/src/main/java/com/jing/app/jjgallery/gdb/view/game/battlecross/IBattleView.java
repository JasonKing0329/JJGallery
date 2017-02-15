package com.jing.app.jjgallery.gdb.view.game.battlecross;

import com.jing.app.jjgallery.gdb.model.game.BattleData;
import com.jing.app.jjgallery.gdb.presenter.game.BattlePresenter;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/2/8 11:18
 */
public interface IBattleView extends IBattleCross<BattleData> {
    void showCoachBattle(int index);
    BattlePresenter getPresenter();
}
