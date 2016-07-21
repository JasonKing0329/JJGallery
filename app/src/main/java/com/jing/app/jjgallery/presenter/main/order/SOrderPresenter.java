package com.jing.app.jjgallery.presenter.main.order;

import android.content.Context;

import com.jing.app.jjgallery.BasePresenter;
import com.jing.app.jjgallery.bean.order.SOrder;
import com.jing.app.jjgallery.bean.order.SOrderCount;
import com.jing.app.jjgallery.bean.order.STag;
import com.jing.app.jjgallery.config.PreferenceValue;
import com.jing.app.jjgallery.model.main.order.SOrderCallback;
import com.jing.app.jjgallery.model.main.order.SOrderHorIndexCreator;
import com.jing.app.jjgallery.model.main.order.SOrderManager;
import com.jing.app.jjgallery.presenter.main.SettingProperties;
import com.jing.app.jjgallery.viewsystem.main.order.HorizontalIndexView;
import com.jing.app.jjgallery.viewsystem.main.order.ISOrderDataCallback;
import com.jing.app.jjgallery.viewsystem.main.order.ISOrderView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by JingYang on 2016/7/11 0011.
 * Description:
 */
public class SOrderPresenter extends BasePresenter implements SOrderCallback {

    private ISOrderView sorderView;
    private ISOrderDataCallback dataCallback;

    private SOrderManager sOrderManager;
    private SOrderHorIndexCreator indexCreator;

    private int orderBy;
    private List<SOrder> orderList;

    public SOrderPresenter(ISOrderView sorderView) {
        this.sorderView = sorderView;
        sOrderManager = new SOrderManager(this);
        indexCreator = new SOrderHorIndexCreator(sOrderManager);
    }

    public void setDataCallback(ISOrderDataCallback dataCallback) {
        this.dataCallback = dataCallback;
    }

    // 异步操作
    public void loadAllOrders(int orderBy) {
        sOrderManager.loadAllOrders(orderBy);
    }

    @Override
    public void onQueryAllOrders(List<SOrder> list, int orderBy) {
        orderList = list;
        if (list != null) {
            if (orderBy == SOrderManager.ORDERBY_NAME) {
                Collections.sort(list, new NameComparator());
            }
            else if (orderBy == SOrderManager.ORDERBY_DATE) {
                Collections.reverse(list);
            }
            indexCreator.createIndex(list);
        }
        dataCallback.onQueryAllOrders(list);
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
            sorderView.onGridPage();
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

    public List<SOrder> getOrderList() {
        return orderList;
    }

    /**
     * 删除列表
     * @param order
     * @return
     */
    public boolean deleteOrder(SOrder order) {
        return sOrderManager.deleteOrder(order);
    }

    /**
     * 检查order是否已存在
     * @param name
     * @return
     */
    public boolean isOrderExist(String name) {
        return sOrderManager.isOrderExist(name);
    }

    /**
     * 重命名order
     * @param order
     * @return
     */
    public boolean renameOrderName(SOrder order) {
        return  sOrderManager.renameOrderName(order);
    }

    private class NameComparator implements Comparator<SOrder> {

        @Override
        public int compare(SOrder lhs, SOrder rhs) {
            return lhs.getName().toLowerCase().compareTo(rhs.getName().toLowerCase());
        }
    }

    public List<String> loadSOrderPreviews(SOrder order, int number) {
        if (order.getImgPathList() == null) {
            sOrderManager.loadOrderItems(order);
        }

        List<String> list = null;
        if (order.getImgPathList() != null) {
            int count = 0, maxNumber = order.getImgPathList().size();
            list = new ArrayList<>();
            Collections.shuffle(order.getImgPathList());
            while (count < number && count < maxNumber) {
                list.add(order.getImgPathList().get(count));
                count ++;
            }
        }
        return list;
    }

    public List<String> loadSOrderCovers(SOrder order, int number) {
        if (order.getImgPathList() == null) {
            sOrderManager.loadOrderItems(order);
        }

        List<String> list = null;
        if (order.getImgPathList() != null) {
            int count = 0, maxNumber = order.getImgPathList().size();
            list = new ArrayList<String>();
            Collections.shuffle(order.getImgPathList());
            while (count < number && count < maxNumber) {
                list.add(order.getImgPathList().get(count));
                count ++;
            }
        }
        return list;
    }

    public List<STag> loadTagList() {
        return sOrderManager.loadTagList();
    }

    /**
     * delete tag
     * @param sTag
     * @param list
     */
    public void deleteTag(STag sTag, List<SOrder> list) {
        sOrderManager.deleteTag(sTag, list);
    }

    public void accessOrder(SOrder order) {
        sOrderManager.accessOrder(order);
    }

    public SOrderCount queryOrderCount(int orderId) {
        return sOrderManager.queryOrderCount(orderId);
    }

    /**
     * 将order还原为未加密目录
     * @param order
     * @return
     */
    public boolean decipherOrderAsFolder(SOrder order) {
        return sOrderManager.decipherOrderAsFolder(order);
    }

    public void initIndexCreator(Context context) {

        indexCreator.setPageMaxItem(SettingProperties.getSOrderPageNumber(context));
        indexCreator.setDefaultMode(SettingProperties.getOrderMode(context));
    }

    public List<HorizontalIndexView.IndexItem> createIndex() {
        return indexCreator.createIndex(orderList);
    }
    public List<HorizontalIndexView.IndexItem> createIndexBydate() {
        return indexCreator.createIndex(orderList, PreferenceValue.ORDERBY_DATE);
    }
    public List<SOrder> getPageItem(int index) throws HorizontalIndexView.PageIndexOutOfBoundsException {
        return indexCreator.getPageItem(orderList, index);
    }

}
