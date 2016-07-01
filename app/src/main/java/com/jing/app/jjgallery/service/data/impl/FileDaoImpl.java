package com.jing.app.jjgallery.service.data.impl;

import android.util.Log;

import com.jing.app.jjgallery.bean.filesystem.FileBean;
import com.jing.app.jjgallery.config.Constants;
import com.jing.app.jjgallery.config.DBInfor;
import com.jing.app.jjgallery.service.data.dao.FileDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by JingYang on 2016/7/1 0001.
 * Description:
 */
public class FileDaoImpl implements FileDao {

    @Override
    public boolean isFileBeanExist(String path, Connection connection) {
        Statement stmt = null;
        try {
            stmt = connection.createStatement();
            ResultSet set = null;

            if (path.contains("'")) {//文件夹名中有时候会有"'"符号，会出现异常
                if (Constants.DEBUG) {
                    Log.e(Constants.LOG_TAG_INIT, "delete ' in folder name");
                }
                path = path.replace("'", "");
            }
            set = stmt.executeQuery("SELECT f_id FROM " + DBInfor.TABLE_FILES
                    + " WHERE f_path = '" + path + "'");

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

    /**
     * 插入多条文件记录
     * @param list
     * @param connection
     * @return
     */
    @Override
    public boolean insertFileBeans(List<FileBean> list, Connection connection) {
        if (connection != null && list != null) {
            String sql = "INSERT INTO " + DBInfor.TABLE_FILES
                    + "(f_path, f_time, f_time_tag, f_width, f_height, f_size, f_other)" +
                    " VALUES(?,?,?,?,?,?,?)";
            PreparedStatement stmt = null;
            try {
                stmt = connection.prepareStatement(sql);
                for (int i = 0; i < list.size(); i ++) {
                    FileBean bean = list.get(i);
                    stmt.setString(1, bean.getPath());
                    stmt.setLong(2, bean.getTime());
                    stmt.setString(3, bean.getTimeTag());
                    stmt.setInt(4, bean.getWidth());
                    stmt.setInt(5, bean.getHeight());
                    stmt.setLong(6, bean.getSize());
                    stmt.setString(7, bean.getOther());
                    stmt.executeUpdate();
                }
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
     *
     * @param connection
     * @param orderBy eg. "fe_time DESC" or "fe_time ASC", no order by if null
     * @return
     */
    @Override
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

    @Override
    public boolean deleteFileBean(long id, Connection connection) {

        if (connection != null) {
            String sql = "DELETE FROM " + DBInfor.TABLE_FILES
                    + " WHERE f_id = " + id;
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
}
