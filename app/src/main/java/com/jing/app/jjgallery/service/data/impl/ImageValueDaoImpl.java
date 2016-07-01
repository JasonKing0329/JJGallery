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
    public ImageValue queryImageValue(String key, Connection connection) {
        Statement stmt = null;
        ImageValue value = null;
        try {
            stmt = connection.createStatement();
            ResultSet set = null;

            set = stmt.executeQuery("SELECT * FROM " + DBInfor.TABLE_IMAGE_PIXEL
                    + " WHERE " + DBInfor.TIP_COL_NAME + " = '" + key + "'");

            if (set.next()) {
                value = new ImageValue();
                value.setName(key);
                value.setId(set.getInt(DBInfor.NUM_TIP_COL_ID));
                value.setWidth(set.getInt(DBInfor.NUM_TIP_COL_WIDTH));
                value.setHeight(set.getInt(DBInfor.NUM_TIP_COL_HEIGHT));
                value.setOther(set.getString(DBInfor.NUM_TIP_COL_OTHER));
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

    @Override
    public boolean insertImagePixel(ImageValue value, Connection connection) {
        if (connection != null && value != null) {
            String sql = "INSERT INTO " + DBInfor.TABLE_IMAGE_PIXEL
                    + "(" + DBInfor.TIP_COL_NAME + "," + DBInfor.TIP_COL_WIDTH + "," + DBInfor.TIP_COL_HEIGHT + ")" +
                    " VALUES(?,?,?)";
            PreparedStatement stmt = null;
            try {
                stmt = connection.prepareStatement(sql);
                stmt.setString(1, value.getName());
                stmt.setInt(2, value.getWidth());
                stmt.setInt(3, value.getHeight());
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

    /**
     * query many items
     * @param keyList its size must be equal with param values'
     * @param values to save key referring result
     * @param connection
     */
    @Override
    public void queryImageValues(List<String> keyList
            , List<ImageValue> values, Connection connection) {
        PreparedStatement stmt = null;
        ImageValue value = null;
        try {
            ResultSet set = null;
            String sql = "SELECT * FROM " + DBInfor.TABLE_IMAGE_PIXEL
                    + " WHERE " + DBInfor.TIP_COL_NAME + " = ?";
            stmt = connection.prepareStatement(sql);

            for (int i = 0; i < keyList.size(); i ++) {
                stmt.setString(1, keyList.get(i));
                set = stmt.executeQuery();
                if (set.next()) {
                    value = new ImageValue();
                    value.setName(keyList.get(i));
                    value.setId(set.getInt(DBInfor.NUM_TIP_COL_ID));
                    value.setWidth(set.getInt(DBInfor.NUM_TIP_COL_WIDTH));
                    value.setHeight(set.getInt(DBInfor.NUM_TIP_COL_HEIGHT));
                    value.setOther(set.getString(DBInfor.NUM_TIP_COL_OTHER));
                    values.set(i, value);
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
