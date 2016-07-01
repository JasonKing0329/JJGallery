package com.jing.app.jjgallery.service.data.impl;

import com.jing.app.jjgallery.config.DBInfor;
import com.jing.app.jjgallery.service.data.dao.VersionDao;

import java.lang.reflect.UndeclaredThrowableException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by JingYang on 2016/7/1 0001.
 * Description:
 */
public class VersionDaoImpl implements VersionDao {

    /**
     * v6.2 add ol_pixel to table fe_order_list
     * record image's width and height
     * @return
     */
    @Override
    public boolean isBelowVersion6_2(Connection connection) {

        Statement stmt = null;
        try {
            String sql = "SELECT * FROM " + DBInfor.TABLE_IMAGE_PIXEL;
            stmt = connection.createStatement();
            stmt.executeQuery(sql);
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (UndeclaredThrowableException e) {
            //column not exist
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
        return true;
    }
    /**
     * v6.2 add ol_pixel to table fe_order_list
     * record image's width and height
     * @return
     */
    public boolean addImagePixelTable(Connection connection) {

        Statement stmt = null;
        try {
            String sql = "CREATE TABLE 'fe_image_pixel' (" +
                    "'fip_id' INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                    + "'fip_name' TEXT NOT NULL,"
                    + "'fip_width' INTEGER,"
                    + "'fip_height' INTEGER,"
                    + "'fip_other' TEXT)";

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
        return false;
    }

    /**
     * v7.0 add fe_files to table
     * record every file's time/size/width/height information
     * @return
     */
    public boolean isBelowVersion7_0(Connection connection) {

        Statement stmt = null;
        try {
            String sql = "SELECT * FROM " + DBInfor.TABLE_FILES;
            stmt = connection.createStatement();
            stmt.executeQuery(sql);
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (UndeclaredThrowableException e) {
            //column not exist
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
        return true;
    }
    /**
     * v7.0 add fe_files to table
     * record every file's time/size/width/height information
     * @return
     */
    public boolean addFilesTable(Connection connection) {

        Statement stmt = null;
        try {
            String sql = "CREATE TABLE 'fe_files' (" +
                    "'f_id' INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "'f_path' TEXT,"
                    + "'f_time' INTEGER,"
                    + "'f_time_tag' TEXT,"
                    + "'f_width' INTEGER,"
                    + "'f_height' INTEGER,"
                    + "'f_size' INTEGER,"
                    + "'f_other' TEXT)";

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
        return false;
    }

}
