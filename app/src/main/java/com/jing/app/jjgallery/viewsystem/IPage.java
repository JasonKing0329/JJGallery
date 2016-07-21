package com.jing.app.jjgallery.viewsystem;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import com.jing.app.jjgallery.BasePresenter;
import com.jing.app.jjgallery.bean.order.SOrder;
import com.jing.app.jjgallery.viewsystem.publicview.ActionBar;

import java.util.List;

/**
 * Created by JingYang on 2016/7/1 0001.
 * Description:
 */
public interface IPage {
    void initData();
    void setPresenter(BasePresenter presenter);
    void initActionbar(ActionBar actionBar);
    void onIconClick(View view);
    void createMenu(MenuInflater menuInflater, Menu menu);
    void onPrepareMenu(MenuInflater menuInflater, Menu menu);
    boolean onMenuItemClick(MenuItem item);
    void onTextChanged(String text, int start, int before, int count);
    void onConfigurationChanged(android.content.res.Configuration newConfig);
    void onTouchEvent(MotionEvent event);
    boolean onBack();
    void onExit();
}
