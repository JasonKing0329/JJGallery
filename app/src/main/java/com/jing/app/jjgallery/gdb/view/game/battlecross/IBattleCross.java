package com.jing.app.jjgallery.gdb.view.game.battlecross;

import com.jing.app.jjgallery.gdb.model.game.BattleData;
import com.jing.app.jjgallery.gdb.presenter.game.GamePresenter;
import com.jing.app.jjgallery.viewsystem.publicview.ActionBar;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/2/14 17:33
 */
public interface IBattleCross<T extends BattleData> {
    ActionBar getActionbar();

    GamePresenter getGamePresenter();

    T getBattleData();

    void onBattleDataLoaded(T data);

}
