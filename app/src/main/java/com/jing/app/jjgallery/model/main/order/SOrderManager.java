package com.jing.app.jjgallery.model.main.order;

import android.os.AsyncTask;
import android.os.Handler;

import com.jing.app.jjgallery.bean.order.SOrder;
import com.jing.app.jjgallery.bean.order.SOrderCount;
import com.jing.app.jjgallery.bean.order.STag;
import com.jing.app.jjgallery.config.Configuration;
import com.jing.app.jjgallery.config.DBInfor;
import com.jing.app.jjgallery.model.main.file.OnOrderItemMoveTrigger;
import com.jing.app.jjgallery.service.data.SqlConnection;
import com.jing.app.jjgallery.service.data.dao.SOrderDao;
import com.jing.app.jjgallery.service.data.impl.SOrderDaoImpl;
import com.jing.app.jjgallery.service.encrypt.EncrypterFactory;
import com.jing.app.jjgallery.service.encrypt.action.Encrypter;
import com.jing.app.jjgallery.util.DebugLog;

import java.io.File;
import java.util.Calendar;
import java.util.List;

/**
 * Created by JingYang on 2016/7/11 0011.
 * Description:
 */
public class SOrderManager {

    private SOrderCallback callback;

    public SOrderManager(SOrderCallback model) {
        this.callback = model;
    }

    /**
     * 加载全部order
     * 异步方法
     * @param orderby default is ORDERBY_NONE
     */
    public void loadAllOrders(int orderby) {
        new QueryTask().execute(orderby);
    }

    /**
     * 加载order内容
     * 同步方法
     * @param sOrder
     */
    public void loadOrderItems(SOrder sOrder) {
        try {
            SqlConnection.getInstance().connect(DBInfor.DB_PATH);
            SOrderDao dao = new SOrderDaoImpl();

            dao.queryOrderItems(sOrder, SqlConnection.getInstance().getConnection());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            SqlConnection.getInstance().close();
        }
    }

    /**
     * 删除列表
     * @param order
     * @return
     */
    public boolean deleteOrder(SOrder order) {
        boolean result = false;
        try {
            SqlConnection.getInstance().connect(DBInfor.DB_PATH);
            SOrderDao dao = new SOrderDaoImpl();

            result = dao.deleteOrder(order, SqlConnection.getInstance().getConnection());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            SqlConnection.getInstance().close();
        }

        return result;
    }

    /**
     * 检查order是否已存在
     * @param name
     * @return
     */
    public boolean isOrderExist(String name) {
        boolean result = false;
        try {
            SqlConnection.getInstance().connect(DBInfor.DB_PATH);
            SOrderDao dao = new SOrderDaoImpl();

            result = dao.isOrderExist(name, SqlConnection.getInstance().getConnection());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            SqlConnection.getInstance().close();
        }

        return result;
    }

    /**
     * 重命名order
     * @param order
     * @return
     */
    public boolean renameOrderName(SOrder order) {
        boolean result = false;
        try {
            SqlConnection.getInstance().connect(DBInfor.DB_PATH);
            SOrderDao dao = new SOrderDaoImpl();

            result = dao.updateOrder(order, SqlConnection.getInstance().getConnection());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            SqlConnection.getInstance().close();
        }
        return result;
    }

    /**
     * 加载tag list
     * 同步方法
     */
    public List<STag> loadTagList() {
        List<STag> list = null;
        try {
            SqlConnection.getInstance().connect(DBInfor.DB_PATH);
            SOrderDao dao = new SOrderDaoImpl();

            list = dao.loadTagList(SqlConnection.getInstance().getConnection());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            SqlConnection.getInstance().close();
        }
        return list;
    }

    /**
     * query tag
     * @param name
     */
    public STag queryTag(String name) {
        STag tag = null;
        try {
            SqlConnection.getInstance().connect(DBInfor.DB_PATH);
            SOrderDao dao = new SOrderDaoImpl();

            tag = dao.queryTag(name, SqlConnection.getInstance().getConnection());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            SqlConnection.getInstance().close();
        }
        return tag;
    }

    /**
     * add tag
     * @param name
     */
    public boolean addTag(String name) {
        boolean result = false;
        try {
            SqlConnection.getInstance().connect(DBInfor.DB_PATH);
            SOrderDao dao = new SOrderDaoImpl();

            result = dao.insertTag(name, SqlConnection.getInstance().getConnection());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            SqlConnection.getInstance().close();
        }
        return result;
    }

