package com.jing.app.jjgallery.gdb.view.game;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.jing.app.jjgallery.R;

/**
 * Created by 景阳 on 2017/1/11.
 */

public abstract class GameEditFragment extends GameFragment implements View.OnClickListener {

    public static final String KEY_INIT_WITH_DATA = "init_with_data";
    public static final String KEY_ID = "key_id";

    protected View actionSave;

    protected IGameManager gameManager;

    @Override
    protected void initView(View contentView, Bundle bundle) {
        actionSave = getActionSaveView(contentView);
        actionSave.setOnClickListener(this);
        initSubView(contentView, bundle);
    }

    protected abstract View getActionSaveView(View contentView);

    protected abstract void initSubView(View contentView, Bundle bundle);

    @Override
    public void onClick(View v) {
        if (v == actionSave) {
            onActionSave();
        }
    }

    protected abstract void onActionSave();

    @Override
    protected void onAttachActivity(Context context) {
        gameManager = (IGameManager) context;
    }
}
