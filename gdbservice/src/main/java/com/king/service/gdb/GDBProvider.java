package com.king.service.gdb;

import com.king.service.gdb.bean.RecordOneVOne;
import com.king.service.gdb.bean.Star;
import com.king.service.gdb.dao.SqliteDao;

import java.util.List;

/**
 * Created by Administrator on 2016/7/30 0030.
 */
public class GDBProvider {

    private String databasePath;
    private SqliteDao sqliteDao;

    public GDBProvider(String databasePath) {
        this.databasePath = databasePath;
        sqliteDao = new SqliteDao();
    }

    public List<RecordOneVOne> getOneVOneRecords() {
        try {
            SqlConnection.getInstance().connect(databasePath);
            return sqliteDao.queryOneVOneRecords(SqlConnection.getInstance().getConnection());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            SqlConnection.getInstance().close();
        }
        return null;
    }

    public List<Star> getStars() {
        try {
            SqlConnection.getInstance().connect(databasePath);
            return sqliteDao.queryAllStars(SqlConnection.getInstance().getConnection());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            SqlConnection.getInstance().close();
        }
        return null;
    }

    public Star getStarRecords(int starId) {
        try {
            SqlConnection.getInstance().connect(databasePath);
            Star star = sqliteDao.queryStarById(SqlConnection.getInstance().getConnection(), starId);
            sqliteDao.loadStarRecords(SqlConnection.getInstance().getConnection(), star);
            return star;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            SqlConnection.getInstance().close();
        }
        return null;
    }
}