    /**
     * delete tag
     * @param sTag
     * @param list
     */
    public void deleteTag(STag sTag, List<SOrder> list) {
        try {
            SqlConnection.getInstance().connect(DBInfor.DB_PATH);
            SOrderDao dao = new SOrderDaoImpl();

            dao.deleteTag(sTag, SqlConnection.getInstance().getConnection());
            if (list != null && list.size() > 0) {
                dao.deleteAllOrdersInTag(sTag, SqlConnection.getInstance().getConnection());
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            SqlConnection.getInstance().close();
        }
    }

    /**
     * after add OK, set id for the order
     * @param order
     * @return
     */
    public boolean addOrder(SOrder order) {
        boolean result = false;

        try {
            SqlConnection.getInstance().connect(DBInfor.DB_PATH);
            SOrderDao dao = new SOrderDaoImpl();

            result = dao.insertOrder(order, SqlConnection.getInstance().getConnection());
            order.setId(dao.queryOrderIdAfterInsert(SqlConnection.getInstance().getConnection()));
            dao.insertOrderCount(order.getId(), SqlConnection.getInstance().getConnection());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            SqlConnection.getInstance().close();
        }
        return result;
    }

    /**
     * update order
     * 同步方法
     * @param sOrder
     */
    public boolean updateOrder(SOrder sOrder) {
        boolean result = false;
        try {
            SqlConnection.getInstance().connect(DBInfor.DB_PATH);
            SOrderDao dao = new SOrderDaoImpl();

            result = dao.updateOrder(sOrder, SqlConnection.getInstance().getConnection());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            SqlConnection.getInstance().close();
        }
        return result;
    }

    public boolean isOrderItemExist(String itemPath, int id) {
        boolean result = false;
        try {
            SqlConnection.getInstance().connect(DBInfor.DB_PATH);
            SOrderDao dao = new SOrderDaoImpl();

            result = dao.isOrderItemExist(itemPath, id, SqlConnection.getInstance().getConnection());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            SqlConnection.getInstance().close();
        }
        return result;
    }

    public boolean addItemToOrder(String path, SOrder order) {
        boolean result = false;
        try {
            SqlConnection.getInstance().connect(DBInfor.DB_PATH);
            SOrderDao dao = new SOrderDaoImpl();

            result = dao.insertOrderItem(order.getId(), path, SqlConnection.getInstance().getConnection());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            SqlConnection.getInstance().close();
        }

        if (result) {
            order.setItemNumber(order.getItemNumber() + 1);
        }
        return result;
    }

    public void moveToFolder(List<String> pathList, File targetFolder
            , final Handler handler, OnOrderItemMoveTrigger trigger) {
        try {
            SqlConnection.getInstance().connect(DBInfor.DB_PATH);
            SOrderDao dao = new SOrderDaoImpl();

            dao.updateOrderItemPath(pathList, targetFolder.getPath()
                    , trigger, SqlConnection.getInstance().getConnection());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            SqlConnection.getInstance().close();
        }

    }

    /**
     * delete item from order
     * @param position
     * @param order
     * @return
     */
    public boolean deleteItemFromOrder(int position, SOrder order) {
        boolean result = false;
        try {
            SqlConnection.getInstance().connect(DBInfor.DB_PATH);
            SOrderDao dao = new SOrderDaoImpl();

            result = dao.deleteOrderItem(order.getImgPathIdList().get(position), SqlConnection.getInstance().getConnection());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            SqlConnection.getInstance().close();
        }
        return result;
    }

    /**
     * query order by id
     * @param orderId
     * @return
     */
    public SOrder queryOrder(int orderId) {
        SOrder order = null;
        try {
            SqlConnection.getInstance().connect(DBInfor.DB_PATH);
            SOrderDao dao = new SOrderDaoImpl();

            order = dao.queryOrder(orderId, SqlConnection.getInstance().getConnection());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            SqlConnection.getInstance().close();
        }
        return order;
    }

    /**
     * 查询访问量前X的列表（总访问量)
     * @return
     */
    public List<SOrder> loadTopOrders(String column, int number) {
        List<SOrder> list = null;
        try {
            SqlConnection.getInstance().connect(DBInfor.DB_PATH);
            SOrderDao dao = new SOrderDaoImpl();

            list = dao.queryOrderAccessList(SqlConnection.getInstance().getConnection(), column, number);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            SqlConnection.getInstance().close();
        }
        return list;
    }

    public void insertOrderCount(int orderId) {
        try {
            SqlConnection.getInstance().connect(DBInfor.DB_PATH);
            SOrderDao dao = new SOrderDaoImpl();

            dao.insertOrderCount(orderId, SqlConnection.getInstance().getConnection());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            SqlConnection.getInstance().close();
        }
    }

    private class QueryTask extends AsyncTask<Integer, Void, List<SOrder>> {

        private int orderBy;

        @Override
        protected List<SOrder> doInBackground(Integer... params) {
            orderBy = params[0];
            List<SOrder> list = null;
            try {
                SqlConnection.getInstance().connect(DBInfor.DB_PATH);
                SOrderDao dao = new SOrderDaoImpl();

                list = dao.queryAllOrders(SqlConnection.getInstance().getConnection());
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                SqlConnection.getInstance().close();
            }
            return list;
        }
        @Override
        protected void onPostExecute(List<SOrder> list) {
            callback.onQueryAllOrders(list, orderBy);
            super.onPostExecute(list);
        }

    }

    /**
     * 查询order统计
     * @param orderId
     * @return
     */
    public SOrderCount queryOrderCount(int orderId) {
        SOrderCount count = null;
        try {
            SqlConnection.getInstance().connect(DBInfor.DB_PATH);
            SOrderDao dao = new SOrderDaoImpl();

            count = dao.queryOrderCount(orderId, SqlConnection.getInstance().getConnection());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            SqlConnection.getInstance().close();
        }
        return count;
    }

    /**
     * order访问统计
     * @param order
     */
    public void accessOrder(SOrder order) {
        try {
            SqlConnection.getInstance().connect(DBInfor.DB_PATH);
            SOrderDao dao = new SOrderDaoImpl();

            SOrderCount orderCount = dao.queryOrderCount(order.getId(), SqlConnection.getInstance().getConnection());
            order.setOrderCount(orderCount);

            // 总访问量+1
            orderCount.countAll ++;

            DebugLog.d("before access, last[year,month,week,day]:[" + orderCount.lastYear + "," + orderCount.lastMonth + "," + orderCount.lastWeek + "," + orderCount.lastDay + "]");
            Calendar calendar = Calendar.getInstance();
            int curYear = calendar.get(Calendar.YEAR);
            // 如果最近的记录不是今年（也就是现在执行accessOrder是在新的一年），那么重置本年/本月/本周/本日访问量为1，同时把lastYear改为今年，lastMonth/week/day重置为当月/周/天
            if (curYear != orderCount.lastYear) {
                orderCount.countYear = 1;
                orderCount.countMonth = 1;
                orderCount.countWeek = 1;
                orderCount.countDay = 1;
            }
            // 执行accessOrder是在本年，年访问量+1
            else {
                orderCount.countYear ++;

                boolean checkday = false;

                int month = calendar.get(Calendar.MONTH) + 1;
                // 执行accessOrder是在新的一月（不一定是新的一周，肯定是新的一天），重置本月/本日访问量为1，同时把lastMonth改为本月，lastDay重置为当天
                if (month != orderCount.lastMonth) {
                    orderCount.countMonth = 1;
                    orderCount.countDay = 1;
                }
                // 执行accessOrder是在本月，月访问量+1
                else {
                    orderCount.countMonth ++;
                    checkday = true;
                }

                // 执行accessOrder是在新的一周，重置本周/本日访问量为1，同时把lastWeek改为本周，lastDay重置为当天
                int week = calendar.get(Calendar.WEEK_OF_YEAR);
                if (week != orderCount.lastWeek) {
                    orderCount.countWeek = 1;
                    orderCount.countDay = 1;
                }
                // 执行accessOrder是在本周，周访问量+1
                else {
                    orderCount.countWeek ++;

                    checkday = true;
                }

                if (checkday) {
                    // 执行accessOrder是在新的一天，重置本日访问量为1，同时把lastDay改为当天
                    int day = calendar.get(Calendar.DAY_OF_MONTH);
                    if (day != orderCount.lastDay) {
                        orderCount.countDay = 1;
                    }
                    // 执行accessOrder是在当天，日访问量+1
                    else {
                        orderCount.countDay ++;
                    }
                }
            }
            // 重置最近访问记录为此刻日期
            orderCount.lastYear = calendar.get(Calendar.YEAR);
            orderCount.lastMonth = calendar.get(Calendar.MONTH) + 1;
            orderCount.lastWeek = calendar.get(Calendar.WEEK_OF_YEAR);
            orderCount.lastDay = calendar.get(Calendar.DAY_OF_MONTH);

            DebugLog.d("[all,year,month,week,day]:[" + orderCount.countAll + "," + orderCount.countYear + "," + orderCount.countMonth + "," + orderCount.countWeek + "," + orderCount.countDay + "]");
            DebugLog.d("after access, last[year,month,week,day]:[" + "," + orderCount.lastYear + "," + orderCount.lastMonth + "," + orderCount.lastWeek + "," + orderCount.lastDay + "]");
            dao.saveOrderCount(orderCount, SqlConnection.getInstance().getConnection());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            SqlConnection.getInstance().close();
        }
    }

    /**
     * 将order还原为未加密目录
     * @param order
     * @return
     */
    public boolean decipherOrderAsFolder(SOrder order) {
        boolean result = true;
        try {
            SqlConnection.getInstance().connect(DBInfor.DB_PATH);
            SOrderDao dao = new SOrderDaoImpl();

            if (order.getImgPathList() == null || order.getImgPathList().size() == 0) {
                dao.queryOrderItems(order, SqlConnection.getInstance().getConnection());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            SqlConnection.getInstance().close();
        }

        if (order.getImgPathList() != null) {
            Encrypter encrypter = EncrypterFactory.create();
            String target = Configuration.APP_DIR_IMG_SAVEAS + "/" + order.getName();
            File file = new File(target);
            if (!file.exists()) {
                file.mkdir();
            }
            for (String path:order.getImgPathList()) {
                file = new File(path);
                encrypter.restore(file, target);
            }
        }

        return result;
    }
}
