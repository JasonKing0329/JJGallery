package com.jing.app.jjgallery.gdb.view.game;

import com.jing.app.jjgallery.gdb.presenter.game.GamePresenter;

/**
 * Created by 景阳 on 2017/1/10.
 */

public interface IGameManager<T> {

    void onSaveData(T data);

    void updateData(T data);

    void deleteData(T data);

    GamePresenter getPresenter();
}
