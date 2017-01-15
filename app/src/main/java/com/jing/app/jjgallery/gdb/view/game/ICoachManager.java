package com.jing.app.jjgallery.gdb.view.game;

import com.jing.app.jjgallery.gdb.presenter.game.CoachSeasonManager;
import com.king.service.gdb.game.bean.CoachBean;

/**
 * Created by Administrator on 2017/1/15 0015.
 * CoachActivity与fragment之间进行数据共享的接口
 */

public interface ICoachManager extends IGameManager<CoachBean> {
    CoachSeasonManager getCoachSeasonManager();

    void onCoachSelect(CoachBean coachBean);
}
