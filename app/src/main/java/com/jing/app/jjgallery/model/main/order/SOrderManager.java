package com.jing.app.jjgallery.model.main.order;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

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

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
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

            SOrderCount orderCount = order.getOrderCount();
            if (orderCount == null) {
                orderCount = dao.queryOrderCount(order.getId(), SqlConnection.getInstance().getConnection());
                order.setOrderCount(orderCount);
            }
            orderCount.countAll ++;
            Calendar calendar = Calendar.getInstance();
            if (calendar.get(Calendar.YEAR) != orderCount.lastYear) {
                orderCount.countYear = 1;
                orderCount.countMonth = 1;
                orderCount.countWeek = 1;
                orderCount.countDay = 1;
            }
            else {
                orderCount.countYear ++;
                if (calendar.get(Calendar.MONTH) + 1 != orderCount.lastMonth) {
                    orderCount.countMonth = 1;
                    orderCount.countDay = 1;
                }
                else {
                    orderCount.countMonth ++;
                }

                if (calendar.get(Calendar.WEEK_OF_YEAR) != orderCount.lastWeek) {
                    orderCount.countWeek = 1;
                    orderCount.countDay = 1;
                }
                else {
                    orderCount.countWeek ++;
                    if (calendar.get(Calendar.DAY_OF_MONTH) != orderCount.lastDay) {
                        orderCount.countDay = 1;
                    }
                    else {
                        orderCount.countDay ++;
                    }
                }
            }
            orderCount.lastYear = calendar.get(Calendar.YEAR);
            orderCount.lastMonth = calendar.get(Calendar.MONTH) + 1;
            orderCount.lastWeek = calendar.get(Calendar.WEEK_OF_YEAR);
            orderCount.lastDay = calendar.get(Calendar.DAY_OF_MONTH);

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
