package com.jing.app.jjgallery.gdb.view.game.view;

import com.king.service.gdb.game.bean.BattleBean;
import com.king.service.gdb.game.bean.PlayerBean;

/**
 * Created by Administrator on 2017/2/11 0011.
 */

public interface OnBattleItemListener<T extends BattleBean> {
    String getPlayerImage(String name);
    PlayerBean getPlayerBean(int playerId);
    void onAddBattleBean(T bean);
    void onRemoveBattleBean(T bean);
}
