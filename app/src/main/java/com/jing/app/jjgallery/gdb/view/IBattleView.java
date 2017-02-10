package com.jing.app.jjgallery.gdb.view;

import com.jing.app.jjgallery.gdb.model.game.BattleData;
import com.jing.app.jjgallery.gdb.presenter.game.BattlePresenter;
import com.jing.app.jjgallery.viewsystem.publicview.ActionBar;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/2/8 11:18
 */
public interface IBattleView {
    ActionBar getActionbar();

    void onBattleDataLoaded(BattleData data);

    BattlePresenter getPresenter();

    BattleData getBattleData();

    void showCoachBattle(int index);
}
