package com.jing.app.jjgallery.viewsystem.sub.book;

/**
 * Created by JingYang on 2016/7/28 0028.
 * Description:
 */
public class OrderBookFragment extends BookFragment {

    private int orderId;

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    @Override
    protected void loadDatas() {
        mPresenter.loadDatasByOlder(orderId);
    }
}
