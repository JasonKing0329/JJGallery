package com.king.service.gdb;

import com.king.service.gdb.bean.Record;
import com.king.service.gdb.bean.RecordOneVOne;
import com.king.service.gdb.bean.Star;
import com.king.service.gdb.dao.SqliteDao;

import java.util.ArrayList;
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

    /**
     * 查询版本号
     * @return
     */
    public String getVersionName() {
        try {
            SqlConnection.getInstance().connect(databasePath);
            return sqliteDao.queryVersion(SqlConnection.getInstance().getConnection());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            SqlConnection.getInstance().close();
        }
        return null;
    }

    /**
     * 查询所有的1v1 record
     * @return
     */
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

    /**
     * 查询所有的star
     * @return
     */
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

    /**
     * 查询star对应的所有record
     * @param starId
     * @return
     */
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

    /**
     * 查询所有的record
     * @return
     */
    public List<Record> getAllRecords() {
        try {
            SqlConnection.getInstance().connect(databasePath);
            List<Record> list = new ArrayList<>();
            List<RecordOneVOne> oList = sqliteDao.queryOneVOneRecords(SqlConnection.getInstance().getConnection());
            for (RecordOneVOne record:oList) {
                list.add(record);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            SqlConnection.getInstance().close();
        }
        return null;
    }

    /**
     * 加载star对应的record数量
     * @param star
     */
    public void loadStarRecordNumber(Star star) {
        try {
            SqlConnection.getInstance().connect(databasePath);
            sqliteDao.loadStarRecordNumber(SqlConnection.getInstance().getConnection(), star);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            SqlConnection.getInstance().close();
        }
    }
}
