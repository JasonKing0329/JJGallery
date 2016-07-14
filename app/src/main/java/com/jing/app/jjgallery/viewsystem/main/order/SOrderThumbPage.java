package com.jing.app.jjgallery.viewsystem.main.order;

import android.content.Context;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.bean.order.SOrder;
import com.jing.app.jjgallery.viewsystem.main.filesystem.FileManagerActivity;
import com.jing.app.jjgallery.viewsystem.publicview.ActionBar;
import com.jing.app.jjgallery.viewsystem.sub.thumb.ThumbFolderAdapter;
import com.jing.app.jjgallery.viewsystem.sub.thumb.ThumbPage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JingYang on 2016/7/11 0011.
 * Description:
 */
public class SOrderThumbPage extends ThumbPage implements ISOrderPage {

    private SOrderThumbFolderAdapter mAdapter;
    
    private List<SOrder> sorderList;
    private List<SOrder> tempList;

    private SOrder mCurrentOrder;
    
    public SOrderThumbPage(Context context, View contentView, boolean isChooserMode) {
        super(context, contentView, isChooserMode);
    }

    @Override
    public void initData() {
        tempList = new ArrayList<>();
        mPresenter.setSOrderView(this);
        loadAllOrders();
    }

    @Override
    public void initActionbar(ActionBar actionBar) {
        super.initActionbar(actionBar);
        actionBar.addThumbIcon();
        actionBar.addAddIcon();
        actionBar.onConfiguration(getContext().getResources().getConfiguration().orientation);
    }

    private void loadAllOrders() {
        // 异步操作
        mPresenter.loadAllOrders();
    }

    @Override
    public void onQueryAllOrders(List<SOrder> list) {
        sorderList = list;
        fillTempList();
        mAdapter.setDatas(tempList);
        mAdapter.notifyDataSetChanged();

        getIndexCreator().createFromOrderList(tempList);
        showIndexView();
        initIndexStateController();
    }

    private void fillTempList() {
        if (sorderList != null) {
            tempList.clear();
            for (SOrder order:sorderList) {
                tempList.add(order);
            }
        }
    }

    @Override
    protected void refreshCurrent() {
        // 发生了delete/move事件，重新刷新当前列表内容
        mPresenter.loadOrderItems(mCurrentOrder);
        showImages(mCurrentOrder.getImgPathList());
    }

    @Override
    protected ThumbFolderAdapter getFolderAdapter() {
        if (mAdapter == null) {
            mAdapter = new SOrderThumbFolderAdapter(getContext(), this);
        }
        return mAdapter;
    }

    @Override
    public void onIconClick(View view) {
        super.onIconClick(view);
        switch (view.getId()) {

            case R.id.actionbar_add:

                break;
            case R.id.actionbar_thumb:
                showViewModePopup(view);
                break;
        }
    }

    @Override
    protected void deleteSelectedFiles() {
        sOrderProvider.deleteItemFromOrder(mCurrentOrder, getSelectedIndex());
    }

    @Override
    protected void onTextFilterChanged(String text) {

        tempList.clear();
        // 回删全部搜索词，显示全部
        if (text.toString().trim().length() == 0) {
            for (int i = 0; i < sorderList.size(); i ++) {
                tempList.add(sorderList.get(i));
            }
            mAdapter.notifyDataSetChanged();
        }
        // 有过滤词
        else {
            String target = null, prefix = text.toString().toLowerCase();
            // startWith的优先
            for (int i = 0; i < sorderList.size(); i ++) {
                target = sorderList.get(i).getName().toLowerCase();
                if (target.startsWith(prefix)) {
                    tempList.add(sorderList.get(i));
                }
            }
            // contains的其次
            for (int i = 0; i < sorderList.size(); i ++) {
                target = sorderList.get(i).getName().toLowerCase();
                if (!target.startsWith(prefix) && target.contains(prefix)) {
                    tempList.add(sorderList.get(i));
                }
            }
        }

        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onUpperClicked() {

    }

    protected void showViewModePopup(View v) {
        PopupMenu menu = new PopupMenu(getContext(), v);
        menu.getMenuInflater().inflate(R.menu.sorder_view_mode, menu.getMenu());
        menu.getMenu().findItem(R.id.menu_thumb_view).setVisible(false);
        menu.show();
        menu.setOnMenuItemClickListener(viewModeListener);
    }

    PopupMenu.OnMenuItemClickListener viewModeListener = new PopupMenu.OnMenuItemClickListener() {

        @Override
        public boolean onMenuItemClick(MenuItem item) {

            switch (item.getItemId()) {
                case R.id.menu_grid_view:
                    if (getContext() instanceof SOrderActivity) {
                        SOrderActivity instance = (SOrderActivity) getContext();
                        instance.getPresenter().switchToGridPage();
                    }
                    break;
                case R.id.menu_index_view:
                    if (getContext() instanceof SOrderActivity) {
                        SOrderActivity instance = (SOrderActivity) getContext();
                        instance.getPresenter().switchToIndexPage();
                    }
                    break;
            }
            return true;
        }

    };

    @Override
    public void onThumbFolderItemClick(View view, int position) {
        mCurrentOrder = tempList.get(position);
        mPresenter.loadOrderItems(mCurrentOrder);

        // 设置选中状态
        focusFolderItem(view, position);

        // 显示当前目录内容
        showImages(mCurrentOrder.getImgPathList());
    }

    @Override
    public void onThumbFolderItemLongClick(View view, int position) {

    }

}
