package com.jing.app.jjgallery.viewsystem.main.filesystem;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import com.jing.app.jjgallery.BasePresenter;
import com.jing.app.jjgallery.viewsystem.publicview.ActionBar;

/**
 * Created by JingYang on 2016/7/1 0001.
 * Description:
 */
public interface IFilePage {
    boolean onBack();
    void onExit();
    void onIconClick(View view);
    void createMenu(MenuInflater menuInflater, Menu menu);
    void onPrepareMenu(MenuInflater menuInflater, Menu menu);
    boolean onMenuItemClick(MenuItem item);
    void onTextChanged(String text, int start, int before, int count);
    void initActionbar(ActionBar actionBar);
    void onConfigurationChanged(android.content.res.Configuration newConfig);
    void onTouchEvent(MotionEvent event);

    void setPresenter(BasePresenter presenter);
}
