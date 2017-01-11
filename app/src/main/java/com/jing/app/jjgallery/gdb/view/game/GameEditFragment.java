package com.jing.app.jjgallery.gdb.view.game;

import android.content.Context;

/**
 * Created by 景阳 on 2017/1/11.
 */

public abstract class GameEditFragment extends GameFragment {

    protected IGameManager gameManager;

    @Override
    protected void onAttachActivity(Context context) {
        gameManager = (IGameManager) context;
    }
}
