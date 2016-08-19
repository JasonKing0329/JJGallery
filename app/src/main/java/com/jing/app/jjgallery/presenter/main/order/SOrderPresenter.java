package com.jing.app.jjgallery.presenter.main.order;

import android.content.Context;

import com.jing.app.jjgallery.BasePresenter;
import com.jing.app.jjgallery.bean.order.SOrder;
import com.jing.app.jjgallery.bean.order.SOrderCount;
import com.jing.app.jjgallery.bean.order.STag;
import com.jing.app.jjgallery.config.DBInfor;
import com.jing.app.jjgallery.config.PreferenceValue;
import com.jing.app.jjgallery.model.main.order.SOrderCallback;
import com.jing.app.jjgallery.model.main.order.SOrderHorIndexCreator;
import com.jing.app.jjgallery.model.main.order.SOrderManager;
import com.jing.app.jjgallery.presenter.main.SettingProperties;
import com.jing.app.jjgallery.viewsystem.main.order.HorizontalIndexView;
import com.jing.app.jjgallery.viewsystem.main.order.ISOrderDataCallback;
import com.jing.app.jjgallery.viewsystem.main.order.ISOrderView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by JingYang on 2016/7/11 0011.
 * Description:
 */
public class SOrderPresenter extends BasePresenter implements SOrderCallback {

    /**
     * SOrderActivity回调
     */
    private ISOrderView sorderView;
    /**
     * 数据操作部分的回调
     */
    private ISOrderDataCallback dataCallback;

    /**
     * SOrder相关的数据库/File I/O操作模型
     */
    private SOrderManager sOrderManager;
    /**
     * Grid page横向页码操作模型
     */
    private SOrderHorIndexCreator indexCreator;

    /**
     * 当前加载的全部列表
     */
    private List<SOrder> orderList;

    public SOrderPresenter(ISOrderView sorderView) {
        this.sorderView = sorderView;
        sOrderManager = new SOrderManager(this);
        indexCreator = new SOrderHorIndexCreator(sOrderManager);
    }

    /**
     * 判断SOrder的启动界面
     * @param context
     */
    public void startSOrderPage(Context context) {
        String mode = SettingProperties.getSOrderDefaultMode(context);
        if (mode.equals(PreferenceValue.VALUE_SORDER_VIEW_THUMB)) {
            sorderView.onThumbPage();
        }
        else if (mode.equals(PreferenceValue.VALUE_SORDER_VIEW_INDEX)) {
            sorderView.onIndexPage();
        }
        else if (mode.equals(PreferenceValue.VALUE_SORDER_VIEW_ACCESS)) {
            sorderView.onAccessCountPage();
        }
        else {
            sorderView.onGridPage();
        }
    }

    /**
     * 切换至Index page
     */
    public void switchToIndexPage() {
        sorderView.onIndexPage();
    }

    /**
     * 切换至Thumb page
     */
    public void switchToThumbPage() {
        sorderView.onThumbPage();
    }

    /**
     * 切换至Grid page
     */
    public void switchToGridPage() {
        sorderView.onGridPage();
    }

    /**
     * 设置数据操作界面回调
     */
    public void setDataCallback(ISOrderDataCallback dataCallback) {
        this.dataCallback = dataCallback;
    }

    /**
     * 获取当前全部列表
     */
    public List<SOrder> getOrderList() {
        return orderList;
    }


    /**
     * 加载全部列表
     * 异步操作
     * @param orderBy see PreferenceValue.ORDERBY_XXX
     */
    public void loadAllOrders(int orderBy) {
        sOrderManager.loadAllOrders(orderBy);
    }

    public void loadOrderItems(SOrder order) {
        sOrderManager.loadOrderItems(order);
    }
    @Override
    /**
     * loadAllOrders回调
     * @param list 全部列表数据
     * @param orderBy see PreferenceValue.ORDERBY_XXX
     */
    public void onQueryAllOrders(List<SOrder> list, int orderBy) {
        orderList = list;
        if (list != null) {
            if (orderBy == PreferenceValue.ORDERBY_NAME) {
                Collections.sort(list, new NameComparator());
            }
            else if (orderBy == PreferenceValue.ORDERBY_DATE) {
                Collections.reverse(list);
            }
            else if (orderBy == PreferenceValue.ORDERBY_ITEMNUMBER) {
                for (SOrder order:list) {
                    sOrderManager.loadOrderItems(order);
                }
                Collections.sort(list, new ItemNumberComparator());
            }
            indexCreator.createIndex(list);

            setOrderCover(list);
        }
        dataCallback.onQueryAllOrders(list);
    }

