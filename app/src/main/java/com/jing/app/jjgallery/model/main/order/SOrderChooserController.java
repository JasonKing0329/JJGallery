package com.jing.app.jjgallery.model.main.order;

import com.jing.app.jjgallery.bean.order.SOrder;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by JingYang on 2016/7/11 0011.
 * Description:
 */
public class SOrderChooserController {

    private SOrderManager sOrderManager;
    private List<SOrder> orderList;

    public SOrderChooserController() {

    }

    public void sortOrderByName(List<SOrder> orderList) {
        if (orderList != null) {
            Collections.sort(orderList, new Comparator<SOrder>() {

                @Override
                public int compare(SOrder lhs, SOrder rhs) {

                    return lhs.getName().toLowerCase().compareTo(rhs.getName().toLowerCase());
                }

            });
        }
    }

    public void setSOrderCallback(SOrderCallback callback) {
        sOrderManager = new SOrderManager(callback);
    }

    public List<SOrder> getOrderList() {
        if (orderList == null) {
            loadAllOrders();
        }
        return orderList;
    }

    public void reloadOrders() {
        clearOrderList();
        loadAllOrders();
    }

    private void clearOrderList() {

        if (orderList != null) {
            orderList.clear();
        }
    }
    public void loadAllOrders() {
        sOrderManager.loadAllOrders();
    }

}
