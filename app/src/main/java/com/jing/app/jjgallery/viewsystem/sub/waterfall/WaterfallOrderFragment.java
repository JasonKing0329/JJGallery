package com.jing.app.jjgallery.viewsystem.sub.waterfall;

import com.jing.app.jjgallery.bean.order.SOrder;
import com.jing.app.jjgallery.presenter.main.order.SOrderProvider;
import com.jing.app.jjgallery.presenter.main.order.SOrderProviderCallback;

/**
 * Created by JingYang on 2016/7/27 0027.
 * Description:
 */
public class WaterfallOrderFragment extends WaterfallFragment {

    private SOrder sOrder;
    private int orderId;
    private BaseWaterfallAdapter mAdapter;

    public void setOrderId(int id) {
        orderId = id;
    }

    @Override
    protected void initSOrderProvider() {
        sOrderProvider = new SOrderProvider(getActivity(), new SOrderProviderCallback() {
            @Override
            public void onMoveFinish(String folderPath) {

            }

            @Override
            public void onAddToOrderFinished() {

            }

            @Override
            public void onDeleteIndex(int index) {
                //show animation
                mAdapter.removeItem(index);
            }

            @Override
            public void onDeleteFinished(int count) {
                sOrder.setItemNumber(sOrder.getItemNumber() - count);
                //restore normal status
                mAdapter.cancelActionMode();
                mAdapter.notifyDataSetChanged();
                showSelectionMode(false);
            }
        });
    }

    @Override
    protected void loadListData() {
        sOrder = mPresenter.queryOrder(orderId);
        mPresenter.loadSOrderItems(sOrder);
    }

    @Override
    protected void onDatasReady(Object data) {
        mAdapter = new OrderWaterfallAdapter(getActivity(), sOrder, nColumn);
    }

    @Override
    protected BaseWaterfallAdapter getAdapter() {
        return mAdapter;
    }

    @Override
    protected void deleteSelectedFile() {
        sOrderProvider.deleteItemFromOrder(sOrder, mAdapter.getSelectedIndex());
    }

}
