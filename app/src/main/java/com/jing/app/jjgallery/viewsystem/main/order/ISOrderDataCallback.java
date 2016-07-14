package com.jing.app.jjgallery.viewsystem.main.order;

import com.jing.app.jjgallery.bean.order.SOrder;

import java.util.List;

/**
 * Created by JingYang on 2016/7/14 0014.
 * Description:
 */
public interface ISOrderDataCallback {
    void onQueryAllOrders(List<SOrder> list);
}
