package com.jing.app.jjgallery.gdb.view.game.view;

import com.jing.app.jjgallery.gdb.model.game.BaseBattleDetailData;

/**
 * Created by Administrator on 2017/2/11 0011.
 */

public interface IBattleRoundProvider {
    BaseBattleDetailData getBattleDetailData();

    void onCardRequestEdit(int round);
    void onCardEditCancel(int round);
}
