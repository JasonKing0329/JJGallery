package com.jing.app.jjgallery.service.data.dao;

import com.jing.app.jjgallery.bean.order.SOrder;
import com.jing.app.jjgallery.bean.order.STag;
import com.jing.app.jjgallery.model.main.file.OnOrderItemMoveTrigger;

import java.sql.Connection;
import java.util.List;

/**
 * Created by JingYang on 2016/7/11 0011.
 * Description:
 */
public interface SOrderDao {
    List<SOrder> queryAllOrders(Connection connection);
    boolean queryOrderItems(SOrder order, Connection connection);
    List<String> queryOrderItemsByName(String name, Connection connection);
    List<STag> loadTagList(Connection connection);
    STag queryTag(String tName, Connection connection);
    boolean insertTag(String tName, Connection connection);
    void deleteTag(STag sTag, Connection connection);
    boolean isOrderExist(String name, Connection connection);
    boolean insertOrder(SOrder order, Connection connection);
    int queryOrderIdAfterInsert(Connection connection);
    boolean insertOrderCount(int orderId, Connection connection);
    boolean updateOrder(SOrder order, Connection connection);
    boolean deleteOrder(SOrder order, Connection connection);
    boolean isOrderItemExist(String path, int orderId, Connection connection);
    boolean insertOrderItem(int orderId, String itemPath, Connection connection);
    boolean deleteOrderItem(int itemId, Connection connection);
    boolean updateOrderItemPath(List<String> originPathList, String targetPath
            , OnOrderItemMoveTrigger trigger, Connection connection);
    boolean deleteItemFromAllOrders(String path, Connection connection);
    SOrder queryOrder(int orderId, Connection connection);
    SOrder queryOrderByName(String name, Connection connection);
}
