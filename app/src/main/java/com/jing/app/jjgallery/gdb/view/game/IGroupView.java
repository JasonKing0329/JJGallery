package com.jing.app.jjgallery.gdb.view.game;

import com.jing.app.jjgallery.gdb.bean.GamePlayerBean;
import com.jing.app.jjgallery.gdb.model.game.GroupData;

/**
 * Created by Administrator on 2017/2/1 0001.
 */

public interface IGroupView {
    void onGroupDataLoaded(GroupData groupData);

    void onPlayerRecommended(GamePlayerBean bean);
}
