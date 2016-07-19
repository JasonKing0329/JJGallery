package com.jing.app.jjgallery.presenter.main.order;

import android.content.Context;

import com.jing.app.jjgallery.BasePresenter;
import com.jing.app.jjgallery.bean.order.SOrder;
import com.jing.app.jjgallery.config.PreferenceKey;
import com.jing.app.jjgallery.config.PreferenceValue;
import com.jing.app.jjgallery.model.main.order.SOrderCallback;
import com.jing.app.jjgallery.model.main.order.SOrderManager;
import com.jing.app.jjgallery.presenter.main.SettingProperties;
import com.jing.app.jjgallery.viewsystem.main.order.ISOrderDataCallback;
import com.jing.app.jjgallery.viewsystem.main.order.ISOrderView;
import com.jing.app.jjgallery.viewsystem.main.order.SOrderActivity;

import java.util.List;

/**
 * Created by JingYang on 2016/7/11 0011.
 * Description:
 */
public class SOrderPresenter extends BasePresenter implements SOrderCallback {

    private ISOrderView sorderView;
    private ISOrderDataCallback dataCallback;

    private SOrderManager sOrderManager;

    public SOrderPresenter(ISOrderView sorderView) {
        this.sorderView = sorderView;
        sOrderManager = new SOrderManager(this);
    }

    public void setDataCallback(ISOrderDataCallback dataCallback) {
        this.dataCallback = dataCallback;
    }

    // 异步操作
    public void loadAllOrders() {
        sOrderManager.loadAllOrders();
    }

    @Override
    public void onQueryAllOrders(List<SOrder> list) {
        if (dataCallback != null) {
            dataCallback.onQueryAllOrders(list);
        }
    }

    public void startSOrderPage(Context context) {
        String mode = SettingProperties.getSOrderDefaultMode(context);
        if (mode.equals(PreferenceValue.VALUE_SORDER_VIEW_THUMB)) {
            sorderView.onThumbPage();
        }
        else if (mode.equals(PreferenceValue.VALUE_SORDER_VIEW_INDEX)) {
            sorderView.onIndexPage();
        }
        else {
//            sorderView.onGridPage();
            sorderView.onThumbPage();
        }
    }

    public void switchToIndexPage() {
        sorderView.onIndexPage();
    }

    public void switchToThumbPage() {
        sorderView.onThumbPage();
    }

    public void switchToGridPage() {
        sorderView.onGridPage();
    }

}
