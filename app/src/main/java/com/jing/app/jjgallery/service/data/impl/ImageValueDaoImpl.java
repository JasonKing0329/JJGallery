package com.jing.app.jjgallery.service.data.impl;

import com.jing.app.jjgallery.config.DBInfor;
import com.jing.app.jjgallery.service.data.dao.ImageValueDao;
import com.jing.app.jjgallery.service.image.ImageValue;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * Created by JingYang on 2016/7/1 0001.
 * Description:
 */
public class ImageValueDaoImpl implements ImageValueDao {

    @Override
    public ImageValue queryImageValue(String path, Connection connection) {
        Statement stmt = null;
        ImageValue value = null;
        try {
            stmt = connection.createStatement();
            ResultSet set = null;

            // 带"'"号的要特殊处理
            if (path.contains("'")) {
                path = path.replace("'", "''");
            }

            set = stmt.executeQuery("SELECT * FROM " + DBInfor.TABLE_FILES
                    + " WHERE " + DBInfor.TF_COL_PATH + " = '" + path + "'");

            if (set.next()) {
                value = new ImageValue();
                value.setPath(path);
                value.setId(set.getInt(DBInfor.TF_COL_ID));
                value.setWidth(set.getInt(DBInfor.TF_COL_WIDTH));
                value.setHeight(set.getInt(DBInfor.TF_COL_HEIGHT));
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
        return value;
    }

    /**
     * query many items
     * @param pathList its size must be equal with param values'
     * @param values each path of pathList refer same position ImageValue, this list should not be null and its size is 0
     * @param connection
     */
    @Override
    public void queryImageValues(List<String> pathList
            , List<ImageValue> values, Connection connection) {
        PreparedStatement stmt = null;
        ImageValue value;
        try {
            ResultSet set;
            String sql = "SELECT * FROM " + DBInfor.TABLE_FILES
                    + " WHERE " + DBInfor.TF_COL_PATH + " = ?";
            stmt = connection.prepareStatement(sql);

            for (int i = 0; i < pathList.size(); i ++) {
                stmt.setString(1, pathList.get(i));
                set = stmt.executeQuery();
                if (set.next()) {
                    value = new ImageValue();
                    value.setPath(pathList.get(i));
                    value.setId(set.getInt(DBInfor.TF_COL_ID));
                    value.setWidth(set.getInt(DBInfor.TF_COL_WIDTH));
                    value.setHeight(set.getInt(DBInfor.TF_COL_HEIGHT));
                    values.add(value);
                }
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
    }
}
