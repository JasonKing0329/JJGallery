package com.jing.app.jjgallery.gdb.view.game.view;

import com.king.service.gdb.game.bean.BattleBean;
import com.king.service.gdb.game.bean.PlayerBean;

/**
 * Created by Administrator on 2017/2/11 0011.
 */

public interface OnBattleItemListener {
    String getPlayerImage(String name);
    PlayerBean getPlayerBean(int playerId);
    void onAddBattleBean(BattleBean bean);
    void onRemoveBattleBean(BattleBean bean);
}
