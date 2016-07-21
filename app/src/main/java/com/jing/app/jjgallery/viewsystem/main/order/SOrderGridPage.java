package com.jing.app.jjgallery.viewsystem.main.order;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.jing.app.jjgallery.Application;
import com.jing.app.jjgallery.BasePresenter;
import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.bean.order.SOrder;
import com.jing.app.jjgallery.bean.order.SOrderCount;
import com.jing.app.jjgallery.bean.order.STag;
import com.jing.app.jjgallery.config.DBInfor;
import com.jing.app.jjgallery.config.PreferenceValue;
import com.jing.app.jjgallery.presenter.main.SettingProperties;
import com.jing.app.jjgallery.presenter.main.order.SOrderPresenter;
import com.jing.app.jjgallery.presenter.main.order.ScrollController;
import com.jing.app.jjgallery.service.image.PictureManagerUpdate;
import com.jing.app.jjgallery.util.ScreenUtils;
import com.jing.app.jjgallery.viewsystem.ActivityManager;
import com.jing.app.jjgallery.viewsystem.IPage;
import com.jing.app.jjgallery.viewsystem.publicview.ActionBar;
import com.jing.app.jjgallery.viewsystem.publicview.CustomDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by JingYang on 2016/7/20 0020.
 * Description:
 */