    /**
     * 加载列表的封面，根据不同的封面模式内容会不同
     * @param list 待加载的列表
     */
    private void setOrderCover(List<SOrder> list) {

        int mode = SettingProperties.getSOrderCoverMode(sorderView.getContext());
        switch (mode) {
            case PreferenceValue.SORDER_COVER_SINGLE:
                for (SOrder order:list) {
                    List<String> covers = new ArrayList<>();
                    covers.add(order.getCoverPath());
                    order.setCoverList(covers);
                }
                break;
            case PreferenceValue.SORDER_COVER_GRID:
                for (SOrder order:list) {
                    List<String> covers = loadSOrderCovers(order, 4);
                    order.setCoverList(covers);
                }
                break;
            case PreferenceValue.SORDER_COVER_CASCADE:
            case PreferenceValue.SORDER_COVER_CASCADE_ROTATE:
                int number = SettingProperties.getCascadeCoverNumber(sorderView.getContext());
                for (SOrder order:list) {
                    List<String> covers = loadSOrderCovers(order, number);
                    order.setCoverList(covers);
                }
                break;
        }
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

    /**
     * 查询访问量前X的列表（总访问量)
     * @return
     */
    public List<SOrder> loadTopTotalList(int number) {
        return  sOrderManager.loadTopOrders(DBInfor.TOC_ALL, number);
    }

    /**
     * 查询访问量前X的列表（年访问量)
     * @return
     */
    public List<SOrder> loadTopYearList(int number) {
        return  sOrderManager.loadTopOrders(DBInfor.TOC_YEAR, number);
    }

    /**
     * 查询访问量前X的列表（月访问量)
     * @return
     */
    public List<SOrder> loadTopMonthList(int number) {
        return  sOrderManager.loadTopOrders(DBInfor.TOC_MONTH, number);
    }

    /**
     * 查询访问量前X的列表（周访问量)
     * @return
     */
    public List<SOrder> loadTopWeekList(int number) {
        return  sOrderManager.loadTopOrders(DBInfor.TOC_WEEK, number);
    }

    /**
     * 查询访问量前X的列表（日访问量)
     * @return
     */
    public List<SOrder> loadTopDayList(int number) {
        return  sOrderManager.loadTopOrders(DBInfor.TOC_DAY, number);
    }

    /**
     * 获取访问量统计信息
     * @param order
     * @return
     */
    public String getAccessCountMessage(SOrder order) {
        SOrderCount orderCount = queryOrderCount(order.getId());
        order.setOrderCount(orderCount);

        Calendar calendar = Calendar.getInstance();
        if (orderCount == null) {
            sOrderManager.insertOrderCount(order.getId());
            orderCount = new SOrderCount();
            orderCount.orderId = order.getId();
        }

        // 符合当年/月/周/日情况才显示统计数
        int year;
        int month;
        int week;
        int day;
        if (calendar.get(Calendar.YEAR) == orderCount.lastYear) {
            year = orderCount.countYear;
            if (calendar.get(Calendar.MONTH) + 1 == orderCount.lastMonth) {
                month = orderCount.countMonth;
            }
            else {
                month = 0;
            }
            if (calendar.get(Calendar.WEEK_OF_YEAR) == orderCount.lastWeek) {
                week = orderCount.countWeek;
                if (calendar.get(Calendar.DAY_OF_MONTH) == orderCount.lastDay) {
                    day = orderCount.countDay;
                }
                else {
                    day = 0;
                }
            }
            else {
                week = 0;
                day = 0;
            }
        }
        else {
            year = 0;
            month = 0;
            week = 0;
            day = 0;
        }

        StringBuffer buffer = new StringBuffer("总访问量： ");
        buffer.append(orderCount.countAll)
                .append("\n").append("年访问量:  ").append(year)
                .append("\n").append("月访问量:  ").append(month)
                .append("\n").append("一周访问量:  ").append(week)
                .append("\n").append("今日访问量:  ").append(day)
                .append("\n").append("上次访问时间:  ").append(orderCount.lastYear).append("-").append(orderCount.lastMonth).append("-").append(orderCount.lastDay);
        return buffer.toString();
    }

    /**
     * 按名称排序
     */
    private class NameComparator implements Comparator<SOrder> {

        @Override
        public int compare(SOrder lhs, SOrder rhs) {
            return lhs.getName().toLowerCase().compareTo(rhs.getName().toLowerCase());
        }
    }

    /**
     * 按item numbers排序
     */
    private class ItemNumberComparator implements Comparator<SOrder> {

        @Override
        public int compare(SOrder lhs, SOrder rhs) {
            return rhs.getItemNumber() - lhs.getItemNumber();
        }
    }

    /**
     * 加载Expandable list的preview图片
     * @param order 待加载列表
     * @param number 图片数量
     * @return 图片地址
     */
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

    /**
     * load available covers for order
     * @param order order to load
     * @param number max image number to load
     * @return
     */
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

    /**
     * load tag list
     */
    public List<STag> loadTagList() {
        return sOrderManager.loadTagList();
    }

    /**
     * delete tag
     */
    public void deleteTag(STag sTag, List<SOrder> list) {
        sOrderManager.deleteTag(sTag, list);
    }

    /**
     * order访问统计
     * @param order
     */
    public void accessOrder(SOrder order) {
        sOrderManager.accessOrder(order);
    }

    /**
     * 查询order访问统计数据
     * @param orderId
     * @return
     */
    public SOrderCount queryOrderCount(int orderId) {
        return sOrderManager.queryOrderCount(orderId);
    }

    /**
     * 将order还原为未加密目录
     * @param order order to decipher
     * @return success or fail
     */
    public boolean decipherOrderAsFolder(SOrder order) {
        return sOrderManager.decipherOrderAsFolder(order);
    }

    /**
     * 初始化水平页面
     */
    public void initIndexCreator(Context context) {

        indexCreator.setPageMaxItem(SettingProperties.getSOrderPageNumber(context));
    }

    public List<HorizontalIndexView.IndexItem> createIndex(int mode) {
        return indexCreator.createIndex(orderList, mode);
    }

    public List<SOrder> getPageItem(int index) throws HorizontalIndexView.PageIndexOutOfBoundsException {
        return indexCreator.getPageItem(orderList, index);
    }

}
