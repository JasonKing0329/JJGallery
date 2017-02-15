package com.jing.app.jjgallery.gdb.view.game.view;

import android.content.Context;

import com.king.service.gdb.game.bean.BattleResultBean;
import com.king.service.gdb.game.bean.CrossResultBean;

import java.util.HashMap;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/2/15 15:09
 */
public class CrossResultDialog extends BaseResultDialog {

    public CrossResultDialog(Context context, OnCustomDialogActionListener actionListener) {
        super(context, actionListener);
    }

    @Override
    protected void loadAdapterDatas(HashMap<String, Object> map) {

    }

    @Override
    protected void createResultDatas() {

    }

    private class RoundResultBean extends AbsRoundResultBean {
        CrossResultBean bean;
    }
}
