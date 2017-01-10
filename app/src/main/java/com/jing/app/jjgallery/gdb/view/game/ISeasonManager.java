package com.jing.app.jjgallery.gdb.view.game;

import com.jing.app.jjgallery.gdb.presenter.game.GamePresenter;
import com.king.service.gdb.game.bean.SeasonBean;

/**
 * Created by 景阳 on 2017/1/10.
 */

public interface ISeasonManager {
    GamePresenter getPresenter();

    void onSaveSeasonBean(SeasonBean seasonBean);

    void updateSeason(SeasonBean seasonBean);
}
