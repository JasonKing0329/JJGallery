package com.jing.app.jjgallery.presenter.main.order;

import com.jing.app.jjgallery.BasePresenter;
import com.jing.app.jjgallery.bean.order.SOrder;
import com.jing.app.jjgallery.model.main.order.SOrderCallback;
import com.jing.app.jjgallery.model.main.order.SOrderManager;

import java.util.List;

/**
 * Created by JingYang on 2016/7/11 0011.
 * Description:
 */
public class SOrderPresenter extends BasePresenter implements SOrderCallback {

    private SOrderManager sOrderManager;

    public SOrderPresenter() {
        sOrderManager = new SOrderManager(this);
    }

    // 异步操作
    public void loadAllOrders() {
        sOrderManager.loadAllOrders();
    }

    @Override
    public void onQueryAllOrders(List<SOrder> list) {

    }
}