public class SOrderGridPage implements IPage, ISOrderDataCallback, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener
        , ExpandableListView.OnChildClickListener, View.OnCreateContextMenuListener
        , HorizontalIndexView.OnPageSelectListener {

    private SOrderPresenter mPresenter;
    private final String TAG = "SOrderPage";
    private Context context;

    private View view;
    private final int VIEW_GRID = 0;
    private final int VIEW_LIST = 1;
    private int currentView;

    private ActionBar actionBar;
    private GridView gridView;
    private SOrderGridAdapter gridAdapter;
    private ProgressDialog progressDialog;
    private List<SOrder> currentPageOrders;
    private int currentPage;
    private ImageView previousPageView, nextPageView;

    private View pageItemContainer;
    private ImageView switchAnimView;
    private boolean showAnimation;
    private SOrderMenuDialog sOrderMenuDialog;

    private ExpandableListView expandableView;
    private SOrderExpandableListAdapter expandableAdapter;
    private List<STag> tagList;
    private List<List<SOrder>> orderListInExpandable;

    private int currentOrderBy = -1;

    private HorizontalIndexView horizontalIndexView;

    private ScrollController scrollController;


    public SOrderGridPage(Context context, View view) {
        this.context = context;
        this.view = view;
        currentView = VIEW_GRID;
        currentOrderBy = SettingProperties.getOrderMode(context);
        currentPage = 1;
        initGridViewElement();
    }

    @Override
    public void initData() {
        refresh();
    }

    @Override
    public void setPresenter(BasePresenter presenter) {
        mPresenter = (SOrderPresenter) presenter;
        mPresenter.setDataCallback(this);
        scrollController = new ScrollController(context, mPresenter);
        mPresenter.initIndexCreator(context);
    }

    @Override
    public void initActionbar(ActionBar actionBar) {
        actionBar.clearActionIcon();
        actionBar.addMenuIcon();
        actionBar.addAddIcon();
        actionBar.addThumbIcon();
        actionBar.addColorIcon();
        actionBar.addSortIcon();
        actionBar.addRefreshIcon();
    }

    @Override
    public void onIconClick(View view) {
        switch (view.getId()) {
            case R.id.actionbar_add:
                addNewOrder();
                break;
            case R.id.actionbar_sort:
                showSortPopup(view);
                break;
            case R.id.actionbar_thumb:
                showSelectCoverPopup(view);
                break;

            case R.id.actionbar_refresh:
                refresh();
                break;
        }
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
        menu.setGroupVisible(R.id.group_sorder, true);
        menu.setGroupVisible(R.id.group_home_public, true);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_view_grid:
                PictureManagerUpdate.getInstance().recycleExpandOrderCovers();
                showGridView();
                break;
            case R.id.menu_view_list:
                currentPageOrders = mPresenter.getOrderList();
                showExpandableView();
                break;
        }
        return false;
    }

    @Override
    public void onTextChanged(String text, int start, int before, int count) {

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (sOrderMenuDialog != null) {
            sOrderMenuDialog.dismiss();
        }
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

    private void initGridViewElement() {

        gridView = (GridView) view.findViewById(R.id.sorder_gridview);
        gridView.setOnItemClickListener(this);
        gridView.setOnItemLongClickListener(this);
        gridView.setOnScrollListener(scrollController);

        previousPageView = (ImageView) view.findViewById(R.id.sorder_previous_page);
        nextPageView = (ImageView) view.findViewById(R.id.sorder_next_page);

        if (currentView == VIEW_GRID && SettingProperties.isPageModeEnable(context)) {

            switchAnimView = (ImageView) view.findViewById(R.id.sorder_switch_anim_view);
            pageItemContainer = view.findViewById(R.id.sorder_page_view);
            //目的在于switch page时的动画，动画是通过snap shot截取layout view，加上背景效果更好
            pageItemContainer.setBackgroundColor(context.getResources().getColor(R.color.sorder_bk));

            horizontalIndexView = (HorizontalIndexView) view.findViewById(R.id.sorder_page_index);
            horizontalIndexView.setOnPageSelectListener(this);

//            if (SettingProperties.isMainViewSlidingEnable(context)) {
//                horizontalIndexView.setScroll(false);
//            }

            int size = context.getResources().getDimensionPixelSize(R.dimen.page_index_fixgrid_item_width_large);
            int horPadding = context.getResources().getDimensionPixelSize(R.dimen.page_index_padding_hor);
            int minSize = context.getResources().getDimensionPixelSize(R.dimen.page_index_fixgrid_item_width_small);
            horizontalIndexView.setIndexSize(size, minSize);
            horizontalIndexView.setItemSpace(horPadding);
            horizontalIndexView.setMaxWidth(ScreenUtils.getScreenWidth(context));
        }
    }

    private void showPreNextGuide(boolean preEnable, boolean nextEnable) {
        previousPageView.setVisibility(View.VISIBLE);
        nextPageView.setVisibility(View.VISIBLE);
        previousPageView.setEnabled(preEnable);
        nextPageView.setEnabled(nextEnable);
        if (preEnable) previousPageView.setImageResource(R.drawable.previous_page);
        else previousPageView.setImageResource(R.drawable.previous_page_dis);
        if (nextEnable) nextPageView.setImageResource(R.drawable.next_page);
        else nextPageView.setImageResource(R.drawable.next_page_dis);
        previousPageView.setOnClickListener(pageIndexListener);
        nextPageView.setOnClickListener(pageIndexListener);
    }

    private void showProgress() {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(context.getResources().getString(R.string.loading));
        progressDialog.show();
    }

    @Override
    public void onQueryAllOrders(List<SOrder> list) {
        if (SettingProperties.isPageModeEnable(context) && currentView == VIEW_GRID) {
            if (SettingProperties.isPageModeEnable(context) && currentView == VIEW_GRID) {
                horizontalIndexView.setIndexList(mPresenter.createIndexBydate());
                horizontalIndexView.select(1);
                //choosePage(1);
                initPagePreNextGuid();
                showAnimation = false;
                horizontalIndexView.select(currentPage);
            }
        }
        else {
            currentPageOrders = mPresenter.getOrderList();
            notifyUpdate();
        }
        progressDialog.cancel();
    }

    private void refresh() {
        showProgress();
        mPresenter.loadAllOrders(currentOrderBy);
    }

    public void initPagePreNextGuid() {
        if (horizontalIndexView.getPagesNumber() < 2) {
            showPreNextGuide(false, false);
        }
        else {
            if (currentPage == horizontalIndexView.getPagesNumber()) {
                showPreNextGuide(true, false);
            }
            else if (currentPage == 1) {
                showPreNextGuide(false, true);
            }
            else {
                showPreNextGuide(true, true);
            }
        }
    }

    private void notifyUpdate() {
        Log.d(TAG, "notifyUpdate");
        if (currentView == VIEW_GRID) {
            notifyGridViewUpdate();
			/*
			if (gridAdapter == null) {
				gridAdapter = new SOrderGridAdapter(context, bridge.getOrderList());
				gridView.setAdapter(gridAdapter);
			}
			else {
				gridAdapter.setSorderList(bridge.getOrderList());
				if (gridView.getAdapter() == null) {
					gridView.setAdapter(gridAdapter);
				}
				else {
					gridAdapter.notifyDataSetChanged();
				}
			}
			*/
        }
        else {
            if (horizontalIndexView != null) {
                horizontalIndexView.setVisibility(View.GONE);
            }

            loadTagList();
            suitExpandList();
            if (expandableAdapter == null) {
                expandableAdapter = new SOrderExpandableListAdapter(context, orderListInExpandable, tagList);
                expandableView.setAdapter(expandableAdapter);
            }
            else {
                expandableAdapter.setTagList(tagList);
                expandableAdapter.setListInExpand(orderListInExpandable);

                if (expandableView.getAdapter() == null) {
                    expandableView.setAdapter(expandableAdapter);
                }
                else {
                    expandableAdapter.notifyDataSetChanged();
                }
            }
        }
    }
    private void notifyGridViewUpdate() {
        Log.d(TAG, "notifyGridViewUpdate");
        if (horizontalIndexView != null) {
            if (SettingProperties.isPageModeEnable(context) && currentView == VIEW_GRID) {
                if (horizontalIndexView.getPagesNumber() < 2) {
                    horizontalIndexView.setVisibility(View.GONE);
                }
                else {
                    horizontalIndexView.setVisibility(View.VISIBLE);
                    horizontalIndexView.show();
                }
            }
            else {
                horizontalIndexView.setVisibility(View.GONE);
            }
        }

        if (gridAdapter == null) {
            gridAdapter = new SOrderGridAdapter(context, currentPageOrders);
            gridView.setAdapter(gridAdapter);
            scrollController.setGridAdapter(gridAdapter);
        }

        gridAdapter.setSorderList(currentPageOrders);
        scrollController.notifyCoverDataChanged();//adapt covers

        if (gridView.getAdapter() == null) {
            gridView.setAdapter(gridAdapter);
        }
        else {
            gridAdapter.notifyDataSetChanged();
        }

        //horizontalIndexView.requestLayoutFixGrid();
    }

    View.OnClickListener pageIndexListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            if (view == previousPageView) {
                if (currentPage == 2) {
                    previousPageView.setImageResource(R.drawable.previous_page_dis);
                    previousPageView.setEnabled(false);
                }
                if (!nextPageView.isEnabled()) {
                    nextPageView.setImageResource(R.drawable.next_page);
                    nextPageView.setEnabled(true);
                }
                currentPage --;
            }
            else if (view == nextPageView) {
                if (currentPage == horizontalIndexView.getPagesNumber() - 1) {
                    nextPageView.setImageResource(R.drawable.next_page_dis);
                    nextPageView.setEnabled(false);
                }
                if (!previousPageView.isEnabled()) {
                    previousPageView.setImageResource(R.drawable.previous_page);
                    previousPageView.setEnabled(true);
                }
                currentPage ++;
            }
            else {
                currentPage = (Integer) view.getTag();
                if (currentPage == 1) {
                    previousPageView.setImageResource(R.drawable.previous_page_dis);
                    previousPageView.setEnabled(false);
                    if (horizontalIndexView.getPagesNumber() > 1) {
                        nextPageView.setImageResource(R.drawable.next_page_dis);
                        nextPageView.setEnabled(false);
                    }
                    else {
                        nextPageView.setImageResource(R.drawable.next_page);
                        nextPageView.setEnabled(true);
                    }
                }
                else if (currentPage == horizontalIndexView.getPagesNumber()) {
                    nextPageView.setImageResource(R.drawable.next_page_dis);
                    nextPageView.setEnabled(false);
                    previousPageView.setImageResource(R.drawable.previous_page);
                    previousPageView.setEnabled(true);
                }
                else {
                    if (!previousPageView.isEnabled()) {
                        previousPageView.setImageResource(R.drawable.previous_page);
                        previousPageView.setEnabled(true);
                    }
                    if (!nextPageView.isEnabled()) {
                        nextPageView.setImageResource(R.drawable.next_page);
                        nextPageView.setEnabled(true);
                    }
                }
            }
            horizontalIndexView.select(currentPage);
        }
    };

    @Override
    public void onSelect(int index) {
        try {
            currentPage = index;
            currentPageOrders = mPresenter.getPageItem(index);
            startSwitchAnimation();
            notifyUpdate();
        } catch (HorizontalIndexView.PageIndexOutOfBoundsException e) {
            Log.i("SOrderPage", "index(" + index + ") is out of bounds");
            e.printStackTrace();
        }
    }

    private void startSwitchAnimation() {

        if (showAnimation) {//防止第一次进入sorder page就snapshot
            if (currentView == VIEW_GRID && SettingProperties.isPageModeEnable(context)) {
                final Bitmap snapshot = ScreenUtils.snapShotView(pageItemContainer);
                switchAnimView.setImageBitmap(snapshot);
                switchAnimView.setVisibility(View.VISIBLE);
                Animation animation = AnimationUtils.loadAnimation(context, R.anim.sorder_switch_page);
                switchAnimView.startAnimation(animation);
                animation.setAnimationListener(new Animation.AnimationListener() {

                    @Override
                    public void onAnimationStart(Animation arg0) {
                    }

                    @Override
                    public void onAnimationRepeat(Animation arg0) {
                    }

                    @Override
                    public void onAnimationEnd(Animation arg0) {
                        switchAnimView.setVisibility(View.GONE);
                        switchAnimView.setImageBitmap(null);
                        snapshot.recycle();
                    }
                });
            }
        }
        showAnimation = true;
    }

    public void recycleResource() {
        PictureManagerUpdate.getInstance().recycleOrderPreview();
        PictureManagerUpdate.getInstance().recycleOrderCovers();
    }

    private void showExpandableView() {
        if (currentView != VIEW_LIST) {
            currentView = VIEW_LIST;
            gridView.setVisibility(View.GONE);
            previousPageView.setVisibility(View.GONE);
            nextPageView.setVisibility(View.GONE);

            expandableView = (ExpandableListView) view.findViewById(R.id.sorder_expandableview);
            expandableView.setVisibility(View.VISIBLE);
            expandableView.setOnChildClickListener(this);
            expandableView.setOnCreateContextMenuListener(this);

            notifyUpdate();
        }
    }

    private void loadTagList() {

        if (tagList != null) {
            tagList.clear();
        }
        if (tagList == null) {
            tagList = new ArrayList<STag>();
        }
        tagList = mPresenter.loadTagList();
    }

    private void suitExpandList() {
        if (tagList == null || tagList.size() == 0) {
            return;
        }
        if (orderListInExpandable != null) {
            for (int i = 0; i < orderListInExpandable.size(); i ++) {
                orderListInExpandable.get(i).clear();
            }
            orderListInExpandable.clear();
        }

        orderListInExpandable = new ArrayList<List<SOrder>>();
        List<SOrder> subList = null;
        for (int i = 0; i < tagList.size(); i ++) {
            subList = new ArrayList<SOrder>();
            orderListInExpandable.add(subList);
        }
        List<SOrder> list = currentPageOrders;
        for (SOrder order:list) {
            if (order.getTag().getName() == null) {
                order.setTag(getTag(order.getTag().getId()));
            }
            addToTagSubList(order);
        }
    }

    private void addToTagSubList(SOrder order) {
        for (int i = 0; i < tagList.size(); i ++) {
            if (tagList.get(i).getId() == order.getTag().getId()) {
                orderListInExpandable.get(i).add(order);
                break;
            }
        }
    }

    private STag getTag(int id) {
        STag tag = null;
        for (STag t:tagList) {
            if (t.getId() == id) {
                tag = t;
                break;
            }
        }
        if (tag == null) {
            tag = DBInfor.DEFAULT_TAG;
        }
        return tag;
    }

    private void showGridView() {
        if (currentView != VIEW_GRID) {
            currentView = VIEW_GRID;
            expandableView.setVisibility(View.GONE);
            gridView.setVisibility(View.VISIBLE);
            refresh();
        }
    }

    public void openCreateOrderDialog() {
//		EditText edit = new EditText(context);
//		edit.setInputType(InputType.TYPE_CLASS_TEXT);
//		final EditText ed = edit;

        SOrderCreaterUpdate creater = new SOrderCreaterUpdate(context, new CustomDialog.OnCustomDialogActionListener() {

            @Override
            public boolean onSave(Object object) {

                if (object == null) {
                    Toast.makeText(context, R.string.sorder_create_fail, Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(context, R.string.sorder_success, Toast.LENGTH_LONG).show();
                    refresh();
                }
                return true;
            }

            @Override
            public void onLoadData(HashMap<String, Object> data) {

            }

            @Override
            public boolean onCancel() {

                return false;
            }
        });
        creater.show();
		/*
		new SOrderCreater(context, new OnOrderCreateListener() {

			@Override
			public void onReceiveError(Object object) {
				Toast.makeText(context, R.string.sorder_create_fail, Toast.LENGTH_LONG).show();
			}

			@Override
			public void onOk(SOrder order) {
				Toast.makeText(context, R.string.sorder_success, Toast.LENGTH_LONG).show();
				refresh();

			@Override
			public void onCancel() {

			}
		}).show();*/
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        SOrder order = currentPageOrders.get(position);
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v,
                                int groupPosition, int childPosition, long id) {
        SOrder order = orderListInExpandable.get(groupPosition).get(childPosition);
        return false;
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view,
                                   int position, long id) {

        //截图需要截非按压效果的
        view.findViewById(R.id.sorder_grid_item_layout).setBackgroundResource(R.drawable.shape_order_background);
        Bitmap cover = ScreenUtils.snapShotView(view);
        int viewPos[] = new int[2];
        view.getLocationOnScreen(viewPos);

        final int pos = position;
        sOrderMenuDialog = new SOrderMenuDialog(context, cover, viewPos[0], viewPos[1]
                , new CircleMenuView.OnMenuItemListener() {

            @Override
            public void onMenuClick(int which) {

                sOrderMenuDialog.dismiss();

                if (which == 0) {//rename
                    renameOrder(currentPageOrders.get(pos));
                }
                if (which == 1) {//delete
                    if (currentPageOrders.get(pos).getItemNumber() > 0) {
                        openDeleteWarningDlg(pos);
                    }
                    else {
                        deleteOrder(currentPageOrders.get(pos));
                    }
                }
                else if (which == 2) {//decipher as folder
                    decipherOrderAsFolder(currentPageOrders.get(pos));
                }
                else if (which == 3) {//fullscreen
                    mPresenter.accessOrder(currentPageOrders.get(pos));
                    ActivityManager.startSurfActivity((Activity) context, currentPageOrders.get(pos));
                }
                else if (which == 4) {//view access count
                    showAccessCount(currentPageOrders.get(pos));
                }
                else if (which == 5) {
                    mPresenter.accessOrder(currentPageOrders.get(pos));
                    ActivityManager.startWallActivity((Activity) context, currentPageOrders.get(pos));
                }
                else if (which == 6) {
                    mPresenter.accessOrder(currentPageOrders.get(pos));
                    ActivityManager.startBookActivity((Activity) context, currentPageOrders.get(pos));
                }
                else if (which == 7) {
                    new PreviewDialog(context, currentPageOrders.get(pos)).show();
                }

            }
        });
        sOrderMenuDialog.show();

        //截图后必须还原，否则以后再press没有效果
        if (Application.isLollipop()) {
            view.findViewById(R.id.sorder_grid_item_layout).setBackgroundResource(R.drawable.selector_order_background_l);
        }
        else {
            view.findViewById(R.id.sorder_grid_item_layout).setBackgroundResource(R.drawable.selector_order_background);
        }
        return true;
    }

    protected void showAccessCount(SOrder order) {
        SOrderCount orderCount = order.getOrderCount();
        if (orderCount == null) {
            orderCount = mPresenter.queryOrderCount(order.getId());
            order.setOrderCount(orderCount);
        }
        StringBuffer buffer = new StringBuffer("总访问量： ");
        buffer.append(orderCount.countAll)
                .append("\n").append("年访问量:  ").append(orderCount.countYear)
                .append("\n").append("月访问量:  ").append(orderCount.countMonth)
                .append("\n").append("一周访问量:  ").append(orderCount.countWeek)
                .append("\n").append("今日访问量:  ").append(orderCount.countDay);
        new AlertDialog.Builder(context)
                .setTitle(null)
                .setMessage(buffer.toString())
                .show();
    }

    protected void decipherOrderAsFolder(final SOrder order) {
        showProgress();
        new Thread() {
            public void run() {
                boolean result = mPresenter.decipherOrderAsFolder(order);
                Message message = new Message();
                if (result) {
                    message.what = 1;
                }
                saveAsHandler.sendMessage(message);
            }
        }.start();
    }

    Handler saveAsHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            progressDialog.cancel();
            if (msg.what == 1) {
                Toast.makeText(context, R.string.sorder_success, Toast.LENGTH_LONG).show();
            }
            super.handleMessage(msg);
        }

    };

    private void deleteOrder(SOrder order) {
        if (mPresenter.deleteOrder(order)) {
            refresh();
        }
        else {
            Toast.makeText(context, R.string.sorder_delete_fail, Toast.LENGTH_LONG).show();
        }
    }

    private void openDeleteWarningDlg(final int pos) {
        new AlertDialog.Builder(context)
                .setTitle(R.string.warning)
                .setMessage(R.string.sorder_warning_delete)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteOrder(currentPageOrders.get(pos));
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    private void openDeleteWarningDlg(final int group, final int child) {
        new AlertDialog.Builder(context)
                .setTitle(R.string.warning)
                .setMessage(R.string.sorder_warning_delete)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteOrder(orderListInExpandable.get(group).get(child));
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    private class RenameDialog implements DialogInterface.OnClickListener {

        private SOrder rOrder;
        private EditText edit = new EditText(context);
        public RenameDialog(SOrder order) {
            rOrder = order;
            edit.setInputType(InputType.TYPE_CLASS_TEXT);
            edit.setText(order.getName());
            edit.setSelectAllOnFocus(true);
        }
        public View createView () {
            return edit;
        }
        @Override
        public void onClick(DialogInterface view, int which) {
            String name = edit.getText().toString();
            if (name != null && name.length() > 0) {
                if (mPresenter.isOrderExist(name)) {
                    Toast.makeText(context, R.string.sorder_name_already_exist, Toast.LENGTH_LONG).show();
                }
                else {
                    SOrder order = rOrder;
                    order.setName(name);
                    if (mPresenter.renameOrderName(order)) {
                        refresh();
                    }
                    else {
                        Toast.makeText(context, R.string.sorder_rename_fail, Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
    }

    private void renameOrder(SOrder order) {
        RenameDialog view = new RenameDialog(order);
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle(R.string.menu_create_order);
        dialog.setView(view.createView());
        dialog.setPositiveButton(R.string.ok, view);
        dialog.setNegativeButton(R.string.cancel, null);
        dialog.show();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        ExpandableListView.ExpandableListContextMenuInfo info = (ExpandableListView.ExpandableListContextMenuInfo) menuInfo;
        int type = ExpandableListView.getPackedPositionType(info.packedPosition);
        int group = ExpandableListView.getPackedPositionGroup(info.packedPosition);
        Activity activity = (Activity) context;
        if(type == ExpandableListView.PACKED_POSITION_TYPE_CHILD){
            activity.getMenuInflater().inflate(R.menu.context_sorder_longclick, menu);
        }
        else if (type == ExpandableListView.PACKED_POSITION_TYPE_GROUP) {
            activity.getMenuInflater().inflate(R.menu.context_sorder_head_longclick, menu);
        }
    }

    public void onContextItemSelected(MenuItem item) {
        ExpandableListView.ExpandableListContextMenuInfo info = (ExpandableListView.ExpandableListContextMenuInfo) item.getMenuInfo();
        int group = ExpandableListView.getPackedPositionGroup(info.packedPosition);
        int type = ExpandableListView.getPackedPositionType(info.packedPosition);
        int child = 0;
        if (type == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
            child = ExpandableListView.getPackedPositionChild(info.packedPosition);
            switch (item.getItemId()) {
                case R.id.menu_sorder_rename:

                    renameOrder(orderListInExpandable.get(group).get(child));
                    break;
                case R.id.menu_sorder_delete:

                    if (orderListInExpandable.get(group).get(child).getItemNumber() > 0) {
                        openDeleteWarningDlg(group, child);
                    }
                    else {
                        deleteOrder(orderListInExpandable.get(group).get(child));
                    }
                    break;
                case R.id.menu_sorder_change_tag:

                    break;
                case R.id.menu_sorder_decipher_as_folder:
                    decipherOrderAsFolder(orderListInExpandable.get(group).get(child));

                    break;
                case R.id.menu_sorder_access_count:
                    showAccessCount(orderListInExpandable.get(group).get(child));
                    break;
                case R.id.menu_sorder_open_by_wall:
                    mPresenter.accessOrder(orderListInExpandable.get(group).get(child));
                    ActivityManager.startWallActivity((Activity) context, orderListInExpandable.get(group).get(child));
                    break;
                case R.id.menu_sorder_book_view:
                    mPresenter.accessOrder(orderListInExpandable.get(group).get(child));
                    ActivityManager.startBookActivity((Activity) context, orderListInExpandable.get(group).get(child));
                    break;
                case R.id.menu_sorder_preview:
                    new PreviewDialog(context, orderListInExpandable.get(group).get(child)).show();
                    break;
            }
        }
        else if (type == ExpandableListView.PACKED_POSITION_TYPE_GROUP) {
            switch (item.getItemId()) {
                case R.id.menu_sorder_head_delete:
                    if (orderListInExpandable.get(group).size() > 0) {
                        warningDeleteTag(group, 3);
                    }
                    else {
                        mPresenter.deleteTag(tagList.get(group), null);
                    }
                    break;
                case R.id.menu_sorder_head_rename:

                    break;

            }
        }
    }

    private void warningDeleteTag (final int group, final int left) {
        String message = context.getResources().getString(R.string.sorder_delete_tag_warning);
        message = message.replace("%d", "" + left);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.warning).setMessage(message)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        int remain = left;
                        remain --;
                        if (remain > 0) {
                            warningDeleteTag(group, remain);
                        }
                        else {
                            executeDeleteTag(group);
                            return;
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    private void executeDeleteTag(int group) {
        mPresenter.deleteTag(tagList.get(group), orderListInExpandable.get(group));
    }

    protected void addNewOrder() {
        openCreateOrderDialog();
    }

    protected void showSelectCoverPopup(View v) {
        PopupMenu menu = new PopupMenu(context, v);
        menu.getMenuInflater().inflate(R.menu.sorder_select_cover_mode, menu.getMenu());
        menu.show();
        menu.setOnMenuItemClickListener(selectCoverListener);
    }

    PopupMenu.OnMenuItemClickListener selectCoverListener = new PopupMenu.OnMenuItemClickListener() {

        @Override
        public boolean onMenuItemClick(MenuItem item) {

            switch (item.getItemId()) {
                case R.id.menu_cover_single:
                    if (currentView == VIEW_GRID) {
                        if (gridAdapter != null) {
                            gridAdapter.setCoverMode(SOrderGridAdapter.CoverMode.SINGLE);
                            notifyUpdate();
                        }
                    }
                    break;
                case R.id.menu_cover_cascade:
                    if (currentView == VIEW_GRID) {
                        if (gridAdapter != null) {
                            gridAdapter.setCoverMode(SOrderGridAdapter.CoverMode.CASCADE);
                            notifyUpdate();
                        }
                    }
                    break;
                case R.id.menu_cover_cascade_rotate:
                    if (currentView == VIEW_GRID) {
                        if (gridAdapter != null) {
                            gridAdapter.setCoverMode(SOrderGridAdapter.CoverMode.CASCADE_ROTATE);
                            notifyUpdate();
                        }
                    }
                    break;
                case R.id.menu_cover_grid:
                    if (currentView == VIEW_GRID) {
                        if (gridAdapter != null) {
                            gridAdapter.setCoverMode(SOrderGridAdapter.CoverMode.GRID);
                            notifyUpdate();
                        }
                    }
                    break;
            }
            return true;
        }

    };

    private void showSortPopup(View v) {
        PopupMenu menu = new PopupMenu(context, v);
        menu.getMenuInflater().inflate(R.menu.sort_order, menu.getMenu());
        menu.show();
        menu.setOnMenuItemClickListener(sortListener);
    }

    PopupMenu.OnMenuItemClickListener sortListener = new PopupMenu.OnMenuItemClickListener() {

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.menu_by_date:
                    if (currentOrderBy != PreferenceValue.ORDERBY_DATE) {
                        currentOrderBy = PreferenceValue.ORDERBY_DATE;
                        mPresenter.loadAllOrders(currentOrderBy);
                    }
                    break;
                case R.id.menu_by_name:
                    if (currentOrderBy != PreferenceValue.ORDERBY_NAME) {
                        currentOrderBy = PreferenceValue.ORDERBY_NAME;
                        mPresenter.loadAllOrders(currentOrderBy);
                    }
                    break;

                default:
                    break;
            }
            return true;
        }
    };

    public void reloadCascadeNumber() {
        scrollController.reloadCascadeNum(context);
    }

}
