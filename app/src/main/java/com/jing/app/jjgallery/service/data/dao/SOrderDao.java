package com.jing.app.jjgallery.service.data.dao;

import com.jing.app.jjgallery.bean.order.SOrder;
import com.jing.app.jjgallery.bean.order.SOrderCount;
import com.jing.app.jjgallery.bean.order.STag;
import com.jing.app.jjgallery.model.main.file.OnOrderItemMoveTrigger;

import java.sql.Connection;
import java.util.List;

/**
 * Created by JingYang on 2016/7/11 0011.
 * Description:
 */
public interface SOrderDao {
    /**
     * 查询所有SOrder
     * @param connection
     * @return
     */
    List<SOrder> queryAllOrders(Connection connection);

    /**
     * 查询SOrder所有图片内容
     * @param order 目标列表，图片内容将set至order中
     * @param connection
     * @return
     */
    boolean queryOrderItems(SOrder order, Connection connection);

    /**
     * 通过列表名称查询SOrder的图片内容
     * @param name
     * @param connection
     * @return
     */
    List<String> queryOrderItemsByName(String name, Connection connection);

    /**
     * 查询所有的Stag
     * @param connection
     * @return
     */
    List<STag> loadTagList(Connection connection);

    /**
     * 按名称查询STag
     * @param tName
     * @param connection
     * @return
     */
    STag queryTag(String tName, Connection connection);

    /**
     * 新增STag
     * @param tName
     * @param connection
     * @return
     */
    boolean insertTag(String tName, Connection connection);

    /**
     * 删除STag
     * @param sTag
     * @param connection
     */
    void deleteTag(STag sTag, Connection connection);

    /**
     * 删除STag下所有SOrder
     * @param tag
     * @param connection
     */
    void deleteAllOrdersInTag(STag tag, Connection connection);

    /**
     * 查询SOrder是否存在
     * @param name
     * @param connection
     * @return
     */
    boolean isOrderExist(String name, Connection connection);

    /**
     * 新增SOrder
     * @param order
     * @param connection
     * @return
     */
    boolean insertOrder(SOrder order, Connection connection);

    /**
     * 查询刚刚新增SOrder的id(数据库采用id自增策略)
     * @param connection
     * @return
     */
    int queryOrderIdAfterInsert(Connection connection);

    /**
     * 修改SOrder的访问统计
     * @param count
     * @param connection
     * @return
     */
    boolean saveOrderCount(SOrderCount count, Connection connection);

    /**
     * 查询SOrder的访问统计
     * @param orderId
     * @param connection
     * @return
     */
    SOrderCount queryOrderCount(int orderId, Connection connection);

    /**
     * 新增SOrder的访问统计
     * @param orderId
     * @param connection
     * @return
     */
    boolean insertOrderCount(int orderId, Connection connection);

    /**
     * 保存SOrder
     * @param order
     * @param connection
     * @return
     */
    boolean updateOrder(SOrder order, Connection connection);

    /**
     * 删除SOrder
     * @param order
     * @param connection
     * @return
     */
    boolean deleteOrder(SOrder order, Connection connection);

    /**
     * 查询目标SOrder是否包含目标图片
     * @param path
     * @param orderId
     * @param connection
     * @return
     */
    boolean isOrderItemExist(String path, int orderId, Connection connection);

    /**
     * 向目标SOrder中新增图片
     * @param orderId
     * @param itemPath
     * @param connection
     * @return
     */
    boolean insertOrderItem(int orderId, String itemPath, Connection connection);

    /**
     * 从SOrder中删除图片
     * @param itemId
     * @param connection
     * @return
     */
    boolean deleteOrderItem(int itemId, Connection connection);

    /**
     * 修改SOrder中图片的文件目录
     * @param originPathList
     * @param targetPath
     * @param trigger
     * @param connection
     * @return
     */
    boolean updateOrderItemPath(List<String> originPathList, String targetPath
            , OnOrderItemMoveTrigger trigger, Connection connection);

    /**
     * 从所有列表中删除目标图片
     * @param path
     * @param connection
     * @return
     */
    boolean deleteItemFromAllOrders(String path, Connection connection);

    /**
     * 查询SOrder
     * @param orderId
     * @param connection
     * @return
     */
    SOrder queryOrder(int orderId, Connection connection);

    /**
     * 通过名称查询SOrder
     * @param name
     * @param connection
     * @return
     */
    SOrder queryOrderByName(String name, Connection connection);

    /**
     * 查询访问量前number的列表
     * @param connection
     * @param number
     * @return
     */
    List<SOrder> queryOrderAccessList(Connection connection, String column, int number);
}
