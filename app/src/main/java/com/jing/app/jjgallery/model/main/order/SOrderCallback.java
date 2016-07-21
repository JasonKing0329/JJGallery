package com.jing.app.jjgallery.model.main.order;

import com.jing.app.jjgallery.bean.order.SOrder;

import java.util.List;

/**
 * Created by JingYang on 2016/7/11 0011.
 * Description:
 */
public interface SOrderCallback {
    void onQueryAllOrders(List<SOrder> list, int orderby);
}
