package com.jing.app.jjgallery.gdb.view.game.battlecross;

import com.jing.app.jjgallery.gdb.model.game.CrossData;
import com.jing.app.jjgallery.gdb.presenter.game.CrossPresenter;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/2/8 11:18
 */
public interface ICrossView extends IBattleCross<CrossData> {
    void showCoachCross(int index);
    CrossPresenter getPresenter();

}
