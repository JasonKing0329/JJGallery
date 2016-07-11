package com.jing.app.jjgallery.model.main.order;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.jing.app.jjgallery.bean.order.SOrder;
import com.jing.app.jjgallery.bean.order.STag;
import com.jing.app.jjgallery.config.DBInfor;
import com.jing.app.jjgallery.model.main.file.OnOrderItemMoveTrigger;
import com.jing.app.jjgallery.service.data.SqlConnection;
import com.jing.app.jjgallery.service.data.dao.SOrderDao;
import com.jing.app.jjgallery.service.data.impl.SOrderDaoImpl;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
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
     */
    public void loadAllOrders() {
        new QueryTask().execute();
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
     * add tag
     * @param name
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

    private class QueryTask extends AsyncTask<Void, Void, List<SOrder>> {

        @Override
        protected List<SOrder> doInBackground(Void... params) {
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
            callback.onQueryAllOrders(list);
            super.onPostExecute(list);
        }

    }
}
