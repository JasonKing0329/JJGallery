package com.jing.app.jjgallery.service.data.impl;

import com.jing.app.jjgallery.bean.order.SOrder;
import com.jing.app.jjgallery.bean.order.SOrderCount;
import com.jing.app.jjgallery.bean.order.STag;
import com.jing.app.jjgallery.config.DBInfor;
import com.jing.app.jjgallery.model.main.file.OnOrderItemMoveTrigger;
import com.jing.app.jjgallery.service.data.dao.SOrderDao;
import com.jing.app.jjgallery.util.DebugLog;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by JingYang on 2016/7/11 0011.
 * Description:
 */
public class SOrderDaoImpl implements SOrderDao {
    @Override
    public List<SOrder> queryAllOrders(Connection connection) {
        List<SOrder> list = null;
        Statement stmt = null;
        try {
            stmt = connection.createStatement();
            ResultSet set = null;

            set = stmt.executeQuery("SELECT * FROM " + DBInfor.TABLE_ORDER);
            SOrder order = null;
            while (set.next()) {
                if (list == null) {
                    list = new ArrayList<SOrder>();
                }
                order = new SOrder();
                order.setId(set.getInt(DBInfor.NUM_TO_COL_ID));
                order.setName(set.getString(DBInfor.NUM_TO_COL_NAME));
                order.setCoverPath(set.getString(DBInfor.NUM_TO_COL_COVER));
                order.setTag(new STag(set.getInt(DBInfor.NUM_TO_COL_TAGID), null));
                list.add(order);
            }
            if (list != null) {
                set.close();
                for (int i = 0; i < list.size(); i ++) {
                    order = list.get(i);
                    set = stmt.executeQuery("SELECT COUNT (" + DBInfor.TOL_COL_ID + ") FROM "
                            + DBInfor.TABLE_ORDER_LIST + " WHERE " + DBInfor.TOL_COL_OID + " = " + order.getId());
                    if (set.next()) {
                        order.setItemNumber(set.getInt(1));
                    }
                    set.close();
                }
            }
            else {
                set.close();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    @Override
    public boolean queryOrderItems(SOrder order, Connection connection) {
        Statement stmt = null;
        try {
            stmt = connection.createStatement();
            ResultSet set = null;

            set = stmt.executeQuery("SELECT * FROM " + DBInfor.TABLE_ORDER_LIST + " WHERE " + DBInfor.TOL_COL_OID + " = " + order.getId());

            if (order.getImgPathIdList() != null) {
                order.getImgPathList().clear();
                order.getImgPathIdList().clear();
            }
            while (set.next()) {
                if (order.getImgPathList() == null) {
                    order.setImgPathList(new ArrayList<String>());
                    order.setImgPathIdList(new ArrayList<Integer>());
                }
                order.getImgPathIdList().add(set.getInt(DBInfor.NUM_TOL_COL_ID));
                order.getImgPathList().add(set.getString(DBInfor.NUM_TOL_COL_PATH));
            }
            if (order.getImgPathList() != null) {
                order.setItemNumber(order.getImgPathList().size());
            }
            set.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public List<String> queryOrderItemsByName(String name, Connection connection) {

        SOrder order = queryOrderByName(name, connection);
        if (order == null) {
            return null;
        }

        Statement stmt = null;
        try {
            stmt = connection.createStatement();
            ResultSet set = null;

            set = stmt.executeQuery("SELECT * FROM " + DBInfor.TABLE_ORDER_LIST + " WHERE "
                    + DBInfor.TOL_COL_OID + " = " + order.getId());

            if (order.getImgPathIdList() != null) {
                order.getImgPathList().clear();
                order.getImgPathIdList().clear();
            }
            while (set.next()) {
                if (order.getImgPathList() == null) {
                    order.setImgPathList(new ArrayList<String>());
                    order.setImgPathIdList(new ArrayList<Integer>());
                }
                order.getImgPathIdList().add(set.getInt(DBInfor.NUM_TOL_COL_ID));
                order.getImgPathList().add(set.getString(DBInfor.NUM_TOL_COL_PATH));
            }
            if (order.getImgPathList() != null) {
                order.setItemNumber(order.getImgPathList().size());
            }
            set.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return order.getImgPathList();
    }

    @Override
    public List<STag> loadTagList(Connection connection) {
        List<STag> list = null;
        Statement stmt = null;
        try {
            stmt = connection.createStatement();
            ResultSet set = null;
            String sql = "SELECT * FROM " + DBInfor.TABLE_TAG
                    + " ORDER BY " + DBInfor.TT_COL_TAG + " ASC";
            set = stmt.executeQuery(sql);
            STag tag = null;
            while (set.next()) {
                if (list == null) {
                    list = new ArrayList<STag>();
                }
                tag = new STag(set.getInt(DBInfor.NUM_TT_COL_ID), set.getString(DBInfor.NUM_TT_COL_TAG));
                list.add(tag);
            }
            set.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return list;
    }
    @Override
    public STag queryTag(String tName, Connection connection) {
        Statement stmt = null;
        STag tag = null;
        try {
            stmt = connection.createStatement();
            ResultSet set = null;

            set = stmt.executeQuery("SELECT * FROM " + DBInfor.TABLE_TAG + " WHERE "
                    + DBInfor.TT_COL_TAG + " = '" + tName + "'");

            if (set.next()) {
                tag = new STag(set.getInt(DBInfor.NUM_TT_COL_ID), tName);
            }
            set.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return tag;
    }
    @Override
    public boolean insertTag(String tName, Connection connection) {
        if (connection != null && tName != null) {
            String sql = "INSERT INTO " + DBInfor.TABLE_TAG
                    + "(" + DBInfor.TT_COL_TAG + ")" +
                    " VALUES(?)";
            PreparedStatement stmt = null;
            try {
                stmt = connection.prepareStatement(sql);
                stmt.setString(1, tName);

                stmt.executeUpdate();
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (NumberFormatException e) {
                e.printStackTrace();
            } finally {
                if (stmt != null) {
                    try {
                        stmt.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return false;
    }
    @Override
    public void deleteTag(STag sTag, Connection connection) {
        if (connection != null && sTag != null) {
            String sql = "DELETE FROM " + DBInfor.TABLE_TAG
                    + " WHERE " + DBInfor.TT_COL_ID + " = " + sTag.getId();
            Statement stmt = null;
            try {
                stmt = connection.createStatement();
                stmt.executeUpdate(sql);

            } catch (SQLException e) {
                e.printStackTrace();
            } catch (NumberFormatException e) {
                e.printStackTrace();
            } finally {
                if (stmt != null) {
                    try {
                        stmt.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * 删除整个 order的内容
     * @param tag
     * @param connection
     */
    public void deleteAllOrdersInTag(STag tag, Connection connection) {

        if (connection != null && tag != null) {
            String sql = "DELETE FROM " + DBInfor.TABLE_ORDER
                    + " WHERE " + DBInfor.TO_COL_TAGID + " = " + tag.getId();
            Statement stmt = null;
            try {
                stmt = connection.createStatement();
                stmt.executeUpdate(sql);

            } catch (SQLException e) {
                e.printStackTrace();
            } catch (NumberFormatException e) {
                e.printStackTrace();
            } finally {
                if (stmt != null) {
                    try {
                        stmt.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    public boolean isOrderExist(String name, Connection connection) {
        Statement stmt = null;
        try {
            stmt = connection.createStatement();
            ResultSet set = null;

            set = stmt.executeQuery("SELECT * FROM " + DBInfor.TABLE_ORDER + " WHERE " + DBInfor.TO_COL_NAME + " = '" + name + "'");

            if (set.next()) {
                return true;
            }
            set.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    @Override
    public boolean insertOrder(SOrder order, Connection connection) {

        if (connection != null && order != null) {
            String sql = "INSERT INTO " + DBInfor.TABLE_ORDER
                    + "(" + DBInfor.TO_COL_NAME + "," + DBInfor.TO_COL_COVER + "," + DBInfor.TO_COL_TAGID + ")" +
                    " VALUES(?,?,?)";
            PreparedStatement stmt = null;
            try {
                stmt = connection.prepareStatement(sql);
                stmt.setString(1, order.getName());
                stmt.setString(2, order.getCoverPath());
                stmt.setInt(3, order.getTag().getId());

                stmt.executeUpdate();
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (NumberFormatException e) {
                e.printStackTrace();
            } finally {
                if (stmt != null) {
                    try {
                        stmt.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return false;
    }

    @Override
    public int queryOrderIdAfterInsert(Connection connection) {
        int id = -1;
        Statement stmt = null;
        try {
            stmt = connection.createStatement();
            ResultSet set = null;

            set = stmt.executeQuery("SELECT seq FROM " + DBInfor.TABLE_SEQUENCE
                    + " WHERE " + DBInfor.TS_COL_NAME + " = '" + DBInfor.TABLE_ORDER + "'");

            if (set.next()) {
                id = set.getInt(1);
            }
            set.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return id;
    }

    @Override
    public boolean saveOrderCount(SOrderCount count, Connection connection) {

        if (connection != null && count != null) {
            StringBuffer buffer = new StringBuffer("UPDATE ");
            buffer.append(DBInfor.TABLE_ORDER_COUNT).append(" SET ")
                    .append(DBInfor.TOC_COL[1]).append(" = ").append(count.countAll)
                    .append(", ").append(DBInfor.TOC_COL[2]).append(" = ").append(count.countYear)
                    .append(", ").append(DBInfor.TOC_COL[3]).append(" = ").append(count.countMonth)
                    .append(", ").append(DBInfor.TOC_COL[4]).append(" = ").append(count.countWeek)
                    .append(", ").append(DBInfor.TOC_COL[5]).append(" = ").append(count.countDay)
                    .append(", ").append(DBInfor.TOC_COL[6]).append(" = ").append(count.lastYear)
                    .append(", ").append(DBInfor.TOC_COL[7]).append(" = ").append(count.lastMonth)
                    .append(", ").append(DBInfor.TOC_COL[8]).append(" = ").append(count.lastWeek)
                    .append(", ").append(DBInfor.TOC_COL[9]).append(" = ").append(count.lastDay)
                    .append(" WHERE ").append(DBInfor.TOC_COL[0]).append(" = ").append(count.orderId);

            Statement stmt = null;
            try {
                String sql = buffer.toString();
                stmt = connection.createStatement();
                stmt.executeUpdate(sql);
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (NumberFormatException e) {
                e.printStackTrace();
            } finally {
                if (stmt != null) {
                    try {
                        stmt.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return false;
    }

    @Override
    public SOrderCount queryOrderCount(int orderId, Connection connection) {
        Statement stmt = null;
        SOrderCount orderCount = null;
        try {
            stmt = connection.createStatement();
            ResultSet set = stmt.executeQuery("SELECT * FROM " + DBInfor.TABLE_ORDER_COUNT
                    + " WHERE " + DBInfor.TOC_COL[0] + " = " + orderId);

            if (set.next()) {
                orderCount = getSorderCountFromSet(set);
            }
            set.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return orderCount;
    }

    private SOrderCount getSorderCountFromSet(ResultSet set) throws SQLException {
        SOrderCount orderCount = new SOrderCount();
        orderCount.orderId = set.getInt(DBInfor.TOC_COL[0]);
        orderCount.countAll = set.getInt(DBInfor.TOC_COL[1]);
        orderCount.countYear = set.getInt(DBInfor.TOC_COL[2]);
        orderCount.countMonth = set.getInt(DBInfor.TOC_COL[3]);
        orderCount.countWeek = set.getInt(DBInfor.TOC_COL[4]);
        orderCount.countDay = set.getInt(DBInfor.TOC_COL[5]);
        orderCount.lastYear = set.getInt(DBInfor.TOC_COL[6]);
        orderCount.lastMonth = set.getInt(DBInfor.TOC_COL[7]);
        orderCount.lastWeek = set.getInt(DBInfor.TOC_COL[8]);
        orderCount.lastDay = set.getInt(DBInfor.TOC_COL[9]);
        return orderCount;
    }

    @Override
    public boolean insertOrderCount(int orderId, Connection connection) {

        if (connection != null) {
            Statement stmt = null;
            try {
                String sql = "INSERT INTO " + DBInfor.TABLE_ORDER_COUNT
                        + " VALUES(" + orderId + ",0,0,0,0,0,0,0,0,0)";
                stmt = connection.createStatement();
                stmt.executeUpdate(sql);
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (NumberFormatException e) {
                e.printStackTrace();
            } finally {
                if (stmt != null) {
                    try {
                        stmt.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return false;
    }

    @Override
    public boolean updateOrder(SOrder order, Connection connection) {

        if (connection != null && order != null) {
            String sql = "UPDATE " + DBInfor.TABLE_ORDER + " SET " + DBInfor.TO_COL_NAME + " = '" + order.getName()
                    + "', " + DBInfor.TO_COL_COVER + " = '" + order.getCoverPath()
                    + "' WHERE " + DBInfor.TO_COL_ID + " = " + order.getId();;
            Statement stmt = null;
            try {
                stmt = connection.createStatement();
                stmt.executeUpdate(sql);
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (NumberFormatException e) {
                e.printStackTrace();
            } finally {
                if (stmt != null) {
                    try {
                        stmt.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return false;
    }

    @Override
    public boolean deleteOrder(SOrder order, Connection connection) {

        if (connection != null && order != null) {
            String sql = "DELETE FROM " + DBInfor.TABLE_ORDER
                    + " WHERE " + DBInfor.TO_COL_ID + " = " + order.getId();
            Statement stmt = null;
            try {
                stmt = connection.createStatement();
                stmt.executeUpdate(sql);

                sql = "DELETE FROM " + DBInfor.TABLE_ORDER_LIST
                        + " WHERE " + DBInfor.TOL_COL_OID + " = " + order.getId();
                stmt.executeUpdate(sql);
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (NumberFormatException e) {
                e.printStackTrace();
            } finally {
                if (stmt != null) {
                    try {
                        stmt.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return false;
    }

    @Override
    public boolean isOrderItemExist(String path, int orderId, Connection connection) {
        Statement stmt = null;
        try {
            stmt = connection.createStatement();
            ResultSet set = null;

            set = stmt.executeQuery("SELECT * FROM " + DBInfor.TABLE_ORDER_LIST + " WHERE "
                    + DBInfor.TOL_COL_OID + " = " + orderId + " AND "+ DBInfor.TOL_COL_PATH + " = '" + path + "'");

            if (set.next()) {
                return true;
            }
            set.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public boolean insertOrderItem(int orderId, String itemPath, Connection connection) {

        if (connection != null && itemPath != null) {
            String sql = "INSERT INTO " + DBInfor.TABLE_ORDER_LIST
                    + "(" + DBInfor.TOL_COL_OID + "," + DBInfor.TOL_COL_PATH + ")" +
                    " VALUES(?,?)";
            PreparedStatement stmt = null;
            try {
                stmt = connection.prepareStatement(sql);
                stmt.setInt(1, orderId);
                stmt.setString(2, itemPath);

                stmt.executeUpdate();
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (NumberFormatException e) {
                e.printStackTrace();
            } finally {
                if (stmt != null) {
                    try {
                        stmt.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return false;
    }

    @Override
    public boolean deleteOrderItem(int itemId, Connection connection) {

        if (connection != null) {
            String sql = "DELETE FROM " + DBInfor.TABLE_ORDER_LIST
                    + " WHERE " + DBInfor.TOL_COL_ID + " = " + itemId;
            Statement stmt = null;
            try {
                stmt = connection.createStatement();
                stmt.executeUpdate(sql);
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (NumberFormatException e) {
                e.printStackTrace();
            } finally {
                if (stmt != null) {
                    try {
                        stmt.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return false;
    }

    @Override
    public boolean updateOrderItemPath(List<String> originPathList, String targetPath
            , OnOrderItemMoveTrigger trigger, Connection connection) {

        if (connection != null) {


            StringBuffer buffer = null;
            String name = null;
            String target = null;
            PreparedStatement pStmt = null;
            try {
                connection.setAutoCommit(false);
                int index = 1;
                for (String originPath:originPathList) {

                    boolean isFinish = index == originPathList.size();

                    //update table fe_order_list(ol_item_path)
                    buffer = new StringBuffer();
                    name = originPath.substring(originPath.lastIndexOf("/"));//include '/' symbol
                    target = targetPath + name;

                    //不允许移动至原目录
                    if (originPath.equals(target)) {
                        trigger.onNotSupport(originPath, isFinish);
                        index ++;
                        continue;
                    }

                    buffer.append("UPDATE ").append(DBInfor.TABLE_ORDER_LIST).append(" SET ")
                            .append(DBInfor.TOL_COL_PATH ).append(" = '").append(target)
                            .append("' WHERE ").append(DBInfor.TOL_COL_PATH).append(" = '")
                            .append(originPath).append("'");
                    String sql = buffer.toString();
                    pStmt = connection.prepareStatement(sql);
                    pStmt.executeUpdate();

                    //update table fe_orders(o_cover)
                    buffer = new StringBuffer();
                    buffer.append("UPDATE ").append(DBInfor.TABLE_ORDER).append(" SET ")
                            .append(DBInfor.TO_COL_COVER ).append(" = '").append(target)
                            .append("' WHERE ").append(DBInfor.TO_COL_COVER).append(" = '")
                            .append(originPath).append("'");sql = buffer.toString();
                    sql = buffer.toString();
                    pStmt = connection.prepareStatement(sql);
                    pStmt.executeUpdate();

                    trigger.onTrigger(originPath, target, isFinish);

                    index ++;
                }
                connection.commit();
                //Notice:
                //it's strange that once use transaction, no matter how I
                //try to close connection or statement, when this operation over
                //try other connection operation, it will happen 'database
                //is locked' exception, application will be locked till to die
                //only setAutoCommit(true) the lock will not happened
                connection.setAutoCommit(true);

                return true;
            } catch (SQLException e) {
                e.printStackTrace();
                try {
                    connection.rollback();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
                try {
                    connection.rollback();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            } finally {
                try {
                    if (pStmt != null) {
                        pStmt.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        }
        return false;
    }

    @Override
    public boolean deleteItemFromAllOrders(String path, Connection connection) {
        if (connection != null) {
            String sql = "DELETE FROM " + DBInfor.TABLE_ORDER_LIST
                    + " WHERE " + DBInfor.TOL_COL_PATH + " = '" + path + "'";
            Statement stmt = null;
            try {
                stmt = connection.createStatement();
                stmt.executeUpdate(sql);
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (NumberFormatException e) {
                e.printStackTrace();
            } finally {
                if (stmt != null) {
                    try {
                        stmt.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return false;
    }

    @Override
    public SOrder queryOrder(int orderId, Connection connection) {
        Statement stmt = null;
        SOrder order = null;
        try {
            stmt = connection.createStatement();
            ResultSet set = null;

            set = stmt.executeQuery("SELECT * FROM " + DBInfor.TABLE_ORDER
                    + " WHERE " + DBInfor.TO_COL_ID + " = " + orderId);

            if (set.next()) {
                order = new SOrder();
                order.setId(orderId);
                order.setName(set.getString(DBInfor.NUM_TO_COL_NAME));
                order.setCoverPath(set.getString(DBInfor.NUM_TO_COL_COVER));
                order.setTag(new STag(set.getInt(DBInfor.NUM_TO_COL_TAGID), null));
            }
            set.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return order;
    }

    @Override
    public SOrder queryOrderByName(String name, Connection connection) {
        Statement stmt = null;
        SOrder order = null;
        try {
            stmt = connection.createStatement();
            ResultSet set = null;

            set = stmt.executeQuery("SELECT * FROM " + DBInfor.TABLE_ORDER
                    + " WHERE " + DBInfor.TO_COL_NAME + " = '" + name + "'");

            if (set.next()) {
                order = new SOrder();
                order.setId(set.getInt(DBInfor.NUM_TO_COL_ID));
                order.setName(set.getString(DBInfor.NUM_TO_COL_NAME));
                order.setCoverPath(set.getString(DBInfor.NUM_TO_COL_COVER));
                order.setTag(new STag(set.getInt(DBInfor.NUM_TO_COL_TAGID), null));
            }
            set.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return order;
    }

    @Override
    public List<SOrder> queryOrderAccessList(Connection connection, String column, int number) {
        // 防止有些order已经不存在，查number + 10条取number条
        StringBuffer buffer = new StringBuffer("SELECT * FROM ");
        buffer.append(DBInfor.TABLE_ORDER_COUNT);

        String where;
        // 只有查询总访问量不需要where条件，查询年/月/周/日都需要比较是否是本年/月/周/日
        if (!column.equals(DBInfor.TOC_ALL)) {
            where = " WHERE ";
            buffer.append(where);
            Calendar calendar = Calendar.getInstance();
            if (column.equals(DBInfor.TOC_YEAR)) {
                buffer.append(DBInfor.TOC_COL[6]).append("=").append(calendar.get(Calendar.YEAR));
            }
            else if (column.equals(DBInfor.TOC_MONTH)) {
                buffer.append(DBInfor.TOC_COL[6]).append("=").append(calendar.get(Calendar.YEAR));
                buffer.append(" AND ").append(DBInfor.TOC_COL[7]).append("=").append(calendar.get(Calendar.MONTH) + 1);
            }
            else if (column.equals(DBInfor.TOC_WEEK)) {// week和month不是上下关系，和year是上下关系
                buffer.append(DBInfor.TOC_COL[6]).append("=").append(calendar.get(Calendar.YEAR));
                buffer.append(" AND ").append(DBInfor.TOC_COL[8]).append("=").append(calendar.get(Calendar.WEEK_OF_YEAR));
            }
            else if (column.equals(DBInfor.TOC_DAY)) {// day和month是上下关系
                buffer.append(DBInfor.TOC_COL[6]).append("=").append(calendar.get(Calendar.YEAR));
                buffer.append(" AND ").append(DBInfor.TOC_COL[7]).append("=").append(calendar.get(Calendar.MONTH) + 1);
                buffer.append(" AND ").append(DBInfor.TOC_COL[9]).append("=").append(calendar.get(Calendar.DAY_OF_MONTH));
            }
        }

        buffer.append(" ORDER BY ").append(column).append(" DESC LIMIT 0,").append(number + 10);

        String sql = buffer.toString();
        DebugLog.d(sql);

        List<SOrder> list = new ArrayList<>();
        Statement stmt = null;
        try {
            stmt = connection.createStatement();
            ResultSet set = stmt.executeQuery(sql);
            while (set.next()) {
                SOrderCount count = getSorderCountFromSet(set);
                SOrder order = queryOrder(count.orderId, connection);
                if (order != null) {
                    order.setOrderCount(count);
                    list.add(order);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        // 防止有些order已经不存在，查number + 10条取number条
        for (int i = list.size() - 1; i >= 10; i --) {
            list.remove(i);
        }
        return list;
    }

}
