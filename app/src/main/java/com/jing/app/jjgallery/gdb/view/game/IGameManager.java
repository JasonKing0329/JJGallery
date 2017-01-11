package com.jing.app.jjgallery.gdb.view.game;

/**
 * Created by 景阳 on 2017/1/10.
 */

public interface IGameManager<T> {

    void onSaveData(T data);

    void updateData(T data);
}
