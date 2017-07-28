package com.king.service.gdb;

import com.king.service.gdb.bean.FavorBean;
import com.king.service.gdb.bean.GDBProperites;
import com.king.service.gdb.bean.Record;
import com.king.service.gdb.bean.RecordOneVOne;
import com.king.service.gdb.bean.SceneBean;
import com.king.service.gdb.bean.Star;
import com.king.service.gdb.bean.StarCountBean;
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
     * @param starMode
     */
    public List<Star> getStars(String starMode) {
        try {
            SqlConnection.getInstance().connect(databasePath);
            String where = null;
            if (GDBProperites.STAR_MODE_TOP.equals(starMode)) {
                where = "betop>0 AND bebottom=0";
            }
            else if (GDBProperites.STAR_MODE_BOTTOM.equals(starMode)) {
                where = "bebottom>0 AND betop=0";
            }
            else if (GDBProperites.STAR_MODE_HALF.equals(starMode)) {
                where = "bebottom>0 AND betop>0";
            }
            return sqliteDao.queryAllStars(SqlConnection.getInstance().getConnection(), where);
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
     * 查询star
     * @param name
     * @return
     */
    public Star queryStarByName(String name) {
        try {
            SqlConnection.getInstance().connect(databasePath);
            Star star = sqliteDao.queryStarByName(SqlConnection.getInstance().getConnection(), name);
            return star;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            SqlConnection.getInstance().close();
        }
        return null;
    }

    /**
     * 查询star
     * @param id
     * @return
     */
    public Star queryStarById(int id) {
        try {
            SqlConnection.getInstance().connect(databasePath);
            Star star = sqliteDao.queryStarById(SqlConnection.getInstance().getConnection(), id);
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
     * 查询所有的record
     * @return
     */
    public List<Record> getLatestRecords(int from, int number) {
        try {
            SqlConnection.getInstance().connect(databasePath);
            List<Record> list = new ArrayList<>();
            List<RecordOneVOne> oList = sqliteDao.queryLatestRecords(from, number, SqlConnection.getInstance().getConnection());
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

    public StarCountBean queryStarCount() {
        try {
            SqlConnection.getInstance().connect(databasePath);
            StarCountBean star = sqliteDao.queryStarCount(SqlConnection.getInstance().getConnection());
            return star;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            SqlConnection.getInstance().close();
        }
        return null;
    }

    public StarCountBean queryFavorStarCount() {
        try {
            SqlConnection.getInstance().connect(databasePath);
            StarCountBean star = sqliteDao.queryFavorStarCount(SqlConnection.getInstance().getConnection());
            return star;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            SqlConnection.getInstance().close();
        }
        return null;
    }

    /**
     * 随机查询record
     * @return
     */
    public List<Record> getRandomRecords(int number) {
        try {
            SqlConnection.getInstance().connect(databasePath);
            List<Record> list = new ArrayList<>();
            List<RecordOneVOne> oList = sqliteDao.getRandomRecords(number, SqlConnection.getInstance().getConnection());
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
     * 查询所有的favor数据
     * @return
     */
    public List<FavorBean> getFavors() {
        try {
            SqlConnection.getInstance().connect(databasePath);
            return sqliteDao.queryFavors(SqlConnection.getInstance().getConnection());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            SqlConnection.getInstance().close();
        }
        return null;
    }

    /**
     * 查询所有的favor数据
     * @return
     */
    public List<FavorBean> getTopFavors(int number) {
        try {
            SqlConnection.getInstance().connect(databasePath);
            return sqliteDao.queryTopFavors(number, SqlConnection.getInstance().getConnection());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            SqlConnection.getInstance().close();
        }
        return null;
    }

    public boolean isStarFavor(int starId) {
        try {
            SqlConnection.getInstance().connect(databasePath);
            return sqliteDao.isStarFavor(SqlConnection.getInstance().getConnection(), starId);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            SqlConnection.getInstance().close();
        }
        return false;
    }
    /**
     * 更新favor数据
     * @return
     */
    public void saveFavor(FavorBean bean) {
        try {
            SqlConnection.getInstance().connect(databasePath);
            sqliteDao.saveFavor(SqlConnection.getInstance().getConnection(), bean);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            SqlConnection.getInstance().close();
        }
    }

    /**
     * 创建favor表数据
     * @param favorList
     */
    public void saveFavorList(List<FavorBean> favorList) {
        try {
            SqlConnection.getInstance().connect(databasePath);
            sqliteDao.saveFavorList(SqlConnection.getInstance().getConnection(), favorList);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            SqlConnection.getInstance().close();
        }
    }

    /**
     * name符合关键词nameLike，按sortColumn desc/asc 排序，从第from条记录开始取number条记录
     * @param sortColumn
     * @param desc
     * @param from
     * @param number
     * @param nameLike
     * @return
     */
    public List<Record> getRecords(String sortColumn, boolean desc, boolean includeDeprecated, int from, int number, String nameLike, String scene) {
        try {
            SqlConnection.getInstance().connect(databasePath);
            return sqliteDao.getRecords(sortColumn, desc, includeDeprecated, from, number, nameLike, scene, SqlConnection.getInstance().getConnection());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            SqlConnection.getInstance().close();
        }
        return null;
    }

    /**
     * scene list
     * @return
     */
    public List<SceneBean> getSceneList() {
        try {
            SqlConnection.getInstance().connect(databasePath);
            return sqliteDao.getScenes(SqlConnection.getInstance().getConnection());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            SqlConnection.getInstance().close();
        }
        return null;
    }

    /**
     * get records by scene name
     * @param scene
     * @return
     */
    public List<Record> getRecordsByScene(String scene) {
        try {
            SqlConnection.getInstance().connect(databasePath);
            return sqliteDao.getRecordsByScene(scene, SqlConnection.getInstance().getConnection());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            SqlConnection.getInstance().close();
        }
        return null;
    }

    /**
     * get record by name
     * @param name
     * @return
     */
    public Record getRecordByName(String name) {
        try {
            SqlConnection.getInstance().connect(databasePath);
            return sqliteDao.getRecordByName(name, SqlConnection.getInstance().getConnection());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            SqlConnection.getInstance().close();
        }
        return null;
    }

    public boolean isFavorTableExist() {
        try {
            SqlConnection.getInstance().connect(databasePath);
            return sqliteDao.isFavorTableExist(SqlConnection.getInstance().getConnection());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            SqlConnection.getInstance().close();
        }
        return false;
    }

    public void createFavorTable() {
        try {
            SqlConnection.getInstance().connect(databasePath);
            sqliteDao.createFavorTable(SqlConnection.getInstance().getConnection());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            SqlConnection.getInstance().close();
        }
    }
}
