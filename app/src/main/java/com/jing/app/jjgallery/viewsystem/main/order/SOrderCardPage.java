package com.jing.app.jjgallery.viewsystem.main.order;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import com.jing.app.jjgallery.BasePresenter;
import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.bean.order.SOrder;
import com.jing.app.jjgallery.presenter.main.SettingProperties;
import com.jing.app.jjgallery.presenter.main.order.SOrderPresenter;
import com.jing.app.jjgallery.viewsystem.ActivityManager;
import com.jing.app.jjgallery.viewsystem.IPage;
import com.jing.app.jjgallery.viewsystem.publicview.ActionBar;
import com.loopeer.cardstack.AllMoveDownAnimatorAdapter;
import com.loopeer.cardstack.CardStackView;
import com.loopeer.cardstack.UpDownAnimatorAdapter;
import com.loopeer.cardstack.UpDownStackAnimatorAdapter;

import java.util.List;

/**
 * Created by JingYang on 2016/8/4 0004.
 * Description:
 */
public class SOrderCardPage implements IPage, SOrderCardAdapter.OnExpandActionListener {

    private Context context;
    private SOrderPresenter mPresenter;

    private CardStackView totalStackView, yearStackView, monthStackView, weekStackView, dayStackView;
    private SOrderCardAdapter totalAdapter, yearAdapter, monthAdapter, weekAdapter, dayAdapter;

    public SOrderCardPage(Context context, View view) {
        this.context = context;
        totalStackView = (CardStackView) view.findViewById(R.id.sorder_card_list_total);
        yearStackView = (CardStackView) view.findViewById(R.id.sorder_card_list_year);
        monthStackView = (CardStackView) view.findViewById(R.id.sorder_card_list_month);
        weekStackView = (CardStackView) view.findViewById(R.id.sorder_card_list_week);
        dayStackView = (CardStackView) view.findViewById(R.id.sorder_card_list_day);
        totalStackView.setAnimatorAdapter(new AllMoveDownAnimatorAdapter(totalStackView));
        yearStackView.setAnimatorAdapter(new UpDownStackAnimatorAdapter(yearStackView));
        monthStackView.setAnimatorAdapter(new UpDownAnimatorAdapter(monthStackView));
        weekStackView.setAnimatorAdapter(new AllMoveDownAnimatorAdapter(weekStackView));
        dayStackView.setAnimatorAdapter(new UpDownStackAnimatorAdapter(dayStackView));
    }

    @Override
    public void initData() {
        int rankNumber = SettingProperties.getAccessRankNumbers(context);
        List<SOrder> totalList = mPresenter.loadTopTotalList(rankNumber);
        List<SOrder> yearList = mPresenter.loadTopYearList(rankNumber);
        List<SOrder> monthList = mPresenter.loadTopMonthList(rankNumber);
        List<SOrder> weekList = mPresenter.loadTopWeekList(rankNumber);
        List<SOrder> dayList = mPresenter.loadTopDayList(rankNumber);
        totalAdapter = new SOrderCardAdapter(context, SOrderCardAdapter.HitMode.Total);
        totalAdapter.updateData(totalList);
        totalAdapter.setOnExpandActionListener(this);
        yearAdapter = new SOrderCardAdapter(context, SOrderCardAdapter.HitMode.Year);
        yearAdapter.updateData(yearList);
        yearAdapter.setOnExpandActionListener(this);
        monthAdapter = new SOrderCardAdapter(context, SOrderCardAdapter.HitMode.Month);
        monthAdapter.updateData(monthList);
        monthAdapter.setOnExpandActionListener(this);
        weekAdapter = new SOrderCardAdapter(context, SOrderCardAdapter.HitMode.Week);
        weekAdapter.updateData(weekList);
        weekAdapter.setOnExpandActionListener(this);
        dayAdapter = new SOrderCardAdapter(context, SOrderCardAdapter.HitMode.Day);
        dayAdapter.updateData(dayList);
        dayAdapter.setOnExpandActionListener(this);

        // 延迟加载，渲染的过程较慢，又不能在非UI线程处理
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                totalStackView.setAdapter(totalAdapter);
                yearStackView.setAdapter(yearAdapter);
                monthStackView.setAdapter(monthAdapter);
                weekStackView.setAdapter(weekAdapter);
                dayStackView.setAdapter(dayAdapter);
            }
        }, 500);
    }

    @Override
    public void setPresenter(BasePresenter presenter) {
        mPresenter = (SOrderPresenter) presenter;
    }

    @Override
    public void initActionbar(ActionBar actionBar) {
        actionBar.clearActionIcon();
        actionBar.addColorIcon();
        actionBar.addMenuIcon();
    }

    @Override
    public void onIconClick(View view) {

    }

    @Override
    public void createMenu(MenuInflater menuInflater, Menu menu) {
        loadMenu(menuInflater, menu);
    }

    @Override
    public void onPrepareMenu(MenuInflater menuInflater, Menu menu) {
        loadMenu(menuInflater, menu);
    }

    public void loadMenu(MenuInflater menuInflater, Menu menu) {
        menu.clear();
        menuInflater.inflate(R.menu.home_sorder, menu);
        menu.setGroupVisible(R.id.group_sorder, false);
        menu.setGroupVisible(R.id.group_home_public, true);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }

    @Override
    public void onTextChanged(String text, int start, int before, int count) {

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {

    }

    @Override
    public void onTouchEvent(MotionEvent event) {

    }

    @Override
    public boolean onBack() {
        return false;
    }

    @Override
    public void onExit() {

    }

    @Override
    public void onSurfView(SOrder order) {
        mPresenter.accessOrder(order);
        ActivityManager.startSurfActivity((Activity) context, order);
    }

    @Override
    public void onWallView(SOrder order) {
        mPresenter.accessOrder(order);
        ActivityManager.startWallActivity((Activity) context, order);
    }

    @Override
    public void onBookView(SOrder order) {
        mPresenter.accessOrder(order);
        ActivityManager.startBookActivity((Activity) context, order);
    }

    @Override
    public void onPreView(SOrder order) {
        new PreviewDialog(context, order).show();
    }
}
