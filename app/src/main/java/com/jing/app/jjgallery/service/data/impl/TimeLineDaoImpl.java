package com.jing.app.jjgallery.service.data.impl;

import com.jing.app.jjgallery.bean.filesystem.FileBean;
import com.jing.app.jjgallery.config.DBInfor;
import com.jing.app.jjgallery.service.data.dao.TimeLineDao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by JingYang on 2016/7/18 0018.
 * Description:
 */
public class TimeLineDaoImpl implements TimeLineDao {

    /**
     *
     * @param connection
     * @param orderBy eg. "fe_time DESC" or "fe_time ASC", no order by if null
     * @return
     */
    public List<FileBean> queryAllFileBeans(Connection connection, String orderBy) {

        List<FileBean> list = null;
        Statement stmt = null;
        try {
            stmt = connection.createStatement();
            ResultSet set = null;

            String sql = "SELECT * FROM " + DBInfor.TABLE_FILES;
            if (orderBy != null) {
                sql = sql + " ORDER BY " + orderBy;
            }
            set = stmt.executeQuery(sql);
            while (set.next()) {
                if (list == null) {
                    list = new ArrayList<FileBean>();
                }
                FileBean bean = new FileBean();
                bean.setId(set.getInt(1));
                bean.setPath(set.getString(2));
                bean.setTime(set.getLong(3));
                bean.setTimeTag(set.getString(4));
                bean.setWidth(set.getInt(5));
                bean.setHeight(set.getInt(6));
                bean.setSize(set.getLong(7));
                bean.setOther(set.getString(8));
                list.add(bean);
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
}
